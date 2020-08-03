package sidev.lib.universal.structure.collection.iterator

import android.view.ViewGroup
import sidev.lib.universal.`fun`.*
import sidev.lib.universal.structure.data.TaggedBoxedVal


/**
 * Digunakan untuk melakukan iterasi terhadap data yg memiliki banyak keturunan.
 * Sebagai contoh adalah [ViewGroup] pada Android yg punya banyak child view.
 *
 * Iterasi dilakukan menggunakan metode DEPTH-FIRST PRE-ORDER.
 */
abstract class NestedIteratorImpl<I, O>(internal val startInputIterator: Iterator<I>?)
    : NestedIterator<I, O>,
    SkippableIterator<O> {
    constructor(startInputIterable: Iterable<I>): this(startInputIterable.iterator())
    constructor(start: I): this((start as? Iterable<I>)?.iterator()){
        this.start= start
    }
    open val tag: String= this::class.java.simpleName
    internal var start: I?= null

    internal open val activeOutputLines= ArrayList<Iterator<O>>()
    internal open val activeInputLines= ArrayList<Iterator<I>>()
    internal open var activeOutputIterator: Iterator<O>?= null
    internal open var activeInputIterator: Iterator<I>?= null
    private var hasInited= false
    private var nextState: Int= -1 //-1 blum diketahui, 0 tidak ada lagi iterasi, 1 lanjut.
    private var nowOutput: O?= null
        set(v){
            field= v
            prind("nowOutput= $nowOutput tag= $tag")
        }

    /** Varibael yg mengindikasikan bahwa pengecekan [hasNext] hanya sebatas pengecekan dan hasil dari [next] belum tentu dipakai oleh pemanggil. */
//    private var isHasNextOnlyCheck= false
    /** Variabel yg digunakan untuk menampung nilai dari [activeOutputIterator.next] saat isHasNextOnlyCheck == true. */
//    private val outputWaitingList= ArrayList<TaggedBoxedVal<Int, O>>()
//    private var nowInput: I?= null


    private fun calcNext(): Boolean{
        val hasNext= if(!hasInited){
            hasInited= true
            initActiveIterator()
        } else{
            if(!hasNextOutput()){
                hasNextInput() && getOutputFromInput()
            } else true
        }
        prind("hasNext= $hasNext tag= $tag")

        val hasNextRes= if(hasNext){
            nowOutput= activeOutputIterator!!.next()

            prind("hasNext() nowOutput= $nowOutput tag= $tag")

            if(skip(nowOutput!!)){
                calcNext()
            } else true
        } else false

        nextState= if(hasNextRes) 1 else 0
        return hasNextRes
    }

    final override fun hasNext(): Boolean{
        if(nextState == -1) calcNext()
        return nextState == 1
    }

    final override fun next(): O {
        if(nextState == -1) calcNext()
        if(nextState == 0) throw NoSuchElementException("Isi dari iterator ${this::class.simpleName} udah habis")

        getNextInputIterator(nowOutput!!)

        val res= nowOutput!!
        nowOutput= null
        nextState= -1
        return res //outputWaitingList.takeFirst().value!!
    }

    /** @return null jika sudah tidak ada lagi nested iterator pada [nowInput]. */
    abstract override fun getOutputIterator(nowInput: I): Iterator<O>?

    abstract override fun getInputIterator(nowOutput: O): Iterator<I>?

    /** @return true maka [now] akan dilewati. */
    override fun skip(now: O): Boolean= false

    /**
     * @return true jika [out] menghasilkan [activeInputIterator] yg baru
     *   iterator yg dihasilkan memiliki next ([Iterator.hasNext]).
     */
    private fun getNextInputIterator(out: O): Boolean{
        val inItr= getInputIterator(out)
        return if(inItr != null && inItr.hasNext()){
            addInputIterator(inItr)
//            activeInputLines.add(inItr)
//            activeInputIterator= inItr
            true
        } else false
    }
    internal open fun addInputIterator(inItr: Iterator<I>){
        activeInputLines.add(inItr)
        activeInputIterator= inItr
    }
    /**
     * @return true jika [inn] menghasilkan [activeOutputIterator] yg baru dan
     *   iterator yg dihasilkan memiliki next ([Iterator.hasNext]).
     */
    private fun getNextOutputIterator(inn: I): Boolean{
        val outItr= getOutputIterator(inn)
        return if(outItr != null && outItr.hasNext()){
            addOutputIterator(outItr)
//            activeOutputLines.add(outItr)
//            activeOutputIterator= outItr
            true
        } else false
    }
    internal open fun addOutputIterator(outItr: Iterator<O>){
        activeOutputLines.add(outItr)
        activeOutputIterator= outItr
    }

    private fun getOutputFromInput(): Boolean{
        while(hasNextInput() && !getNextOutputIterator(activeInputIterator!!.next()));
        return hasNextOutput()
    }

    private fun hasNextInput(): Boolean{
        if(activeInputIterator != null){
            while(!activeInputIterator!!.hasNext() && activeInputLines.size > 1){
                changeLastActiveInputIterator(activeInputIterator!!)
//                activeInputLines.remove(activeInputIterator!!)
//                activeInputIterator= activeInputLines.last()
            }
        }
        return activeInputIterator?.hasNext() == true
    }
    internal open fun changeLastActiveInputIterator(currentActiveInputIterator: Iterator<I>){
        activeInputLines.remove(currentActiveInputIterator)
        activeInputIterator= activeInputLines.last()
    }

    private fun hasNextOutput(): Boolean{
        if(activeOutputIterator != null){
            while(!activeOutputIterator!!.hasNext() && activeOutputLines.size > 1){
                changeLastActiveOutputIterator(activeOutputIterator!!)
//                activeOutputLines.remove(activeOutputIterator!!)
//                activeOutputIterator= activeOutputLines.last()
            }
        }
        return activeOutputIterator?.hasNext() == true
    }
    internal open fun changeLastActiveOutputIterator(currentActiveOutputIterator: Iterator<O>){
        activeOutputLines.remove(currentActiveOutputIterator)
        activeOutputIterator= activeOutputLines.last()
    }

    private fun initActiveIterator(): Boolean{
        val itr= startInputIterator //?.iterator()
            ?: if(start != null) newIteratorSimple(start!!) else null

        return if(itr?.hasNext() == true) {
            addInputIterator(itr)
//            activeInputLines.add(itr)
//            activeInputIterator= itr

            if(!hasNextOutput()){
                getOutputFromInput()
            } else true
        } else false
    }
}




/*
<20 Juli 2020> => Rancangan lama, lebih berat karena ada cache.

/**
 * Digunakan untuk melakukan iterasi terhadap data yg memiliki banyak keturunan.
 * Sebagai contoh adalah [ViewGroup] pada Android yg punya banyak child view.
 *
 * Iterasi dilakukan menggunakan metode DEPTH-FIRST PRE-ORDER.
 */
abstract class NestedIteratorImpl<I, O>(private val startInputIterator: Iterator<I>?)
    : NestedIterator<I, O>,
    SkippableIterator<O> {
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
    private var nextState: Int= -1 //-1 blum kiketahui, 0 tidak lagi iterasi, 1 lanjut.
    private var nowOutput: O?= null
        set(v){
            field= v
            prinr("nowOutput= $nowOutput tag= $tag")
        }

    /** Varibael yg mengindikasikan bahwa pengecekan [hasNext] hanya sebatas pengecekan dan hasil dari [next] belum tentu dipakai oleh pemanggil. */
//    private var isHasNextOnlyCheck= false
    /** Variabel yg digunakan untuk menampung nilai dari [activeOutputIterator.next] saat isHasNextOnlyCheck == true. */
    private val outputWaitingList= ArrayList<TaggedBoxedVal<Int, O>>()
//    private var nowInput: I?= null


    private fun calcNext(): Boolean{
        val hasNext= if(!hasInited){
            hasInited= true
            initActiveIterator()
        } else{
            //1. Teruskan semua activeOutputIterator.next()
            //2. Selagi iterasi activeOutputIterator, cek apakah terdapat nested activeInputIterator
            //3. Jika !activeOutputIterator.hasNext(), maka ganti activeInputIterator.next()
            //4. Selagi iterasi activeInputIterator, cek apakah terdapat nested activeOutputIterator

//            prind("activeOutputIterator?.hasNext()= ${activeOutputIterator?.hasNext()}")
//            prind("activeInputIterator?.hasNext()= ${activeInputIterator?.hasNext()}")

//            prinr("dumpedOutput.isEmpty()= ${outputWaitingList.isEmpty()} !hasNextOutput()= ${!hasNextOutput()} tag= $tag")
//            outputWaitingList.isEmpty() && !hasNextOutput() && hasNextInput() && getOutputFromInput()

            if(!hasNextOutput()){
                hasNextInput() && getOutputFromInput()
            } else true
        }
        prind("hasNext= $hasNext tag= $tag")

        val hasNextRes= if(hasNext){
            nowOutput= activeOutputIterator!!.next()

            prind("hasNext() nowOutput= $nowOutput tag= $tag")

            if(skip(nowOutput!!)){
                calcNext()
            } else true
        } else false

        nextState= if(hasNextRes) 1 else 0
        return hasNextRes
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

//            prinr("dumpedOutput.isEmpty()= ${outputWaitingList.isEmpty()} !hasNextOutput()= ${!hasNextOutput()} tag= $tag")
//            outputWaitingList.isEmpty() && !hasNextOutput() && hasNextInput() && getOutputFromInput()

            if(outputWaitingList.isEmpty() && !hasNextOutput()){
                hasNextInput() && getOutputFromInput()
            } else true
/*
            if(outputWaitingList.isEmpty()){
                if(!hasNextOutput()){
                    if(hasNextInput()){
                        getOutputFromInput()
                    } else false
                } else true
            } else true
 */
        }
        //Jika ternyata [now] diskip, maka lakukan perhitungan [hasNext] selanjutnya pada fungsi ini.
        prind("hasNext= $hasNext tag= $tag")

        val hasNextRes= if(hasNext){
            val taggedOutput=
                outputWaitingList.firstOrNull() ?: TaggedBoxedVal(tag= outputWaitingList.size)

            nowOutput= taggedOutput.value ?: activeOutputIterator!!.next()

            if(taggedOutput.value == null)
                taggedOutput.value= nowOutput

            outputWaitingList.addIfAbsent(taggedOutput) { it.tag == taggedOutput.tag }

            prind("hasNext() nowOutput= $nowOutput tag= $tag")

            if(skip(nowOutput!!)){
                outputWaitingList.takeFirstOrNull()
                hasNext()
            } else true
        } else false

        return hasNextRes
    }

    final override fun next(): O {
        if(nowOutput != null)
            getNextInputIterator(nowOutput!!)

//        prind("nowOutput!! = ${nowOutput!!} tag= $tag")

        return outputWaitingList.takeFirst().value!!
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
        return if(inItr != null && inItr.hasNext()){
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
        return if(outItr != null && outItr.hasNext()){
            activeOutputLines.add(outItr)
            activeOutputIterator= outItr
            true
        } else false
    }

    protected fun getOutputFromInput(): Boolean{
        while(hasNextInput() && !getNextOutputIterator(activeInputIterator!!.next()));
        return hasNextOutput()
    }

    protected fun hasNextInput(): Boolean{
        if(activeInputIterator != null){
            while(!activeInputIterator!!.hasNext() && activeInputLines.size > 1){
                activeInputLines.remove(activeInputIterator!!)
                activeInputIterator= activeInputLines.last()
            }
        }
        return activeInputIterator?.hasNext() == true
    }

    protected fun hasNextOutput(): Boolean{
        if(activeOutputIterator != null && outputWaitingList.isEmpty()){
            while(!activeOutputIterator!!.hasNext() && activeOutputLines.size > 1){
                activeOutputLines.remove(activeOutputIterator!!)
                activeOutputIterator= activeOutputLines.last()
            }
        }
        return activeOutputIterator?.hasNext() == true || outputWaitingList.isNotEmpty()
    }
/*
    protected fun <T> activeIteratorHasNext(itr: Iterator<T>?): Boolean{
        return itr.asNotNullTo { itrIn: NestedIteratorImpl<*, *> -> itrIn.hasNext(true) }
            ?: itr?.hasNext() == true
    }
 */

    private fun initActiveIterator(): Boolean{
        val itr= startInputIterator?.iterator()
            ?: if(start != null) newIterator(start!!) else null

        return if(itr?.hasNext() == true) {
            activeInputLines.add(itr)
            activeInputIterator= itr

            if(!hasNextOutput()){
                getOutputFromInput()
            } else true
        } else false
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

 */