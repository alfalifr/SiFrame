package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId

/**
 * Gak perlu disimpen di bd gpp.
 * Model ini digunakan untuk merepresentasikan tgl dan minggu perkuliahan terkini pada menu presensi.
 */
data class WeekTime(private val _id: String, var date: String, var week: Int): DataWithId<WeekTime>(_id){
    @Deprecated("Masih blum diimplement scr benar.", ReplaceWith("copy()"))
    override fun copy_(prop: Map<String, Any?>): WeekTime = copy()
}