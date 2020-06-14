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
 * Kelas yang merepresentasikan db perantara yang menghubungkan 2 tabel.
 */
data class FK_DB<FROM, TO>(var fkRow: ArrayList<FK_Row<FROM, TO>> = ArrayList()): Fk, Serializable{
    override fun getCount(): Int = fkRow.size
    override fun getFkId(pos: Int): String = fkRow[pos].toId
}