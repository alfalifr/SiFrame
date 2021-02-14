package sidev.lib.implementation.util

import android.content.Context
import sidev.lib.android.siframe.tool.util.`fun`.fkmId
import sidev.lib.android.siframe.tool.SQLiteHandler
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.implementation.model.Page

class PageSqlite(c: Context) : SQLiteHandler<Page>(c){
    override val modelClass: Class<Page>
        get() = Page::class.java

    override fun createModel(petaNilai: Map<String, *>): Page {
        loge("PageSqlite.createModel() petaNilai= $petaNilai")
        val id= petaNilai["id"] as String //attribName[0]
        val contentFk= petaNilai["contentFk"] as String //attribName[1]
        val no= petaNilai["no"] as Int //attribName[2]

        return Page(id, fkmId(contentFk), no)
    }
}