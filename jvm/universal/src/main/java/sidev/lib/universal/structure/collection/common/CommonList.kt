package sidev.lib.universal.structure.collection.common

import org.apache.commons.lang3.mutable.Mutable
import sidev.lib.universal.`fun`.*
import sidev.lib.universal.`val`.SuppressLiteral
import sidev.lib.universal.annotation.Unsafe
import sidev.lib.universal.structure.data.MapEntry
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Struktur data yg menunjukan semua jenis tipe yg dapat menyimpan banyak data dg jenis [V] dan key [K].
 * Untuk kasus [List], [K] dapat berupa [Int].
 */
interface CommonList<K, V> : CommonIterable<V>, List<V>, Map<K, V>, ArrayWrapper<V> {
    /**
     * Menunjukan apakah key berupa Int atau tidak. Jika `true` artinya `this` berupa [List], bkn [Map]
     * sehingga key tidak tidak perlu disebutkan scr langsung saat operasi [set].
     */
    val isIndexed: Boolean

    /** Iterator yg berisi [Map.Entry] dari [K] dan [V]. Iterator ini berguna terutama bagi [Map]. */
    val keyValueIterator: Iterator<Map.Entry<K, V>>

    override fun iterator(): Iterator<V>
}
/** [CommonList] dg key merupakan [Int]. */
interface CommonIndexedList<T> : CommonList<Int, T> {
    override val isIndexed: Boolean get() = true
    /** Override dg return non-nullable bertujuan agar tidak terjadi ambiguity saat operasi [get]. */
    @Suppress(SuppressLiteral.PARAMETER_NAME_CHANGED_ON_OVERRIDE)
    override fun get(index: Int): T
}

/**
 * Turunan [CommonList] yg mutable. Interface ini tidak meng-extend [MutableMap] karena alasan
 * `platform declaration clash` karena ada 2 fungsi dg nama dan param yg sama yaitu `remove(key)`.
 * Untuk menanggulangi masalah tersebut, 2 fungsi utama [MutableMap] langsung dideklarasikan di interface ini.
 */
//MutableMap
interface CommonMutableList<K, V>: CommonList<K, V>, MutableList<V>, Map<K, V>, MutableArrayWrapper<V> {
//    operator fun getValue(thisRef: MutableMap<K, V>, property: KProperty<*>): MutableMap<K, V>
//    operator fun setValue(thisRef: MutableMap<K, V>, property: KProperty<*>, value: MutableMap<K, V>): MutableMap<K, V>
    /**
     * @inheritdocs
     *
     * Dalam konteks immutability, jika `this` berupa [Map], sebenarnya pemberian key pada operasi [set] dapat diabaikan.
     * Scr default, new key yg diberikan merupakan cloningan dari lastKey. Namun, hal tersebut tidak
     * menjamin keberhasilan operasi [set].
     */
    override val isIndexed: Boolean

    /** Untuk membantu operasi [set] pada [Map] yg mengabaikan pemberian nilai key. */
    val defaultKey: K?

    override val keys: MutableSet<K>
    override val values: MutableCollection<V>
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>

    /** @return nilai sebelumnya pada [key], `null` jika belum ada nilai pada [key] sebelumnya. */
    fun put(key: K, value: V): V?
    fun putAll(from: Map<out K, V>)

    /** @return nilai sebelumnya pada [key], `null` jika belum ada nilai pada [key] sebelumnya. */
    fun removeKey(key: K): V?
    //TODO <5 Agustus 2020> => Untuk sementara di-disable karena menyebabkan error internal dari Kotlin code-generator.
    //  Sbg gantinya, ada di extension.

    /**
     * Menghapus semua entri pada `this` dg pasangan key [K] dan value [V] yg sesuai.
     * @return `true` jika isi dari `this` berubah.
     */
    fun removeAll(from: Map<out K, V>): Boolean

    /**
     * Mengahapus semua nilai yg sama dg [element].
     * @return `true` jika [element] sebelumnya ada di `this` dan berhasil dihapus.
     */
    fun removeAll(element: V): Boolean
//    fun remove(key: K, element: V): Boolean
}
/** [CommonMutableList] dg key merupakan [Int]. */
interface CommonIndexedMutableList<T> : CommonIndexedList<T>, CommonMutableList<Int, T> {
    override val isIndexed: Boolean get() = true
    /** Override dg return non-nullable bertujuan agar tidak terjadi ambiguity saat operasi [get]. */
    @Suppress(SuppressLiteral.PARAMETER_NAME_CHANGED_ON_OVERRIDE)
    override fun get(index: Int): T
    override fun addAll(elements: Collection<T>): Boolean
}

//class CommonMutableListMapDelegate<K, V>(val mutableMap: MutableMap<K, V>): CommonMutableListImpl_Map<K, V>


internal open class CommonListImpl_List<V>(open val list: List<V>): CommonIndexedList<V> {
//    override val isIndexed: Boolean get() = true
    override val size: Int get() = list.size
    override val keys: Set<Int> get() = list.indices.toSet()
    override val values: Collection<V> get() = list
    override val entries: Set<Map.Entry<Int, V>>
        get()= list.mapIndexed { i, v ->
                object : Map.Entry<Int, V>{
                override val key: Int get() = i
                override val value: V get() = v
            }
        }.toSet()
    override val keyValueIterator: Iterator<Map.Entry<Int, V>>
        get() = object : Iterator<MapEntry<Int, V>>{
            val indexedIterator= this@CommonListImpl_List.iterator().withIndex()
            override fun hasNext(): Boolean = indexedIterator.hasNext()

            override fun next(): MapEntry<Int, V> {
                val next= indexedIterator.next()
                return MapEntry(next.index, next.value)
            }
        }

    override fun toString(): String = "CommonIndexedMutableList$list"

    @Suppress(SuppressLiteral.PARAMETER_NAME_CHANGED_ON_OVERRIDE)
    override fun get(index: Int): V = list[index]
//    override fun get(key: Int): V? = list[index]
    override fun indexOf(element: V): Int = list.indexOf(element)
    override fun isEmpty(): Boolean = list.isEmpty()
    override fun contains(element: V): Boolean = list.contains(element)
    override fun containsAll(elements: Collection<V>): Boolean = list.containsAll(elements)
    override fun containsKey(key: Int): Boolean = key in list.indices
    override fun containsValue(value: V): Boolean = contains(value)
    override fun iterator(): Iterator<V> = list.iterator()
    override fun lastIndexOf(element: V): Int = list.lastIndexOf(element)
    override fun listIterator(): ListIterator<V> = list.listIterator()
    override fun listIterator(index: Int): ListIterator<V> = list.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<V> = list.subList(fromIndex, toIndex)
}
internal open class CommonMutableListImpl_List<V>(override val list: MutableList<V>)
    : CommonListImpl_List<V>(list), CommonIndexedMutableList<V> {
    override val defaultKey: Int? get() = size +1
    override val keys: MutableSet<Int> get() = list.indices.toMutableSet()
    override val values: MutableCollection<V> get() = list
    override val entries: MutableSet<MutableMap.MutableEntry<Int, V>>
        get()= list.mapIndexed { i, v ->
            object : MutableMap.MutableEntry<Int, V>{
                override val key: Int get() = i
                override var value: V = v
                    private set
                override fun setValue(newValue: V): V {
                    val prevVal= value
                    value= newValue
                    return prevVal
                }
            }
        }.toMutableSet()

    override fun toString(): String = "CommonIndexedList$list"

    override fun add(element: V): Boolean = list.add(element)
    override fun add(index: Int, element: V) = list.add(index, element)
    override fun addAll(elements: Collection<V>): Boolean = list.addAll(elements)
    override fun addAll(index: Int, elements: Collection<V>): Boolean = list.addAll(index, elements)
    override fun remove(element: V): Boolean = list.remove(element)
//    override fun remove(key: Int, element: V): Boolean = false
/*
    override fun remove(key: Int, element: V): Boolean{
        return if(key in indices && get(key) == element){
            removeAt(key)
            true
        } else false
    }

 */
    override fun removeKey(key: Int): V? = if(key in indices) removeAt(key) else null

    override fun removeAt(index: Int): V = list.removeAt(index)
    override fun removeAll(elements: Collection<V>): Boolean = list.removeAll(elements)
    override fun removeAll(element: V): Boolean = list.removeAll(listOf(element))
    override fun removeAll(from: Map<out Int, V>): Boolean {
        var res= false
        for((index, e) in entries)
            res= remove(index, e) || res
        return res
    }
    override fun retainAll(elements: Collection<V>): Boolean = list.retainAll(elements)
    override fun clear() = list.clear()
    override fun set(index: Int, element: V): V = list.set(index, element)
    override fun put(key: Int, value: V): V? = if(key in indices) set(key, value) else { add(value); null }
    override fun putAll(from: Map<out Int, V>) {
        for((key, value) in from)
            put(key, value)
    }
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<V> = list.subList(fromIndex, toIndex)
    override fun iterator(): MutableIterator<V> = list.iterator()
    override fun listIterator(): MutableListIterator<V> = list.listIterator()
    override fun listIterator(index: Int): MutableListIterator<V> = list.listIterator(index)
}

internal open class CommonListImpl_Array<V>(array: Array<V>): CommonListImpl_List<V>(array.toList())
internal open class CommonMutableListImpl_Array<V>(array: Array<V>): CommonMutableListImpl_List<V>(array.toMutableList())

internal open class CommonListImpl_ArrayWrapper<V>(array: ArrayWrapper<V>): CommonListImpl_List<V>((array as Iterable<V>).toList())
internal open class CommonMutableListImpl_ArrayWrapper<V>(array: ArrayWrapper<V>): CommonMutableListImpl_List<V>((array as Iterable<V>).toMutableList())



internal open class CommonListImpl_Map<K, V>(open val map: Map<K, V>): CommonList<K, V> {
    override val isIndexed: Boolean get() = false
    override val size: Int get() = map.size
    override val keys: Set<K> get() = map.keys
    override val values: Collection<V> get() = map.values
    override val entries: Set<Map.Entry<K, V>> get() = map.entries
    override val keyValueIterator: Iterator<Map.Entry<K, V>> get() = map.iterator()

    override fun toString(): String = "CommonList$map"

    override fun get(key: K): V? = map[key]
    override fun get(index: Int): V = map[map.keys.elementAt(index)] ?: throw IndexOutOfBoundsException("Jumlah elemen hanya $size, namun index $index")
    override fun indexOf(element: V): Int = map.values.indexOf(element)
    override fun isEmpty(): Boolean = map.isEmpty()
    override fun contains(element: V): Boolean = map.values.contains(element)
    override fun containsAll(elements: Collection<V>): Boolean = map.values.containsAll(elements)
    override fun containsKey(key: K): Boolean = map.containsKey(key)
    override fun containsValue(value: V): Boolean = contains(value)
    override fun iterator(): Iterator<V> = map.values.iterator()
    override fun lastIndexOf(element: V): Int = map.values.lastIndexOf(element)
    override fun listIterator(): ListIterator<V> = listIterator(0)
    override fun listIterator(index: Int): ListIterator<V>
            = object : ListIterator<V>{
        val valsList= values.toList()
        var index= index
        override fun hasNext(): Boolean = this.index < size
        override fun hasPrevious(): Boolean = this.index >= 0
        override fun next(): V = valsList[this.index++]
        override fun nextIndex(): Int = this.index +1
        override fun previous(): V = valsList[this.index--]
        override fun previousIndex(): Int = this.index -1
    }
    override fun subList(fromIndex: Int, toIndex: Int): List<V> = map.values.toList().subList(fromIndex, toIndex)
}
internal open class CommonMutableListImpl_Map<K, V>(override val map: MutableMap<K, V>)
    : CommonListImpl_Map<K, V>(map), CommonMutableList<K, V> {
    override val defaultKey: K? = null
    override val keys: MutableSet<K> get() = map.keys
    override val values: MutableCollection<V> get() = map.values
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> get() = map.entries

    override fun toString(): String = "CommonMutableList$map"

    @Unsafe("Nilai key baru dapat berupa apa saja.")
    override fun add(element: V): Boolean{
        val newKey= newUniqueValueIn(keys, defaultKey)
            .also { if(it == null) return false }!!
        put(newKey, element)
        return true
    }

    @Unsafe("[index] diabaikan.")
    @Deprecated("Tidak aman karena [index] diabaikan.", ReplaceWith("put(key, element)"))
    override fun add(index: Int, element: V) {
        add(element)
    }

    @Unsafe("[index] diabaikan.")
    @Deprecated("Tidak aman karena [index] diabaikan.", ReplaceWith("putAll(from)"))
    override fun addAll(index: Int, elements: Collection<V>): Boolean = addAll(elements)

    @Unsafe("Nilai key baru dapat berupa apa saja.")
    override fun addAll(elements: Collection<V>): Boolean {
        var res= false
        for(added in elements)
            res= add(added) || res
        return res
    }

    override fun put(key: K, value: V): V? = map.put(key, value)
    override fun putAll(from: Map<out K, V>) = map.putAll(from)
    override fun set(index: Int, element: V): V {
        if(index !in indices)
            throw IndexOutOfBoundsException("Jumlah elemen hanya $size, namun index $index")
        for((i, entry) in map.iterator().withIndex())
            if(i == index){
                put(entry.key, element)
                return entry.value
            }
        throw IndexOutOfBoundsException("Jumlah elemen hanya $size, namun index $index") //Harusnya scr prinsip gak akan pernah sampe sini.
    }

    override fun remove(element: V): Boolean = map.removeValue(element)
    override fun removeAll(element: V): Boolean {
        val removedKeyList= ArrayList<K>()
        for((key, existing) in entries)
            if(existing == element)
                removedKeyList += key
        for(key in removedKeyList)
            removeKey(key)
        return removedKeyList.isNotEmpty()
    }

    override fun removeAll(elements: Collection<V>): Boolean {
        var res= false
        for(removed in elements.toSet()){
            res= removeAll(removed) || res
        }
        return res
    }
    override fun removeAll(from: Map<out K, V>): Boolean {
        var res= false
        for((index, e) in entries)
            res= remove(index, e) || res
        return res
    }
    override fun removeAt(index: Int): V {
        if(index !in indices)
            throw IndexOutOfBoundsException("Jumlah elemen hanya $size, namun index $index")
        for((i, entry) in map.entries.withIndex())
            if(i == index)
                return map.remove(entry.key)!!
        throw IndexOutOfBoundsException("Jumlah elemen hanya $size, namun index $index") //Harusnya scr prinsip gak akan pernah sampe sini.
    }
/*
//    @RequiresApi(Build.VERSION_CODES.N)
    //TODO <5 Agustus 2020> => Sama kyk yg di atas.
    override fun remove(key: K, element: V): Boolean {
//    map.remove(key, element)
        return if (key in map.keys) {
            if(map[key] == element){
                map.remove(key)
                true
            } else false
        } else false
    }
 */
    override fun removeKey(key: K): V? = map.remove(key)
    override fun retainAll(elements: Collection<V>): Boolean {
        val removedElements= (map.values as Collection<V>) - elements
        return removeAll(removedElements)
    }

    override fun clear() = map.clear()
    override fun iterator(): MutableIterator<V>
        = object : MutableIterator<V>{
        val innerItr= map.values.iterator()
        var next: V?= null

        override fun hasNext(): Boolean = innerItr.hasNext()
        override fun next(): V = innerItr.next().also { next= it }

        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        override fun remove() {
            innerItr.remove()
            remove(next as V) //Knp kok gak di !!, karena bisa aja tipe dari V adalah nullable sehingga akan throw KotlinNPE.
        }
    }

    override fun listIterator(): MutableListIterator<V> = listIterator(0)
    override fun listIterator(index: Int): MutableListIterator<V> {
        val innerItr= map.values.toMutableList().listIterator(index)
        val innerEntryItr= map.iterator()
        return object : MutableListIterator<V> by innerItr{
            var now: V?= null
            override fun next(): V = innerItr.next().also { now= it }
            override fun previous(): V = innerItr.previous().also { now= it }

            override fun add(element: V) {
                this@CommonMutableListImpl_Map.add(element)
                innerItr.add(element)
            }

            @Suppress(SuppressLiteral.UNCHECKED_CAST)
            override fun remove() {
                innerItr.remove()
                remove(now as V) //Knp kok gak di !!, karena bisa aja tipe dari V adalah nullable sehingga akan throw KotlinNPE.
            }

            override fun set(element: V) {
                for((key, value) in innerEntryItr)
                    if(value == now){
                        innerItr.set(element)
                        now= element
                        this@CommonMutableListImpl_Map.put(key, element)
                        break
                    }
            }
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<V> = map.values.toMutableList().subList(fromIndex, toIndex)
}

@Suppress(SuppressLiteral.UNCHECKED_CAST, SuppressLiteral.IMPLICIT_CAST_TO_ANY)
fun <T> newUniqueValueIn(inCollection: Collection<T?>, default: T?= null, constructorParamValFunc: ((KClass<*>, KParameter) -> Any?)?= null): T? {
    var newVal= inCollection.lastOrNull() ?: default
    while(newVal != null && newVal in inCollection){
        newVal= when(newVal){
            is Number -> newVal + 1
            is String -> "$newVal:@"
            else -> {
                if(!newVal.isKReflectionElement)
                    try{ newVal.clone<Any>(constructorParamValFunc = constructorParamValFunc) }
                    catch (e: Exception){ default }
                else default
            }
        } as? T
    }
    return newVal
}