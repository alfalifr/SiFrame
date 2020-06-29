package sidev.lib.universal.`fun`

import android.util.SparseArray
import android.util.SparseIntArray
import kotlin.collections.ArrayList

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
fun <T> Array<T>.toArrayList(): ArrayList<T>{
    val list= ArrayList<T>()
    this.forEach { list.add(it) }
    return list
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

fun Any.indices(): IntRange? {
    return when(this){
        is Array<*> -> this.indices
        is Collection<*> -> this.indices
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



fun <T> SparseArray<T>.keys(): List<Int>{
    val keys= ArrayList<Int>()
    for(i in 0 until this.size()){
        keys.add(this.keyAt(i))
    }
    return keys
}

operator fun <T> SparseArray<T>.iterator(): Iterator<Pair<Int, T>> {
    return object : Iterator<Pair<Int, T>>{
        var i= 0
        override fun hasNext(): Boolean {
            return i < this@iterator.size()
        }

        override fun next(): Pair<Int, T> {
            val key= this@iterator.keyAt(i++)
            return Pair(
                key,
                this@iterator[key]
            )
        }
    }
}
//Anggap key urut walau urutan assign tidak urut.
operator fun SparseIntArray.iterator(): Iterator<Pair<Int, Int>> {
    return object : Iterator<Pair<Int, Int>>{
        var i= 0
        override fun hasNext(): Boolean {
            return i < this@iterator.size()
        }

        override fun next(): Pair<Int, Int> {
            val key= this@iterator.keyAt(i++)
            return Pair(
                key,
                this@iterator[key]
            )
        }
    }
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