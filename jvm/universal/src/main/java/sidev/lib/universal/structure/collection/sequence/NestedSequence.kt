package sidev.lib.universal.structure.collection.sequence

import sidev.lib.universal.annotation.Interface
import sidev.lib.universal.structure.collection.iterator.NestedIterator

/**
 * Struktur data yg sama seperti [Sequence] namun juga menyertakan
 * sequence turunan (nested sequence).
 */
@Interface
interface NestedSequence<T>: Sequence<T>{
    override fun iterator(): NestedIterator<*, T>
}