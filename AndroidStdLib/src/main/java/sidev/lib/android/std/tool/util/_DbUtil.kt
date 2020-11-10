package sidev.lib.android.std.tool.util

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import sidev.lib.android.std._val._Config
import sidev.lib.android.std.db.Attribute
import sidev.lib.android.std.db.DbCursor
import sidev.lib.android.std.db.DbFactory
//import sidev.lib.android.std.db.Attribute
//import sidev.lib.android.std.db.DbCursor
//import sidev.lib.android.std.db.DbFactory
import sidev.lib.check.notNull
import java.util.*
import kotlin.collections.ArrayList


object _DbUtil{
    object SQLite{
        const val MASTER_TABLE_NAME= "sqlite_master"
        const val MASTER_TYPE_TABLE= "table"
        const val MASTER_COLUMN_NAME= "name"
        const val MASTER_ATTRIB_PRIMARY_KEY= "PRIMARY KEY"
        const val SQL_QUERY_ALL_TABLE_NAME= "SELECT $MASTER_COLUMN_NAME FROM $MASTER_TABLE_NAME WHERE type='$MASTER_TYPE_TABLE'"

        lateinit var dbHelper: SQLiteOpenHelper
        val db: SQLiteDatabase
            get()= dbHelper.writableDatabase

        @JvmOverloads
        fun setDb(
            ctx: Context,
            dbName: String = _Config.DB_NAME,
            factory: SQLiteDatabase.CursorFactory? = null,
            version: Int = _Config.DB_VERSION
        ): SQLiteOpenHelper{
           return object: SQLiteOpenHelper(ctx, dbName, factory, version){
               override fun onCreate(db: SQLiteDatabase?) {/*Abaikan dulu*/}
               override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {/*Abaikan dulu*/}
           }.also{ dbHelper = it }
        }

        @JvmOverloads
        fun beginOp(
            ctx: Context,
            dbName: String = _Config.DB_NAME,
            factory: SQLiteDatabase.CursorFactory? = null,
            version: Int = _Config.DB_VERSION
        ): Operation = Operation(setDb(ctx, dbName, factory, version).also{ dbHelper = it })

        @JvmOverloads
        fun beginOp(dbHelper: SQLiteOpenHelper?= null): Operation = try{ Operation((dbHelper?.also{ SQLite.dbHelper = it }) ?: SQLite.dbHelper) }
            catch (e: UninitializedPropertyAccessException){
                throw UninitializedPropertyAccessException("_DbUtil.SQlite.db belum di-init, harap init dulu.")
            }

        class Operation(private val helper: SQLiteOpenHelper){

            /**
             * Melakukan query terhadap db yg diperoleh dari [helper].
             */
            fun query(q: String, vararg args: String): DbCursor {
                val db: SQLiteDatabase= helper.writableDatabase
                val c: Cursor = db.rawQuery(q, args)
                return DbCursor(c)
/*

                val list= ArrayList<Pair<String, Any?>>()
                if (c.moveToFirst()) {
                    while (!c.isAfterLast) {
                        for((i, name) in c.columnNames.withIndex()){
                            val rawVal= c.getString(i)
                            @Suppress(SuppressLiteral.IMPLICIT_CAST_TO_ANY)
                            val value= when(c.getType(i)){
                                Cursor.FIELD_TYPE_INTEGER -> c.getInt(i)
                                Cursor.FIELD_TYPE_FLOAT -> c.getDouble(i)
                                Cursor.FIELD_TYPE_STRING -> c.getString(i)
                                else -> null
                            }
                            loge("query() rawVal= $rawVal value= $value")
                            list += name to value
                        }
                        c.moveToNext()
                    }
                }
                c.close()
                db.close()
                return list
 */
            }

            /**
             * Mengambil semua nama tabel yg ada pada db yg diperoleh dari [helper].
             */
            fun listAllTableName(): Array<String>{
                val list= ArrayList<String>()
                query(SQL_QUERY_ALL_TABLE_NAME)
                    .forEach {
//                        loge("listAllTableName() list= $it")
                        list += it[0].second as String
                    }
                return list.toTypedArray()
            }

            /**
             * Mengambil semua atribut dari tabel dg nama [tableName].
             */
            @JvmOverloads
            fun listTableAttribute(tableName: String, ignoreCase: Boolean= true): List<Attribute>{
                val checkedTableName = if (ignoreCase) tableName.toLowerCase(Locale.ROOT) else tableName
                val attribList= ArrayList<Attribute>()
/*
                listAllTableName().find {
                    (if(ignoreCase) it.toLowerCase(Locale.ROOT) else it) == checkedTableName
                }.notNull {
                }
 */
                query("SELECT sql FROM $MASTER_TABLE_NAME WHERE $MASTER_COLUMN_NAME='$checkedTableName'")
                    .get<String?>(0,0).notNull { sqlDecStr ->
//                    loge("listTableAttribute sqlDecStr= $sqlDecStr")
                        val regex= "CREATE TABLE ([a-zA-Z0-9_$]+)\\s*\\(([\\s\\S]*)\\)".toRegex()
                        regex.find(sqlDecStr).notNull { res ->
//                        loge("listTableAttribute res.groupValues= ${res.groupValues}")
                            var isPrimaryFound= false
                            res.groupValues[2].split(",").forEachIndexed { i, attrib ->
                                val attribWordList= attrib.trim().split(" ")
                                val name= attribWordList[0]
//                            loge("listTableAttribute i= $i name= $name attrib= $attrib")
                                var type= Attribute.Type.NULL
                                var isPrimary= false

                                if(attribWordList.size > 1){
                                    type= Attribute.Type.getByName(attribWordList[1])

                                    if(!isPrimaryFound && attribWordList.size > 2){
                                        if(MASTER_ATTRIB_PRIMARY_KEY in attrib){
                                            isPrimary= true
                                            isPrimaryFound= true
                                        }
                                    }
                                }
                                attribList += DbFactory.createAttribute(name, i, type, isPrimary)
                            }
                        }
                    }

                return attribList
            }

            /**
             * Mengambil sql dekalarasi dari tabel dg nama [tableName].
             */
            @JvmOverloads
            fun getTableSqlDeclaration(tableName: String, ignoreCase: Boolean= true): String{
                val checkedTableName = if (ignoreCase) tableName.toLowerCase(Locale.ROOT) else tableName
                return query("SELECT sql FROM $MASTER_TABLE_NAME WHERE $MASTER_COLUMN_NAME='$checkedTableName'")
                    .get(0,0) ?: "<null>"
            }

            /**
             * Mengambil list dari semua nilai dari tiap kolom dan baris yg diratakan.
             * Hasil return yaitu Triple<rowNumber, columnName, columnValue>.
             * @see DbCursor.flatten()
             */
            fun getAllRowFromTable(tableName: String, vararg columnNames: String): List<Triple<Int, String, Any?>>{
                var columnStr= ""
                for(col in columnNames)
                    columnStr += "$col, "
                columnStr = if(columnStr.isBlank()) "*"
                    else columnStr.removeSuffix(", ")
                return query("SELECT $columnStr FROM $tableName").flatten()
            }
        }
    }
}