package sidev.lib.implementation.util

import android.content.Context
import id.go.surabaya.ediscont.utilities.modelutil.fkmId
import sidev.lib.android.siframe.tool.SQLiteHandler
import sidev.lib.implementation.model.Page

class PageSqlite(c: Context) : SQLiteHandler<Page>(c){
    override val modelClass: Class<Page>
        get() = Page::class.java

    override fun createModel(petaNilai: Map<String, *>): Page {
        val id= petaNilai[attribName[0]] as String
        val contentFk= petaNilai[attribName[1]] as String
        val no= petaNilai[attribName[2]] as Int

        return Page(id, fkmId(contentFk), no)
    }
}