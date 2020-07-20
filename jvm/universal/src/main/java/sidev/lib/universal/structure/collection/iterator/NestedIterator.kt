package sidev.lib.universal.structure.collection.iterator

import android.view.ViewGroup
import sidev.lib.universal.annotation.Interface

/**
 * Digunakan untuk melakukan iterasi terhadap data yg memiliki banyak keturunan.
 * Sebagai contoh adalah [ViewGroup] pada Android yg punya banyak child view.
 *
 * [I] adalah tipe data yg diinputkan selama iterasi untuk menghasilkan [Iterator] dg tipe [O],
 * yaitu tipe data yg dioutputkan saat iterasi.
 *
 * Iterasi dilakukan menggunakan metode DEPTH-FIRST PRE-ORDER.
 */
@Interface
interface NestedIterator<I, O>: Iterator<O>{
    /**
     * @return null jika sudah tidak ada lagi nested iterator pada [nowInput].
     */
    fun getOutputIterator(nowInput: I): Iterator<O>?
    /**
     * @return null jika sudah tidak ada lagi nested iterator pada [nowOutput].
     */
    fun getInputIterator(nowOutput: O): Iterator<I>?
}