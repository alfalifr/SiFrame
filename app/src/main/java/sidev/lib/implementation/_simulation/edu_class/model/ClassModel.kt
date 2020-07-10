package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M
import sidev.lib.android.siframe.model.PictModel
import java.io.Serializable

/**
 * Model untuk kelas.
 *
 * @param name nama kelas
 * @param subname pembagian kelasnya, misalnya kelas A, B, C, dkk.
 * @param teacher nama pengajar.
 * @param sks jml.
 * @param moduleList fk ke model Module yg isinya bisa lebih dari satu.
 * @param img berisi gmbar. Jika di db server, dapat hanya berisi url ke gmbr.
 */
data class ClassModel(private val _id: String,
                var name: String, var subname: String= "_",
                var teacher: String, var sks: Int,
                var moduleList: FK_M<Module>?,
                var img: PictModel?): DataWithId(_id)