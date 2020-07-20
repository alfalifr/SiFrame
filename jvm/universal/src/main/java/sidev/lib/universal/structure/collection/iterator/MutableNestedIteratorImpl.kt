package sidev.lib.universal.structure.collection.iterator

abstract class MutableNestedIteratorImpl<I, O>(startInputIterator: Iterator<I>?)
    : NestedIteratorImpl<I, O>(startInputIterator), MutableNestedIterator<I, O>{
    constructor(startInputIterable: Iterable<I>): this(startInputIterable.iterator())
    constructor(start: I): this((start as? Iterable<I>)?.iterator()){
        this.start= start
    }

    override fun remove() { /* abaikan karena kelas ini bkn list */ }
}