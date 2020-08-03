package sidev.lib.universal.structure.collection.lazy_list

import sidev.lib.universal.`fun`.isNegative
import sidev.lib.universal.`fun`.withKeyIndexed

/**
 * Turunan [ArrayList] yg sumber datanya berasal dari sequence sehingga
 * data yg disimpan tidak besar di awal dan sesuai kebutuhan.
 */
open class CachedSequence<T>(): ArrayList<T>(),
    MutableIndexedCachedLazyList_Internal<T> {
    constructor(iterator: Iterator<T>): this(){
        builderIterator= iterator.withKeyIndexed { index, value -> index }
    }
    constructor(iterable: Iterable<T>): this(){
        builderIterator= iterable.iterator().withKeyIndexed { index, value -> index }
    }
    constructor(inSequence: Sequence<T>): this(){
        builderIterator= inSequence.iterator().withKeyIndexed { index, value -> index }
    }

    final override val iteratorList: MutableList<Iterator<Pair<Int, T>>> = ArrayList()
    final override lateinit var builderIterator: Iterator<Pair<Int, T>>
    /**
     * Digunakan untuk menyocokan apakah hasil dari [findNext] sesuai dg index yg direquest.
     * Variabel ini digunakan pada [isNextMatched].
     */
    private var requestedGetIndex: Int= -1

    override fun getExisting(key: Int): T? = if(key in indices) super.get(key) else null
    override fun getExistingKey(value: T): Int?{
        val index= super.indexOf(value)
        return if(index >= 0) index else null
    }
    override fun containsExistingValue(value: T): Boolean = super.indexOf(value) >= 0
    override fun containsExistingKey(key: Int): Boolean = key in indices
    override fun isNextMatched(key: Int, addedNext: T): Boolean = key == requestedGetIndex //key in indices

    override fun addNext(key: Int, value: T): Boolean = add(value)
    override fun addValueIterator(itr: Iterator<T>): Boolean = addIterator(itr.withKeyIndexed{ index, _ -> index })

    //    /** @return -1 karena `key` dalam konteks ArrayList tidak penting. */
//    override fun extractKeyFrom(addedNext: T): Int = -1
    override fun get(index: Int): T {
        if(index.isNegative())
            throw ArrayIndexOutOfBoundsException("get() -> index tidak boleh negatif: \"$index\"")
        requestedGetIndex= index
        val el= findNext(index)
        requestedGetIndex= -1
        return el
            ?: throw ArrayIndexOutOfBoundsException("CachedSequence: ${this::class.simpleName} hanya memiliki element sebanyak $size tapi index= $index")
/*
        while(builderIterator.hasNext() && index >= size){
            add(builderIterator.next())
        }
        if(index >= size)
            throw ArrayIndexOutOfBoundsException("CachedSequence: ${this::class.simpleName} hanya memiliki element sebanyak $size tapi index= $index")
        return super.get(index)
 */
    }

    override fun contains(element: T): Boolean = containsNextValue(element)

    override fun indexOf(element: T): Int = findNextKey(element) ?: -1
    override fun lastIndexOf(element: T): Int {
        val index= super.lastIndexOf(element)
        return if(index >= 0) index
        else findNextKey(element) ?: -1
    }

    override fun iterator(): MutableIterator<T>
        = object : MutableIterator<T>{
        var index= 0
        val initialIndices= 0 until size

        override fun hasNext(): Boolean = index in initialIndices || iteratorHasNext()
        override fun next(): T = get(index++) //if(index in initialIndices) get(index++) else getNext()!!.second
        override fun remove(){ removeAt(--index) }
    }
}