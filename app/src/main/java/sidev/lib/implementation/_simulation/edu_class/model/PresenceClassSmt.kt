package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M
import sidev.lib.implementation.model.Content
import java.io.Serializable

/**
 * Model yg merepresentasikan kelompok PresenceClass dalam satu smt.
 * Sebenarnya di db model ini dapat dilebur dg PresenceClass dg menambahkan attrib smt di dalamnya.
 * @param smt berupa angka
 * @param presenceClassList berisi banyak fk ke PresenceClass.
 */
data class PresenceClassSmt(private val _id: String,
                    var smt: String,
                    var presenceClassList: FK_M<PresenceClass>?): DataWithId<PresenceClassSmt>(_id){
    @Deprecated("Masih blum diimplement scr benar.", ReplaceWith("copy()"))
    override fun copy_(prop: Map<String, Any?>): PresenceClassSmt = copy()
}