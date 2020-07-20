package sidev.lib.universal.structure.collection.lazy_list

/** [LazyList] yg elemen nya bisa ditambah menggunakan iterator. */
interface MutableLazyList<T> :
    LazyList<T> {
    val iteratorList: List<Iterator<T>>

//    override fun iteratorHasNext(): Boolean
    fun addIterator(itr: Iterator<T>): Boolean
}