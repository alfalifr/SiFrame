package sidev.lib.universal.structure.collection.lazy_list

import sidev.lib.universal.`fun`.asNotNullTo
import sidev.lib.universal.structure.collection.iterator.NestedIteratorImpl

/**
 * List yg bersifat lazy, yaitu berukuran kecil di awal dan akan berkembang
 * seiring penggunaannya yg melibatkan pemanggilan [builderIterator].
 */
interface LazyList<T>{
    /** Digunakan untuk mengambil iterator yg berfungsi sbg pengisi [LazyList] ini. */
    val builderIterator: Iterator<T>
    fun iteratorHasNext(): Boolean = builderIterator.hasNext()
}