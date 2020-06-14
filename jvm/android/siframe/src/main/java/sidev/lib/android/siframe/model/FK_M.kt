package sidev.lib.android.siframe.model

import sidev.lib.android.siframe.model.intfc.Fk
import java.io.Serializable

/**
 * Model data yang berfungsi sebagai jembatan antar 2 data class.
 * Hal ini dilatarbelakangi oleh struktur data yang berubah. Awalnya Commodity memiliki 1 unit / satuan,
 * namun tahap ke-2 memiliki banyak satuan.
 *
 * Sebaiknya cara menghubungkan 2 data class jangan langsung menghubungkan ke classnya, namun menggunakan
 * peranta class IntermediateModel ini.
 *
 * Kelas ini hanya berlaku satu arah (dari fromId ke toId) dan tidak berlaku sebaliknya.
 *
 * Kelas ini hanya digunakan pada instance object, bkn merepresentasikan row tabel perantara pada DB.
 *
 * Kelas yang merepresentasikan FK pada model.
 */
data class FK_M<TO>(var toId: Array<String>, var toObj: Array<TO>?= null): Fk, Serializable {
    override fun getCount(): Int = toId.size
    override fun getFkId(pos: Int): String = toId[pos]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FK_M<*>

        if (!toId.contentEquals(other.toId)) return false
/*
        if (toObj != null) {
            if (other.toObj == null) return false
            if (!toObj!!.contentEquals(other.toObj!!)) return false
        } else if (other.toObj != null) return false
 */

        return true
    }

    override fun hashCode(): Int {
        var result = toId.contentHashCode()
//        result = 31 * result + (toObj?.contentHashCode() ?: 0)
        return result
    }

    
    fun equalsAll(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FK_M<*>

        if (!toId.contentEquals(other.toId)) return false
        if (toObj != null) {
            if (other.toObj == null) return false
            if (!toObj!!.contentEquals(other.toObj!!)) return false
        } else if (other.toObj != null) return false

        return true
    }

    fun hashCodeAll(): Int {
        var result = toId.contentHashCode()
        result = 31 * result + (toObj?.contentHashCode() ?: 0)
        return result
    }
}