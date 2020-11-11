package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.implementation.model.Content
import java.io.Serializable

/**
 * Model yg merepresentasikan notifkasi pada menu Notifikasi.
 *
 * @param timestamp format $Const.FORMAT_TIMESTAMP "YYYY-MM-dd HH:mm:ss"
 */
data class Notif(private val _id: String,
                 var title: String?, var desc: String,
                 var timestamp: String): DataWithId<Notif>(_id){
    @Deprecated("Masih blum diimplement scr benar.", ReplaceWith("copy()"))
    override fun copy_(prop: Map<String, Any?>): Notif = copy()
}