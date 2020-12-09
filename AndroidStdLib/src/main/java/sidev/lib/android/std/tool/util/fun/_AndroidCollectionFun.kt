package sidev.lib.android.std.tool.util.`fun`

import android.os.Build
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.util.SparseLongArray
import androidx.annotation.RequiresApi
import androidx.core.util.set
import sidev.lib.`val`.Order
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.collection.SparseArrayArrangeable
import sidev.lib.android.std.collection.SparseBooleanArrayArrangeable
import sidev.lib.android.std.collection.SparseIntArrayArrangeable
import sidev.lib.android.std.collection.SparseLongArrayArrangeable
import sidev.lib.collection.array.forEachIndexed
import sidev.lib.collection.fastSort
import sidev.lib.collection.fastSortBy
import sidev.lib.collection.fastSortWith
import sidev.lib.structure.data.Arrangeable


val SparseArray<*>.lastIndex: Int
    get()= keyAt(size()-1)
val SparseIntArray.lastIndex: Int
    get()= keyAt(size()-1)
@get:RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
val SparseLongArray.lastIndex: Int
    get()= keyAt(size()-1)
val SparseBooleanArray.lastIndex: Int
    get()= keyAt(size()-1)

fun SparseArray<*>.isEmpty(): Boolean = size() == 0
fun SparseIntArray.isEmpty(): Boolean = size() == 0
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.isEmpty(): Boolean = size() == 0
fun SparseBooleanArray.isEmpty(): Boolean = size() == 0

fun SparseArray<*>.isNotEmpty(): Boolean = !isEmpty()
fun SparseIntArray.isNotEmpty(): Boolean = !isEmpty()
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.isNotEmpty(): Boolean = !isEmpty()
fun SparseBooleanArray.isNotEmpty(): Boolean = !isEmpty()

fun <T> SparseArray<T>.firstOrNull(): T? = if(isNotEmpty()) valueAt(0) else null
fun SparseIntArray.firstOrNull(): Int? = if(isNotEmpty()) valueAt(0) else null
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.firstOrNull(): Long? = if(isNotEmpty()) valueAt(0) else null
fun SparseBooleanArray.firstOrNull(): Boolean? = if(isNotEmpty()) valueAt(0) else null

fun <T> SparseArray<T>.first(): T = firstOrNull() ?: throw NoSuchElementException()
fun SparseIntArray.first(): Int = firstOrNull() ?: throw NoSuchElementException()
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.first(): Long = firstOrNull() ?: throw NoSuchElementException()
fun SparseBooleanArray.first(): Boolean = firstOrNull() ?: throw NoSuchElementException()

fun <T> SparseArray<T>.lastOrNull(): T? = if(isNotEmpty()) valueAt(lastIndex) else null
fun SparseIntArray.lastOrNull(): Int? = if(isNotEmpty()) valueAt(lastIndex) else null
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.lastOrNull(): Long? = if(isNotEmpty()) valueAt(lastIndex) else null
fun SparseBooleanArray.lastOrNull(): Boolean? = if(isNotEmpty()) valueAt(lastIndex) else null

fun <T> SparseArray<T>.last(): T = lastOrNull() ?: throw NoSuchElementException()
fun SparseIntArray.last(): Int = lastOrNull() ?: throw NoSuchElementException()
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.last(): Long = lastOrNull() ?: throw NoSuchElementException()
fun SparseBooleanArray.last(): Boolean = lastOrNull() ?: throw NoSuchElementException()


fun <T> SparseArray<T>.addLast(element: T) = append(lastIndex +1, element)
fun SparseIntArray.addLast(element: Int= lastIndex +1) = append(lastIndex +1, element)
//@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.addLast(element: Long= (lastIndex +1).toLong()) = append(lastIndex +1, element)
fun SparseBooleanArray.addLast(element: Boolean= false) = append(lastIndex +1, element)


val <T> SparseArray<T>.keys: Sequence<Int>
    get()= object: Sequence<Int>{
        override fun iterator(): Iterator<Int>
            = object : Iterator<Int>{
            private var index= 0
            override fun hasNext(): Boolean = index < size()
            override fun next(): Int = keyAt(index++)
        }
    }
val SparseIntArray.keys: Sequence<Int>
    get()= object: Sequence<Int>{
        override fun iterator(): Iterator<Int>
            = object : Iterator<Int>{
            private var index= 0
            override fun hasNext(): Boolean = index < size()
            override fun next(): Int = keyAt(index++)
        }
    }
val SparseLongArray.keys: Sequence<Int>
    get()= object: Sequence<Int>{
        override fun iterator(): Iterator<Int>
            = object : Iterator<Int>{
            private var index= 0
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            override fun hasNext(): Boolean = index < size()
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            override fun next(): Int = keyAt(index++)
        }
    }
val SparseBooleanArray.keys: Sequence<Int>
    get()= object: Sequence<Int>{
        override fun iterator(): Iterator<Int>
            = object : Iterator<Int>{
            private var index= 0
            override fun hasNext(): Boolean = index < size()
            override fun next(): Int = keyAt(index++)
        }
    }
val <T> SparseArray<T>.values: Sequence<T>
    get()= object: Sequence<T>{
        override fun iterator(): Iterator<T>
            = object : Iterator<T>{
            private var index= 0
            override fun hasNext(): Boolean = index < size()
            override fun next(): T = valueAt(index++)
        }
    }
val SparseIntArray.values: Sequence<Int>
    get()= object: Sequence<Int>{
        override fun iterator(): Iterator<Int>
            = object : Iterator<Int>{
            private var index= 0
            override fun hasNext(): Boolean = index < size()
            override fun next(): Int = valueAt(index++)
        }
    }
val SparseLongArray.values: Sequence<Long>
    get()= object: Sequence<Long>{
        override fun iterator(): Iterator<Long>
            = object : Iterator<Long>{
            private var index= 0
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            override fun hasNext(): Boolean = index < size()
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            override fun next(): Long = valueAt(index++)
        }
    }
val SparseBooleanArray.values: Sequence<Boolean>
    get()= object: Sequence<Boolean>{
        override fun iterator(): Iterator<Boolean>
            = object : Iterator<Boolean>{
            private var index= 0
            override fun hasNext(): Boolean = index < size()
            override fun next(): Boolean = valueAt(index++)
        }
    }

/** Removes the entry for [key] only if it is mapped to [value]. */
fun <T> SparseArray<T>.remove_(key: Int, value: T): Boolean {
    val index = indexOfKey(key).let {
        if(it >= -1) it else -it //Terkadang index yg dihasilkan menjadi kebalikan dari index itu, contoh 11 jadi -11 dan 5 jadi -5.
    }

    if (index != -1 && value == valueAt(index)) {
        removeAt(index)
        return true
    }
    return false
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
        private var i= 0
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

operator fun SparseLongArray.iterator(): Iterator<Pair<Int, Long>> {
    return object : Iterator<Pair<Int, Long>>{
        private var i= 0
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        override fun hasNext(): Boolean {
            return i < this@iterator.size()
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        override fun next(): Pair<Int, Long> {
            val key= this@iterator.keyAt(i++)
            return Pair(
                key,
                this@iterator[key]
            )
        }
    }
}

operator fun SparseBooleanArray.iterator(): Iterator<Pair<Int, Boolean>> {
    return object : Iterator<Pair<Int, Boolean>>{
        private var i= 0
        override fun hasNext(): Boolean {
            return i < this@iterator.size()
        }

        override fun next(): Pair<Int, Boolean> {
            val key= this@iterator.keyAt(i++)
            return Pair(
                key,
                this@iterator[key]
            )
        }
    }
}


fun <T> SparseArray<T>.asArrangeable(): Arrangeable<T> = SparseArrayArrangeable(this)
fun SparseIntArray.asArrangeable(): Arrangeable<Int> = SparseIntArrayArrangeable(this)
fun SparseLongArray.asArrangeable(): Arrangeable<Long> = SparseLongArrayArrangeable(this)
fun SparseBooleanArray.asArrangeable(): Arrangeable<Boolean> = SparseBooleanArrayArrangeable(this)



inline fun <reified T> SparseArray<T>.toTypedArray(): Array<T> = Array(size()){ valueAt(it) }
fun SparseArray<*>.toArray(): Array<Any?> = Array(size()){ valueAt(it) }


fun <T: Comparable<*>> SparseArray<T>.sort(from: Int= 0, until: Int= size(), order: Order = Order.ASC) {
    val arr= toArray().apply { fastSort(from, until, order) }
    clear()
    @Suppress(SuppressLiteral.UNCHECKED_CAST)
    arr.forEachIndexed { i, e -> this[i]= e as T }
}
fun <T, R: Comparable<*>> SparseArray<T>.sortBy(
    from: Int= 0, until: Int= size(), order: Order = Order.ASC,
    toComparableFun: (T) -> R
) {
    @Suppress(SuppressLiteral.UNCHECKED_CAST)
    val arr= toArray().apply { fastSortBy(from, until, order, toComparableFun as (Any?) -> R) }
    clear()
    @Suppress(SuppressLiteral.UNCHECKED_CAST)
    arr.forEachIndexed { i, e -> this[i]= e as T }
}
fun <T> SparseArray<T>.sortWith(
    from: Int= 0, until: Int= size(),
    comparator: (n1: T, n2: T) -> Int
) {
    @Suppress(SuppressLiteral.UNCHECKED_CAST)
    val arr= toArray().apply { fastSortWith(from, until, comparator as (Any?, Any?) -> Int) }
    clear()
    @Suppress(SuppressLiteral.UNCHECKED_CAST)
    arr.forEachIndexed { i, e -> this[i]= e as T }
}
fun <T: Comparable<*>> SparseArray<T>.sorted(from: Int= 0, until: Int= size(), order: Order = Order.ASC): SparseArray<T> =
    clone().apply { sort(from, until, order) }
fun <T, R: Comparable<*>> SparseArray<T>.sortedBy(
    from: Int= 0, until: Int= size(), order: Order = Order.ASC,
    toComparableFun: (T) -> R
): SparseArray<T> = clone().apply { sortBy(from, until, order, toComparableFun) }
fun <T> SparseArray<T>.sortedWith(
    from: Int= 0, until: Int= size(),
    comparator: (n1: T, n2: T) -> Int
): SparseArray<T> = clone().apply { sortWith(from, until, comparator) }


fun SparseIntArray.toArray(): IntArray = IntArray(size()){ valueAt(it) }

fun SparseIntArray.sort(from: Int= 0, until: Int= size(), order: Order = Order.ASC) {
    val arr= toArray().apply { fastSort(from, until, order) }
    clear()
    arr.forEachIndexed { i, e -> this[i]= e }
}
fun SparseIntArray.sortWith(from: Int= 0, until: Int= size(), comparator: (n1: Int, n2: Int) -> Int) {
    val arr= toArray().apply { fastSortWith(from, until, comparator) }
    clear()
    arr.forEachIndexed { i, e -> this[i]= e }
}
fun SparseIntArray.sorted(from: Int= 0, until: Int= size(), order: Order = Order.ASC): SparseIntArray =
    clone().apply { sort(from, until, order) }
fun SparseIntArray.sortedWith(
    from: Int= 0, until: Int= size(),
    comparator: (n1: Int, n2: Int) -> Int
): SparseIntArray = clone().apply { sortWith(from, until, comparator) }


@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.toArray(): LongArray = LongArray(size()){ valueAt(it) }

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.sort(from: Int= 0, until: Int= size(), order: Order = Order.ASC) {
    val arr= toArray().apply { fastSort(from, until, order) }
    clear()
    arr.forEachIndexed { i, e -> this[i]= e }
}
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.sortWith(from: Int= 0, until: Int= size(), comparator: (n1: Long, n2: Long) -> Int) {
    val arr= toArray().apply { fastSortWith(from, until, comparator) }
    clear()
    arr.forEachIndexed { i, e -> this[i]= e }
}
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.sorted(from: Int= 0, until: Int= size(), order: Order = Order.ASC): SparseLongArray =
    clone().apply { sort(from, until, order) }
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SparseLongArray.sortedWith(
    from: Int= 0, until: Int= size(),
    comparator: (n1: Long, n2: Long) -> Int
): SparseLongArray = clone().apply { sortWith(from, until, comparator) }


fun SparseBooleanArray.toArray(): BooleanArray = BooleanArray(size()){ valueAt(it) }

fun SparseBooleanArray.sort(from: Int= 0, until: Int= size(), order: Order = Order.ASC) {
    val arr= toArray().apply { fastSort(from, until, order) }
    clear()
    arr.forEachIndexed { i, e -> this[i]= e }
}
fun SparseBooleanArray.sortWith(from: Int= 0, until: Int= size(), comparator: (n1: Boolean, n2: Boolean) -> Int) {
    val arr= toArray().apply { fastSortWith(from, until, comparator) }
    clear()
    arr.forEachIndexed { i, e -> this[i]= e }
}
fun SparseBooleanArray.sorted(from: Int= 0, until: Int= size(), order: Order = Order.ASC): SparseBooleanArray =
    clone().apply { sort(from, until, order) }
fun SparseBooleanArray.sortedWith(
    from: Int= 0, until: Int= size(),
    comparator: (n1: Boolean, n2: Boolean) -> Int
): SparseBooleanArray = clone().apply { sortWith(from, until, comparator) }