package sidev.lib.android.siframe.view.comp

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.util.remove
import androidx.core.util.set
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.universal.structure.data.BoxedVal
import sidev.lib.universal.exception.ResourceNotFoundExc
import sidev.lib.android.siframe.tool.util.`fun`.findView
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.android.siframe.tool.util.`fun`.iterator
import sidev.lib.universal.`fun`.*
//import sidev.lib.universal.`fun`.iterator
import sidev.lib.universal.structure.collection.iterator.SkippableIteratorImpl

/**
 * Komponen yg digunakan sbg wadah paket view yg memiliki operasi uniknya masing-masing.
 * Komponen ini biasanya digunakan untuk menyimpan data [mData] yg dapat berubah scr dinamis
 * yg didapat dari view dg jumlah banyak, seperti data yg diambil dari input user pada RecyclerView.
 * [mData] dapat berupa object selain data yg juga memiliki jml yg banyak, sprti nested adapter.
 *
 * Tujuan utama dari kelas [ViewComp] ini adalah untuk memanajemen data/object yg banyak
 * yg dapat berubah scr dinamis yg biasanya terdapat pada adapter.
 *
 * Kelas ini menyimpan 2 komponen, yaitu data dg tipe [D] yg disimpan dalam [mData]
 * dan view yg dibind yg disimpan di [mView]. Isi dari [mData] dan [mView] sama" dapat didaur ulang
 * atau dihapus saat [onRecycle] dipanggil. Namun, untuk daur ulang [mData] adalah opsional
 * sedangkan untuk [mView] adalah wajib karena tujuan dari kelas ini hanya sebatas bind data,
 * bkn penyimpanan view. Sementara penyimpanan [mData] adalah wajib sedangkan untuk [mView] opsional.
 *
 * @param D adalah tipe data yg akan dimanage oleh kelas [ViewComp] ini.
 * @param I adalah tipe data inputan dari [RecyclerView.Adapter].
 */
abstract class ViewComp<D, I>(val ctx: Context) {
//    final override var isInit: Boolean= false

    abstract val viewLayoutId: Int
    private val mData= SparseArray<BoxedVal<D>>()
    private val mAdditionalData: SparseArray<Any> by lazy{ SparseArray<Any>() }
    /** Yg disimpan adalah view dg id [compId], atau view yg didapatkan dari [onBind] scr penuh jika [compId] tidak ditemukan. */
    private var mView: SparseArray<View>?= null
    open val isDataRecycled= false
    open val isAdditionalDataRecycled
        get()= isDataRecycled
    open val isViewSaved= false

    /**
     * <13 Juli 2020>
     *   Menunjukan scr spesifik komponen dalam view [viewLayoutId] yg data bindingnya
     *   diurus oleh [ViewComp] ini. Hal ini bertujuan pada saat bbrp ViewComp memiliki
     *   [viewLayoutId] yg sama, namun memiliki tanggung jawab data binding terhadap
     *   komponen yg berbeda-beda.
     *
     *   [compId] disarankan sebagai val karena perubahan id komponen scr dinamis
     *   adalah kasus yg sangat langkah.
     */
    @IdRes
    open val compId: Int= _Config.INT_EMPTY
//    private var isCompIdValid= true
    var isCompVisible= true
        set(v){
            field= v
            val vis= if(v) View.VISIBLE
                else View.GONE
            if(isViewSaved){
                for(view in viewIterator)
                    view.visibility= vis
            } else{
                rvAdp?.notifyDataSetChanged_() //Agar view yg gak disave dibind.
            }
        }

    /**
     * Jika tidak null, maka fungsi [setComponentEnabled] akan dipanggil setiap kali
     * fungsi [onBind] dipanggil.
     */
    var isEnabled: Boolean?= null
        set(v){
            field= v
            if(v != null){
                if(isViewSaved){
                    for((i, view) in viewIterator.withIndex())
                        setComponentEnabled(i, view, v)
                } else{
                    rvAdp?.notifyDataSetChanged_() //Agar view yg gak disave dibind.
                }
            }
        }

    val savedDataCount: Int
        get()= mData.size()
    val savedAdditionalDataCount: Int
        get()= mAdditionalData.size()
    val savedViewCount: Int
        get()= mView?.size() ?: 0

    protected var rvAdp: SimpleRvAdp<*, *>?= null
        private set //set lewat [setupWithRvAdapter].
    private var onBindViewListener: ((SimpleRvAdp<*, *>.SimpleViewHolder, Int, Any?) -> Unit)?= null
    private var onViewRecycledListener: ((SimpleRvAdp<*, *>.SimpleViewHolder) -> Unit)?= null
    /**
     * Lambda ini digunakan untuk mengkoversi data dari [rvAdp] menjadi inputData dg tipe [I].
     */
    protected open val rvAdpInputDataConverter: ((Any?) -> I?)?= {
        try{ it as I }
        catch (e: ClassCastException){ null }
    }

    /** Iterator view yg disimpan di dalam [ViewComp] ini. */
    val viewIterator: Iterator<View>
        get()= mView?.iterator()?.toOtherIterator { it.second } ?: newIteratorSimple()
/*
            get()= object: Iterator<View>{
        private var innerIterator= mView?.iterator()
        override fun hasNext(): Boolean= innerIterator?.hasNext() == true
        override fun next(): View = innerIterator!!.next().second
    }
 */

    /** @param [skipNulls] true jika iterator yg dihasilkan tidak menampilkan data null pada [mData]. */
    fun dataIterator(skipNulls: Boolean= true): Iterator<D?>
        = object: SkippableIteratorImpl<D?>(mData.iterator().toOtherIterator { it.second.value }){
        override fun skip(now: D?): Boolean = now == null && skipNulls
    }
    /** @param [skipNulls] true jika iterator yg dihasilkan tidak menampilkan data null pada [mData]. */
    fun additionalDataIterator(skipNulls: Boolean= true): Iterator<Any?>
        = object: SkippableIteratorImpl<Any?>(mAdditionalData.iterator().toOtherIterator { it.second }){
        override fun skip(now: Any?): Boolean = now == null && skipNulls
    }
/*
        = object: Iterator<D?>{
            private var innerIterator= mData.iterator()
            private var next: D?= null
            override fun hasNext(): Boolean{
                return if(innerIterator.hasNext()){
                    next= innerIterator.next().second.value
                    //Mencari nilai next yg tidak null
                    while(skipNulls && next == null && innerIterator.hasNext()){
                        next= innerIterator.next().second.value
                    }
                    //Jika sampai iterasi trahir dan data yg dihasilkan msh null,
                    // maka fungsi [hasNext] pada object ini akan menghasilkan false.
                    next != null || !skipNulls
                } else false
            }
            override fun next(): D? = next
        }
 */

    fun getDataAt(dataPos: Int): D?= mData[dataPos]?.value
    fun getAdditionalDataAt(dataPos: Int): Any?= mAdditionalData[dataPos]
    fun setAdditionalDataAt(dataPos: Int, additionalData: Any?){
        if(additionalData != null) mAdditionalData[dataPos]= additionalData
        else mAdditionalData.removeAt(dataPos)
    }
    fun getViewAt(dataPos: Int): View?= mView?.get(dataPos)
    fun getInputDataAt(adpPos: Int): I?= try{ rvAdp?.getDataAt(adpPos) as? I } catch (e: ClassCastException){ null }
    /** Mengambil posisi sebenarnya dari data kesuluruhan yg terdapat pada [rvAdp]. */
    fun getDataPosition(adpPos: Int): Int
        = rvAdp.asNotNullTo { adp: RvAdp<*, *> ->  adp.getDataShownIndex(adpPos) }
        ?: rvAdp?.getDataIndex(adpPos)
        ?: adpPos

    /**
     * Fungsi ini dapat dipakai untuk memasang maupun mencopot [rvAdp].
     */
    fun setupWithRvAdapter(rvAdp: SimpleRvAdp<*, *>?){
//        initProp { isCompIdValid= compId isIdIn ctx }
        if(rvAdp != null){
            onBindViewListener= { holder, pos, dataInput ->
                val dataInputRes= rvAdpInputDataConverter?.invoke(dataInput)
                onBind(pos, holder.itemView, dataInputRes)
            }
            onViewRecycledListener= { holder ->
                onRecycle(holder.adapterPosition, holder.itemView)
            }
            rvAdp.addOnBindViewListener(onBindViewListener!!)
            rvAdp.addOnViewRecycledListener(onViewRecycledListener!!)
        } else{
            this.rvAdp?.removeOnBindViewListener(onBindViewListener!!)
            this.rvAdp?.removeOnViewRecycledListener(onViewRecycledListener!!)
            onBindViewListener= null
            onViewRecycledListener= null
        }
        this.rvAdp= rvAdp
    }

    /**
     * Fungsi yg dipanggil saat sebuah [ViewComp] ditampilkan ke layar.
     * Param [adpPos] adalah posisi yg ditampilkan pada [rvAdp], bkn posisi data keseluruhan yg sebenarnya.
     */
    fun onBind(adpPos: Int, v: View, inputData: I?){
        val dataPos= getDataPosition(adpPos)
        var valueBox= mData[dataPos]

        if(valueBox == null){
            valueBox= BoxedVal()
            valueBox.value= initData(dataPos, inputData)
            mData[dataPos]= valueBox
        }
        val additionalData= mAdditionalData[dataPos]

        /** Diletakan sebelum [bindComponent]  agar programmer dapat menyesuaikan lagi visibilitas komponen. */
//        if(isCompIdValid)
        val compView= v.findView<View>(compId).notNull {
            it.visibility= if(isCompVisible) View.VISIBLE else View.GONE
        } ?: v //.isNull { isCompIdValid= false }

        if(isViewSaved){
            if(mView == null) mView= SparseArray()
            mView!![dataPos]= compView //v.findView(compId) ?: v
        }

        if(isEnabled != null)
            setComponentEnabled(adpPos, compView, isEnabled!!)
        bindComponent(adpPos, compView, valueBox, additionalData, inputData)
    }

    /**
     * Fungsi yg dipanggil saat sebuah [ViewComp] dihancurkan.
     */
    fun onRecycle(adpPos: Int, v: View){
        val dataPos= getDataPosition(adpPos)
        val additionalData= mAdditionalData[dataPos]
        if(isDataRecycled){
            mData[dataPos].notNull {
                onDataRecycled(dataPos, it, additionalData)
                mData.remove(dataPos, it)
            }
        }
        if(isAdditionalDataRecycled)
            mAdditionalData.remove(dataPos, additionalData)
        mView?.remove(dataPos, v)
    }

    /**
     * Digunakan untuk mendapat view dari layout [viewLayoutId] untuk posisi [adpPos].
     * Fungsi ini juga akan melakukan pemanggilan fungsi [onBind].
     * Param [adpPos] adalah posisi yg ditampilkan pada adapter, dalam konteks ini [rvAdp].
     */
    fun inflateView(adpPos: Int, vg: ViewGroup?= null, attachToRoot: Boolean= false): View{
        val v= ctx.inflate(viewLayoutId, vg, attachToRoot)
            ?: throw ResourceNotFoundExc(resourceName = "viewLayoutId", msg = "Tidak dapat menginflate view")

        onBind(adpPos, v, null)

        return v
    }

    /**
     * Fungsi yg dipanggil saat [onBind] pada posisi [dataPos] dan kebetulan
     * pada posisi [dataPos] tidak ada data di dalam [mData]. Jika ternyata bind dilakukan
     * saat sudah terdapat data pada [mData] pada posisi [dataPos], maka fungsi ini
     * tidak dipanggil.
     *
     * Param [dataPos] adalah posisi data yg sebenarnya yg terdapat pada [rvAdp].
     */
    abstract fun initData(dataPos: Int, inputData: I?): D? //, valueBox: BoxedVal<D?>)

    /**
     * Fungsi yg dipanggil saat [onRecycle] pada posisi [dataPos] dipanggil.
     * Param [dataPos] adalah posisi data yg sebenarnya yg terdapat pada [rvAdp].
     * Param [additionalData] adalah data tambahan yg ditambahkan scr manual yg tidak terkait dg [rvAdp].
     */
    open fun onDataRecycled(dataPos: Int, valueBox: BoxedVal<D>, additionalData: Any?){}

    /**
     * Digunakan untuk mengatur tampilan saat view akan ditampilkan pada adapter.
     * Fungsi ini juga dipanggil sbg peng-init view setelah fungsi [inflateView] dipanggil.
     *
     * @param valueBox adalah wadah untuk menyimpan data yg diambil dari
     *   input user pada view.
     * @param v adalah view hasil inflate dari [viewLayoutId].
     * Param [additionalData] adalah data tambahan yg ditambahkan scr manual yg tidak terkait dg [rvAdp].
     */
    abstract fun bindComponent(adpPos: Int, v: View, valueBox: BoxedVal<D>, additionalData: Any?, inputData: I?)

    /**
     * Fungsi yg digunakan untuk me-enabled atau tidak komponen view yg dikelola oleh kelas [ViewComp] ini.
     * Fungsi ini akan dipanggil setiap kali [onBind] dipanggil jika [isEnabled] != null.
     *
     * Komponen view didapat dari parameter [v] atau jika null, maka didapat dari fungsi [getViewAt].
     */
    open fun setComponentEnabled(adpPos: Int, v: View?= null, enable: Boolean= true){}
/*
    fun iterateSavedView(iterator: (View) -> Unit){
        if(mView != null)
            for((i, view) in mView!!)
                iterator(view)
    }
 */
}