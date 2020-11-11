package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId
import java.io.Serializable

/**
 * @param role nilai int nya sesuai dg Const.ROLE_STUDENT dkk.
 */
data class Profil(private val _id: String,
                  var role: Int,
                  var uname: String, var name: String, var nrp: String, var email: String): DataWithId<Profil>(_id){
    @Deprecated("Masih blum diimplement scr benar.", ReplaceWith("copy()"))
    override fun copy_(prop: Map<String, Any?>): Profil = copy()
}