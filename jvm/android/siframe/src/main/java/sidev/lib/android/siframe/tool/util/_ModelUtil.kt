package id.go.surabaya.ediscont.utilities.modelutil

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import org.json.JSONArray
import org.json.JSONObject
import sidev.lib.android.siframe.model.FK_DB
import sidev.lib.android.siframe.model.FK_M
import sidev.lib.android.siframe.model.FK_Row
import sidev.lib.android.siframe.model.PictModel
import sidev.lib.android.siframe.tool.util._BitmapUtil
import sidev.lib.universal.`fun`.getField
import sidev.lib.universal.`fun`.getV
import sidev.lib.universal.`fun`.setV
import sidev.lib.universal.`fun`.get
import sidev.lib.universal.`fun`.indices
import sidev.lib.universal.`fun`.notNull
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType


object _ModelUtil {
    fun getId(model: Any): String? {
        return if(model is String){ model }
        else {
            val idField= try{ model::class.java.getDeclaredField("id") }
            catch (e: NoSuchFieldException){ model::class.java.getDeclaredField("_id") }

            idField.getV(model)
        }
    }

    fun <M: Any> searchForId(modelList: List<M>, id: String): M? {
        for(model in modelList)
            if(getId(model) == id)
                return model
        return null
    }
    fun <M: Any> searchForId(modelList: Array<M>, id: String): M? {
        for(model in modelList)
            if(getId(model) == id)
                return model
        return null
    }

    fun <I: Any, O: Any> searchWhereSingleTo(modelList: Collection<I>, iterator: (I) -> O?): O? {
        for(model in modelList){
            val out= iterator(model)
            if(out != null)
                return out
        }
        return null
    }
    fun <I: Any, O: Any> searchWhereSingleTo(modelList: Array<I>, iterator: (I) -> O?): O? {
        for(model in modelList){
            val out= iterator(model)
            if(out != null)
                return out
        }
        return null
    }

    fun <I: Any> searchWhereSingle(modelList: Collection<I>, iterator: (I) -> Boolean): I? {
        for(model in modelList)
            if(iterator(model))
                return model
        return null
    }
    fun <I: Any> searchWhereSingle(modelList: Array<I>, iterator: (I) -> Boolean): I? {
        for(model in modelList)
            if(iterator(model))
                return model
        return null
    }

    fun <I: Any, O: Any> searchWhere(modelList: Array<I>, iterator: (I) -> O?): MutableList<O>? {
        val list= ArrayList<O>()
        for(model in modelList){
            val output= iterator(model)
            if(output != null)
                list.add(output)
        }
        return if(list.isNotEmpty()) list
        else null
    }
    fun <I: Any, O: Any> searchWhere(modelList: Collection<I>, iterator: (I) -> O?): MutableList<O>? {
        val list= ArrayList<O>()
        for(model in modelList){
            val output= iterator(model)
            if(output != null)
                list.add(output)
        }
        return if(list.isNotEmpty()) list
        else null
    }

    fun <M: Any> searchToListForId(modelList: List<M>, id: String): List<M>? {
        val res= ArrayList<M>()
        for(model in modelList)
            if(getId(model) == id)
                res.add(model)
        return null
    }
    fun <M: Any> searchToListForId(modelList: Array<M>, id: String): List<M>? {
        val res= ArrayList<M>()
        Log.e("MODEUL_UTIL", "id= $id")
        for(model in modelList){
            val v= getId(model)!!
            Log.e("MODEUL_UTIL", "fromId= $v")
            if(v == id)
                res.add(model)
        }
        return null
    }

    inline fun <reified FROM: Any, reified TO: Any> searchDependent(list: List<FROM>, model: TO): ArrayList<FROM>? {
        if(list.isNotEmpty()){
            val fkField= getFkField<TO>(list[0]) ?: return null
            val independentId= getId(model)

            val res= ArrayList<FROM>()
            for(d in list){
                val fk= fkField.getV<FK_M<TO>>(d)!!
                for(toId in fk.toId)
                    if(toId == independentId){
                        res.add(d)
                        break
                    }
            }
            return res
        }
        return null
    }
    inline fun <reified FROM: Any, reified TO: Any> searchDependent(list: Array<FROM>, model: TO): ArrayList<FROM>? {
        if(list.isNotEmpty()){
            val fkField= getFkField<TO>(list[0]) ?: return null
            val independentId= getId(model)

//            Log.e("MODEUL_UTIL", "independentId= $independentId")

            val res= ArrayList<FROM>()
            for(d in list){
                val fk= fkField.getV<FK_M<TO>>(d)!!
                for(toId in fk.toId){
//                    Log.e("MODEUL_UTIL", "toId= $toId")

                    if(toId == independentId){
                        res.add(d)
                        break
                    }
                }
            }
            return res
        }
        return null
    }

    fun <FROM: Any, TO: Any> searchDependent(fkDB: FK_DB<FROM, TO>, model: TO): ArrayList<FROM>? {
        if(fkDB.fkRow.isNotEmpty()){
            val independentId= getId(model)

            val res= ArrayList<FROM>()
            for(fk in fkDB.fkRow){
                if(fk.toId == independentId)
                    res.add(fk.fromObj!!)
            }
            return res
        }
        return null
    }

    inline fun <reified FROM: Any, reified TO: Any> searchDependentFromId(list: Array<FROM>, id: String): ArrayList<FROM>? {
        if(list.isNotEmpty()){
            val fkField= getFkField<TO>(list[0]) ?: return null
            val independentId= id

//            Log.e("MODEUL_UTIL", "independentId= $independentId")

            val res= ArrayList<FROM>()
            Log.e("MODEL_UTIL", "FROM= ${FROM::class.java.simpleName}")
            for(d in list){
                Log.e("MODEL_UTIL", "d= $d")
                val fk= fkField.getV<FK_M<TO>>(d)!!
                for(toId in fk.toId){
//                    Log.e("MODEUL_UTIL", "toId= $toId")

                    if(toId == independentId){
                        res.add(d)
                        break
                    }
                }
            }
            return res
        }
        return null
    }

    inline fun <reified TO: Any> getFkField(model: Any): Field? {
        val fkType= FK_M<TO>(arrayOf())::class.java
        val paramName= TO::class.java.name

        val fields= model::class.java.declaredFields
        var fkField: Field?= null
        for(field in fields){
            val fieldParamName=
                try{
                    Log.e("MODEL_UTIL", "field.name ${field.name} .type= ${field.type}")
                    val name= ((field.genericType as ParameterizedType)
                        .actualTypeArguments.get(0) as Class<*>).name
                    Log.e("MODEL_UTIL", "NO ERROR name= $name")
                    name
                } catch (e: ClassCastException){
                    Log.e("MODEL_UTIL", "e.message= ${e.message} field.genericType= ${"..."}")
                    null
                }

            if(field.type.isAssignableFrom(fkType)
                && (fieldParamName == paramName || paramName == Any::class.java.name)) {
                Log.e("MODEL_UTIL", "fieldName= $fieldParamName paramName= $paramName")
                fkField= field
                break
            }
        }
        return fkField
    }

    /**
     * Setiap kelas FROM pasti punya FK. Fungsi untuk mengisi referensi object dalam FK.
     * Kemungkinan akan membutuhkan banyak waktu selama pengoperasiannya.
     * Dengan anggapan bahwa FROM memiliki satu FK yang terhubung ke TO.
     * @param fromDatalist dg anggapan bahwa FK tidak null dan id di dalam FK udah terisi.
     *
     * @return tabel perantara yang berisi id dari 2 tabel lainnya yang saling terhubung.
     */
    inline fun <reified FROM: Any, reified TO: Any> pairFk(fromDatalist: List<FROM>, toDatalist: List<TO>): FK_DB<FROM, TO>? {
        if(fromDatalist.isNotEmpty() && toDatalist.isNotEmpty()){
            var fkField= getFkField<TO>(fromDatalist[0])

            val fkDb= FK_DB<FROM, TO>()
            for(fromData in fromDatalist){
                (fkField?.getV<FK_M<TO>>(fromData))
                    .notNull { fk ->
                        var toObjList= ArrayList<TO>()
                        for(id in fk.toId){
                            searchForId(
                                toDatalist,
                                id
                            ).notNull {
                                toObjList.add(it)
                                fkDb.fkRow.add(FK_Row(getId(fromData)!!, id, fromData, it))
                            }
                        }
                        if(toObjList.isNotEmpty())
                            fk.toObj= toObjList.toTypedArray()
                    }
            }
            return fkDb
        }
        return null
    }
    /**
     * Setiap kelas FROM pasti punya FK. Fungsi untuk mengisi referensi object dalam FK.
     * Kemungkinan akan membutuhkan banyak waktu selama pengoperasiannya.
     * Dengan anggapan bahwa FROM memiliki satu FK yang terhubung ke TO.
     * @param fromDatalist dg anggapan bahwa FK tidak null dan id di dalam FK udah terisi.
     */
    inline fun <reified FROM: Any, reified TO: Any> pairFkWithoutDb(fromDatalist: List<FROM>, toDatalist: List<TO>){
        if(fromDatalist.isNotEmpty() && toDatalist.isNotEmpty()){
            var fkField= getFkField<TO>(fromDatalist[0])

            for(fromData in fromDatalist){
                (fkField?.getV<FK_M<TO>>(fromData))
                    .notNull { fk ->
                        var toObjList= ArrayList<TO>()
                        for(id in fk.toId){
                            searchForId(
                                toDatalist,
                                id
                            ).notNull {
                                toObjList.add(it)
                            }
                        }
                        if(toObjList.isNotEmpty())
                            fk.toObj= toObjList.toTypedArray()
                    }
            }
        }
    }
    inline fun <reified FROM: Any, reified TO: Any> pairFkWithoutDb(fromDatalist: Array<FROM>, toDatalist: Array<TO>){
        if(fromDatalist.isNotEmpty() && toDatalist.isNotEmpty()){
            Log.e("MODEL_UTIL", "FROM= ${FROM::class.java.simpleName} TO= ${TO::class.java.simpleName} ====AWAL====")

            var fkField= getFkField<TO>(fromDatalist[0])

            Log.e("MODEL_UTIL", "fkField.name= ${fkField?.name}")

            for(fromData in fromDatalist){
                (fkField?.getV<FK_M<TO>>(fromData))
                    .notNull { fk ->
                        var toObjList= ArrayList<TO>()
                        for(id in fk.toId){
                            searchForId(
                                toDatalist,
                                id
                            ).notNull {
                                toObjList.add(it)
                                Log.e("MODEL_UTIL", "FROM= ${FROM::class.java.simpleName} TO= ${TO::class.java.simpleName} id= $id obj inde= $it")
                            }
                        }
                        if(toObjList.isNotEmpty())
                            fk.toObj= toObjList.toTypedArray()
                    }
            }
        }
    }
    inline fun <reified FROM: Any, reified TO: Any> pairFkWithoutDb(fromDatalist: Array<FROM>, toData: TO){
        if(fromDatalist.isNotEmpty()){
            Log.e("MODEL_UTIL", "FROM= ${FROM::class.java.simpleName} TO= ${TO::class.java.simpleName} ====AWAL====")

            var fkField= getFkField<TO>(fromDatalist[0])

            Log.e("MODEL_UTIL", "fkField.name= ${fkField?.name}")

            val toId= getId(toData)

            for(fromData in fromDatalist){
                (fkField?.getV<FK_M<TO>>(fromData))
                    .notNull { fk ->
                        var toObjList= ArrayList<TO>()
                        for(id in fk.toId){
                            if(id == toId){
                                toObjList.add(toData)
                                Log.e("MODEL_UTIL", "FROM= ${FROM::class.java.simpleName} TO= ${TO::class.java.simpleName} id= $id")
                            }
                        }
                        if(toObjList.isNotEmpty())
                            fk.toObj= toObjList.toTypedArray()
                    }
            }
        }
    }

    /**
     * PictModel yang akan dipindahkan melalui extra intent harus diproses melalui fungsi ini
     * agar tidak terjadi error parsing Parcelable.
     */
    fun preparePictForIntentPass(datalist: Array<*>){
        val pictField= datalist.first()!!.getField<PictModel>()!!
        for(data in datalist){
            val pict= pictField.getV<PictModel>(data!!)
            pict?.bm= null
        }
    }

    /**
     * PictModel yang diekstrak dari extra intent atau bm nya null tapi
     * udah diisi direktori gambarnya diproses dg fungsi ini agar bm nya gak null.
     */
    fun fillPict(c: Context, datalist: Array<*>){
        val pictField= datalist.first()!!.getField<PictModel>()!!
        for(data in datalist){
            val pict= pictField.getV<PictModel>(data!!)!!
            Log.e("MODEL_UTIL", "AWAL====== pict.bm != null = ${pict.bm != null} pict.dir= ${pict.dir}")
            if(pict.dir == null){
                if(pict.file != null) pict.dir= pict.file!!.toUri().toString()
            }
            if(pict.file == null){
                if(pict.dir != null) pict.file= File(pict.dir!!)
            }
            if(pict.bm == null){
                if(pict.dir != null)
                    pict.bm= _BitmapUtil.from(c, dir = pict.dir)!!
                else if(pict.file != null)
                    pict.bm= _BitmapUtil.from(c, file = pict.file)!!
                Log.e("MODEL_UTIL", "pict.bm != null = ${pict.bm != null} pict.dir= ${pict.dir}")
            }
        }
    }

    fun clearEmptyFk(model: Any){
        if(model is Array<*> || model is Collection<*>){
            val ind= model.indices()!!
            for(i in ind){
                val e= model.get<Any>(i)
                if(e != null){
                    val fkField= getFkField<Any>(e)
                    var fkIsEmpty= false
                    fkField?.getV<FK_M<*>>(e)
                        .notNull { fk ->
                            fkIsEmpty = fk.toId.isEmpty()
                                    && (fk.toObj == null ||
                                    (fk.toObj != null && fk.toObj!!.isNotEmpty())
                                    )
                        }
                    if(fkIsEmpty)
                        fkField?.setV(e, null)
                }
            }
        } else {
            val e= model
            val fkField= getFkField<Any>(e)
            var fkIsEmpty= false
            fkField?.getV<FK_M<*>>(e)
                .notNull { fk ->
                    fkIsEmpty = fk.toId.isEmpty()
                            && (fk.toObj == null ||
                            (fk.toObj != null && fk.toObj!!.isNotEmpty())
                            )
                }
            if(fkIsEmpty)
                fkField?.setV(e, null)
        }
    }

/*
===================
Convert Zone
===================
 */
    fun toJson(model: Any): JSONObject {
        val json= JSONObject()
        val fields= model::class.java.declaredFields

        for(field in fields){
            val name= field.name
            val valueTemp= field.getV<Any>(model)

            val value=
                when(valueTemp){
                    is FK_M<*> -> {
                        if(valueTemp.toId.size == 1)
                            valueTemp.toId[0]
                        else if(valueTemp.toId.isEmpty())
                            ""
                        else{
                            val jsonArray= JSONArray()
                            for(id in valueTemp.toId)
                                jsonArray.put(id)
                            //<Alif, 9 Mei 2020> <Selesai: 0> => Blum ditest sprt apa outputnya. Harapnnya "[ 1, 2, 3 ]"
                            jsonArray.toString()
                        }
                    }
                    is Array<*> -> {
                        val jsonArray= JSONArray()
                        for(e in valueTemp)
                            jsonArray.put(e.toString())
                        //<Alif, 9 Mei 2020> <Selesai: 0> => Blum ditest sprt apa outputnya. Harapnnya "[ 1, 2, 3 ]"
                        jsonArray.toString()
                    }
                    else -> valueTemp.toString()
                }

            //.toString()
            json.put(name, value)
        }
        return json
    }
}