package sidev.lib.universal.structure

import sidev.lib.universal.annotation.Interface

/**
 * Struktur data yg sama seperti [Sequence] namun juga menyertakan
 * sequence turunan (nested sequence).
 */
@Interface
interface NestedSequence<T>: Sequence<T>{
    override fun iterator(): NestedIterator<T>
}