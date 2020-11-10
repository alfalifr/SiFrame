package sidev.lib.android.std.tool.util.`fun`

import android.util.SparseArray
import android.util.SparseIntArray


fun <T> SparseArray<T>.addLast(element: T) = append(keys.last() +1, element)
fun SparseIntArray.addLast(element: Int= keys.last() +1) = append(keys.last() +1, element)

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