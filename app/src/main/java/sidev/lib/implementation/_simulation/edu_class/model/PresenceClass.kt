package sidev.kuliah.tekber.edu_class.model

import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M
import java.io.Serializable

/**
 * Model yg merepresentasikan tiap kelas pada menu presensi.
 *
 * @param scheduleList jadwal kelas. Misalkan dalam sehari ada 2 pertemuan, scheduleList berisi 2 ScheduleModel.
 * @param clazz isinya cuma 1 fk ke ClassModel.
 * @param presenceList berisi fk ke tiap item presensi.
 * @param presentCount berisi jml dari item yg ada pada presenceList yg statusnya == Const.STATUS_PRESENCE_PRESENT.
 * @param ijinCount berisi jml dari item yg ada pada presenceList yg statusnya == Const.STATUS_PRESENCE_IJIN.
 * @param alphaCount berisi jml dari item yg ada pada presenceList yg statusnya == Const.STATUS_PRESENCE_ALPHA.
 */
data class PresenceClass(private val _id: String,
                         var clazz: FK_M<ClassModel>?,
                         var scheduleList: FK_M<ScheduleModel>?,
                         var presenceList: FK_M<Presence_>?,
                         var presentCount: Int= 0,
                         var ijinCount: Int= 0,
                         var alphaCount: Int= 0): DataWithId(_id)