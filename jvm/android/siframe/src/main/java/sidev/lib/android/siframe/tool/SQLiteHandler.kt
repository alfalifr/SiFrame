package sidev.lib.android.siframe.tool

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.db.Attribute
import sidev.lib.android.siframe.intfc.listener.LiveVal
import sidev.lib.android.siframe.intfc.listener.ProgressListener
import sidev.lib.android.siframe.model.intfc.Exclude
import sidev.lib.android.siframe.model.intfc.ModelId
import sidev.lib.android.siframe.model.intfc.StorageKind
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.log.LogApp
import sidev.lib.android.siframe.tool.util.log.ToastApp
import sidev.lib.check.asNotNullTo
import sidev.lib.jvm.tool.util.StringUtil
import sidev.lib.jvm.tool.util.ThreadUtil
import sidev.lib.number.isNotNegative
import sidev.lib.reflex.full.isCollection
import java.lang.reflect.Field

/**
 * Aturan penyimpanan data:
 *      Format yg tersedia: INTEGER, STRING, DOUBLE
 *              Tipe data di luar itu dikonversi ke bentuk yg mirip:
 *                      Int, Long, Boolean -> INTEGER
 *                      String -> STRING
 *                      Double, Float -> Double
 *
 *
 *  NOTE: <1, 14 Juni 2020> sementara msh bisa hanya read/write FK_M.toId.first()
 *        <1, 15 Juni 2020>!!! Fk tidak dianggap sbg data yg dicatat karena secara logis beda tabel.
 *        <2, 15 Juni 2020> Declared field dg nama berawalan _ dianggap sbg private dan tidak dicatat.
 *        <3, 15 Juni 2020> Hanya mencatat field primitive, bkn obj.
 *        <4, 27 Juni 2020> Kelas ini menggunakan LiveVal sbg future karena jika menggunakan LifeData, maka akan
 *                           kesulitan untuk mendapatkan LifecycleOwnernya.
 */
abstract class SQLiteHandler<M>(val ctx: Context/*= App.ctx*/){
//    val ctx: Context= App.ctx
//    : SQLiteOpenHelper(konteks, BuildConfig.DB_NAMA, null, BuildConfig.DB_VERSI){
    constructor(ctx: Context, collectionTypeHandler: CollectionTypeHandler<M>): this(ctx){
        this.collectionTypeHandler= collectionTypeHandler
        initHandler()
    }

    companion object{
        val TYPE_NULL= Attribute.Type.NULL.name /* "NULL" / *jika suatu field tidak merepresentasikan nilai scr langsung, atau dg kata lain merupakan object,
                                maka tidak dianggap sbg attribut pada db SQLite.
                             */
        var EXCLUDED_FIELD_NAME= "_id" //Replika dari id. <2 Juni 2020> => field yg gak dianggap adalah yg dg nama berawalan _
        val ERROR= "error"
    }

    /**
     * Property ini digunakan pada instansiasi kelas ini,
     * jadi tidak perlu diubah nilainya setelahnya,
     * kecuali saat [readAttribFromModel] di mana penyimpanan data bertipe koleksi dibatalkan
     * karena tabel pada handler ini memiliki id ([primaryKey].isNotBlank()).
     */
    protected var collectionTypeHandler: CollectionTypeHandler<M>?= null
    protected var isCollectionTypePresent: Boolean= false

    open val prefixName: String= "_\$USER\$_"

    /**
     * Helper yg ditujukan untuk pengaturan tabel inti pada kelas ini.
     * Properti ini memiliki setter private. Untuk settingan tambahan, gunakan [sqliteHelperDelegate].
     */
    lateinit var sqliteHelper: SQLiteOpenHelper
        private set
    /** Helper tambahan yg ditujukan untuk settingan tambahan untuk derived class.*/
    var sqliteHelperDelegate: SQLiteOpenHelper?= null
        protected set
    var isDbOpen= true
        private set

    /** Nama kolom pada DB yg merupakan id. */
    open var primaryKey: String= ""
        protected set
    /** Nama field pada Model [M] yg merupakan id yg berdasarkan [primaryKey]. */
    protected val primaryKeyFieldName: String
        get()= if(primaryKey.isNotBlank()) StringUtil.toCamelCase(primaryKey) else "id"
    /** String where clause yg sama dg [primaryKey]. */
    protected val primaryKeyWhereClause
        get()= if(primaryKey.isNotBlank()) "$primaryKey = ?" else ""

    var tableName: String= createTableName()
        protected set
    protected abstract val modelClass: Class<M>

    /**
     * Knp kok gak pake java.reflect aja? Krn gak smua declaredField merupakan attrib, sprti attrib yg berupa obj.
     */
    protected lateinit var attribField: Array<Field>
        private set
    var attribName= Array(0) {""}
        private set
    /**
     * Index untuk tiap elemen dari [attribName] pada field yg terdapat dalam model [M].
     * Properti ini berguna saat mengakses field dari kelas model [M],
     * karena nama field pada kelas model [M] berbeda dg nama attribut pada DB,
     * yaitu nama attribut merupakan versi snake_case.
     */
    protected var attribNameFieldIndex= Array(0) {it}
        private set
    var attribType= Array(0) {""}
        private set

    /**
     * Nama attribut dg tipe koleksi pada DB.
     * TODO: <Sabtu, 26 Sep 2020> => Untuk smtr, data tipe koleksi yg disimpan hanya dapat berjumlah 1.
     *   Hal tersebut dikarenakan akan membingungkan untuk menyimpan banyak koleksi dalam 1 tabel.
     */
    protected var collectionTypeAttribName= Array(0) {""}
        private set

    val sqlCreateTable: String
        get(){
            var queryPembuatanTabel= "CREATE TABLE $tableName ("
            for(i in attribName.indices){
                queryPembuatanTabel += "${attribName[i]} ${attribType[i]}"
                if(i < attribName.size -1)
                    queryPembuatanTabel += ", "
            }
            return "$queryPembuatanTabel);"
        }
    val sqlDropTable: String
        get()= "DROP TABLE IF EXISTS $tableName"

    //    private val pekerjaLatar= ArrayList<AsyncTask<Unit, Int, Unit>>()
    private var progressListener: ProgressListener<M>?= null
    //    private var pengawasProgresAtomik: PengawasProgres<Boolean>?= null //untuk mengawasi proses yang yang tidak mengembalikan data seperti SELECT, INSERT, UPDATE, DELETE
    var totalFix= -1

//    private var isHandlerInited: Boolean= false

//    private val DATA_HOLDER_= ViewModelHolder<M>()

    init{
        initHandler()
//        DATA_HOLDER_.idVHM= "DATA_HOLDER_$tableName"
    }

/*
    override fun onCreate(db: SQLiteDatabase?) {
        var queryPembuatanTabel= "CREATE TABLE $NAMA_TABEL ("
        for(i in 0 until namaAtribut.size){
            queryPembuatanTabel += "${namaAtribut[i]} ${ciriAtribut[i]}"
            if(i < namaAtribut.size -1)
                queryPembuatanTabel += ", "
        }
        queryPembuatanTabel += ");"

        LogApp.e("SQLITE", "queryPembuatanTabel= $queryPembuatanTabel")
        db?.execSQL(queryPembuatanTabel)
//        db?.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $NAMA_TABEL")
//        db?.close()
    }
*/
    /**
     * Membuat instance model [M] dg nilai attribut [petaNilai] dg key adalah nama field
     * pada kelas model [M] dan value adalah nilai dari atribut.
     *
     * <2, 15 Juni 2020> Sementara tidak mengepass fk karena berada di beda tabel.
     */
    abstract fun createModel(petaNilai: Map<String, *>): M

    fun initHandler(){
//        tableName= createTableName()
        readAttribFromModel()
    }

    fun deleteDb(): Boolean{
        return ctx.deleteDatabase(sqliteHelper.databaseName)
    }

    fun dropTable(): LiveVal<Boolean>{
        val liveVal= LiveVal<Boolean>(ctx)
        ThreadUtil.Pool.submit {
            val db= sqliteHelper.writableDatabase
            try{
                db?.execSQL(sqlDropTable)
                liveVal.value= true
                liveVal
            } catch(error: Exception){
                liveVal.setVal(false, ERROR)
                null
            }
        }
        return liveVal
    }

    fun createTable(): Exception? {
        return try{
//            val jikaTabelAda=
            if(!tableExists())
                sqliteHelper.writableDatabase.execSQL(sqlCreateTable)
//                sqliteHelper.onCreate(sqliteHelper.writableDatabase)
            null
        } catch (error: Exception){
            error
        }
    }

    private fun mapValue(valueCursor: Cursor): HashMap<String, Any>{
        val valMap= HashMap<String, Any>()
        val attribs= modelClass.declaredFields
        var defAcces: Boolean
        for(i in attribNameFieldIndex){
            val attrib= attribs[i]
            defAcces= attrib.isAccessible
            attrib.isAccessible= true
//            loge("labeliAtribut() atributAsli= ${attrib.name} tipe= ${attrib.type}")
            valMap[attrib.name]=
                    when(attrib.type){
                        Int::class.java -> valueCursor.getInt(i)
                        Long::class.java -> valueCursor.getInt(i)
                        Boolean::class.java -> valueCursor.getInt(i) == 1
                        String::class.java -> valueCursor.getString(i)
                        Double::class.java -> valueCursor.getDouble(i)
                        Float::class.java -> valueCursor.getDouble(i)
                        else -> ""
                    }
            attribs[i].isAccessible= defAcces
        }
        return valMap
    }

    open fun createTableName(): String{
        val namaTabel= modelClass.simpleName
        return prefixName + StringUtil.toSnakeCase(namaTabel)
    }

    @JvmOverloads
    fun tableExists(tableName: String= this.tableName): Boolean{
        try{
            val db= sqliteHelper.readableDatabase
/*
        if(db.isOpen){
/*
        if(openDb) {
            if(!db.isOpen)
                db = readableDatabase

            if(!db.isReadOnly) {
                db.close()
                db= readableDatabase
            }
        }
*/
        }
*/
            var exists= false
            val cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '$tableName'", null)
            if(cursor!=null) {
                if(cursor.count > 0)
                    exists= true
                cursor.close()
            }
            return exists
        } catch(error: Exception){
            if(error is IllegalStateException || error is UninitializedPropertyAccessException){
                openConn()
                return tableExists()
            } else
                throw error
        }
    }
    /*
        fun snakeCase(masuk: String, kecilSemua: Boolean= false): String{
            var strHasil= masuk
            var i= -1
            while(++i < strHasil.length)
                if(strHasil[i].isUpperCase() && i > 0){
                    strHasil= strHasil.substring(0, i) +"_" +strHasil.substring(i)
                    if(kecilSemua)
                        strHasil= strHasil.substring(0, i+1) +strHasil[i+1].toLowerCase() +(if(i+2 < strHasil.length) strHasil.substring(i+2) else "")
                    i++
                }
            return strHasil
        }
    */

    /**
     * Sekalian assign ke declared field terkait attribField, attribName, attribType.
     */
    protected open fun readAttribFromModel(){
        val attribs= modelClass.declaredFields
        val attribCount= attribs.size
/*
        loge("modelClass.simpleName= ${modelClass.simpleName} attribs.size= ${attribs.size}")
        for((i, att) in attribs.withIndex()){
            loge("i= $i att.name= ${att.name}")
        }
 */
/*
        val anotasiKelas= modelClass.annotations
        for(perAnotasi in anotasiKelas)
            if(perAnotasi is Model_Atribut_Jml){
                jmlAtribut= perAnotasi.jmlDariDepan
                break
            }
 */

        val attribFieldList= ArrayList<Field>(attribCount)
        val attribNameList= ArrayList<String>(attribCount)
        val attribNameFieldIndexList= ArrayList<Int>(attribCount)
        val attribTypeList= ArrayList<String>(attribCount)
        val collectionTypeAttribNameList= ArrayList<String>(attribCount)

        for(i in 0 until attribCount){
            //Tidak mengambil private prop
/*
            val pub= Modifier.isPublic(attribs[i].modifiers)
            val pro= Modifier.isProtected(attribs[i].modifiers)
            val priv= Modifier.isPrivate(attribs[i].modifiers)
            val isAc= attribs[i].isAccessible
 */

            val field= attribs[i]
            val attName= field.name
//            loge("attName= $attName pub= $pub pro= $pro priv= $priv isAc= $isAc")
            //TODO: Data yg disimpan hanya berupa primitif
            if(!attName.startsWith("_")){
                if(field.annotations.find { it is Exclude }
                    .asNotNullTo { annot: Exclude -> annot.kind == StorageKind.ANY || annot.kind == StorageKind.SQLITE }
                    == true
                ) continue

                var isCollection= false
                var type= when(val fieldType= field.type){
                    Int::class.java -> "INTEGER"
                    Long::class.java -> "INTEGER"
                    Boolean::class.java -> "INTEGER"
                    String::class.java -> "STRING"
                    Double::class.java -> "DOUBLE"
                    Float::class.java -> "DOUBLE"
                    else -> {
                        var type= if(fieldType.kotlin.isCollection || fieldType.isArray)
                            collectionTypeHandler?.resolveColumnType(field)?.let {
                                Attribute.Type.getByClass(it)
                            }?.name ?: TYPE_NULL
                        else TYPE_NULL

                        isCollection= type != TYPE_NULL
/*
                        loge("readAttr() field= $field fieldType= $fieldType isCollection= $isCollection isCollectionTypePresent= $isCollectionTypePresent")
                        loge("readAttr() field= $field fieldType.kotlin.isCollection= ${fieldType.kotlin.isCollection} fieldType.isArray= ${fieldType.isArray}")
                        loge("readAttr() field= $field collectionTypeHandler == null => ${collectionTypeHandler == null}")
 */

                        if(!isCollectionTypePresent && type != TYPE_NULL){
                            isCollectionTypePresent= true
                        } else if(isCollectionTypePresent && isCollection){
                            isCollection= false
                            type= TYPE_NULL
                            //TODO: <Sabtu, 26 Sep 2020> => Untuk smtr, data tipe koleksi yg disimpan hanya dapat berjumlah 1.
                            //  Hal tersebut dikarenakan akan membingungkan untuk menyimpan banyak koleksi dalam 1 tabel.
                        }



//                        field.type.interfaces.forEach { i -> loge("attName= $attName i::class.java.simpleName= ${i::class.java.simpleName}") }
/*
                        if(field.type.interfaces.ifExists { intfc -> intfc == Fk::class.java }){
                            type= "STRING"
//                            loge("intfc == Fk::class.java")
                        }
 */
                        type
                    }
                }
//                loge("i= $i type= $type")
                if(type != TYPE_NULL){
                    val colName= StringUtil.toSnakeCase(attName, true)
                    attribFieldList += field
                    attribNameList += colName
                    if(isCollection)
                        collectionTypeAttribNameList += colName
                    attribNameFieldIndexList += i

                    for(annot in field.annotations)
                        if(annot is ModelId
                            && (annot.kind == StorageKind.ANY || annot.kind == StorageKind.SQLITE)
                        ){
                            primaryKey= colName //attribNameList[i]
                            type += " PRIMARY KEY"
                            break
                        }
                    attribTypeList.add(type)

//                    val i= attribNameList.lastIndex
//                    loge("attribFieldList[$i] = ${attribFieldList[i]} attribNameList[$i] = ${attribNameList[i]} attribTypeList[$i] = ${attribTypeList[i]}")
                }
/*
            else{
                attribFieldList.removeAt(i)
                attribNameList.removeAt(i)
                attribTypeList.removeAt(i)
            }
 */
            }
        }

        //TODO: <Sabtu, 26 Sep 2020> => Untuk smtr, tabel yg memiliki kolom bertipe koleksi dan punya id,
        //  tidak akan disimpan. Hal tersebut dikarenakan handler ini menjadikan kolom bertipe
        //  koleksi menjadi bbrp baris sehingga id tidak bisa sama.
        if(isCollectionTypePresent && primaryKey.isNotBlank()){
            isCollectionTypePresent= false
            collectionTypeHandler= null // Jika tidak ada koleksi yg disimpan, maka akan sia" nilainya disimpan.
            collectionTypeAttribNameList.clear()
        }
/*
        <Sabtu, 26 Sep 2020> => primaryKey tidak dianggap sbg keharusan. Hal tersebut lebih masuk akal.
        if(primaryKey.isEmpty()){
            val idField= DataWithId::class.java.declaredFields.first()
            attribFieldList.add(0, idField)
            attribNameList.add(0, StringUtil.toSnakeCase(idField.name, true))
            attribTypeList.add(0, "STRING PRIMARY KEY")
            primaryKey= attribNameList[0]
        }
 */
        attribField= attribFieldList.toTypedArray()
        attribName= attribNameList.toTypedArray()
        attribNameFieldIndex= attribNameFieldIndexList.toTypedArray()
        attribType= attribTypeList.toTypedArray()
        collectionTypeAttribName= collectionTypeAttribNameList.toTypedArray()
/*
        for((i, name) in attribName.withIndex()){
//            loge("i= $i attribName= $name attribType= ${attribType[i]}")
        }
 */

/*
        for((i, perNamaAtrib) in namaAtribut.withIndex())
            LogApp.e("SQLITE", "namaAtribut= $perNamaAtrib ke= $i")
        for((i, perCiriAtrib) in ciriAtribut.withIndex())
            LogApp.e("SQLITE", "ciriAtribut= $perCiriAtrib ke= $i")
 */
    }

    /** Mengambil id dari [model]. */
    protected open fun getIdFromModel(model: M): String? =
        try{ (model as Any).javaClass.getField(primaryKeyFieldName).get(model)?.toString() }
        catch (e: Exception){ null }


    fun getAttribType(attribName: String): Attribute.Type{
        val ind= this.attribName.indexOf(attribName)
        return if(ind.isNotNegative())
            Attribute.Type.getByName(attribType[ind])
        else Attribute.Type.NULL
    }
/*
===================
Koneksi
===================
*/
    /**
     * @return true jika koneksi ke db berhasil dan sebaliknya.
     */
    fun checkConn(assert: Boolean= true): Boolean{
        val error= createTable()
        return if(error != null){
            progressListener?.sendError(error, true)
            ToastApp.debugTeks(ctx, "Terjadi kesalahan pada DB.\nHarap reload halaman ini.", Toast.LENGTH_LONG)
            try {
                openConn()
                true
            } catch (e: Exception){
                if(assert)
                    throw error
                else false
            }
        } else true
    }

    /**
     * @return true jika berhasil disambungkan ke db dan sebaliknya.
     */
    fun openConn(): Boolean{
        if(!isDbOpen || !this::sqliteHelper.isInitialized){
            sqliteHelper= object: SQLiteOpenHelper(ctx, _Config.DB_NAME, null, _Config.DB_VERSION){

                override fun getWritableDatabase(): SQLiteDatabase =
                    sqliteHelperDelegate?.writableDatabase ?: super.getWritableDatabase()
                override fun getReadableDatabase(): SQLiteDatabase =
                    sqliteHelperDelegate?.readableDatabase ?: super.getReadableDatabase()

                override fun getDatabaseName(): String =
                    sqliteHelperDelegate?.databaseName ?: super.getDatabaseName()

                override fun close() = sqliteHelperDelegate?.close() ?: super.close()

                override fun onConfigure(db: SQLiteDatabase?) =
                    sqliteHelperDelegate?.onConfigure(db) ?: super.onConfigure(db)

                override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) =
                    sqliteHelperDelegate?.onDowngrade(db, oldVersion, newVersion) ?: super.onDowngrade(db, oldVersion, newVersion)

                override fun onOpen(db: SQLiteDatabase?) =
                    sqliteHelperDelegate?.onOpen(db) ?: super.onOpen(db)


                override fun onCreate(db: SQLiteDatabase?) {
//                    loge("queryPembuatanTabel= $queryPembuatanTabel")
                    sqliteHelperDelegate?.onCreate(db)
                    db?.execSQL(sqlCreateTable)
                }

                override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
                    sqliteHelperDelegate?.onUpgrade(db, oldVersion, newVersion)
                    db?.execSQL(sqlDropTable)
                }
            }
            isDbOpen= true
        }
        return isDbOpen
    }

    fun closeConn(){
        sqliteHelper.close()
        isDbOpen= false
    }
/*
===================
CRUD

Blum ada konsep yg mampu menyatukan konsep antara ViewModelHolder yg updatenya memberi input Data
dan Pengawas ViewModel/Handler yg memberi input Progres dan Total
===================
*/
    //@return true jika db terbuka, bisa berarti db sudah ada atau belum ada dan akan dibuat
    //      false jika db terkunci

    fun insert(vararg model: M): LiveVal<Map<String, Boolean>>{
        progressListener?.total(if(totalFix > 0) totalFix else model.size, "save")
/*
        loge("simpan data LUAR JML_MODEL= ${model.size}")
        loge("simpan data LUAR JML_MODEL= ${model.size}")
        loge("simpan data LUAR JML_MODEL= ${model.size}")
        for(perModel in model){
            loge("simpan data LUAR JML_MODEL= ${perModel.id}")
            loge("simpan data LUAR JML_MODEL= ${perModel.id}")
        }
 */

        checkConn()

        val liveVal= LiveVal<Map<String, Boolean>>(ctx)
        ThreadUtil.Pool.submit {
            try{
                val db= sqliteHelper.writableDatabase
//                LogApp.e("SQLITE", "simpan data DALAM JML_MODEL= ${model.size}")
//                LogApp.e("SQLITE", "simpan data DALAM JML_MODEL= ${model.size}")

                val resList= HashMap<String, Boolean>()
                for(perModel in model){
                    val id= getIdFromModel(perModel) ?: "" //perModel.id
                    try{
                        val nilai= extractVal(perModel)
                        val hasil= db.insert(tableName, null, nilai)

//                        LogApp.e("SQLITE", "hasil simpan data DALAM HASIL= $hasil")
//                        LogApp.e("SQLITE", "hasil simpan data DALAM HASIL= $hasil")

                        if(hasil >= 0){
                            resList[id]= true
                            progressListener?.progresSucc(perModel)
//                            LogApp.e("SQLITE", "hasil simpan data DALAM HASIL= $hasil")
                        }
                        else{
                            resList[id]= false
                            progressListener?.progresDone()
                        }
                    } catch (e: Exception){
                        resList[id]= false
                        progressListener?.progresDone()
                    }
                }
                liveVal.value= resList
                liveVal
//                db.close()
            } catch(error: Exception){
                liveVal.setVal(null, ERROR)
                LogApp.e("SQLITE", "simpan data GAGAL!!!!!\n error= ${error::class.java.simpleName} \n pesan= ${error.message} \n karena= ${error.cause}")
                progressListener?.sendError(error, true)
                null
            }
        }
//        LogApp.e("SQLITE", "simpan data JALAN!!!!!")
        return liveVal
    }

    //@param mulaiBaris -> 0 baris pertama
    //      batas -> jml kueri yang ditampilkan. 0 berarti tidak ada hasil (0 baris)
    @JvmOverloads
    fun readAllData(mulaiBaris: Int= 0, batas: Int= 0): LiveVal<List<M>>{
        checkConn()

        var liveVal= LiveVal<List<M>>(ctx)
        ThreadUtil.Pool.submit {
//            var liveVal= LiveVal<List<M>>()
            try{
                val db = sqliteHelper.readableDatabase
                var strKueri= "SELECT * FROM $tableName"
                var batasKueri= ""
                var mulaiKueri= ""
                if(batas > 0)
                    batasKueri= batas.toString()
                if(mulaiBaris > 0){
                    if(batas <= 0)
                        batasKueri= DatabaseUtils.queryNumEntries(db, tableName).toString()
                    mulaiKueri= "$mulaiBaris, "
                }
                if(batasKueri.isNotEmpty())
                    strKueri += " LIMIT $mulaiKueri$batasKueri"

                val kursor= db.rawQuery(strKueri, null)
                progressListener?.total(if(totalFix > 0) totalFix else kursor.count, "readAllData")

//                LogApp.e("SQLITE", "bacaSemuaData kursor.count= ${kursor.count}")

                if(kursor.moveToFirst()){
                    var resList= ArrayList<M>()
                    do{
                        if(kursor.count > 0){
                            val model= createModel(mapValue(kursor))
                            resList.add(model)
//                            isiData(model)
                            progressListener?.progresSucc(model)
                        } else
                            progressListener?.progresDone()
                    } while(kursor.moveToNext())
                    kursor.close()
                    if(isCollectionTypePresent && resList.size > 1)
                        collectionTypeHandler?.flattenQueryResult(resList, collectionTypeAttribName)?.let { resList= it }
                    liveVal.value= resList
                }
//                liveVal= DATA_HOLDER_
//                db.close()
                liveVal
            }catch(error: Exception){
                liveVal.setVal(null, ERROR)
                progressListener?.sendError(error, true)
                null
//                liveVal= null
            }
//            liveVal
        }
        return liveVal
    }

    fun readWithId(vararg id: String): LiveVal<List<M>>{
        checkConn()

        val liveVal= LiveVal<List<M>>(ctx)
        ThreadUtil.Pool.submit {
            progressListener?.total(if(totalFix > 0) totalFix else id.size, "readWithId")
            try{
                val db= sqliteHelper.readableDatabase
//                kosongkanData()
                var resList= ArrayList<M>()
                for(perId in id){
                    try{
                        val kursor= db.query(
                            tableName,
                            attribName,
                            primaryKeyWhereClause, arrayOf(perId),
                            null, null, null, null)

                        if(kursor.moveToFirst()){
                            val model= createModel(mapValue(kursor))
                            resList.add(model)
//                        isiData(model)
                            progressListener?.progresSucc(model)
                        } else
                            progressListener?.progresDone()
                        kursor.close()
                    }catch (e: Exception){
                        progressListener?.progresDone()
                    }
                }
                if(isCollectionTypePresent && resList.size > 1)
                    collectionTypeHandler?.flattenQueryResult(resList, collectionTypeAttribName)?.let { resList= it }
                liveVal.value= resList
                liveVal
//                db.close()
            } catch(error: Exception){
                liveVal.setVal(null, ERROR)
                progressListener?.sendError(error, true)
                null
            }
        }
        return liveVal
    }

    @JvmOverloads
    fun read(kondisi: String= "", vararg argumen: String): LiveVal<List<M>>{
        checkConn()

        val liveVal= LiveVal<List<M>>(ctx)
        ThreadUtil.Pool.submit {
            try{
                val db= sqliteHelper.readableDatabase
//                kosongkanData()
                val kursor  = db.query(
                        tableName,
                        attribName,
                        kondisi, argumen,
                        null, null, null, null)

                progressListener?.total(if(totalFix > 0) totalFix else kursor.count, "read")
/*
                LogApp.e("SQLITE", "bacaData===========LUAR METHOD=============== jmlKursor= ${kursor.count} kursor.moveToFirst()= ${kursor.moveToFirst()} ${progressListener == null}")

                LogApp.e("SQLITE", "bacaData kondisi= $kondisi")
                for(perArgumen in argumen)
                    LogApp.e("SQLITE", "bacaData argumen= $perArgumen")
 */
                if(kursor.moveToFirst()){
                    var resList= ArrayList<M>()
                    do{
                        if(kursor.count > 0){
                            val model= createModel(mapValue(kursor))
                            resList.add(model)
//                            isiData(model)
                            progressListener?.progresSucc(model)
                        } else
                            progressListener?.progresDone()
                    } while(kursor.moveToNext())
                    kursor.close()
                    if(isCollectionTypePresent && resList.size > 1)
                        collectionTypeHandler?.flattenQueryResult(resList, collectionTypeAttribName)?.let { resList= it }
                    liveVal.value= resList
                }
//                db.close()
                liveVal
            }catch(error: Exception){
                liveVal.setVal(null, ERROR)
                progressListener?.sendError(error, true)
                null
            }
        }
        return liveVal
    }


    /**
     * @return jml row yg udah ada.
     */
    fun ifExists(condition: String, arg: Array<String>): LiveVal<Int>{
        checkConn()

        val liveVal= LiveVal<Int>(ctx)
        ThreadUtil.Pool.submit {
            var count= 0
            try{
                val db= sqliteHelper.readableDatabase
//                kosongkanData()
                val kursor  = db.query(
                        tableName,
                        attribName,
                        condition, arg,
                        null, null, null, null)

                progressListener?.total(if(totalFix > 0) totalFix else kursor.count, "read")
/*
                LogApp.e("SQLITE", "bacaData===========LUAR METHOD=============== jmlKursor= ${kursor.count} kursor.moveToFirst()= ${kursor.moveToFirst()} ${progressListener == null}")

                LogApp.e("SQLITE", "bacaData kondisi= $condition")
                for(perArgumen in arg)
                    LogApp.e("SQLITE", "bacaData argumen= $perArgumen")
 */
                if(kursor.moveToFirst()){
                    do{
                        if(kursor.count > 0){
                            val model= createModel(mapValue(kursor))
                            count++
//                            isiData(model)
                            progressListener?.progresSucc(model)
                        } else
                            progressListener?.progresDone()
                    } while(kursor.moveToNext())
                    kursor.close()
                }
                liveVal.value= count
//                db.close()
                liveVal
            }catch(error: Exception){
                liveVal.setVal(null, ERROR)
                progressListener?.sendError(error, true)
                null
            }
        }
        return liveVal
    }

    /**
     * @return map id => Boolean
     */
    fun ifExists(vararg model: M): LiveVal<Map<String, Boolean>>{
        checkConn()

        val liveVal= LiveVal<Map<String, Boolean>>(ctx)
//        loge("ifExists() MELBU AWAL!!!")
        val future= ThreadUtil.Pool.submit {
            val db= sqliteHelper.writableDatabase
//            loge("ifExists() ThreadUtil.Pool.submit")
            try{
//                loge("ifExists() try outer MELBU")
                val resList= HashMap<String, Boolean>()
                var i= -1
                for(perModel in model){
                    val id= getIdFromModel(perModel) ?: "" //perModel.id
                    resList[id]= false
                    i++
//                    loge("ifExists() for i= $i i $id")
                    try{
//                        loge("ifExists() TRY!!!")
                        val strCondition= getConditionString(perModel)
//                    val hasil= db.query(tableName, attribName, strCondition, null)//db.update(tableName, values, strCondition, null)
                        val kursor  = db.query(
                            tableName,
                            arrayOf(primaryKey),
                            strCondition, null,
                            null, null, null, null)

                        if(kursor.moveToFirst()){
//                            loge("ifExists() if(kursor.moveToFirst())")
                            do{
//                                loge("ifExists() kursor.count= ${kursor.count}")
                                if(kursor.count > 0){
//                                val model= createModel(mapValue(kursor))
                                    resList[id]= true
//                            isiData(model)
                                    progressListener?.progresSucc(perModel)
                                } else
                                    progressListener?.progresDone()
                            } while(kursor.moveToNext())
//                            loge("ifExists() while CLOSE")
                            kursor.close()
                        }
                    } catch (e: Exception){
//                        loge("ifExists() EXC")
                        progressListener?.progresDone()
                    }
                }
                liveVal.value= resList
                liveVal
            } catch (e: Exception){
//                loge("ifExists() EXC outer")
                liveVal.setVal(null, ERROR)
                progressListener?.sendError(e, true)
                null
            }
        }
/*
        val isDone= future.isDone
        val isCancel= future.isCancelled
        val value= future.get()
//        loge("isDone= $isDone isCancel= $isCancel value= $value")
 */
        return liveVal
    }


    /**
     * @return List pasangan Id => Boolean, true jika berhasil dan sebaliknya.
     */
    fun update(vararg model: M): LiveVal<Map<String, Boolean>>{
        checkConn()

        val liveVal= LiveVal<Map<String, Boolean>>(ctx)
        ThreadUtil.Pool.submit {
            progressListener?.total(if(totalFix > 0) totalFix else model.size, "update")

            val db= sqliteHelper.writableDatabase
            try{
                db.beginTransaction() //==========TRANSAKSI===========
                val resList= HashMap<String, Boolean>()
                for(perModel in model){
                    val id= getIdFromModel(perModel) ?: "" //perModel.id
                    try{
                        val nilai= extractVal(perModel)
                        val hasil= db.update(tableName, nilai, primaryKeyWhereClause, arrayOf(id))
                        if(hasil > 0){
                            resList[id]= true
//                            val petaNilai= ModelUtil.petaNilai(nilai)
                            progressListener?.progresSucc(perModel)
                        }
                        else{
                            resList[id]= false
                            progressListener?.progresDone()
                        }
                    } catch (e: Exception){
                        resList[id]= false
                        progressListener?.progresDone()
                    }
                }
                liveVal.value= resList
                db.setTransactionSuccessful()
                liveVal
            }catch(error: Exception){
                liveVal.setVal(null, ERROR)
                progressListener?.sendError(error, true)
                null
            } finally{
                db.endTransaction()
                null
            }
//            db.close()
        }
        return liveVal
    }

    fun delete(vararg model: M): LiveVal<Map<String, Boolean>>{
//        loge("delete() MASUK")
        checkConn()

        val liveVal= LiveVal<Map<String, Boolean>>(ctx)
        ThreadUtil.Pool.submit {
            progressListener?.total(if(totalFix > 0) totalFix else model.size, "delete")
//            loge("delete() submit")
            try{
//                loge("delete() TRY!!!")
                val db= sqliteHelper.writableDatabase
                val resList= HashMap<String, Boolean>()
                for(perModel in model){
                    val id= getIdFromModel(perModel) ?: "" //perModel.id
//                    loge("delete() for id= $id")

                    try{
//                        loge("delete() TRY dalem !!!")
                        val hasil= db.delete(tableName, primaryKeyWhereClause, arrayOf(id))
//                        loge("delete() hasil= $hasil")
                        if(hasil > 0){
                            resList[id]= true
                            progressListener?.progresSucc(perModel)
                        } else{
                            resList[id]= false
                            progressListener?.progresDone()
                        }
                    } catch (e: Exception){
                        resList[id]= false
                        progressListener?.progresDone()
                        loge("delete() e.message= ${e.message}")
                    }
                }
                liveVal.value= resList
                liveVal
//                db.close()
            } catch(error: Exception){
                liveVal.setVal(null, ERROR)
                progressListener?.sendError(error, true)
                loge("delete() error.message= ${error.message}")
                null
            }
        }
        return liveVal
    }
/*
    protected open fun isiData(isi: M){
        DATA_HOLDER_.isi(isi)
    }
    open fun kosongkanData(){
        DATA_HOLDER_.kosongkan()
    }
    fun data(): ViewModelHolder<M>{
        return DATA_HOLDER_
    }
    fun dataKe(indek: Int): M?{
        return DATA_HOLDER_.ambilData(indek)
    }
 */

//===================

    fun extractVal(model: M): ContentValues{
        val nilai= ContentValues()
//        val atributDideklarasikan= model::class.java.declaredFields
        var aksesibilitasAwal: Boolean
        for((i, perNamaAtribut) in attribName.withIndex()){
            val atributIsi= attribField[i]
            aksesibilitasAwal= atributIsi.isAccessible
            atributIsi.isAccessible= true

            val perNilai= atributIsi.get(model)
            if(perNilai != null){
//                if(!atributIsi.type.interfaces.ifExists { intfc -> intfc == Fk::class.java }){
                    when(atributIsi.type){
                        Int::class.java -> nilai.put(perNamaAtribut, perNilai as Int) ///*atributIsi.getInt(model)*/)
                        Long::class.java -> nilai.put(perNamaAtribut, perNilai as Long)
                        Boolean::class.java -> nilai.put(perNamaAtribut, if(perNilai as Boolean) 1 else 0)
                        String::class.java -> nilai.put(perNamaAtribut, perNilai as String)
                        Double::class.java -> nilai.put(perNamaAtribut, perNilai as Double) //atributIsi.getDouble(model))
                        Float::class.java -> nilai.put(perNamaAtribut, perNilai as Float) //atributIsi.getDouble(model))
                    }
//                }
                /*
                else {
                    perNilai.asNotNull { fk: Fk ->
                        if(fk.getCount() > 0){
                            val fkVal= fk.getFkId(0) /*<1>*/
                            nilai.put(perNamaAtribut, fkVal)
                        } else{
                            nilai.putNull(perNamaAtribut)
                        }
                    }
                }
                */
            } else{
                nilai.putNull(perNamaAtribut)
                LogApp.e("SQLITE", "NILAI NULL!!! namaAtribut= $perNamaAtribut")
            }
            atributIsi.isAccessible= aksesibilitasAwal
        }
        return nilai
    }

    fun getConditionString(model: M): String{
        var res= ""
        for((i, field) in attribField.withIndex()){
            val name= attribName[i]
            val defAcces= field.isAccessible
            field.isAccessible= true
            var value= field.get(model)
            /*
            if(field.type.iff{
                    it.interfaces.ifExists { intfc -> intfc == Fk::class.java }
                }
            ) value= value.asNotNullTo { fk: Fk -> fk.getFkId(0) /*<1>*/ }
            */
            field.isAccessible= defAcces

            res += "$name = $value AND "
        }
        res= res.removeSuffix("AND ")
//        loge("getConditionString() res= $res")
        return res
    }

    fun setProgresListener(akt: Activity, pengawas: ProgressListener<M>, idPengawas: String?= tableName){
        progressListener= pengawas
        progressListener!!.act= akt
        progressListener!!.listenerId= idPengawas
    }
    fun setProgresListener(akt: Activity, idPengawas: String?= tableName,
                           pengawas: (progres: Int, total: Int, isiProgres: M?, idProgres: String) -> Unit){
        progressListener= object: ProgressListener<M>(akt, idPengawas){
            override fun onProgres(progres: Int, total: Int, isiProgres: M?, idProgres: String) {
                pengawas(progres, total, isiProgres, idProgres)
            }
        }
    }



    /**
     * Interface menangani masalah penyimpanan data dg tipe koleksi (Collection atau Array).
     */
//    @FunctionalInterface
    interface CollectionTypeHandler<M>{
        /**
         * Menyelesaikan cara penyimpanan data dg tipe koleksi (Collection atau Array).
         * Fungsi ini mengembalikan (return) kelas tipe data yg sesuai untuk
         * data yg bertipe asli koleksi.
         *
         * Param [field] adalah field pada model [M] yg akan disimpan ke DB
         * yg memiliki tipe data koleksi.
         *
         * Fungsi harus mengembalikan tipe data primitif. Jika tidak, maka [field] tidak
         * dianggap sbg kolom pada DB.
         */
        fun resolveColumnType(field: Field): Class<*>?

        /**
         * Merampingkan dataList hasil query dg model yg memiliki data bertipe koleksi.
         * Hal tersebut dikarenakan Handler ini menangani data bertipe kolesi dg cara
         * menyimpannya menjadi bbrp baris, sehingga fungsi ini dapat digunakan untuk merampingkan
         * data bbrp baris menjadi 1 baris dg data bertipe koleksi yg memiliki banyak element.
         */
        fun <C: List<M>> flattenQueryResult(dataList: C, collectionAttribNameList: Array<String>): C
        //TODO: <Sabtu, 26 Sep 2020> => Buat implementasi default.
    }
/*
    //@return null jika tidak ada objek pengawasProgres
    fun progresBerjalan(): Boolean?{
        return progressListener?.isRunning()
    }
    //@return null jika tidak ada objek pengawasProgres
    fun progresDijeda(): Boolean?{
        return if(progressListener == null) null
        else !progressListener!!.isFinished()
    }
    //@return null jika tidak ada objek pengawasProgres
    fun progresSelesai(): Boolean?{
        return if(progressListener == null) null
        else progressListener!!.isFinished()
    }
 */
}