package sidev.lib.universal.structure

import android.util.Log
import android.view.ViewGroup

/**
 * Digunakan untuk melakukan iterasi terhadap data yg memiliki banyak keturunan.
 * Sebagai contoh adalah [ViewGroup] pada Android yg punya banyak child view.
 *
 * Iterasi dilakukan menggunakan metode DEPTH-FIRST PRE-ORDER.
 */
abstract class NestedIterator<T>(val start: T): Iterator<T>{
    private val activeLines= ArrayList<Iterator<T>>()
//    private var activeLineRowIndex= -1 //Child ke pertama dimulai dari index 0 [activeLines].
    private var activeIterator: Iterator<T>?= null
    private var hasInited= false
//    private var now: T?= null

    override fun hasNext(): Boolean{
        return if(!hasInited){
            getChildIterator(start)
            hasInited= true
            activeIterator != null //Jika null, itu artinya object dg iterator ini gak punya element.
        } else{
            activeIterator?.hasNext() == true
                    || activeLines.size > 1
        }
    }

    override fun next(): T {
        val now=
            if(activeIterator?.hasNext() == true) activeIterator!!.next()
            else {
                activeLines.remove(activeIterator!!)
                activeIterator= activeLines.last()
                activeIterator?.next()
            }

//        var child: Iterator<T>?= null
        if(now != null)
            getChildIterator(now)

        return now!!
    }

    /**
     * @return null jika sudah tidak ada lagi element pada [index].
     */
//    abstract fun getElement(index: Int): T?
    abstract fun getIterator(now: T): Iterator<T>?

    private fun getChildIterator(now: T){
        val child= getIterator(now)
        if(child != null){
            activeLines.add(child)
            activeIterator= child
        }
    }
}