package sidev.lib.universal.structure

import sidev.lib.universal.annotation.Interface

@Interface
interface SkippableIterator<T>: Iterator<T>{
    /**
     * @return true maka [now] akan dilewati
     */
    fun skip(now: T): Boolean
}