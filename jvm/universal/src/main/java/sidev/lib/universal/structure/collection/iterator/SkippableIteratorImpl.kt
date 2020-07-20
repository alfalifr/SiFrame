package sidev.lib.universal.structure.collection.iterator

abstract class SkippableIteratorImpl<T>(private val startIterator: Iterator<T>) :
    SkippableIterator<T> {
    constructor(startInputIterable: Iterable<T>): this(startInputIterable.iterator())

    private var now: T?= null
    private var nextState: Int= -1 //-1 blum diketahui, 0 tidak ada lagi iterasi, 1 lanjut.

    private fun calcNext(): Boolean{
        nextState= 0
        while(startIterator.hasNext() && nextState == 0){
            val next= startIterator.next()
            if(!skip(next)){
                nextState= 1
                now= next
            }
        }
        return nextState == 1
    }
    final override fun hasNext(): Boolean {
        if(nextState == -1) calcNext()
        return nextState == 1
    }
    final override fun next(): T{
        if(nextState == -1) calcNext()
        if(nextState == 0) throw NoSuchElementException("Isi dari iterator ${this::class.simpleName} udah habis")

        val res= now!!
        now= null
        nextState= -1
        return res
    }
    abstract override fun skip(now: T): Boolean
}