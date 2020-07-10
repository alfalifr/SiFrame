package sidev.kuliah.tekber.edu_class.model

//import sidev.kuliah.tekber.edu_class.util.Const
import sidev.lib.android.siframe.model.DataWithId
import java.io.Serializable

/**
 * Model yg digunakan untuk merepresentasikan tiap item dalam presensi.
 *
 * @param date contoh: "17 Juni 2020".
 * @param status isinya int yg nilainya sesuai Const.STATUS_PRESENCE_PRESENT dkk.
 * @param news adalah berita acara.
 * @param excuseReason adalah keterangan ijin.
 * @param attachment digunakan untuk menyertakan file surat ijin.
 *          Jika di db dapat berupa url ke file.
 */
data class Presence_(private val _id: String,
                     var date: String, var status: Int,
                     var news: String?,
                     var excuseReason: String?,
                     var attachment: ArrayList<Any>?): DataWithId(_id)