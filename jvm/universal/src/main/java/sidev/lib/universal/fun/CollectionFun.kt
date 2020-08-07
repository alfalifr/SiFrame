package sidev.lib.universal.`fun`

import sidev.lib.universal.`val`.SuppressLiteral
import sidev.lib.universal.exception.UndefinedDeclarationExc
import sidev.lib.universal.structure.collection.common.CommonList
import sidev.lib.universal.structure.collection.common.CommonMutableList
import sidev.lib.universal.structure.collection.iterator.NestedIterator
import sidev.lib.universal.structure.collection.iterator.NestedIteratorImpl
import sidev.lib.universal.structure.collection.iterator.SkippableIteratorImpl
import sidev.lib.universal.structure.collection.lazy_list.CachedSequence
import sidev.lib.universal.structure.collection.lazy_list.LazyHashMap
import sidev.lib.universal.structure.collection.sequence.NestedSequence
import sidev.lib.universal.structure.data.MapEntry
import sidev.lib.universal.structure.data.MutableMapEntry
import kotlin.collections.ArrayList
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

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
Comparison
===============
 */
fun <T: Comparable<T>> Collection<T>.sortedWith(f: (T, T) -> Boolean = ::asc): List<T> = this.toMutableList().sort(f)
/**
 * Mengurutkan isi dari `this.extension` [List] jika isinya merupakan turunan [Comparable]
 * dan mengembalikan `this.extension` sehingga dapat di-chain.
 */
fun <L: MutableList<T>, T: Comparable<T>> L.sort(f: (T, T) -> Boolean = ::asc): L {
    for(i in indices)
        for(u in i+1 until size){
            val isOrderTrue = try{ f(this[i], this[u]) }
            catch (e: ClassCastException){
                val ascFun: (T, T) -> Boolean = ::asc
                val descFun: (T, T) -> Boolean = ::desc
                when(f){
                    ascFun -> univAsc(this[i], this[u])
                    descFun -> univDesc(this[i], this[u])
                    else -> throw UndefinedDeclarationExc(undefinedDeclaration = "${this[i]::class}.compareTo(${this[u]::class})")
                }
            }
            if(!isOrderTrue){
                val temp= this[i]
                this[i]= this[u]
                this[u]= temp
            }
        }
    return this
}
/** Sama dg [MutableList.sort] di atas, namun digunakan pada [Array]. */
fun <T: Comparable<T>> Array<T>.sort(f: (T, T) -> Boolean = ::asc): Array<T> {
    for(i in indices)
        for(u in i+1 until size){
            val isOrderTrue = try{ f(this[i], this[u]) }
            catch (e: ClassCastException){
                val ascFun: (T, T) -> Boolean = ::asc
                val descFun: (T, T) -> Boolean = ::desc
                when(f){
                    ascFun -> univAsc(this[i], this[u])
                    descFun -> univDesc(this[i], this[u])
                    else -> throw UndefinedDeclarationExc(undefinedDeclaration = "${this[i]::class}.compareTo(${this[u]::class})")
                }
            }
            if(!isOrderTrue){
                val temp= this[i]
                this[i]= this[u]
                this[u]= temp
            }
        }
    return this
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
/*
@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <T> Any.get(index: Int): T? {
    return when{
        this::class.isArray -> this.getArrayElementAt(index) as? T
        this is List<*> -> this[index] as? T
        else -> {
            prine("""this: "$this" bkn array atau list.""")
            null
        }
    }
}
 */
/*
fun Any.getArrayElementAt(index: Int): Any?{
    return when{
        this::class.isObjectArray -> (this as Array<*>)[index]
        this::class.isPrimitiveArray -> when(this){
            is IntArray -> this[index]
            is LongArray -> this[index]
            is FloatArray -> this[index]
            is DoubleArray -> this[index]
            is CharArray -> this[index]
            is ShortArray -> this[index]
            is BooleanArray -> this[index]
            is ByteArray -> this[index]
            else -> null
        }
        else -> {
            prine("""this: "$this" bkn array.""")
            null
        }
    }
}
 */

val Map<*, *>.indices: IntRange
    get()= 0 until size

/*
fun Any.indices(): IntRange? {
    return when(this){
        is Array<*> -> this.indices
        is Collection<*> -> this.indices
        is Map<*, *> -> this.indices
        else -> null
    }
}
 */

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
fun <K, V> MutableMap<K, V>.removeValue(value: V): K?{
    return try{
        val key= keys.toList()[values.indexOf(value)]
        remove(key)
        key
    } catch (e: Exception){ null }
}
 */

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

/** Sama seperti [remove], namun menghapus last occurrence. */
fun <T> MutableList<T>.removeLast(element: T): Boolean{ //= if(isEmpty()) throw NoSuchElementException("List is Empty") else removeAt(lastIndex)
    for(i in size -1 downTo 0)
        if(this[i] == element){
            removeAt(i)
            return true
        }
    return false
}

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


operator fun <K, V> LazyHashMap<K, V>.plus(other: Sequence<Pair<K, V>>): LazyHashMap<K, V>{
    this.addIterator(other.iterator())
    return this
}
operator fun <K, V> LazyHashMap<K, V>.plus(other: Iterator<Pair<K, V>>): LazyHashMap<K, V>{
    this.addIterator(other)
    return this
}
operator fun <K, V> LazyHashMap<K, V>.plus(other: Iterable<Pair<K, V>>): LazyHashMap<K, V>{
    this.addIterator(other.iterator())
    return this
}


fun <T> Sequence<T>.asCached(): CachedSequence<T> = CachedSequence(this)
fun <T> Iterator<T>.asCached(): CachedSequence<T> = CachedSequence(this)

operator fun <T> CachedSequence<T>.plus(other: Sequence<T>): CachedSequence<T>{
    this.addValueIterator(other.iterator())
    return this
}
operator fun <T> CachedSequence<T>.plus(other: Iterator<T>): CachedSequence<T>{
    this.addValueIterator(other)
    return this
}
operator fun <T> CachedSequence<T>.plus(other: Iterable<T>): CachedSequence<T>{
    this.addValueIterator(other.iterator())
    return this
}

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

fun <T> Sequence<Sequence<T>>.toLinear(): NestedSequence<T>
        = object : NestedSequence<T> {
    override fun iterator(): NestedIterator<Sequence<T>, T>
            = object : NestedIteratorImpl<Sequence<T>, T>(this@toLinear.iterator()){
        override fun getOutputIterator(nowInput: Sequence<T>): Iterator<T>? = nowInput.iterator()
        override fun getInputIterator(nowOutput: T): Iterator<Sequence<T>>? = null
    }
}

fun <Cin: Collection<Cout>, Cout: Collection<T>, T> Cin.toLinear(): Cout{
    val res= ArrayList<T>()
    for(coll in this)
        res += coll
    return res as Cout
}
inline fun <reified T> Array<Array<T>>.toLinear(): Array<T>{
    val res= ArrayList<T>()
    for(coll in this)
        res += coll
    return res.toTypedArray()
}

fun <T> Sequence<T>.isEmpty(): Boolean = !iterator().hasNext()
fun <T> Sequence<T>.isNotEmpty(): Boolean = !isEmpty()

/** Mengambil element pada [index] pada semua list yg disimpan dalam `this.extension`. */
fun <T> List<List<T>>.getAtAllLevelAt(index: Int): List<T>{
    val res= ArrayList<T>()
    for(list in this){
        res += list[index]
    }
    return res
}
/** Mengambil element pada [index] pada semua array yg disimpan dalam `this.extension`. */
inline fun <reified T> Array<Array<T>>.getAtAllLevelAt(index: Int): Array<T> = Array(size){ this[it][index] }

/** Size paling kecil dari bbrp [Collection] yg disimpan dalam `this.extension`. */
val <T> Collection<Collection<T>>.minSize: Int
    get()= minSize(*toTypedArray())?.size ?: 0
/** Size paling kecil dari bbrp [Array] yg disimpan dalam `this.extension`. */
val <T> Array<Array<T>>.minSize: Int
    get()= minSize(*this)?.size ?: 0

/** Size paling besar dari bbrp [Collection] yg disimpan dalam `this.extension`. */
val <T> Collection<Collection<T>>.maxSize: Int
    get()= maxSize(*toTypedArray())?.size ?: 0
/** Size paling besar dari bbrp [Array] yg disimpan dalam `this.extension`. */
val <T> Array<Array<T>>.maxSize: Int
    get()= maxSize(*this)?.size ?: 0

/** Size paling besar dari bbrp [Collection] yg disimpan dalam `this.extension`. */
val <T> Collection<Collection<T>>.totalSize: Int
    get(){
        var size= 0
        for(coll in this)
            size += coll.size
        return size
    }
/** Size paling besar dari bbrp [Array] yg disimpan dalam `this.extension`. */
val <T> Array<Array<T>>.totalSize: Int
    get(){
        var size= 0
        for(array in this)
            size += array.size
        return size
    }

/** Size paling kecil dari bbrp [Collection] yg disimpan dalam `this.extension`. */
val <T> List<List<T>>.leveledIterator: Iterator<List<T>>
    get()= object: Iterator<List<T>>{
        val size= minSize
        var index= 0
        override fun hasNext(): Boolean = index < size
        override fun next(): List<T> = getAtAllLevelAt(index++)
    }
/** Size paling kecil dari bbrp [Collection] yg disimpan dalam `this.extension`. */
inline val <reified T> Array<Array<T>>.leveledIterator: Iterator<Array<T>>
    get()= object: Iterator<Array<T>>{
        val size= minSize
        var index= 0
        override fun hasNext(): Boolean = index < size
        override fun next(): Array<T> = getAtAllLevelAt(index++)
    }

/** Mengambil list yg berisi [Pair.first] dari `this.extension` [Array]. */
val <A, B> Array<out Pair<A, B>>.firstList: List<A>
    get(){
        val res= ArrayList<A>()
        for(pair in this)
            res += pair.first
        return res
    }
/** Mengambil list yg berisi [Pair.second] dari `this.extension` [Array]. */
val <A, B> Array<out Pair<A, B>>.secondList: List<B>
    get(){
        val res= ArrayList<B>()
        for(pair in this)
            res += pair.second
        return res
    }
fun <A, B> Array<out Pair<A, B>>.asMap(): Map<A, B> = mapOf(*this)
fun <A, B> List<Pair<A, B>>.asMap(): Map<A, B> = mapOf(*toTypedArray())

fun <K, V> Collection<V>.asMapWithKeys(keys: List<K>): Map<K, V>{
    if(keys.size < size)
        throw IllegalArgumentException("""Ukuran keys: ${keys.size} krg dari ukuran `this` Collection: $size.""")

    val map= mutableMapOf<K, V>()
    for((i, value) in this.withIndex()){
        map += keys[i] to value
    }
    return map
}

fun <K, V> Collection<K>.asMapWithValues(values: List<V>): Map<K, V>{
    if(values.size < size)
        throw IllegalArgumentException("""Ukuran values: ${values.size} krg dari ukuran `this` Collection: $size.""")

    val map= mutableMapOf<K, V>()
    for((i, key) in this.withIndex()){
        map += key to values[i]
    }
    return map
}



/*
===============
List Operation
===============
 */

operator fun <C: MutableCollection<T>, T> C.plus(element: T): C{
    this.add(element)
    return this
}
operator fun <C: MutableCollection<T>, T> C.plus(collection: Collection<T>): C{
    this.addAll(collection)
    return this
}

operator fun <C: MutableCollection<T>, T> C.minus(element: T): C{
    this.remove(element)
    return this
}
operator fun <C: MutableCollection<T>, T> C.minus(collection: Collection<T>): C{
    this.removeAll(collection)
    return this
}

operator fun <L: MutableList<T>, T> L.times(factor: Int): L{
    this.growTimely(factor)
    return this
}

infix fun <C: MutableCollection<T>, T> C.intersect(other: Iterable<T>): C{
    this.retainAll(other)
    return this
}
fun <C: MutableCollection<T>, T> C.distinct(): C{
    return toMutableSet().toMutableList() as C
}

/** Mengambil [Collection] dg jml size paling kecil. [includeEmpty] == true, maka hasil @return juga menyertakan empty-collection. */
fun <C: Collection<T>, T> minSize(vararg collections: C, includeEmpty: Boolean= true): C?{
    var collOut: C?= null //collections.first()
//    if(collOut.isEmpty() && includeEmpty || collOut.size == 1) return collOut

    for(c in collections)
        if(c.size < collOut?.size ?: 0 || collOut == null){
            if(c.isEmpty() && includeEmpty || c.size == 1) return c
            collOut= c
        }
    return collOut
}
/** Mengambil [Array] dg jml size paling kecil. [includeEmpty] == true, maka hasil @return juga menyertakan empty-array. */
fun <T> minSize(vararg arrays: Array<T>, includeEmpty: Boolean= true): Array<T>?{
    var arrayOut: Array<T>?= null //arrays.first()
//    if(arrayOut.isEmpty() && includeEmpty || arrayOut.size == 1) return arrayOut

    for(arr in arrays)
        if(arr.size < arrayOut?.size ?: 0 || arrayOut == null){
            if(arr.isEmpty() && includeEmpty || arr.size == 1) return arr
            arrayOut= arr
        }
    return arrayOut
}

/** Mengambil [Collection] dg jml size paling besar. [includeEmpty] == true, maka hasil @return juga menyertakan empty-collection. */
fun <C: Collection<T>, T> maxSize(vararg collections: C, includeEmpty: Boolean= true): C?{
    var collOut: C?= null //collections.first()
    for(c in collections)
        if(c.size > collOut?.size ?: 0 || collOut == null){
            if(c.isEmpty() && !includeEmpty) continue
            collOut= c
        }
    return collOut
}
/** Mengambil [Array] dg jml size paling besar. [includeEmpty] == true, maka hasil @return juga menyertakan empty-array. */
fun <T> maxSize(vararg arrays: Array<T>, includeEmpty: Boolean= true): Array<T>?{
    var arrayOut: Array<T>?= null //arrays.first()
    for(arr in arrays)
        if(arr.size > arrayOut?.size ?: 0 || arrayOut == null){
            if(arr.isEmpty() && !includeEmpty) continue
            arrayOut= arr
        }
    return arrayOut
}



val Array<*>.string: String
    get(){
        var str= "${this::class.simpleName}["
        for(e in this){
            str += e.toString() +", "
        }
        str= str.removeSuffix(", ")
        return "$str]"
    }
/*
val Collection<*>.string: String
    get(){
        var str= "${this::class.simpleName}["
        for(e in this){
            str += e.toString() +", "
        }
        str= str.removeSuffix(", ")
        return "$str]"
    }
 */

val IntArray.string: String
    get(){
        var str= "${this::class.simpleName}["
        for(e in this){
            str += e.toString() +", "
        }
        str= str.removeSuffix(", ")
        return "$str]"
    }
val LongArray.string: String
    get(){
        var str= "${this::class.simpleName}["
        for(e in this){
            str += e.toString() +", "
        }
        str= str.removeSuffix(", ")
        return "$str]"
    }
val DoubleArray.string: String
    get(){
        var str= "${this::class.simpleName}["
        for(e in this){
            str += e.toString() +", "
        }
        str= str.removeSuffix(", ")
        return "$str]"
    }
val FloatArray.string: String
    get(){
        var str= "${this::class.simpleName}["
        for(e in this){
            str += e.toString() +", "
        }
        str= str.removeSuffix(", ")
        return "$str]"
    }



/*
=============================
Collection Operation
=============================
 */

fun <T> Iterable<T>.findIndexed(predicate: (IndexedValue<T>) -> Boolean): IndexedValue<T>?{
    for(vals in this.withIndex()){
        if(predicate(vals))
            return vals
    }
    return null
}

fun <T> Iterable<T>.findLastIndexed(predicate: (IndexedValue<T>) -> Boolean): IndexedValue<T>?{
    var foundElement: IndexedValue<T>?= null
    for(vals in this.withIndex()){
        if(predicate(vals))
            foundElement= vals
    }
    return foundElement
}


fun <T> Sequence<T>.findIndexed(predicate: (IndexedValue<T>) -> Boolean): IndexedValue<T>?{
    for(vals in this.withIndex()){
        if(predicate(vals))
            return vals
    }
    return null
}

fun <T> Sequence<T>.findLastIndexed(predicate: (IndexedValue<T>) -> Boolean): IndexedValue<T>?{
    var foundElement: IndexedValue<T>?= null
    for(vals in this.withIndex()){
        if(predicate(vals))
            foundElement= vals
    }
    return foundElement
}


fun <T> Array<T>.findIndexed(predicate: (IndexedValue<T>) -> Boolean): IndexedValue<T>?{
    for(vals in this.withIndex()){
        if(predicate(vals))
            return vals
    }
    return null
}

fun <T> Array<T>.findLastIndexed(predicate: (IndexedValue<T>) -> Boolean): IndexedValue<T>?{
    var foundElement: IndexedValue<T>?= null
    for(vals in this.withIndex()){
        if(predicate(vals))
            foundElement= vals
    }
    return foundElement
}


fun <T> Iterable<T>.filterIndex(predicate: (IndexedValue<T>) -> Boolean): List<IndexedValue<T>>{
    val res= ArrayList<IndexedValue<T>>()
    for(vals in this.withIndex())
        if(predicate(vals))
            res += vals
    return res
}
fun <T> Sequence<T>.filterIndex(predicate: (IndexedValue<T>) -> Boolean): Sequence<IndexedValue<T>>{
    var index= 0
    return toOtherSequence { IndexedValue(index++, it) }.filter(predicate)
}
fun <T> Array<T>.filterIndex(predicate: (IndexedValue<T>) -> Boolean): List<IndexedValue<T>>{
    val res= ArrayList<IndexedValue<T>>()
    for(vals in this.withIndex()){
        if(predicate(vals)){
            res += vals
        }
    }
    return res
}

inline fun <T> List<T>.notEmpty(block: (List<T>) -> Unit): List<T>{
    if(isNotEmpty()) block(this)
    return this
}
inline fun <T> Array<T>.notEmpty(block: (Array<T>) -> Unit): Array<T>{
    if(isNotEmpty()) block(this)
    return this
}

fun <T> Collection<Collection<T>>.isElementEmpty(): Boolean{
    for(coll in this){
        if(coll.isNotEmpty())
            return false
    }
    return true
}
fun <T> Array<Array<T>>.isElementEmpty(): Boolean{
    for(coll in this){
        if(coll.isNotEmpty())
            return false
    }
    return true
}

fun <T: Number> Array<T>.isElementZero(): Boolean{
    for(e in this)
        if(!e.isZero())
            return false
    return true
}
fun <T: Number> Collection<T>.isElementZero(): Boolean{
    for(e in this)
        if(!e.isZero())
            return false
    return true
}


fun IntArray.isElementZero(): Boolean{
    for(e in this)
        if(!e.isZero())
            return false
    return true
}
fun LongArray.isElementZero(): Boolean{
    for(e in this)
        if(!e.isZero())
            return false
    return true
}
fun DoubleArray.isElementZero(): Boolean{
    for(e in this)
        if(!e.isZero())
            return false
    return true
}
fun FloatArray.isElementZero(): Boolean{
    for(e in this)
        if(!e.isZero())
            return false
    return true
}


val Map<*, *>.string: String
    get(){
        return if(!this::class.isInstantiable){
            var res= "{"
            for(e in entries)
                res += "${e.key}=${e.value}, "
            res= res.removeSuffix(", ")
            res += "}"
            res
        }
        else toString()
    }
val Map<*, *>.namedString: String
    get()= "${this::class.simpleName}$string"

val Collection<*>.string: String
    get(){
        return if(!this::class.isInstantiable){
            var res= "["
            for(e in this)
                res += "$e, "
            res= res.removeSuffix(", ")
            res += "]"
            res
        }
        else toString()
    }

val Collection<*>.namedString: String
    get()= "${this::class.simpleName}$string"




/*
=============================
New Unique Value Creation
=============================
 */
@Suppress(SuppressLiteral.UNCHECKED_CAST, SuppressLiteral.IMPLICIT_CAST_TO_ANY)
fun <T> newUniqueValueIn(inCollection: Collection<T?>, default: T?= null, constructorParamValFunc: ((KClass<*>, KParameter) -> Any?)?= null): T? {
    var newVal= inCollection.lastOrNull()
    if(newVal == null){
        prine("inCollection kosong, nilai default: \"$default\" dikembalikan.")
        return default
    }
    while(newVal != null && newVal in inCollection){
        newVal= when(newVal){
            is Number -> newVal + 1
            is String -> "$newVal:@"
            else -> {
                if(!newVal.isKReflectionElement)
                    try{ newVal.clone<Any>(constructorParamValFunc = constructorParamValFunc)!! }
                    catch (e: Exception){
                        prine("Tidak dapat meng-instantiate key dg kelas: \"${newVal.clazz}\", nilai default: \"$default\" dikembalikan.")
                        return default
                    }
                else return default
            }
        } as? T
    }
    return newVal
}