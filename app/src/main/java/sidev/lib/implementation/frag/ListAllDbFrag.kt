package sidev.lib.implementation.frag

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.View
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.std.tool.util._DbUtil
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.`val`._Config
import sidev.lib.android.std.tool.util.`fun`.toast
import sidev.lib.implementation.adp.StrAdp
import sidev.lib.implementation.util.PageSqlite


class ListAllDbFrag : RvFrag<StrAdp>(){
    val strList= ArrayList<String>()
    override fun initRvAdp(): StrAdp = StrAdp(context!!, strList)

    override fun _initView(layoutView: View) {
        val op= _DbUtil.SQLite.beginOp(context!!)
        op.listAllTableName().forEach { strList += it }
        strList += "========= column names =========="
        op.listTableAttribute(PageSqlite(context!!).tableName).forEach { strList += "${it.name} ${it.type.name} isPrimary= ${it.isPrimary}" }
        strList += "========= master column names =========="
        listMasterColNames()
        rvAdp.dataList= strList
//        rvAdp.notifyDataSetChanged_()
        loge("rvAdp.dataList?.size= ${rvAdp.dataList?.size}")
    }

    fun listMasterColNames(){
        val helper= object: SQLiteOpenHelper(context, _Config.DB_NAME, null, _Config.DB_VERSION){
            override fun onCreate(db: SQLiteDatabase?) { /*Abaikan dulu*/ }
            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { /*Abaikan dulu*/ }
        }
        val db= helper.writableDatabase
        val c: Cursor = db.rawQuery("SELECT * FROM sqlite_master", null) //WHERE type='table'
        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                for((i, name) in c.columnNames.withIndex())
                    strList += (name ?: "null") +" >> ${c.getString(i)}"
                c.moveToNext()
            }
        }
        c.close()
        db.close()
    }

    fun listAllDb(): String{
        val helper= object: SQLiteOpenHelper(context, _Config.DB_NAME, null, _Config.DB_VERSION){
            override fun onCreate(db: SQLiteDatabase?) {
                //Abaikan dulu
            }

            override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
                //Abaikan dulu
            }
        }
        val db= helper.writableDatabase
        val c: Cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)

        var tableNames= "Table name => "
        if (c.moveToFirst()) {
            var i= 0
            while (!c.isAfterLast()) {
                val tableName= "[${i++}] >> ${c.getString(0)}"
                strList += tableName
                tableNames += "$tableName, "
                c.moveToNext()
            }
        }
        tableNames= tableNames.removeSuffix(", ")
        c.close()
        db.close()
        toast(tableNames)
        loge(tableNames)
        return tableNames
    }

//    private class MasterSqlite: SQLiteHandler
}