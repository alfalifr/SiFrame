package sidev.lib.android.siframe.tool

import android.util.SparseIntArray
import androidx.core.util.set
import sidev.lib.android.siframe.intfc.ReArrangeable

/**
 * Sementara ini penggunaannya msh hanya untuk android.
 */
abstract class ContentArranger<T>: ReArrangeable<T> {
    protected var sortedInd= SparseIntArray()
    protected var filteredInd= SparseIntArray()

    /**
     * Mapping indeks hasil akhir content yg ditampilkan.
     * [resultInd] dapat diset dari luar kelas ini.
     */
    var resultInd= SparseIntArray()

    /**
     * @param pos merupakan indeks sesungguhnya dari konten yg sedang di-arrange,
     *   bkn indeks [resultInd].
     */
    abstract fun getContent(pos: Int): T
    abstract fun getContentCount(): Int
    abstract fun isContentEmpty(): Boolean

    /**
     * [pos1] dijamin selalu lebih kecil dari [pos2].
     * [pos1] dan [pos2] merupakan indeks dari [resultInd],
     * bkn indeks sesungguhnya dari konten yg sedang di-arrange.
     */
    final override fun sort(func: (pos1: Int, data1: T, pos2: Int, data2: T) -> Boolean) {
        if(isContentEmpty()) return

        if(filteredInd.size() != sortedInd.size())
            resetSort()

        for(i in 0 until resultInd.size())
            for(u in i+1 until resultInd.size()){
                val sortedIndI= sortedInd[i]
                val sortedIndU= sortedInd[u]
                if(!func(i, getContent(sortedIndI), u, getContent(sortedIndU))){
                    val temp= sortedInd[i]
                    sortedInd[i]= sortedInd[u]
                    sortedInd[u]= temp
//                    loge("sort() TUKAR i= $i u= $u \n sortedIndMap[i]= ${sortedIndMap[i]} sortedIndMap[u]= ${sortedIndMap[u]}")
                }
            }
        mapResultToSortedInd()
    }

    /**
     * [pos] merupakan indeks dari [resultInd],
     * bkn indeks sesungguhnya dari konten yg sedang di-arrange.
     */
    final override fun filter(func: (pos: Int, data: T) -> Boolean) {
        if(isContentEmpty()) return

        filteredInd.clear()

        var u= -1
        for(i in 0 until resultInd.size())
            if(func(i, getContent(resultInd[i])))
                filteredInd[++u]= resultInd[i]
        mapResultToFilteredInd()
    }

    protected fun mapResultToSortedInd(){
        resultInd= sortedInd.clone()
    }
    protected fun mapResultToFilteredInd(){
        resultInd= filteredInd.clone()
    }

    /**
     * Mengembalikan urutan sesuai dg urutan semula, yaitu sesuai indeks pada [filteredInd].
     */
    fun resetSort(){
        sortedInd= filteredInd.clone()
        resultInd= filteredInd.clone()
    }
    /**
     * Mengembalikan jumlah konten sesuai dg yg semula, yaitu sesuai indeks pada [sortedInd].
     */
    fun resetFilter(){
        filteredInd= sortedInd.clone()
        resultInd= sortedInd.clone()
    }

    /**
     * Mengembalikan arrangement konten sesuai dg yg paling awal.
     * Mengembalikan indeks pada [sortedInd] dan [filteredInd].
     */
    fun reset(){
        resultInd.clear()
        filteredInd.clear()
        sortedInd.clear()
        for(i in 0 until getContentCount()){
            resultInd[i]= i
            filteredInd[i]= i
            sortedInd[i]= i
        }
    }
}