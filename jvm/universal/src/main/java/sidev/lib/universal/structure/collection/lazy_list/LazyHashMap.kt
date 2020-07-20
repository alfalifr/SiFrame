package sidev.lib.universal.structure.collection.lazy_list

import sidev.lib.universal.`fun`.*


open class LazyHashMap<K, V>()
    : HashMap<K, V>(), MutableCachedLazyList_Internal<K, V>, MutableIterable<MutableMap.MutableEntry<K, V>> {
    constructor(iterator: Iterator<Pair<K, V>>): this(){
        builderIterator= iterator
    }
    constructor(iterable: Iterable<Pair<K, V>>): this(){
        builderIterator= iterable.iterator()
    }
    constructor(inSequence: Sequence<Pair<K, V>>): this(){
        builderIterator= inSequence.iterator()
    }

    /** Jika `true` maka jika value yg dipass ke [add] mengandung key yg sudah ada, maka nilai yg sudah ada akan ditimpa dg yg baru. */
    open var allowOverwrite: Boolean= true

    private var requestedAddedKey: K?= null

    final override val iteratorList: MutableList<Iterator<Pair<K, V>>> = ArrayList()
    final override lateinit var builderIterator: Iterator<Pair<K, V>>

    override fun getExisting(key: K): V? = entries.find { it.key == key }?.value
    override fun getExistingKey(value: V): K? = entries.find { it.value == value }?.key
    override fun containsExistingValue(value: V): Boolean = values.find { it == value } != null //containsValue(value)
    override fun containsExistingKey(key: K): Boolean = keys.find { it == key } != null //containsKey(key)

    override fun isNextMatched(key: K, addedNext: V): Boolean = key == requestedAddedKey

    override fun addNext(key: K, value: V): Boolean{
        val canAdd= allowOverwrite || !containsExistingKey(key)
        if(canAdd) set(key, value)
        return canAdd
    }

    override fun get(key: K): V? {
        requestedAddedKey= key
        return findNext(key)
    }

    override fun containsValue(value: V): Boolean = containsNextValue(value)
    override fun containsKey(key: K): Boolean = containsNextKey(key)

    override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>>
        = object : MutableIterator<MutableMap.MutableEntry<K, V>>{
        var index= 0
        val initialIndices= 0 until size
        val existingKeys= keys
        var nowKey: K?= null

        override fun hasNext(): Boolean = index in initialIndices || iteratorHasNext()

        override fun next(): MutableMap.MutableEntry<K, V>{
            val next= if(index in initialIndices) entries.find { it.key == existingKeys.elementAt(index).also { nowKey= it } }!!
                else getNext()!!.toMutableMapEntry()
            index++
            return next
        }

        override fun remove() { remove(nowKey) }
    }
}

/*
open class LazyHashMap<K, V>(inSequence: Sequence<Pair<K, V>>)
    : HashMap<K, V>(), CachedLazyList<K, MutableMap.MutableEntry<K, V>>{
    /** Jika `true` maka jika value yg dipass ke [add] mengandung key yg sudah ada, maka nilai yg sudah ada akan ditimpa dg yg baru. */
    open var allowOverwrite: Boolean= true

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


    override fun getExisting(key: K): MutableMap.MutableEntry<K, V>? = entries.find { it.key == key }

    override fun getExistingKey(value: MutableMap.MutableEntry<K, V>): K? = keys.find { it == value.key }

    override fun isNextMatched(key: K, addedNext: MutableMap.MutableEntry<K, V>): Boolean = addedNext.key == key

    override fun add(value: MutableMap.MutableEntry<K, V>): Boolean {
        val canAdd= allowOverwrite || getExistingKey(value) == null
        if(canAdd)
            set(value.key, value.value)
        return canAdd
    }

    override fun get(key: K): V? = findNext(key)?.value
    override fun containsKey(key: K): Boolean = containsNextKey(key)
    override fun containsValue(value: V): Boolean = containsNextValue(value)

    override fun containsValue(value: MutableMap.MutableEntry<K, V>): Boolean = contains(value.key)
}
 */
/*
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
 */