package sidev.lib.universal.structure

/**
 * Turunan [ArrayList] yg sumber datanya berasal dari sequence sehingga
 * data yg disimpan tidak besar di awal dan sesuai kebutuhan.
 */
open class CachedSequence<T>(inSequence: Sequence<T>): ArrayList<T>(), LazyList<T>{
    override val builderIterator: Iterator<T> = inSequence.iterator()

    override fun get(index: Int): T {
        while(builderIterator.hasNext() && index >= size){
            add(builderIterator.next())
        }
        if(index >= size)
            throw ArrayIndexOutOfBoundsException("CachedSequence: ${this::class.simpleName} hanya memiliki element sebanyak $size tapi index= $index")
        return super.get(index)
    }
}