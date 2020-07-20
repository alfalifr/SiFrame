package sidev.lib.universal.structure.collection.iterator

import sidev.lib.universal.annotation.Interface

@Interface
interface SkippableIterator<T>: Iterator<T>{
    /** @return true maka [now] akan dilewati. */
    fun skip(now: T): Boolean
}