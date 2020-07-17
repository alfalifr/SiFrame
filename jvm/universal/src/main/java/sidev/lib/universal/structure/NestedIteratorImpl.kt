package sidev.lib.universal.structure

import android.view.ViewGroup
import sidev.lib.universal.`fun`.*

/**
 * Digunakan untuk melakukan iterasi terhadap data yg memiliki banyak keturunan.
 * Sebagai contoh adalah [ViewGroup] pada Android yg punya banyak child view.
 *
 * Iterasi dilakukan menggunakan metode DEPTH-FIRST PRE-ORDER.
 */
abstract class NestedIteratorImpl<I, O>(private val startInputIterator: Iterator<I>?)
    : NestedIterator<I, O>, SkippableIterator<O>{
    constructor(startInputIterable: Iterable<I>): this(startInputIterable.iterator())
    constructor(start: I): this((start as? Iterable<I>)?.iterator()){
        this.start= start
    }
    open val tag: String= this::class.java.simpleName
    protected var start: I?= null

    internal open val activeOutputLines= ArrayList<Iterator<O>>()
    internal open val activeInputLines= ArrayList<Iterator<I>>()
//    private var activeLineRowIndex= -1 //Child ke pertama dimulai dari index 0 [activeLines].
    internal open var activeOutputIterator: Iterator<O>?= null
    internal open var activeInputIterator: Iterator<I>?= null
    private var hasInited= false
    private var nowIntput: I?= null
        set(v){
            field= v
            prinr("nowIntput= $nowIntput tag= $tag")
        }
    private var nowOutput: O?= null
        set(v){
            field= v
            prinr("nowOutput= $nowOutput tag= $tag")
        }

    /** Varibael yg mengindikasikan bahwa pengecekan [hasNext] hanya sebatas pengecekan dan hasil dari [next] belum tentu dipakai oleh pemanggil. */
    private var isHasNextOnlyCheck= false
    /** Variabel yg digunakan untuk menampung nilai dari [activeOutputIterator.next] saat isHasNextOnlyCheck == true. */
    private val dumpedOutput= ArrayList<O>()
//    private var nowInput: I?= null


    fun hasNext(onlyCheck: Boolean): Boolean{
        isHasNextOnlyCheck= onlyCheck
        return hasNext()
    }

    final override fun hasNext(): Boolean{
        val hasNext= if(!hasInited){
//            getChildIterator(start)
            hasInited= true
//            activeIterator != null //Jika null, itu artinya object dg iterator ini gak punya element.
//            activeOutputIterator?.hasNext() == true
            initActiveIterator()
        } else{
            //1. Teruskan semua activeOutputIterator.next()
            //2. Selagi iterasi activeOutputIterator, cek apakah terdapat nested activeInputIterator
            //3. Jika !activeOutputIterator.hasNext(), maka ganti activeInputIterator.next()
            //4. Selagi iterasi activeInputIterator, cek apakah terdapat nested activeOutputIterator

//            prind("activeOutputIterator?.hasNext()= ${activeOutputIterator?.hasNext()}")
//            prind("activeInputIterator?.hasNext()= ${activeInputIterator?.hasNext()}")

            prinr("dumpedOutput.isEmpty()= ${dumpedOutput.isEmpty()} tag= $tag")
            if(dumpedOutput.isEmpty()){
//                val hasNextOutput= hasNextOutput()
//                prinr("hasNextOutput= $hasNextOutput tag= $tag")
                if(!hasNextOutput()){
//                    val hasNextInput= hasNextInput()
//                    prinr("hasNextInput= $hasNextInput tag= $tag")
//                val a= hasNextInput()
//                prind("hasNext() hasNextInput()= ${hasNextInput()}")
                    if(hasNextInput()){
                        prind("hasNext() TESTTT asasa tag= $tag")
//                        var doneFirst= false
                        do{
                            prind("hasNext() TESTTT tag= $tag")
                            if(!hasNextInput()) break
//                        doneFirst= true
                            nowIntput= activeInputIterator!!.next()
                            prind("hasNext() input= $nowIntput tag= $tag")
                        } while (!getNextOutputIterator(nowIntput!!))
                        hasNextOutput()
                    } else false
                } else true
            } else true
/*
            while(!activeOutputIterator!!.hasNext() && activeOutputLines.size > 1){
                activeOutputLines.remove(activeOutputIterator!!)
                activeOutputIterator= activeOutputLines.last()
            }
            if(!activeOutputIterator!!.hasNext()){
                while(!activeInputIterator!!.hasNext() && activeInputLines.size > 1){
                    activeInputLines.remove(activeInputIterator!!)
                    activeInputIterator= activeInputLines.last()
                }
                getOutputIterator()
            }
 */
//            activeOutputIterator!!.hasNext() //Gak mungkin null, karena kalo ada null, nullnya di awal.
//                    || activeLines.size > 1
        }
        //Jika ternyata [now] diskip, maka lakukan perhitungan [hasNext] selanjutnya pada fungsi ini.
        prind("hasNext= $hasNext tag= $tag")

        val hasNextRes= if(hasNext){
            nowOutput= try{ activeOutputIterator!!.next() } catch(e: NoSuchElementException){ dumpedOutput.first() }
//            if(!isHasNextOnlyCheck)
//                nowOutput= activeOutputIterator!!.next()
            //dumpedOutput.takeFirstOrNull() ?:

            if(isHasNextOnlyCheck)
                dumpedOutput.addIfAbsent(nowOutput!!) { it == nowOutput }

            prind("hasNext() nowOutput= $nowOutput tag= $tag")

            if(skip(nowOutput!!)){
                if(isHasNextOnlyCheck) dumpedOutput.takeFirstOrNull()
                hasNext()
            } else true
        } else false

//        isHasNextOnlyCheck= false
        return hasNextRes
    }

    final override fun next(): O {
        if(nowOutput != null)
            getNextInputIterator(nowOutput!!)

        prind("nowOutput!! = ${nowOutput!!} tag= $tag")
        return if(!isHasNextOnlyCheck) nowOutput!!
        else {
//            isHasNextOnlyCheck= false
            dumpedOutput.takeFirst()
        }
    }

    /**
     * @return null jika sudah tidak ada lagi nested iterator pada [nowInput].
     */
    abstract override fun getOutputIterator(nowInput: I): Iterator<O>?

    abstract override fun getInputIterator(nowOutput: O): Iterator<I>?

    /**
     * @return true maka [now] akan dilewati
     */
    override fun skip(now: O): Boolean= false

    /**
     * @return true jika [out] menghasilkan [activeInputIterator] yg baru
     *   iterator yg dihasilkan memiliki next ([Iterator.hasNext]).
     */
    private fun getNextInputIterator(out: O): Boolean{
        val inItr= getInputIterator(out)
//        prine("inItr != null => ${inItr != null} ")
        return if(inItr != null &&
            inItr.asNotNullTo { itr: NestedIteratorImpl<*, *> -> itr.hasNext(true) }
            ?: inItr.hasNext()){
            activeInputLines.add(inItr)
            activeInputIterator= inItr
            true
        } else false
    }
    /**
     * @return true jika [inn] menghasilkan [activeOutputIterator] yg baru dan
     *   iterator yg dihasilkan memiliki next ([Iterator.hasNext]).
     */
    private fun getNextOutputIterator(inn: I): Boolean{
        val outItr= getOutputIterator(inn)
//        prind("getNextOutputIterator() inn= $inn tag= $tag")
//        prind("outItr != null => ${outItr != null} outItr.hasNext() => ${outItr?.hasNext()}")
        return if(outItr != null &&
            outItr.asNotNullTo { itr: NestedIteratorImpl<*, *> -> itr.hasNext(true) }
            ?: outItr.hasNext()){
            activeOutputLines.add(outItr)
            activeOutputIterator= outItr
            true
        } else false
    }

    protected fun hasNextInput(): Boolean{
        if(activeInputIterator != null){
            while(
                !(activeInputIterator.asNotNullTo { itr: NestedIteratorImpl<*, *> -> itr.hasNext(true) }
                    ?: activeInputIterator!!.hasNext())
                && activeInputLines.size > 1){
                activeInputLines.remove(activeInputIterator!!)
                activeInputIterator= activeInputLines.last()
            }
        }
        return (activeInputIterator.asNotNullTo { itr: NestedIteratorImpl<*, *> -> itr.hasNext(true) }
            ?: activeInputIterator?.hasNext() ?: false)
    }

    protected fun hasNextOutput(): Boolean{
        if(activeOutputIterator != null && dumpedOutput.isEmpty()){
            while(
                !(activeOutputIterator.asNotNullTo { itr: NestedIteratorImpl<*, *> -> itr.hasNext(true) }
                    ?: activeOutputIterator!!.hasNext())
                && activeOutputLines.size > 1){
                activeOutputLines.remove(activeOutputIterator!!)
                activeOutputIterator= activeOutputLines.last()
            }
        }
        return (activeOutputIterator.asNotNullTo { itr: NestedIteratorImpl<*, *> -> itr.hasNext(true) }
                ?: activeOutputIterator?.hasNext() ?: false)
                || dumpedOutput.isNotEmpty()
    }

    private fun initActiveIterator(): Boolean{
        val itr= startInputIterator?.iterator()
        val hasStart= if(itr?.hasNext() == true) {
            activeInputLines.add(itr)
            activeInputIterator= itr
            true
        } else start != null
        prind("initActiveIterator() hasStart= $hasStart start != null => ${start != null} itr?.hasNext()= ${itr?.hasNext()}")
        return if(hasStart) {
            if(!hasNextOutput()){
                (activeInputIterator?.next() ?: start)
                    .notNullTo { getNextOutputIterator(it) } == true
            } else true
        }
        else false
    }
/*
    private fun isStartSameTypeAs(output: O): Boolean{
        val startClass= start?.clazz
        isStartSameTypeAsOut= if(startClass != null){
            output?.clazz?.isSuperclassOf(startClass) == true
        } else false
        return isStartSameTypeAsOut
    }
 */
}