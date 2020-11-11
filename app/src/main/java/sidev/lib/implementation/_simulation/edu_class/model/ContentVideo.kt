package sidev.kuliah.tekber.edu_class.model

//import sidev.kuliah.tekber.edu_class.intfc.Content
import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.implementation._simulation.edu_class.model.Content
import java.io.Serializable

/**
 * Model yg berisi video dan catatan di bawahnya.
 * @param link ke video
 */
data class ContentVideo(private val _id: String, var link: String, var note: String?): Content<ContentVideo>(_id){
    @Deprecated("Masih blum diimplement scr benar.", ReplaceWith("copy()"))
    override fun copy_(prop: Map<String, Any?>): ContentVideo = copy()
}