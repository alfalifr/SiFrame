package sidev.kuliah.tekber.edu_class.model

//import sidev.kuliah.tekber.edu_class.intfc.Content
import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.implementation._simulation.edu_class.model.Content
import java.io.Serializable

/**
 * Model yg digunakan untuk menyimpan data bacaan yg berisi:
 * @param title
 * @param desc
 */
data class ContentRead(private val _id: String, var title: String?, var desc: String): Content<ContentRead>(_id){
    @Deprecated("Masih blum diimplement scr benar.", ReplaceWith("copy()"))
    override fun copy_(prop: Map<String, Any?>): ContentRead = copy()
}