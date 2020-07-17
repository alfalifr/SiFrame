package sidev.lib.universal.structure

/**
 * Turunan [HashMap] yg sumber datanya berasal dari sequence sehingga
 * data yg disimpan tidak besar di awal dan sesuai kebutuhan.
 */
open class LazyHashMap<K, V>(inSequence: Sequence<Pair<K, V>>)
    : HashMap<K, V>(), LazyList<MutableMap.MutableEntry<K, V>>{

    override val builderIterator: Iterator<MutableMap.MutableEntry<K, V>>
        = object : Iterator<MutableMap.MutableEntry<K, V>>{
        private val sequenceItr= inSequence.iterator()

        override fun hasNext(): Boolean = sequenceItr.hasNext()
        override fun next(): MutableMap.MutableEntry<K, V> {
            val next= sequenceItr.next()

            return object : MutableMap.MutableEntry<K, V>{
                override val key: K= next.first
                override var value: V= next.second

                override fun setValue(newValue: V): V {
                    val oldVal= value
                    value= newValue
                    return oldVal
                }
            }
        }
    }

    override fun contains(element: MutableMap.MutableEntry<K, V>): Boolean {
        val existing= this[element.key]
        return existing == element.value
    }

    override fun containsAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
        var res= true
        for(element in elements){
            val existing= this[element.key]
            res= res && existing == element.value
        }
        return res
    }

    override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>>
            = (this as MutableMap<K, V>).iterator()

    override fun get(key: K): V? {
        val existing= super.get(key)
        if(existing == null && builderIterator.hasNext()){
            var pairItr = builderIterator.next()
            while(pairItr.key != key && builderIterator.hasNext()){
                pairItr= builderIterator.next()
            }
            return if(pairItr.key != key) null
            else {
                set(pairItr.key, pairItr.value)
                pairItr.value
            }
        }
        return existing
    }
}