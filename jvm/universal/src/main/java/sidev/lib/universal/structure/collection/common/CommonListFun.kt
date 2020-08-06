package sidev.lib.universal.structure.collection.common

import sidev.lib.universal.`fun`.sort
import sidev.lib.universal.`val`.SuppressLiteral


val CommonList<*, *>.indices: IntRange
    get()= 0 until size

fun <V> CommonIterable<V>.asCommonIndexedList(): CommonIndexedList<V> = (this as Iterable<V>).toList().asCommonIndexedList()!!
fun <V> CommonIterable<V>.asCommonIndexedMutableList(): CommonIndexedMutableList<V> = (this as Iterable<V>).toMutableList().asCommonIndexedMutableList()!!

@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <K, V> Any.asCommonList(): CommonList<K, V>?{
    return when(this){
        is CommonList<*, *> -> this as CommonList<K, V>
        is List<*> -> (this as List<V>).asCommonList() as CommonList<K, V>
        is Map<*, *> -> (this as Map<K, V>).asCommonList()
        is Array<*> -> (this as Array<V>).asCommonList() as CommonList<K, V>
        is ArrayWrapper<*> -> (this as ArrayWrapper<V>).asCommonList() as CommonList<K, V>
        else -> null
    }
}

@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <K, V> Any.asCommonMutableList(): CommonMutableList<K, V>?{
    return when(this){
        is CommonMutableList<*, *> -> this as CommonMutableList<K, V>
        is MutableList<*> -> (this as MutableList<V>).asCommonMutableList() as CommonMutableList<K, V>
        is MutableMap<*, *> -> (this as MutableMap<K, V>).asCommonMutableList()
        is Array<*> -> (this as Array<V>).asCommonMutableList() as CommonMutableList<K, V>
        is ArrayWrapper<*> -> (this as ArrayWrapper<V>).asCommonMutableList() as CommonMutableList<K, V>
        else -> null
    }
}

@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <V> Any.asCommonIndexedList(): CommonIndexedList<V>?{
    return when(this){
        //is CommonList<*, *> tidak dicek karena bisa saja K bkn Int
        is List<*> -> (this as List<V>).asCommonList() as CommonIndexedList<V>
        is Map<*, *> -> (this as Map<*, V>).map { it.value }.asCommonList() as CommonIndexedList<V>
        is Array<*> -> (this as Array<V>).asCommonList() as CommonIndexedList<V>
        is ArrayWrapper<*> -> (this as ArrayWrapper<V>).asCommonList() as CommonIndexedList<V>
        else -> null
    }
}

@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <V> Any.asCommonIndexedMutableList(): CommonIndexedMutableList<V>?{
    return when(this){
        //is CommonMutableList<*, *> tidak dicek karena bisa saja K bkn Int
        is MutableList<*> -> (this as MutableList<V>).asCommonMutableList() as CommonIndexedMutableList<V>
        is MutableMap<*, *> -> ((this as MutableMap<*, V>).map { it.value } as MutableList<V>).asCommonMutableList() as CommonIndexedMutableList<V>
        is Array<*> -> (this as Array<V>).asCommonMutableList() as CommonIndexedMutableList<V>
        is ArrayWrapper<*> -> (this as ArrayWrapper<V>).asCommonMutableList() as CommonIndexedMutableList<V>
        else -> null
    }
}


fun <K, V> commonListOf(vararg elements: Pair<K, V>): CommonList<K, V>
        = CommonListImpl_Map(mapOf(*elements))
fun <K, V> commonMutableListOf(vararg elements: Pair<K, V>): CommonMutableList<K, V>
        = CommonMutableListImpl_Map(mutableMapOf(*elements))

fun <T> commonIndexedListOf(vararg elements: T): CommonIndexedList<T>
        = CommonListImpl_Array(elements as Array<T>) // as CommonList<Int, T>
fun <T> commonIndexedMutableListOf(vararg elements: T): CommonIndexedMutableList<T>
        = CommonMutableListImpl_Array(elements as Array<T>)


fun <T> List<T>.asCommonList(): CommonList<Int, T> = CommonListImpl_List(this)
fun <T> MutableList<T>.asCommonMutableList(): CommonMutableList<Int, T> = CommonMutableListImpl_List(this)

fun <T> Array<T>.asCommonList(): CommonList<Int, T> = CommonListImpl_Array(this)
fun <T> Array<T>.asCommonMutableList(): CommonMutableList<Int, T> = CommonMutableListImpl_Array(this)

fun <T> ArrayWrapper<T>.asCommonList(): CommonList<Int, T> = CommonListImpl_ArrayWrapper(this)
fun <T> ArrayWrapper<T>.asCommonMutableList(): CommonMutableList<Int, T>
        = CommonMutableListImpl_ArrayWrapper(this)

fun <K, V> Map<K, V>.asCommonList(): CommonList<K, V> = CommonListImpl_Map(this)
fun <K, V> MutableMap<K, V>.asCommonMutableList(): CommonMutableList<K, V>
        = CommonMutableListImpl_Map(this)

fun <V> CommonList<*, V>.withIndex(): Iterator<IndexedValue<V>> = iterator().withIndex()
fun <V> ArrayWrapper<V>.withIndex(): Iterator<IndexedValue<V>> = iterator().withIndex()
fun <V> CommonIterable<V>.withIndex(): Iterator<IndexedValue<V>> = iterator().withIndex()


operator fun <V> CommonIterable<V>.plus(other: CommonIterable<V>): CommonIterable<V>
    = ((this as Iterable<V>) + (other as Iterable<V>)).asCommonIterable()

fun <V> CommonIterable<V>.asSequence(): Sequence<V> = this


@Suppress(SuppressLiteral.UNCHECKED_CAST)
operator fun <K, V> CommonList<K, out V>.plus(other: CommonIterable<V>): CommonList<K, V>{
    val res= (
            if(!isIndexed || other is CommonList<*, *> && !other.isIndexed) commonMutableListOf<K, V>()
            else commonIndexedMutableListOf<V>()
    ) as CommonMutableList<K, V>

    for((key, value) in keyValueIterator)
        res.put(key, value)

    try{
        when(other){
            is CommonIndexedList<*> -> res.addAll(other as List<V>) //CommonIndexedList juga List.
            is CommonList<*, *> -> {
                for((key, value) in (other as CommonList<K, V>).keyValueIterator)
                    res.put(key, value)
            }
            else -> res.addAll(other as Iterable<V>)
        }
    } catch (e: ClassCastException){ //Jika ternyata Iterable juga CommonList, namun tipe data key-nya salah, maka tambah sprti Iterable biasa.
        res.addAll(other as Iterable<V>)
    }
    return res
}


operator fun <V> CommonIterable<V>.minus(other: CommonIterable<V>): CommonIterable<V>
    = ((this as Iterable<V>) - (other as Iterable<V>)).asCommonIterable()

@Suppress(SuppressLiteral.UNCHECKED_CAST)
operator fun <K, V> CommonList<K, V>.minus(other: CommonIterable<V>): CommonList<K, V>{
    val res= commonMutableListOf<K, V>()
    when(this){
        is CommonIndexedList<*> -> res.addAll(this)
        else -> {
            for((key, value) in keyValueIterator)
                res.put(key, value)
        }
    }
    try{
        when(other){
            is CommonIndexedList<*> -> res.removeAll(other as List<V>) //CommonIndexedList juga List.
            is CommonList<*, *> -> {
                for(key in (other as CommonList<K, V>).keys)
                    res.removeKey(key)
            }
            else -> res.removeAll(other as Iterable<V>)
        }
    } catch (e: ClassCastException){ //Jika ternyata Iterable juga CommonList, namun tipe data key-nya salah, maka tambah sprti Iterable biasa.
        res.removeAll(other as Iterable<V>)
    }
    return res
}

/*
operator fun <V> CommonIterable<V>.plusAssign(other: CommonIterable<V>): Unit
    = ((this as Iterable<V>) + (other as Iterable<V>)).asCommonIterable()
 */

@Suppress(SuppressLiteral.UNCHECKED_CAST)
operator fun <K, V> CommonMutableList<K, V>.plusAssign(other: CommonIterable<V>){
    try{
        when(other){
            is CommonIndexedList<*> -> addAll(other as List<V>) //CommonIndexedList juga List.
            is CommonList<*, *> -> {
                for((key, value) in (other as CommonList<K, V>).keyValueIterator)
                    put(key, value)
            }
            else -> addAll(other as Iterable<V>)
        }
    } catch (e: ClassCastException){ //Jika ternyata Iterable juga CommonList, namun tipe data key-nya salah, maka tambah sprti Iterable biasa.
        addAll(other as Iterable<V>)
    }
}


@Suppress(SuppressLiteral.UNCHECKED_CAST)
operator fun <K, V> CommonMutableList<K, V>.minusAssign(other: CommonIterable<V>){
    try{
        when(other){
            is CommonIndexedList<*> -> removeAll(other as List<V>) //CommonIndexedList juga List.
            is CommonList<*, *> -> {
                for(key in (other as CommonList<K, V>).keys)
                    removeKey(key)
            }
            else -> removeAll(other as Iterable<V>)
        }
    } catch (e: ClassCastException){ //Jika ternyata Iterable juga CommonList, namun tipe data key-nya salah, maka tambah sprti Iterable biasa.
        removeAll(other as Iterable<V>)
    }
}

@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <C: CommonIndexedMutableList<V>, V: Comparable<V>> C.sort(func: (V, V) -> Boolean): C
        = (this as MutableList<V>).sort(func) as C

/** Untuk indexed access dari [CommonMutableList]. */
operator fun <K, V> CommonMutableList<K, V>.set(key: K, element: V){
    put(key, element)
}
/** Untuk mengakomodasi [remove(key, value)]. */
fun <K, V> CommonMutableList<K, V>.remove(key: K, element: V): Boolean{
    return if (key in keys) {
        if(this[key] == element){
            removeKey(key)
            true
        } else false
    } else false
}