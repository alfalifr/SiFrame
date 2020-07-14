package sidev.lib.universal.structure

import android.view.ViewGroup
import sidev.lib.universal.annotation.Interface

/**
 * Digunakan untuk melakukan iterasi terhadap data yg memiliki banyak keturunan.
 * Sebagai contoh adalah [ViewGroup] pada Android yg punya banyak child view.
 *
 * Iterasi dilakukan menggunakan metode DEPTH-FIRST PRE-ORDER.
 */
@Interface
interface NestedIterator<T>: Iterator<T>{
    /**
     * @return null jika sudah tidak ada lagi nested iterator pada [now].
     */
    fun getIterator(now: T): Iterator<T>?
}