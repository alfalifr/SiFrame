package sidev.lib.universal.`fun`

import sidev.lib.universal.`val`.SuppressLiteral
import sidev.lib.universal.structure.collection.iterator.*
import sidev.lib.universal.structure.collection.sequence.NestedSequence
import sidev.lib.universal.structure.data.LeveledValue

fun <T> T.withLevel(level: Int= 0): LeveledValue<T> = LeveledValue(level, this)

fun <O> NestedSequence<O>.withLevel(): Sequence<LeveledValue<O>>
    = object : Sequence<LeveledValue<O>>{
    override fun iterator(): Iterator<LeveledValue<O>>
        = this@withLevel.iterator().withLevel()
}

@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <I, O> NestedIterator<I, O>.withLevel(): LeveledNestedIterator<I, O>{
    val startInputItr= if(this is NestedIteratorImpl) startInputIterator else null
    val start= if(this is NestedIteratorImpl) start else null

    return if(this is NestedIteratorSimple<*>){
        object : LeveledNestedIteratorSimpleImpl<O>(startInputItr as? Iterator<O>){
            init{ this.start= start?.withLevel() as? LeveledValue<O> }

            override fun getOutputValueIterator(nowInput: O): Iterator<O>?
                = this@withLevel.getOutputIterator(nowInput as I)

            override fun getInputValueIterator(nowOutput: O): Iterator<O>?
                    = getOutputValueIterator(nowOutput)

        } as LeveledNestedIterator<I, O>
    } else object : LeveledNestedIteratorImpl<I, O>(startInputItr){
        init{ this.start= start?.withLevel() }

        override fun getInputValueIterator(nowOutput: O): Iterator<I>?
            = this@withLevel.getInputIterator(nowOutput)

        override fun getOutputValueIterator(nowInput: I): Iterator<O>?
            = this@withLevel.getOutputIterator(nowInput)
    }
}