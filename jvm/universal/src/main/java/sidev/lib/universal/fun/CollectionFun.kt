package sidev.lib.universal.`fun`

import sidev.lib.universal.structure.collection.iterator.NestedIteratorImpl
import sidev.lib.universal.structure.collection.iterator.SkippableIteratorImpl
import sidev.lib.universal.structure.collection.lazy_list.CachedSequence
import sidev.lib.universal.structure.collection.lazy_list.LazyHashMap
import sidev.lib.universal.structure.data.MapEntry
import sidev.lib.universal.structure.data.MutableMapEntry
import kotlin.collections.ArrayList
import kotlin.reflect.KClass

/*
===============
Check
===============
 */
fun <T> Collection<T>?.isNotNullAndEmpty(): Boolean{
    return this != null && this.isNotEmpty()
}

fun <T> Array<T>?.isNotNullAndEmpty(): Boolean{
    return this != null && this.isNotEmpty()
}



/*
===============
Convert
===============
 */
//<4 Juli 2020> => Definisi baru
fun <T> Array<T>.toArrayList(): ArrayList<T>{
    return this.toMutableList() as ArrayList<T>
}
/*
<4 Juli 2020> => Definisi lama.
fun <T> Array<T>.toArrayList(): ArrayList<T>{
    val list= ArrayList<T>()
    this.forEach { list.add(it) }
    return list
}
 */


/*
===============
findElement()
===============
 */
inline fun <reified E> Array<out Any?>.findElementByType(): E?{
    return this.findElement { it is E } as? E
}
inline fun <reified E> Collection<Any?>.findElementByType(): E?{
    return this.findElement { it is E } as? E
}

fun <C> Array<C>.findElement(iterator: (C) -> Boolean): C?{
    for(e in this)
        if(iterator(e))
            return e
    return null
}
fun <C> Collection<C>.findElement(iterator: (C) -> Boolean): C?{
    for(e in this)
        if(iterator(e))
            return e
    return null
}

/*
===============
Fungsi lain
===============
 */
fun <T> Array<T>.ifExists(func: (T) -> Boolean): Boolean{
    var exists= false
    this.forEach { t ->
        if(func(t)){
            exists= true
            return@forEach
        }
    }
    return exists
}
fun <T> Iterable<T>.ifExists(func: (T) -> Boolean): Boolean{
    var exists= false
    this.forEach { t ->
        if(func(t)){
            exists= true
            return@forEach
        }
    }
    return exists
}

fun <T> Array<T>.filterIn(array: Array<T>): Iterable<T> {
    val out= ArrayList<T>()
    for(e in this)
        if(e in array)
            out.add(e)
    return out
}
fun <T> Iterable<T>.filterIn(array: Array<T>): Iterable<T> {
    val out= ArrayList<T>()
    for(e in this)
        if(e in array)
            out.add(e)
    return out
}


fun CharSequence.filterIn(array: Array<String>): Iterable<String> {
    val out= ArrayList<String>()
    for(e in array)
        if(e in this)
            out.add(e)
    return out
}


fun <T: String> Iterable<T>.indexOf(e: T, ignoreCase: Boolean= false): Int {
    if(ignoreCase){
        val e2= e.toLowerCase()
        for((i, e1) in this.withIndex()){
            if(e1.toLowerCase() == e2)
                return i
        }
    } else
        return this.indexOf(e)
    return -1
}
fun <T: String> Array<T>.indexOf(e: T, ignoreCase: Boolean= false): Int {
    if(ignoreCase){
        val e2= e.toLowerCase()
        for((i, e1) in this.withIndex()){
            if(e1.toLowerCase() == e2)
                return i
        }
    } else
        return this.indexOf(e)
    return -1
}



inline fun <T> Iterable<T>.filter(func: (pos: Int, element: T) -> Boolean): List<T> {
    val filteredList= ArrayList<T>()
    for((i, e) in this.withIndex())
        if(func(i, e))
            filteredList.add(e)
    return filteredList
}
inline fun <T> Array<out T>.filter(func: (pos: Int, element: T) -> Boolean): List<T> {
    val filteredList= ArrayList<T>()
    for((i, e) in this.withIndex())
        if(func(i, e))
            filteredList.add(e)
    return filteredList
}


inline fun <T> Iterable<T>.search(filter: (element: T, pos: Int) -> Boolean): T?{
    for((i, el) in this.withIndex()){
        val bool= filter(el, i)
        if(bool) return el
    }
    return null
}
inline fun <T> Iterable<T>.search(filter: (element: T) -> Boolean): T?{
    for(el in this){
        val bool= filter(el)
        if(bool) return el
    }
    return null
}

inline fun <T> Array<T>.search(filter: (element: T, pos: Int) -> Boolean): T?{
    for((i, el) in this.withIndex()){
        val bool= filter(el, i)
        if(bool) return el
    }
    return null
}
inline fun <T> Array<T>.search(filter: (element: T) -> Boolean): T?{
    for(el in this){
        val bool= filter(el)
        if(bool) return el
    }
    return null
}


inline fun <T> Iterable<T>.indexOf(filter: (element: T) -> Boolean): Int{
    for((i, el) in this.withIndex()){
        if(filter(el)) return i
    }
    return -1
}
inline fun <T> Iterable<T>.indexOf(filter: (element: T, pos: Int) -> Boolean): Int{
    for((i, el) in this.withIndex()){
        if(filter(el, i)) return i
    }
    return -1
}

inline fun <T> Array<T>.indexOf(filter: (element: T) -> Boolean): Int{
    for((i, el) in this.withIndex()){
        if(filter(el)) return i
    }
    return -1
}
inline fun <T> Array<T>.indexOf(filter: (element: T, pos: Int) -> Boolean): Int{
    for((i, el) in this.withIndex()){
        if(filter(el, i)) return i
    }
    return -1
}


inline fun <T, C: MutableCollection<in String>> Iterable<T>.toStringList(dest: C, filter: (element: T, pos: Int) -> String?): C{
    dest.clear()
    for((i, el) in this.withIndex()){
        val res= filter(el, i)
        if(res != null)
            dest.add(res)
    }
    return dest
}


fun <T> Any.get(pos: Int): T? {
    return when(this){
        is Array<*> -> this[pos] as T?
        is List<*> -> this[pos] as T?
        else -> null
    }
}

val Map<*, *>.indices: IntRange
    get()= 0 until size

fun Any.indices(): IntRange? {
    return when(this){
        is Array<*> -> this.indices
        is Collection<*> -> this.indices
        is Map<*, *> -> this.indices
        else -> null
    }
}

inline fun <reified T> Array<T>.copy(reversed: Boolean= false): Array<T> {
    return if(!reversed) this.copyOf()
        else Array(this.size){this[size -it -1]}
}

fun <T> List<T>.copy(reversed: Boolean= false): List<T> {
    val newList= mutableListOf<T>()
    val range= if(!reversed) this.indices
        else this.size .. 0
    for(i in range)
        newList.add(this[i])
    return newList
}

fun <I, O> Array<I>.toListOf(func: (I) -> O?): MutableList<O> {
    val newList= mutableListOf<O>()
    for(inn in this){
        func(inn).notNull { newList.add(it) }
    }
    return newList
}
fun <I, O> Iterable<I>.toListOf(func: (I) -> O?): MutableList<O> {
    val newList= mutableListOf<O>()
    for(inn in this){
        func(inn).notNull { newList.add(it) }
    }
    return newList
}

inline fun <I, reified O> Array<I>.toArrayOf(func: (I) -> O?): Array<O> {
    val newList= mutableListOf<O>()
    for(inn in this){
        func(inn).notNull { newList.add(it) }
    }
    return newList.toTypedArray()
}
inline fun <I, reified O> Iterable<I>.toArrayOf(func: (I) -> O?): Array<O> {
    val newList= mutableListOf<O>()
    for(inn in this){
        func(inn).notNull { newList.add(it) }
    }
    return newList.toTypedArray()
}



inline fun <reified T> Collection<T>.toSimpleString(): String{
    val elClsName= T::class.java.simpleName
    var str= "Collection:$elClsName:("
    for(e in this)
        str += "$e, "
    str= str.removeSuffix(", ")
    str += ")"
    return str
}
inline fun <reified T> Array<T>.toSimpleString(): String{
    val elClsName= T::class.java.simpleName
    var str= "Array:$elClsName:("
    for(e in this)
        str += "$e, "
    str= str.removeSuffix(", ")
    str += ")"
    return str
}

fun <K, V> MutableMap<K, V>.add(pair: Pair<K, V>){
    this[pair.first]= pair.second
}

/*
=============================
Grow List Function
=============================
 */

//======List==========
fun <T> MutableList<T>.growIncremental(factor: Int){
    val initSize= this.size
    for(i in 0 until factor)
        this.add(this[i % initSize])
}
fun <T> MutableList<T>.growTimely(factor: Int){
    val initSize= this.size
    for(i in 0 until factor)
        for(u in 0 until initSize)
            this.add(this[u])
}
fun <T> MutableList<T>.growExponentially(factor: Int){
    val initSize= this.size
    for(i in 0 until factor)
        for(u in 0 until factor)
            for(e in 0 until initSize)
                this.add(this[e])
}

fun <T> MutableList<T>.copyGrowIncremental(factor: Int, collIn: MutableCollection<T>?= null): MutableCollection<T>{
    val newList= collIn ?: ArrayList(this)
    val initSize= this.size
    for(i in 0 until factor)
        newList.add(this[i % initSize])
    return newList
}
fun <T> MutableList<T>.copyGrowTimely(factor: Int, collIn: MutableCollection<T>?= null): MutableCollection<T>{
    val newList= collIn ?: ArrayList(this)
    val initSize= this.size
    for(i in 0 until factor)
        for(u in 0 until initSize)
            newList.add(this[u])
    return newList
}
fun <T> MutableList<T>.copyGrowExponentially(factor: Int, collIn: MutableCollection<T>?= null): MutableCollection<T>{
    val newList= collIn ?: ArrayList(this)
    val initSize= this.size
    for(i in 0 until factor)
        for(u in 0 until factor)
            for(e in 0 until initSize)
                newList.add(this[e])
    return newList
}

//======Array==========
fun <T> Array<T>.copyGrowIncremental(factor: Int, collIn: MutableCollection<T>?= null): MutableCollection<T>{
    val newList= collIn ?: ArrayList(this.toList())
    val initSize= this.size
    for(i in 0 until factor)
        newList.add(this[i % initSize])
    return newList
}
fun <T> Array<T>.copyGrowTimely(factor: Int, collIn: MutableCollection<T>?= null): MutableCollection<T>{
    val newList= collIn ?: ArrayList(this.toList())
    val initSize= this.size
    for(i in 0 until factor)
        for(u in 0 until initSize)
            newList.add(this[u])
    return newList
}
fun <T> Array<T>.copyGrowExponentially(factor: Int, collIn: MutableCollection<T>?= null): MutableCollection<T>{
    val newList= collIn ?: ArrayList(this.toList())
    val initSize= this.size
    for(i in 0 until factor)
        for(u in 0 until factor)
            for(e in 0 until initSize)
                newList.add(this[e])
    return newList
}


fun <I, O> newIterator(vararg element: I, mapping: ((I) -> O)?= null): Iterator<O>{
    return if(mapping != null) element.iterator().toOtherIterator(mapping)
    else element.iterator() as Iterator<O>
} //= element.iterator()


fun <T> newIteratorSimple(vararg element: T): Iterator<T> = element.iterator()




/** Sama seperti [first] sekaligus mengahpus element pertama */
fun <T> MutableList<T>.takeFirst(): T = if(isEmpty()) throw NoSuchElementException("List is Empty") else removeAt(0)

/** Sama seperti [firstOrNull] sekaligus mengahpus element pertama
 *  Di Kotlin sama seperti [removeFirstOrNull].
 * */
fun <T> MutableList<T>.takeFirstOrNull(): T? = if(isEmpty()) null else removeAt(0)

/** Sama seperti [last] sekaligus mengahpus element pertama */
fun <T> MutableList<T>.takeLast(): T = if(isEmpty()) throw NoSuchElementException("List is Empty") else removeAt(lastIndex)

/** Sama seperti [lastOrNull] sekaligus mengahpus element pertama */
fun <T> MutableList<T>.takeLastOrNull(): T? = if(isEmpty()) null else removeAt(lastIndex)

/**
 * @return true jika [element] tidak terdapat sebelumnya di list ini.
 * Fungsi ini menggunakan standard equals().
 */
inline fun <T> MutableList<T>.addIfAbsent(element: T, chekcFun: ((existingElement: T) -> Boolean)= {true}): Boolean{
    prinw("addIfAbsent element= $element")
    for(e in this){
        prinw("element= $element e == element => ${e == element}")
    }
    val existingElementIndex= indexOf(element)
    var canAdd= if(existingElementIndex < 0) true
        else !chekcFun(this[existingElementIndex])
    prinw("size= $size existingElementIndex= $existingElementIndex canAdd= $canAdd")
    if(canAdd)
        this.add(element)
    return canAdd
}



fun <T> Iterator<IndexedValue<T>>.toPairIterator(): Iterator<Pair<Int, T>>
    = object : Iterator<Pair<Int, T>>{
        override fun hasNext(): Boolean = this@toPairIterator.hasNext()
        override fun next(): Pair<Int, T>{
            val next= this@toPairIterator.next()
            return Pair(next.index, next.value)
        }
    }

/** Menjadikan nilai pada `this.extension` [Iterator] sbg `value` dg `key` yg dipetakan oleh [func]. */
fun <K, V> Iterator<V>.withKey(func: (value: V) -> K): Iterator<Pair<K, V>>
    = object : Iterator<Pair<K, V>>{
    override fun hasNext(): Boolean = this@withKey.hasNext()

    override fun next(): Pair<K, V>{
        val next= this@withKey.next()
        val key= func(next)
        return Pair(key, next)
    }
}
/** Menjadikan nilai pada `this.extension` [Iterator] sbg `key` dg `value` yg dipetakan oleh [func]. */
fun <K, V> Iterator<K>.withValue(func: (key: K) -> V): Iterator<Pair<K, V>>
    = object : Iterator<Pair<K, V>>{
    override fun hasNext(): Boolean = this@withValue.hasNext()

    override fun next(): Pair<K, V>{
        val next= this@withValue.next()
        val value= func(next)
        return Pair(next, value)
    }
}

/** Menjadikan nilai pada `this.extension` [Iterator] sbg `value` dg `key` yg dipetakan oleh [func]. */
fun <K, V> Iterator<V>.withKeyIndexed(func: (index: Int, value: V) -> K): Iterator<Pair<K, V>>
    = object : Iterator<Pair<K, V>>{
    private var index= 0
    override fun hasNext(): Boolean = this@withKeyIndexed.hasNext()

    override fun next(): Pair<K, V>{
        val next= this@withKeyIndexed.next()
        val key= func(index++, next)
        return Pair(key, next)
    }
}
/** Menjadikan nilai pada `this.extension` [Iterator] sbg `key` dg `value` yg dipetakan oleh [func]. */
fun <K, V> Iterator<K>.withValueIndexed(func: (index: Int, key: K) -> V): Iterator<Pair<K, V>>
    = object : Iterator<Pair<K, V>>{
    private var index= 0
    override fun hasNext(): Boolean = this@withValueIndexed.hasNext()

    override fun next(): Pair<K, V>{
        val next= this@withValueIndexed.next()
        val value= func(index++, next)
        return Pair(next, value)
    }
}


fun <T> cachedSequenceOf(vararg elements: T): CachedSequence<T> = CachedSequence(elements.iterator())
fun <K, V> lazaMapOf(vararg elements: Pair<K, V>): LazyHashMap<K, V> = LazyHashMap(elements.iterator())

fun <T> Sequence<T>.asCached(): CachedSequence<T> = CachedSequence(this)
fun <T> Iterator<T>.asCached(): CachedSequence<T> = CachedSequence(this)

operator fun <T> Sequence<T>.minus(other: Sequence<T>): Sequence<T> = cut(other)
operator fun <T> Sequence<T>.minus(other: Iterator<T>): Sequence<T> = cut(other)
operator fun <T> Sequence<T>.minus(other: Iterable<T>): Sequence<T> = cut(other)

fun <T> Sequence<T>.cut(other: Sequence<T>): Sequence<T>
    = object : Sequence<T>{
    override fun iterator(): Iterator<T>
        = object : SkippableIteratorImpl<T>(this@cut.iterator()){
        val otherAsCached= other.asCached()
        override fun skip(now: T): Boolean = now in otherAsCached
    }
}

fun <T> Sequence<T>.cut(other: Iterator<T>): Sequence<T>
    = object : Sequence<T>{
    override fun iterator(): Iterator<T>
        = object : SkippableIteratorImpl<T>(this@cut.iterator()){
        val otherAsCached= other.asCached()
        override fun skip(now: T): Boolean = now in otherAsCached
    }
}

fun <T> Sequence<T>.cut(other: Iterable<T>): Sequence<T>
    = object : Sequence<T>{
    override fun iterator(): Iterator<T>
        = object : SkippableIteratorImpl<T>(this@cut.iterator()){
//        val otherAsCached= other
        override fun skip(now: T): Boolean = now in other
    }
}


fun <K, V> Pair<K, V>.toMutableMapEntry(): MutableMap.MutableEntry<K, V> = MutableMapEntry(first, second)
fun <K, V> Pair<K, V>.toMapEntry(): Map.Entry<K, V> = MapEntry(first, second)


fun <I, O> Sequence<I>.toOtherSequence(mapping: (I) -> O): Sequence<O>
    = object: Sequence<O>{
    override fun iterator(): Iterator<O>
        = object: Iterator<O>{
        private val originItr= this@toOtherSequence.iterator()
        override fun hasNext(): Boolean = originItr.hasNext()
        override fun next(): O = mapping(originItr.next())
    }
}

fun <I, O> Iterator<I>.toOtherIterator(mapping: (I) -> O): Iterator<O>
    = object: Iterator<O>{
    override fun hasNext(): Boolean = this@toOtherIterator.hasNext()
    override fun next(): O = mapping(this@toOtherIterator.next())
}


val Array<*>.string: String
    get(){
        var str= "${this::class.simpleName}("
        for(e in this){
            str += e.toString() +", "
        }
        str= str.removeSuffix(", ")
        return "$str)"
    }

val Collection<*>.string: String
    get(){
        var str= "${this::class.simpleName}("
        for(e in this){
            str += e.toString() +", "
        }
        str= str.removeSuffix(", ")
        return "$str)"
    }

//fun <K, V> Map.Entry<K, V>.toString(): String = "$key=$value"