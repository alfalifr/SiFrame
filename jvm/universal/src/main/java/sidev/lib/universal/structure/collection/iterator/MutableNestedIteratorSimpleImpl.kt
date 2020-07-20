package sidev.lib.universal.structure.collection.iterator

import android.view.ViewGroup

/**
 * Digunakan untuk melakukan iterasi terhadap data yg memiliki banyak keturunan.
 * Sebagai contoh adalah [ViewGroup] pada Android yg punya banyak child view.
 *
 * Iterasi dilakukan menggunakan metode DEPTH-FIRST PRE-ORDER.
 */
abstract class MutableNestedIteratorSimpleImpl<T>(startIterator: Iterator<T>?)
    : NestedIteratorSimpleImpl<T>(startIterator), MutableNestedIteratorSimple<T>{
    constructor(startIterable: Iterable<T>): this(startIterable.iterator())
    constructor(start: T): this((start as? Iterable<T>)?.iterator()){
        this.start= start
    }

    override fun remove() { /* abaikan karena kelas ini bkn list */ }
}