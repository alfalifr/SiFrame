package sidev.lib.android.std.tool.util

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import sidev.lib.android.std.tool.util.`fun`.loge

object _IntentUtil{
    const val TYPE_ALL= "*/*"
    const val TYPE_IMAGE= "image/*"
    const val TYPE_TEXT= "text/*"

    /**
     * @param [actOrFrag] sesuai namanya, isinya adalah Activity atau Fragment.
     */
    fun pickFile(actOrFrag: Any, type: String, title: String, reqCode: Int){
        val i= Intent()
        i.type= type //"*/*"
        i.action= Intent.ACTION_GET_CONTENT

        val iNew= Intent.createChooser(i, title)
        when(actOrFrag){
            is Activity -> actOrFrag.startActivityForResult(iNew, reqCode)
            is Fragment -> actOrFrag.startActivityForResult(iNew, reqCode)
            else -> loge("pickFile() gagal")
        }
    }
}