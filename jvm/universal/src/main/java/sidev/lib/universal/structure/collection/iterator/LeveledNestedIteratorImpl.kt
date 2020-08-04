package sidev.lib.universal.structure.collection.iterator

import sidev.lib.universal.`fun`.notNullTo
import sidev.lib.universal.`fun`.toOtherIterator
import sidev.lib.universal.structure.data.LeveledValue
import sidev.lib.universal.`fun`.removeLast
import sidev.lib.universal.`fun`.withLevel

abstract class LeveledNestedIteratorImpl<I, O>(/*private val */startInputIterator: Iterator<I>?)
    : NestedIteratorImpl<LeveledValue<I>, LeveledValue<O>>(
        startInputIterator?.toOtherIterator { LeveledValue(0, it) }
), LeveledNestedIterator<I, O> {
    constructor(startInputIterable: Iterable<I>): this(startInputIterable.iterator())
    constructor(start: I?): this((start as? Iterable<I>)?.iterator()){
        this.start= start?.withLevel()
    }
/*
    internal open val activeOutputLineLevels= ArrayList<Int>()
    internal open val activeInputLineLevels= ArrayList<Int>()
    internal open var activeOutputIteratorLevel: Int= -1
    internal open var activeInputIteratorLevel: Int= -1
 */

    override fun getOutputIterator(nowInput: LeveledValue<I>): Iterator<LeveledValue<O>>? {
        return getOutputValueIterator(nowInput.value).notNullTo { outItr ->
            val outItrLevel= getOutputIteratorLevel(outItr, nowInput.value, nowInput.level)
            outItr.toOtherIterator { LeveledValue(outItrLevel, it) }
        }
    }

    final override fun getInputIterator(nowOutput: LeveledValue<O>): Iterator<LeveledValue<I>>? {
        return getInputValueIterator(nowOutput.value).notNullTo { inItr ->
            val inItrLevel= getInputIteratorLevel(inItr, nowOutput.value, nowOutput.level +1)
            inItr.toOtherIterator { LeveledValue(inItrLevel, it) }
        }
    }

    override fun getOutputIteratorLevel(outputIterator: Iterator<O>, fromInput: I, level: Int): Int = level
    override fun getInputIteratorLevel(inputIterator: Iterator<I>, fromOutput: O, level: Int): Int = level
/*
    override fun addInputIterator(inItr: Iterator<I>) {
        super.addInputIterator(inItr)
        activeInputLineLevels += ++activeInputIteratorLevel
    }
    override fun getOutputIterator(nowInput: I): Iterator<LeveledValue<O>>? {
        return getOutputValueIterator(nowInput).notNullTo { outItr ->
            val outItrLevel= getOutputIteratorLevel(outItr, nowInput, activeOutputIteratorLevel +1) //activeOutputIteratorLevel +1 karena penambahan level dilakukan setelah pengambilan outputIterator.
            outItr.toOtherIterator { LeveledValue(outItrLevel, it) }
        }
    }
 */

/*
    override fun addOutputIterator(outItr: Iterator<LeveledValue<O>>) {
        super.addOutputIterator(outItr)
        activeOutputLineLevels += ++activeOutputIteratorLevel
    }
    override fun addOutputIterator(outItr: Iterator<O>) {
        super.addOutputIterator(outItr)
        activeOutputLineLevels += ++activeOutputIteratorLevel
    }
    override fun changeLastActiveInputIterator(currentActiveInputIterator: Iterator<I>) {
        super.changeLastActiveInputIterator(currentActiveInputIterator)
        activeInputLineLevels.removeLast(activeInputIteratorLevel) //--
//        activeInputIteratorLevel= activeInputLineLevels.last()
    }
    override fun changeLastActiveOutputIterator(currentActiveOutputIterator: Iterator<LeveledValue<O>>) {
        super.changeLastActiveOutputIterator(currentActiveOutputIterator)
        activeOutputLineLevels.removeLast(activeOutputIteratorLevel) //--
//        activeOutputIteratorLevel= activeOutputLineLevels.last()
    }
 */


/*
    override fun changeLastActiveOutputIterator(currentActiveOutputIterator: Iterator<O>) {
        super.changeLastActiveOutputIterator(currentActiveOutputIterator)
        activeOutputLineLevels -= activeOutputIteratorLevel--
    }
 */
}