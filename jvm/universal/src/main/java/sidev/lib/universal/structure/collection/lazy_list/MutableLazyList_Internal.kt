package sidev.lib.universal.structure.collection.lazy_list

import sidev.lib.universal.`fun`.takeFirst

internal interface MutableLazyList_Internal<T>:
    MutableLazyList<T> {
    override val iteratorList: MutableList<Iterator<T>>
    override var builderIterator: Iterator<T>

    override fun iteratorHasNext(): Boolean {
        var hasNext= try{ builderIterator.hasNext() }
        catch(e: UninitializedPropertyAccessException){ false }

        while(!hasNext && iteratorList.isNotEmpty()){
            builderIterator= iteratorList.takeFirst()
            hasNext= builderIterator.hasNext()
        }
        return hasNext
    }
    override fun addIterator(itr: Iterator<T>): Boolean = iteratorList.add(itr)
}