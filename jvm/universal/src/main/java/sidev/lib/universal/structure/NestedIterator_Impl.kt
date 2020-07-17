package sidev.lib.universal.structure

import android.view.ViewGroup

/**
 * Digunakan untuk melakukan iterasi terhadap data yg memiliki banyak keturunan.
 * Sebagai contoh adalah [ViewGroup] pada Android yg punya banyak child view.
 *
 * Iterasi dilakukan menggunakan metode DEPTH-FIRST PRE-ORDER.
 */
abstract class NestedIterator_Impl<T>(private val startIterator: Iterable<T>?)
    : NestedIterator_<T>, SkippableIterator<T>{
    constructor(start: T): this(start as? Iterable<T>){
        this.start= start
    }
    private var start: T?= null

    private val activeLines= ArrayList<Iterator<T>>()
//    private var activeLineRowIndex= -1 //Child ke pertama dimulai dari index 0 [activeLines].
    private var activeIterator: Iterator<T>?= null
    private var hasInited= false
    private var now: T?= null

    final override fun hasNext(): Boolean{
        val hasNext= if(!hasInited){
//            getChildIterator(start)
            initActiveIterator()
            hasInited= true
//            activeIterator != null //Jika null, itu artinya object dg iterator ini gak punya element.
            activeIterator?.hasNext() == true
        } else{
            while(!activeIterator!!.hasNext() && activeLines.size > 1){
                activeLines.remove(activeIterator!!)
                activeIterator= activeLines.last()
            }
            activeIterator!!.hasNext() //Gak mungkin null, karena kalo ada null, nullnya di awal.
//                    || activeLines.size > 1
        }
        //Jika ternyata [now] diskip, maka lakukan perhitungan [hasNext] selanjutnya pada fungsi ini.
        return if(hasNext && skip(activeIterator!!.next().also { now= it })){
            hasNext()
        } else hasNext
    }

    final override fun next(): T {
//        val now= now//activeIterator!!.next()
/*
            <13 Juli 2020> => Definisi lama. Pergantian [activeIterator] dilakukan di [hasNext].
            if(activeIterator?.hasNext() == true) activeIterator!!.next()
            else {
                activeLines.remove(activeIterator!!)
                activeIterator= activeLines.last()
                activeIterator?.next()
            }
 */

//        var child: Iterator<T>?= null
        if(now != null)
            getChildIterator(now!!)

        return now!!
    }

    /**
     * @return null jika sudah tidak ada lagi nested iterator pada [now].
     */
    abstract override fun getIterator(now: T): Iterator<T>?

    /**
     * @return true maka [now] akan dilewati
     */
    override fun skip(now: T): Boolean= false

    private fun getChildIterator(now: T){
        val child= getIterator(now)
        if(child != null){
            activeLines.add(child)
            activeIterator= child
        }
    }

    private fun initActiveIterator(){
        val itr= startIterator?.iterator()
        if(itr != null){
            activeLines.add(itr)
            activeIterator= itr
        } else if(start != null)
            getChildIterator(start!!)
    }
}