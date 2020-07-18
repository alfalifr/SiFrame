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
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.arch.value.BoxedVal
import sidev.lib.android.siframe.exception.ResourceNotFoundExc
import sidev.lib.android.siframe.intfc.`fun`.InitPropFun
import sidev.lib.android.siframe.tool.util.`fun`.findView
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.android.siframe.tool.util.`fun`.iterator
import sidev.lib.android.siframe.tool.util.isIdIn
import sidev.lib.universal.`fun`.isNull
//import sidev.lib.universal.`fun`.iterator
import sidev.lib.universal.`fun`.notNull

/**
 * Komponen yg digunakan sbg wadah paket view yg memiliki operasi uniknya masing-masing.
 * Komponen ini biasanya digunakan untuk menyimpan data [mData] yg dapat berubah scr dinamis
 * yg didapat dari view dg jumlah banyak, seperti data yg diambil dari input user pada RecyclerView.
 * [mData] dapat berupa object selain data yg juga memiliki jml yg banyak, sprti nested adapter.
 *
 * Tujuan utama dari kelas [ViewComp] ini adalah untuk memanajemen data/object yg banyak
 * yg dapat berubah scr dinamis yg biasanya terdapat pada adapter.
 *
 * @param D adalah tipe data yg akan dimanage oleh kelas [ViewComp] ini.
 * @param I adalah tipe data inputan dari [RecyclerView.Adapter].
 */
abstract class ViewComp<D, I>(val ctx: Context): InitPropFun {
    final override var isInit: Boolean= false

    abstract val viewLayoutId: Int
    private val mData= SparseArray<BoxedVal<D>>()
    private var mView: SparseArray<View>?= null
    open val isDataRecycled= false
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
    private var isCompIdValid= true
    var isCompVisible= true
        set(v){
            field= v
            val vis= if(v) View.VISIBLE
                else View.GONE
            for(view in viewIterator)
                view.visibility= vis
        }

    /**
     * Jika tidak null, maka fungsi [setComponentEnabled] akan dipanggil setiap kali
     * fungsi [onBind] dipanggil.
     */
    var isEnabled: Boolean?= null
        set(v){
            field= v
            if(v != null)
                for((i, view) in viewIterator.withIndex()){
                    val compView= if(isCompIdValid) view.findViewById<View>(compId) else view
                    setComponentEnabled(i, compView, v)
                }
        }

    val savedDataCount: Int
        get()= mData.size()
    val savedViewCount: Int
        get()= mView?.size() ?: 0

    private var rvAdp: SimpleRvAdp<*, *>?= null
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
            = object: Iterator<View>{
        private var innerIterator= mView?.iterator()
        override fun hasNext(): Boolean= innerIterator?.hasNext() == true
        override fun next(): View = innerIterator!!.next().second
    }

    /**
     * @param skipNulls true jika data pada [mData] tidak akan di
     */
    fun dataIterator(skipNulls: Boolean= true): Iterator<D?>
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

    fun getDataAt(pos: Int): D?= mData[pos]?.value
    fun getViewAt(pos: Int): View?= mView?.get(pos)

    /**
     * Fungsi ini dapat dipakai untuk memasang maupun mencopot [rvAdp].
     */
    fun setupWithRvAdapter(rvAdp: SimpleRvAdp<*, *>?){
        initProp { isCompIdValid= compId isIdIn ctx }
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
     */
    fun onBind(position: Int, v: View, inputData: I?){
        var valueBox= mData[position]

        if(valueBox == null){
            valueBox= BoxedVal()
            valueBox.value= initData(position, inputData)
            mData[position]= valueBox
        }

        if(isViewSaved){
            if(mView == null) mView= SparseArray()
            mView!![position]= v
        }

        /** Diletakan sebelum [bindComponent]  agar programmer dapat menyesuaikan lagi visibilitas komponen. */
        if(isCompIdValid)
            v.findViewById<View>(compId).notNull {
                it.visibility= if(isCompVisible) View.VISIBLE else View.GONE
            }.isNull { isCompIdValid= false }

        val compView= if(isCompIdValid) v.findViewById<View>(compId) else v
        if(isEnabled != null)
            setComponentEnabled(position, compView, isEnabled!!)
        bindComponent(position, compView, valueBox, inputData)
    }

    /**
     * Fungsi yg dipanggil saat sebuah [ViewComp] dihancurkan.
     */
    fun onRecycle(position: Int, v: View){
        if(isDataRecycled){
            mData[position].notNull {
                onDataRecycled(position, it)
                mData.remove(position, it)
            }
        }
        mView?.remove(position, v)
    }

    /**
     * Digunakan untuk mendapat view dari layout [viewLayoutId] untuk posisi [position].
     * Fungsi ini juga akan melakukan pemanggilan fungsi [onBind].
     */
    fun inflateView(position: Int, vg: ViewGroup?= null, attachToRoot: Boolean= false): View{
        val v= ctx.inflate(viewLayoutId, vg, attachToRoot)
            ?: throw ResourceNotFoundExc(resourceName = "viewLayoutId", msg = "Tidak dapat menginflate view")

        onBind(position, v, null)

        return v
    }

    /**
     * Fungsi yg dipanggil saat [onBind] pada posisi [position] dan kebetulan
     * pada posisi [position] tidak ada data di dalam [mData]. Jika ternyata bind dilakukan
     * saat sudah terdapat data pada [mData] pada posisi [position], maka fungsi ini
     * tidak dipanggil.
     */
    abstract fun initData(position: Int, inputData: I?): D? //, valueBox: BoxedVal<D?>)

    /**
     * Fungsi yg dipanggil saat [onRecycle] pada posisi [position] dipanggil.
     */
    open fun onDataRecycled(position: Int, valueBox: BoxedVal<D>){}

    /**
     * Digunakan untuk mengatur tampilan saat view akan ditampilkan pada adapter.
     * Fungsi ini juga dipanggil sbg peng-init view setelah fungsi [inflateView] dipanggil.
     *
     * @param valueBox adalah wadah untuk menyimpan data yg diambil dari
     *   input user pada view.
     * @param v adalah view hasil inflate dari [viewLayoutId].
     */
    abstract fun bindComponent(position: Int, v: View, valueBox: BoxedVal<D>, inputData: I?)

    /**
     * Fungsi yg digunakan untuk me-enabled atau tidak komponen view yg dikelola oleh kelas [ViewComp] ini.
     * Fungsi ini akan dipanggil setiap kali [onBind] dipanggil jika [isEnabled] != null.
     *
     * Komponen view didapat dari parameter [v] atau jika null, maka didapat dari fungsi [getViewAt].
     */
    open fun setComponentEnabled(position: Int, v: View?= null, enable: Boolean= true){}
/*
    fun iterateSavedView(iterator: (View) -> Unit){
        if(mView != null)
            for((i, view) in mView!!)
                iterator(view)
    }
 */
}