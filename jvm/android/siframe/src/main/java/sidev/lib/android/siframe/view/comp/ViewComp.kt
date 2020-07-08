package sidev.lib.android.siframe.view.comp

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.remove
import androidx.core.util.set
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.arch.value.BoxedVal
import sidev.lib.android.siframe.exception.ResourceNotFoundExc
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.universal.`fun`.iterator
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
 * @param T adalah tipe data yg akan dimanage oleh kelas [ViewComp] ini.
 */
abstract class ViewComp<T>(val ctx: Context) {
    abstract val viewLayoutId: Int
    private val mData= SparseArray<BoxedVal<T>>()
    private var mView: SparseArray<View>?= null
    open val isDataRecycled= false
    open val isViewSaved= false

    private var rvAdp: SimpleRvAdp<*, *>?= null
    private var onBindViewListener: ((SimpleRvAdp<*, *>.SimpleViewHolder, Int) -> Unit)?= null
    private var onViewRecycledListener: ((SimpleRvAdp<*, *>.SimpleViewHolder) -> Unit)?= null

//    open val isViewDefaultEnabled= true


    fun getDataAt(pos: Int): T?= mData[pos].value
    fun getViewAt(pos: Int): View?= mView?.get(pos)

    /**
     * Fungsi ini dapat dipakai untuk memasang maupun mencopot [rvAdp].
     */
    fun setupWithRvAdapter(rvAdp: SimpleRvAdp<*, *>?){
        if(rvAdp != null){
            onBindViewListener= { holder, pos ->
                onBind(pos, holder.itemView)
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
    fun onBind(position: Int, v: View){
        var valueBox= mData[position]

        if(valueBox == null){
            valueBox= BoxedVal()
            valueBox.value= initData(position)
            mData[position]= valueBox
        }

        if(isViewSaved){
            if(mView == null) mView= SparseArray()
            mView!![position]= v
        }

        bindComponent(position, v, valueBox)
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

        onBind(position, v)

        return v
    }

    /**
     * Fungsi yg dipanggil saat [onBind] pada posisi [position] dan kebetulan
     * pada posisi [position] tidak ada data di dalam [mData]. Jika ternyata bind dilakukan
     * saat sudah terdapat data pada [mData] pada posisi [position], maka fungsi ini
     * tidak dipanggil.
     */
    abstract fun initData(position: Int): T? //, valueBox: BoxedVal<T?>)

    /**
     * Fungsi yg dipanggil saat [onRecycle] pada posisi [position] dipanggil.
     */
    open fun onDataRecycled(position: Int, valueBox: BoxedVal<T>){}

    /**
     * Digunakan untuk mengatur tampilan saat view akan ditampilkan pada adapter.
     * Fungsi ini juga dipanggil sbg peng-init view setelah fungsi [inflateView] dipanggil.
     *
     * @param valueBox adalah wadah untuk menyimpan data yg diambil dari
     *   input user pada view.
     */
    abstract fun bindComponent(position: Int, v: View, valueBox: BoxedVal<T>)

    /**
     * @return true jika komponen tidak null. Komponen didapat dari parameter [v]
     *   atau jika null, maka didapat dari fungsi [getViewAt].
     */
    open fun setComponentEnabled(position: Int, v: View?= null, enable: Boolean= true): Boolean= false

    fun iterateSavedView(iterator: (View) -> Unit){
        if(mView != null)
            for((i, view) in mView!!)
                iterator(view)
    }
}