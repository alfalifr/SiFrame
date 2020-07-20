package sidev.lib.universal.structure.collection.iterator

import android.view.ViewGroup
import sidev.lib.universal.annotation.Interface

/**
 * Digunakan untuk melakukan iterasi terhadap data yg memiliki banyak keturunan.
 * Sebagai contoh adalah [ViewGroup] pada Android yg punya banyak child view.
 *
 * Iterasi dilakukan menggunakan metode DEPTH-FIRST PRE-ORDER.
 */
@Interface
interface NestedIteratorSimple<T>: NestedIterator<T, T>, Iterator<T>{
    override fun getOutputIterator(nowInput: T): Iterator<T>?
    override fun getInputIterator(nowOutput: T): Iterator<T>?
}