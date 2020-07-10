package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId

/**
 * Gak perlu disimpen di bd gpp.
 * Model ini digunakan untuk merepresentasikan tgl dan minggu perkuliahan terkini pada menu presensi.
 */
data class WeekTime(private val _id: String, var date: String, var week: Int): DataWithId(_id)