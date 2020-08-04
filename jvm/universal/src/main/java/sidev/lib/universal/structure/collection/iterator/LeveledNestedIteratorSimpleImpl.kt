package sidev.lib.universal.structure.collection.iterator

import sidev.lib.universal.`fun`.notNullTo
import sidev.lib.universal.`fun`.toOtherIterator
import sidev.lib.universal.`fun`.withLevel
import sidev.lib.universal.structure.data.LeveledValue

abstract class LeveledNestedIteratorSimpleImpl<T>(/*private val */startIterator: Iterator<T>?)
    : LeveledNestedIteratorImpl<T, T>(startIterator) {
    constructor(startInputIterable: Iterable<T>): this(startInputIterable.iterator())
    constructor(start: T?): this((start as? Iterable<T>)?.iterator()){
        this.start= start?.withLevel()
    }

    final override val activeInputLines: ArrayList<Iterator<LeveledValue<T>>>
        get() = super.activeOutputLines
    final override var activeInputIterator: Iterator<LeveledValue<T>>?
        get() = super.activeOutputIterator
        set(value) { super.activeOutputIterator= value }
/*
    final override val activeInputLineLevels: ArrayList<Int>
        get() = super.activeOutputLineLevels
    final override var activeInputIteratorLevel: Int
        get() = super.activeOutputIteratorLevel
        set(value) { super.activeOutputIteratorLevel= value }
 */
    /*
    override val activeOutputLines: ArrayList<Iterator<T>>
        get() = super.activeInputLines
    override var activeOutputIterator: Iterator<T>?
        get() = super.activeInputIterator
        set(value) {
            super.activeInputIterator= value
        }
    override fun addOutputIterator(outItr: Iterator<LeveledValue<T>>) {
        super.addOutputIterator(outItr)
        activeOutputLineLevels += ++activeOutputIteratorLevel
    }

    override fun changeLastActiveOutputIterator(currentActiveOutputIterator: Iterator<LeveledValue<T>>) {
        super.changeLastActiveOutputIterator(currentActiveOutputIterator)
        activeOutputLineLevels -= activeOutputIteratorLevel--
    }
 */
    final override fun getOutputIterator(nowInput: LeveledValue<T>): Iterator<LeveledValue<T>>? = getInputIterator(nowInput) //Yg di-override adalah output karena getInputIterator menambah nilai level.
    final override fun addInputIterator(inItr: Iterator<LeveledValue<T>>) = addOutputIterator(inItr)
    final override fun changeLastActiveInputIterator(currentActiveInputIterator: Iterator<LeveledValue<T>>)
            = changeLastActiveOutputIterator(currentActiveInputIterator)

//    internal open val activeOutputLineLevels= ArrayList<Int>()
//    internal open val activeInputLineLevels= ArrayList<Int>()
//    internal open var activeOutputIteratorLevel: Int= -1
//    internal open var activeInputIteratorLevel: Int= -1

    /*
    override fun getOutputIterator(nowInput: I): Iterator<LeveledValue<O>>? {
        return getOutputValueIterator(nowInput).notNullTo { outItr ->
            val outItrLevel= getOutputIteratorLevel(outItr, nowInput, activeOutputIteratorLevel +1) //activeOutputIteratorLevel +1 karena penambahan level dilakukan setelah pengambilan outputIterator.
            outItr.toOtherIterator { LeveledValue(outItrLevel, it) }
        }
    }
 */

    //    override fun getOutputIteratorLevel(outputIterator: Iterator<O>, fromInput: I, level: Int): Int = level
//    override fun getInputIteratorLevel(inputIterator: Iterator<I>, fromOutput: O, level: Int): Int = level
/*
    override fun addInputIterator(inItr: Iterator<I>) {
        super.addInputIterator(inItr)
        activeInputLineLevels += ++activeInputIteratorLevel
    }

    override fun addOutputIterator(outItr: Iterator<LeveledValue<O>>) {
        super.addOutputIterator(outItr)
        activeOutputLineLevels += ++activeOutputIteratorLevel
    }
 */
/*
    override fun addOutputIterator(outItr: Iterator<O>) {
        super.addOutputIterator(outItr)
        activeOutputLineLevels += ++activeOutputIteratorLevel
    }
 */
/*
    override fun changeLastActiveInputIterator(currentActiveInputIterator: Iterator<I>) {
        super.changeLastActiveInputIterator(currentActiveInputIterator)
        activeInputLineLevels -= activeInputIteratorLevel--
    }

    override fun changeLastActiveOutputIterator(currentActiveOutputIterator: Iterator<LeveledValue<O>>) {
        super.changeLastActiveOutputIterator(currentActiveOutputIterator)
        activeOutputLineLevels -= activeOutputIteratorLevel--
    }
 */
/*
    override fun changeLastActiveOutputIterator(currentActiveOutputIterator: Iterator<O>) {
        super.changeLastActiveOutputIterator(currentActiveOutputIterator)
        activeOutputLineLevels -= activeOutputIteratorLevel--
    }
 */
}