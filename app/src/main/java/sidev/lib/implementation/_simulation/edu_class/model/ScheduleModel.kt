package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M
import java.io.Serializable

/**
 * Model yg merepresentasikan jadwal pertemuan tiap kelas
 * @param clazz isinya cuma 1 fk ke ClassModel.
 */
data class ScheduleModel(private val _id: String,
                         var clazz: FK_M<ClassModel>?,
                        var day: String, var duration: Duration, var place: String): DataWithId(_id)