package sidev.lib.universal.structure.collection.iterator

import sidev.lib.universal.structure.data.LeveledValue

/** [NestedIterator] yg disertai dg level hirarki untuk setiap next. */
interface LeveledNestedIterator<I, O>: NestedIterator<LeveledValue<I>, LeveledValue<O>>{
    /** Mengambil level untuk [outputIterator] yg dihasilkan dari [fromInput] dg nilai default level [level]. */
    fun getOutputIteratorLevel(outputIterator: Iterator<O>, fromInput: I, level: Int): Int
    fun getInputIteratorLevel(inputIterator: Iterator<I>, fromOutput: O, level: Int): Int

    /** Mengambil iterator yg berisi nilai [O] tanpa level. */
    fun getOutputValueIterator(nowInput: I): Iterator<O>?
    fun getInputValueIterator(nowOutput: O): Iterator<I>?
}