package sidev.lib.android.siframe.tool.util.`fun`

import android.util.SparseArray
import android.util.SparseIntArray


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