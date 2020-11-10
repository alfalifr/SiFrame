package sidev.lib.android.siframe.tool

import android.graphics.drawable.Drawable
//import android.util.Size
import sidev.lib.android.siframe.tool.support.Size
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import sidev.lib.android.std.tool.util.`fun`.bg
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.*


open class ViewContentExtractor{
    enum class Content{
        TEXT,
        DRAWABLE,
        BG_DRAWABLE,
        SIZE,
        ORIENTATION,
    }

    val contents: HashMap<String, MutableMap<Content, Any?>> by lazy {
        HashMap<String, MutableMap<Content, Any?>>()
    }
    protected val views: HashMap<String, View> by lazy { HashMap<String, View>() }


    /**
     * Digunakan untuk menyimpan view [v] ke dalam [views] dg [id].
     * Jika [id] sebelumnya sudah terdapat pada [views], maka input [v] akan dijadikan
     * sbg target restorasi data yg tersimpan di dalam [contents].
     */
    fun registerView(id: String, v: View){
        contents[id].notNull{
            restoreViewContent(id, v)
            //Orientasi layar diambil di awal karena saat extractViewContent di mana
            // dipanggil saat onDestroy, orientasi layar udah berubah.
            it[Content.ORIENTATION]= v.context.resources.configuration.orientation
        }.isNull {
            contents[id]= mutableMapOf<Content, Any?>(
                Content.ORIENTATION to v.context.resources.configuration.orientation
            )
        }
        views[id]= v
    }

    /**
     * Digunakan untuk mengekstrak semua view yg ada di dalam [views].
     */
    fun extractAllViewContent(){
        for((id, view) in views)
            extractViewContent(id, view)
    }

    fun extractViewContent(id: String, v: View?){
        (v ?: views[id]).notNull { view ->
            var map= contents[id]
            if(map == null){
                map= mutableMapOf(
                    Content.SIZE to Size(view.width, view.height),
                    Content.BG_DRAWABLE to view.background,
                    Content.ORIENTATION to view.context.resources.configuration.orientation
                )
            } else{
                map[Content.SIZE]= Size(view.width, view.height)
                map[Content.BG_DRAWABLE]= view.background
            }

//            val ori= map[Content.ORIENTATION]
//            loge("ori == Configuration.ORIENTATION_PORTRAIT => ${ori == Configuration.ORIENTATION_PORTRAIT}")
//            loge("ori == Configuration.ORIENTATION_LANDSCAPE => ${ori == Configuration.ORIENTATION_LANDSCAPE}")
            when(view){
                is ImageView -> map[Content.DRAWABLE]= view.drawable
                is TextView -> map[Content.TEXT]= view.text.toString()
            }
            for(c in map){
                val key= c.key
                val content= c.value
                val contentNull= content == null
                loge("extractViewContent() id= $id key= $key contentNull= $contentNull content= $content")
            }
            contents[id]= map
        }
    }

    /**
     * Digunakan untuk menghapus seluruh isi [views].
     * Fungsi ini tidak menghapus isi [contents].
     */
    fun clearAllSavedViews(){
        views.clear()
    }

    /**
     * Digunakan untuk merestorasi semua view yg ada di dalam [views].
     * @return true jika proses restorasi seluruh isi [views] berhasil.
     */
    fun restoreAllViewContent(): Boolean{
        var bool= true
        for((id, view) in views)
            bool= bool && restoreViewContent(id, view)
        return bool
    }

    /**
     * @return true jika proses restorasi berhasil.
     */
    fun restoreViewContent(id: String, v: View?): Boolean{
        return contents[id].notNullTo { contents ->
            (v ?: views[id]).notNullTo { view ->
                for(content in contents){
                    when(content.key){
                        Content.SIZE -> {
                            val size= content.value as Size
//                            val isOnScreenRotation= false
//                                contents[Content.ORIENTATION] != view.context.resources.configuration.orientation
                            val lp= view.layoutParams

//                            loge("isOnScreenRotation= $isOnScreenRotation contents[Content.ORIENTATION] == null => ${contents[Content.ORIENTATION] == null}")
                            loge("size= $size")

                            if(lp.width != ViewGroup.LayoutParams.MATCH_PARENT
                                && lp.width != ViewGroup.LayoutParams.WRAP_CONTENT){
                                lp.width= size.width
//                                    if(!isOnScreenRotation) size.width else size.height
                            }

                            if(lp.height != ViewGroup.LayoutParams.MATCH_PARENT
                                && lp.height != ViewGroup.LayoutParams.WRAP_CONTENT){
                                lp.height= size.height
//                                    if(!isOnScreenRotation) size.height else size.width
                            }
                        }
                        Content.BG_DRAWABLE -> {
                            val bg= content.value as? Drawable
                            view.bg= bg
                        }
                        Content.TEXT -> {
                            view.asNotNull { tv: TextView -> tv.text= content.value as? String }
                                .asnt<Any, TextView> { loge("restoreViewContent() v bkn merupakan TextView, Content.TEXT diabaikan.") }
                        }
                        Content.DRAWABLE -> {
                            view.asNotNull { iv: ImageView -> iv.setImageDrawable(content.value as? Drawable) }
                                .asnt<Any, ImageView> { loge("restoreViewContent() v bkn merupakan ImageView, Content.DRAWABLE diabaikan.") }
                        }
                    }
                }
                true
            }
        } == true
    }
}