package sidev.lib.android.siframe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.adapter.decoration.RvSmoothScroller
import sidev.lib.android.siframe.adapter.layoutmanager.LinearLm
import sidev.lib.universal.exception.ResourceNotFoundExc
import sidev.lib.android.siframe.intfc.adp.Adp
import sidev.lib.android.siframe.intfc.adp.MultiViewAdp
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.universal.`fun`.asNotNullTo
import sidev.lib.universal.`fun`.notNull
import java.lang.Exception

//!!!!!!@@ 18 Jan 2020
/**
 * <8 Juli 2020> => Diganti menjadi versi ringan
 */
abstract class SimpleRvAdp <D, LM: RecyclerView.LayoutManager> (
    val ctx: Context //, dataList: ArrayList<D>? <27 Juni 2020> => param konstruktor primer dataList jadi opsional agar menghemat waktu ngoding.
    )
    : RecyclerView.Adapter<SimpleRvAdp<D, LM>.SimpleViewHolder>(), Adp{

    //<27 Juni 2020> => konstruktor dg param dataList jadi konstruktor sekunder agar menghemat waktu ngoding.
    constructor(ctx: Context, dataList: ArrayList<D>?): this(ctx){
        this.dataList= dataList
    }

//    protected var isInternalEdit= false
    protected var isInternalEdit= false
    /**
     * List data yang akan dipake untuk ditampilkan
     */
    open var dataList: ArrayList<D>?= null
        set(v){
            field= v
//                dataListFull= v
            updateData_int(v, isInternalEdit)
/*
            val copiedList=
                if(dataList != null) ArrayList(dataList!!)
                else ArrayList()
            onUpdateDataListener?.onUpdateData(copiedList, -1, DataUpdateKind.SET)
 */
            notifyDataSetChanged_()
        }

    var headerView: View?= null
        set(v){
            field= v
            notifyDataSetChanged_()
        }
    var footerView: View?= null
        set(v){
            field= v
            notifyDataSetChanged_()
        }

    /**
     * Hanya sbg marker bahwa pada posisi tertentu, sebuah view pada adapter ini
     * merupakan [headerView] atau [footerView].
     */
    val headerViewType= 10
    val footerViewType= 11
/*
    /**
     * Marker bahwa pada posisi tertentu, sebuah view pada adapter ini
     * merupakan view normal dg id layout [itemLayoutId].
     */
    @Deprecated("Gunakan itemLayoutId scr langsung untuk menandakan view adalah sebuah konten")
    val contentItemType= 12
 */
/*
    var hasHeader= false
        private set
    var hasFooter= false
        private set
    var headerLayoutId= _Config.INT_EMPTY
        set(v){
            field= v
            hasHeader= ctx.inflate(v) != null
        }
    var footerLayoutId= _Config.INT_EMPTY
        set(v){
            field= v
            hasFooter= ctx.inflate(v) != null
        }
// */
/*
    protected enum class IndexMapping{
        SORT, FILTER
    }
    /**
     * Karena proses mapping dapat dilakukan berkali-kali scr berurutan.
     */
    protected var lastMapping: IndexMapping= IndexMapping.SORT
 */
/*
    /**
     * Penanda jika true maka adpPosMap akan dilakukan penyesuaian.
     */
    protected var isMappingChanged= true
 */
/*
    /**
     * Berisi mapping dari posisi adapter ke indeks sortedIndMap.
     * Hal tersebut dikarenakan layar Sort tidak disesuaikan setelah dilakukan filter di mana filter
     * dapat menyebabkan ukuran data yg ditampilkan lebih sedikit dari ukuran sortedIndMap, sehingga
     * kemungkinan IndexOutBound dapat terjadi.
     */
    protected var adpPosMap= Array(0){-1} //SparseIntArray()
        private set
    /**
     * Berisi mapping dari layar {@link #filteredIndMap} dg {@link #dataList} yg berguna sbg layar pengurut.
     * Layar yg berfungsi untuk mengubah urutan indeks 1 layar di depannya.
     * Ukuran layar ini harus sama dg 1 layar di depannya.
     *
     * <29 Juni 2020> => Berisi mapping indeks dari posisi adapter ke layar Filter.
     */
    protected var sortedIndMap= SparseIntArray()
        private set
    /**
     * Berisi mapping dari yg menentukan mana dari mapping ind yg ada di layar {@link #sortedIndMap} yg akan ditampilkan.
     * Layar yg berfungsi untuk memangkas indeks 1 layar di depannya.
     * Indeks yg ada di dalam layar ini dapat acak dan gak harus urut.
     *
     * <29 Juni 2020> => Berisi mapping indeks dari layar Sort ke dataList.
     */
    protected var filteredIndMap= SparseIntArray()
        private set
// */
/*
    var dataListFull: ArrayList<D>?= null
        protected set(v){
            field= v
            if(isInternalEdit){
                dataList= v
//                updateData_int(containerView)
            }
        }
 * /

    /**
     * Kenapa menggunakan lambda? Karena lebih fleksibel
     * saat mengganti kondisi search, yaitu dengan mengganti dg lambda lainnya pada
     * file constants
     */
    open val searchFilterFun: (data: D, keyword: String) -> Boolean= { _, _ -> true }
    open val selectFilterFun: ((dataFromList: D, dataFromInput: D, posFromList: Int) -> Boolean) ?= null
 */

    var rv: RecyclerView?= null
        set(v){
            field?.adapter= null
            field= v
            setupRv()
        }
    abstract val itemLayoutId: Int
    val itemContainerLayoutId= _Config.LAYOUT_ITEM_ADP_CONTAINER //R.layout._t_item_adp_container
/*
    var isMultiSelectionEnabled= false
    var selectedItemPos_list: ArrayList<Int>?= null
        protected set
    var selectedItemPos_single= -1
        protected set

//    protected var selectedDataList: ArrayList<D>?= null
 */
/*
    /**
     * Akan menjadi krg reliable jika isMultiSelectionEnabled = true.
     * Gunakan getItem(Int) sebagai gantinya
     */
    var selectedItemView: View?= null
        protected set


    var isCheckIndicatorShown= false
        set(v){
            field= v
            showCheckIndicator(v)
        }
    var isOverlayShown= false
        set(v){
            field= v
            showOverlay(v)
        }
 */

    override fun getItemId(pos: Int): Long = super<RecyclerView.Adapter>.getItemId(pos)

    /**
     * Dalam konteks [SimpleRvAdp], implementasi fungsi ini digunakan untuk menentukan
     * pakah sebuah view merupakan header, footer, atau konten biasa.
     *
     * <10 Juli 2020> => Implementasi fungsi ini di-final karena implementasi fungsi ini penting
     *   bagi penentuan struktur scr internal. Apabila programmer ingin mengubah tampilan view
     *   untuk tiap posisinya, maka gunakan fungsi [MultiViewAdp.getItemViewType].
     */
    final override fun getItemViewType(pos: Int): Int { //<10 Juli 2020> => Untuk mengakomodasi header dan footer.
        return when{
            pos == 0 && headerView != null -> headerViewType
            pos == itemCount-1 && footerView != null -> footerViewType
            else -> this.asNotNullTo { adp: MultiViewAdp<D, *> -> adp.getItemViewType(pos, getDataAt(pos)!!) }
                ?: itemLayoutId
        }
    }// = super<RecyclerView.Adapter>.getItemViewType(pos) //getItemLayoutId(pos, itemLayoutId)
/*
    /**
     * Fungsi internal untuk menentukan apakah jika:
     *  -Pada [pos] 0, layout berupa [headerLayoutId]
     *   atau [itemLayoutId] yg di-pass ke fungsi ini.
     *  -Pada [pos] == [getItemCount]-1, layout berupa [footerLayoutId]
     *   atau [itemLayoutId] yg di-pass ke fungsi ini.
     */
    protected fun getItemLayoutId(pos: Int, itemLayoutId: Int): Int{
        return if(pos == 0 && hasHeader) headerLayoutId
        else if(pos == itemCount-1 && hasFooter) footerLayoutId
        else itemLayoutId
    }
 */
    /**
     * Fungsi yg digunakan untuk mengambil indeks dari [dataList]
     * yg dipengaruhi oleh ada tidaknya (true/false) [hasHeader].
     *
     * Jika [hasHeader] == true, maka indeks yg dihasilkan akan
     * bergeser / berkurang 1 dari indeks normal.
     *
     * @return indeks untuk [dataList].
     *   Null jika [SimpleRvAdp] ini punya footer ([hasFooter] == true) dan [adpPos] == [getItemCount] -1
     *   atau [adpPos] di luar indeks yg tersedia.
     */
    fun getDataIndex(adpPos: Int): Int?{
        return if(adpPos in 0 until itemCount){
            if(adpPos < itemCount -1){
                if(headerView == null) adpPos
                else adpPos -1
            } else if(footerView != null) null
            else adpPos
        } else null
    }
    /*
            if(headerView == null) adpPos
            else if(adpPos < itemCount -1 && headerView != null) adpPos -1
//            else if(adpPos == itemCount-1 && footerView != null) null
            else null
     */

    /**
     * @return posisi data yg ditampilkan oleh adp [SimpleRvAdp] ini,
     *   null jika [dataPos] IndexOutOfBound.
     */
    open fun getRawAdpPos(dataPos: Int): Int?{
        return if(dataPos in 0 until itemCount){
            if(dataPos < itemCount -1){
                if(headerView == null) dataPos
                else dataPos +1
            } else if(footerView != null) null
            else dataPos
        } else null
    }

    //    override fun hasStableIds() = super<RecyclerView.Adapter>.hasStableIds()
    /**
     * Fungsi ini digunakan untuk bind view yg bkn merupakan header atau footer.
     */
    abstract fun bindVH(vh: SimpleViewHolder, pos: Int, data: D)
    abstract fun setupLayoutManager(context: Context): LM
    /**
     * Fungsi ini digunakan untuk bind view yg bkn merupakan header atau footer.
     */
    @CallSuper
    open fun __bindVH(vh: SimpleViewHolder, pos: Int, data: D){
        if(onBindViewListener != null){
            for(l in onBindViewListener!!)
                l(vh, pos, data)
        }
    }

    open inner class SimpleViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun isAdpPositionSameWith(bindPos: Int): Boolean{
            return adapterPosition == bindPos
        }
    }

    /**
     * <20 Juli 2020> => Di-final karena implementasi fungsi ini sudah dapat meng-inflate untuk
     *   [viewType] yg berbeda.
     */
    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val v= LayoutInflater.from(ctx).inflate(itemContainerLayoutId, parent, false)
        val contentV= when(viewType){ //<10 Juli 2020> => Untuk mengakomodasi header dan footer.
            headerViewType -> headerView
            footerViewType -> footerView
            else -> ctx.inflate(viewType, parent)
                ?: throw ResourceNotFoundExc(
                    relatedClass = this::class,
                    resourceName = "viewType: '$viewType' dari getItemViewType(Int, D)"
                )
        }!!
//            LayoutInflater.from(ctx).inflate(itemLayoutId, parent, false)
        v.findViewById<LinearLayout>(_Config.ID_VG_CONTENT_CONTAINER)!! //R.id.ll_content_container
            .notNull { vg ->
                setupItemContainer(vg)
                vg.addView(contentV)
            }
        return SimpleViewHolder(v)
    }

    override fun getItemCount(): Int {
        var count= dataList?.size ?: 0
        if(headerView != null) count++
        if(footerView != null) count++
        return count
    }

//    @CallSuper
    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        getDataAt(position).notNull { data ->
            __bindVH(holder, position, data)
            bindVH(holder, position, data)
        } //dataList!![position]
                //<9 Juli 2020> => Pakai fungsi [getDataAt] agar definisi diperolehnya data bisa dioverride.
//        loge("bindVh() position= $position dataInd= $dataInd name= ${this::class.java.simpleName}")
//        selectedItemView= holder.itemView
/*
        holder.itemView.setOnClickListener { v ->
            onItemClickListener?.onClickItem(v, holder.adapterPosition, data)
        }
 */
    }

    inline fun notifyDataSetChanged_(f: (() -> Unit)= {}){
        val lm= rv?.layoutManager as LM?
        if(lm != null){
            val recyclerViewState= rv!!.layoutManager?.onSaveInstanceState()
            f()
            notifyDataSetChanged()
//            Log.e(this::class.java.simpleName, "notifyDatasetChenged!!! name= ${this::class.java.simpleName}")
            rv!!.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }
    }

    /**
     * Fungsi internal untuk menyesuaikan [itemContainer].
     * Fungsi ini dikhususkan jika [RecyclerView.LayoutManager] == [LinearLayoutManager]
     * maka panjang atau lebar [itemContainer] akan diubah menjadi [ViewGroup.LayoutParams.MATCH_PARENT].
     */
    private fun setupItemContainer(itemContainer: View){
        if(rv?.layoutManager is LinearLayoutManager){
            val lm= rv!!.layoutManager!! as LinearLayoutManager
            when(lm.orientation){
                RecyclerView.VERTICAL -> itemContainer.layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT
                RecyclerView.HORIZONTAL -> itemContainer.layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
    }

    protected open fun setupRv(){
        if(rv != null){
            rv!!.adapter= this
            val lm= setupLayoutManager(ctx)
            if(lm is LinearLm)
                lm.smoothScroller= RvSmoothScroller(ctx)
            rv!!.layoutManager= lm
            initLayoutManager(lm)
        }
    }
    protected open fun initLayoutManager(layoutManager: LM){}

    fun updateLayoutManager(func: (lm: LM) -> Unit){
        if(rv != null)
            func(rv!!.layoutManager as LM)
    }

    /**
     * <10 Juli 2020> => Untuk sementara [isSmoothScroll] blum dipake karena
     *   pada fungsi ini hanya dapat dilakukan smoothScroll.
     */
    fun scrollToPosition(pos: Int, isSmoothScroll: Boolean= true): Boolean{
        return rv?.layoutManager.asNotNullTo { lm: LinearLm ->
            lm.smoothScrollTo(pos)
            true
        } ?: false
    }
/*
    /**
     * @param itemPos merupakan index itemView yg ditampilkan di adapter, bkn index dari {@link #dataList}.
     */
    fun getShownIndex(itemPos: Int): Int{
        try{
            return filteredIndMap[sortedIndMap[adpPosMap[itemPos]]]
            //return sortedIndMap[filteredIndMap[itemPos]]
/*
            return when(lastMapping){
                IndexMapping.FILTER -> sortedIndMap[filteredIndMap[itemPos]]
                IndexMapping.SORT -> filteredIndMap[sortedIndMap[itemPos]]
            }
 */
        } catch (e: IndexOutOfBoundsException){
            throw IndexOutOfBoundsException("itemPos ($itemPos) melebihi itemCount ($itemCount)")
        }
    }

    /**
     * Dg anggapan bahwa elemen di dalam
     * @param list tidak boleh null
     *
     * <28 Juni 2020> => Fungsi hanya untuk data yg terlihat karena erat kaitannya dg view.
     */
    open fun selectItem(list: Collection<D>?){
        if(list == null)
            return
        if(dataList != null){
            if(isMultiSelectionEnabled){
                if(selectFilterFun != null){
                    val isListAlreadyFiltered= Array(itemCount){false}
                    for((i, dataFromInput) in list.withIndex())
                        for((posFromList, dataFromList) in dataList!!.withIndex()){
//                            Log.e("SimpleAbsRVA", "i= $i posFromList= $posFromList isListAlreadyFiltered[posFromList]= ${isListAlreadyFiltered[posFromList]}")
                            if(!isListAlreadyFiltered[posFromList])
                                if(selectFilterFun!!(dataFromList, dataFromInput, posFromList)){
                                    selectItem(posFromList)
                                    isListAlreadyFiltered[posFromList]= true
                                    break
                                }
                        }
                } else{
                    for(data in list){
                        val pos= dataList!!.indexOf(data)
                        if(pos >= 0)
                            selectItem(pos)
                    }
                }
            } else {
                selectItem(list.firstOrNull())
            }
        }
    }
    /**
     * <28 Juni 2020> => Fungsi hanya untuk data yg terlihat karena erat kaitannya dg view.
     */
    open fun selectItem(data: D?): Int{
        var pos= -1
        if(selectFilterFun != null && data != null){
            for((i, dataFromList) in dataList!!.withIndex())
                if(selectFilterFun!!(dataFromList, data, i)){
                    pos= i
                    break
                }
        } else
            pos= dataList?.indexOf(data) ?: -1

        selectItem(pos)
        return pos
    }
    /**
     * <28 Juni 2020> => Fungsi hanya untuk data yg terlihat karena erat kaitannya dg view.
     */
    open fun selectItem(pos: Int, v: View?= null){
        if(!isMultiSelectionEnabled){
            val isSelectedBefore= selectedItemPos_single >= 0
            var selectedItemView_before: View?= null
            if(isSelectedBefore){
                onItemSelectedListener?.onUnSelectItem(
                    selectedItemView,
                    selectedItemPos_single,
                    getDataAt(selectedItemPos_single)!!
                )
                selectedItemView_before= selectedItemView
                selectedItemView= null
            }
            if(pos in 0 until (dataList?.size ?: 0)){
                /**
                 * Anggapannya pada umumnya jika user menekan item yang sama,
                 * maka berarti bahwa user tersebut ingin me-unselect item tersebut.
                 */
                if(selectedItemPos_single != pos){
                    selectedItemView= v ?: getView(pos)
                    selectedItemPos_single= pos
//                    Log.e("SIMPLE_RV_ADP", "selectedItemView == null = ${selectedItemView == null}")
                    onItemSelectedListener?.onSelectItem(
                        selectedItemView,
                        pos,
                        getDataAt(pos)!!
                    )
                } else{
                    selectedItemPos_single= -1
                }
            }

            if(isCheckIndicatorShown){
                selectedItemView?.findViewById<ImageView>(_Config.ID_IV_CHECK) //R.id.iv_check
                    ?.visibility= View.VISIBLE
                selectedItemView_before?.findViewById<ImageView>(_Config.ID_IV_CHECK) //R.id.iv_check
                    ?.visibility= View.GONE
            }
            if(isOverlayShown){
                selectedItemView?.findViewById<ImageView>(_Config.ID_IV_OVERLAY) //R.id.iv_overlay
                    ?.visibility= View.VISIBLE
                selectedItemView_before?.findViewById<ImageView>(_Config.ID_IV_OVERLAY) //R.id.iv_overlay
                    ?.visibility= View.GONE
            }
        } else{
            if(selectedItemPos_list == null)
                selectedItemPos_list= ArrayList()

            selectedItemView= v ?: getView(pos)

            val isPosNotExisting= selectedItemPos_list!!.indexOf(pos) < 0
            if(isPosNotExisting){
                selectedItemPos_list!!.add(pos)
                onItemSelectedListener?.onSelectItem(
                    selectedItemView,
                    pos,
                    getDataAt(pos)!!
                )
            } else{
                selectedItemPos_list!!.removeAt(pos)
                onItemSelectedListener?.onUnSelectItem(
                    selectedItemView,
                    pos,
                    getDataAt(pos)!!
                )
            }

            if(isCheckIndicatorShown){
                selectedItemView?.findViewById<ImageView>(_Config.ID_IV_CHECK) //R.id.iv_check
                    ?.visibility=
                        if(isPosNotExisting) View.VISIBLE
                        else View.GONE
            }
            if(isOverlayShown){
                selectedItemView?.findViewById<ImageView>(_Config.ID_IV_OVERLAY)
                    ?.visibility=
                        if(isPosNotExisting) View.VISIBLE
                        else View.GONE
            }
        }
    }
    fun resetItemSelection(){
        if(selectedItemPos_single >= 0){
            onItemSelectedListener?.onUnSelectItem(
                selectedItemView,
                selectedItemPos_single,
                getDataAt(selectedItemPos_single)!!
            )
            selectedItemPos_single= -1
            selectedItemView= null
        }
    }

    protected fun showCheckIndicator(isShown: Boolean= true, pos: Int= selectedItemPos_single){
        getView(pos)?.findViewById<ImageView>(_Config.ID_IV_CHECK)
            ?.visibility=
                if(isShown) View.VISIBLE
                else View.GONE
    }

    protected fun showOverlay(isShown: Boolean= true){
        val vis= if(isShown) View.VISIBLE
            else View.GONE
        if(!isMultiSelectionEnabled){
            selectedItemView?.findViewById<ImageView>(_Config.ID_IV_OVERLAY)
                ?.visibility= vis
        } else{
            for(selectedPos in selectedItemPos_list!!){
                getView(selectedPos)
            }
        }
    }
 */
    /**
     * Mengembalikan data dari dataset scr keseluruhan.
     * Jika pos IndexOutOfBound, maka return pos.
     */
    override fun getItem(pos: Int): Any{
        return try{ dataList!![pos]!! }
        catch (e: Exception){ pos }
    }

    /**
     * Bentuk spesifik untuk mendapat itemView.
     * <28 Juni 2020> => Hanya untuk view yg terlihat, bkn semuanya.
     */
    override fun getView(pos: Int): View?
        = rv?.layoutManager?.findViewByPosition(pos)

    /**
     * Digunakan untuk mengambil data yg akan ditampilkan pada [SimpleRvAdp] ini.
     * Scr default, data yg diambil berasal dari [dataList], namun programmer dapat mendefinisikan
     * sendiri dari mana data pada adapter ini diambil.
     *
     * Param [onlyShownItem] -> `true` jika data yg diambil hanya yg ditampilkan pada adapter,
     *   -> `false` jika data yg diambil merupakan [dataList] scr utuh beserta data yg tidak ditampilkan.
     * Param [isIndexProcessed] -> `true` maka [pos] tidak diproses lagi dan langsung digunakan untuk
     *   mengambil data langsung dari [dataList],
     *   -> `false` jika [pos] perlu diproses lagi agar dapat digunakan untuk mengambil data dari [dataList].
     */
    open fun getDataAt(pos: Int, onlyShownItem: Boolean= true, isIndexProcessed: Boolean= false): D?{
        return if(!isIndexProcessed){
            try{ dataList?.get(getDataIndex(pos)!!) }
            catch (e: KotlinNullPointerException){ null }
        } else dataList?.get(pos)
    }

/*
    fun getSelectedData(): List<D>?{
        if(dataList != null){
            if(selectedDataList != null && selectedItemPos_list!!.size == selectedDataList!!.size)
                return selectedDataList

            val selectedList= ArrayList<D>()
            if(isMultiSelectionEnabled){
                if(selectedItemPos_list != null){
                    for(i in selectedItemPos_list!!)
                        selectedList.add(dataList!![i])
                }
            } else if(selectedItemPos_single >= 0)
                selectedList.add(
                    dataList!![selectedItemPos_single]
                )
            else
                return null
            selectedDataList= selectedList
            return selectedDataList
        }
        return null
    }
 */
    protected open fun updateData_int(dataList: ArrayList<D>?, isInternalEdit: Boolean) {}

    protected inline fun internalEdit(func: () -> Unit){
        val isInternalEdit_init= isInternalEdit
        isInternalEdit= true
        func()
        isInternalEdit= isInternalEdit_init
    }

    open fun deleteItemAt(pos: Int, onlyShownItem: Boolean= true): D?{
        val e= dataList?.removeAt(pos)
        notifyDataSetChanged_()
        return e
    }
    fun clearData(){
        dataList= null
    }

    open fun modifyDataAt(ind: Int, onlyShownItem: Boolean= true, func: (data: D) -> D){
        dataList.notNull { list ->
            val data= list.getOrNull(ind)
            if(data != null){
//                val indInFull= dataList!!.indexOf(data)
                val dataNew= func(data)
                dataList!![ind]= dataNew
//                dataListFull!![indInFull]= dataNew
//                onUpdateDataListener?.onUpdateData(dataList, ind, DataUpdateKind.EDIT)
            }
        }
    }
/*
    fun modifyDataInnerVarAt(ind: Int, func: (data: D) -> Unit){
        dataList.notNull { list ->
            val data= list.getOrNull(ind)
            if(data != null)
                func(data)
        }
    }
 */
/*
    //<28 Juni 2020> => Definisi baru.
    /**
     * Memfilter index yg ada di dalam layar di depannya. //sortedIndMap.
     * Memungkinkan jika hasil filter kosong.
     *
     * @param resetFirst true jika filter dilakukan dari awal, bkn dari hasil filter yg telah dilakukan.
     *                    Ada bbrp skenario:
     *                      1. resetFirst == false && lastMapping == IndexMapping.FILTER
     *                          Filter dapat berjalan normal, filter dimulai dari hasil filter seblumnya
     *                      2. resetFirst == true && lastMapping == IndexMapping.FILTER
     *                          Filter dapat berjalan normal, filter dimulai dari awal.
     *                      3. resetFirst == false && lastMapping == IndexMapping.SORT
     *                          Filter dapat berjalan normal, namun perlu melakukan mapping terhadap sortedIndMap.
     *                          Proses filter tidak jauh beda dg skenario #1 karena pada dasarnya sort hanya
     *                          mengubah urutan.
     *                      4. resetFirst == true && lastMapping == IndexMapping.SORT
     *                          Filter dapat berjalan normal, namun kemungkinan hasil sort sblumnya dihilangkan.
     *
     * <29 Juni 2020> => Fungsi ini tidak terbatas pada sortedIndMap.
     *                   Pada definisi ini, filteredIndMap memiliki value yg urut namun bisa bolong-bolong.
     *
     */
    //<29 Juni 2020> => Definisi baru: 3.
    fun filter(resetFirst: Boolean= false, func: (pos: Int, data: D) -> Boolean){
        if(dataList.isNullOrEmpty()) return

        var u= -1
        if(resetFirst){
            filteredIndMap.clear()
            //Pake sortedIndMap karena sortedIndMap ukurannya sama dg dataList dan dg urutan yg benar.
//            val copySort= sortedIndMap.clone()
            for((i, data) in dataList!!.withIndex()){
                if(func(i, data)){
                    filteredIndMap[++u]= i
//                    loge("filter() u= $u i= $i")
                }
            }
        } else{
            //Menghasilkan filteredIndMap yg lebih kecil atau sama dengan awal.
            for((key, value) in filteredIndMap){
                if(func(key, dataList!![value])){
                    filteredIndMap[++u]= value
//                    loge("filter() u= $u value= $value")
                }
            }
            //Jika filteredIndMap yg dihasilkan lebih kecil dari awal (i akhir dari loop di atas < size()),
            // maka hilangi sisa di ekornya.
            for(i in u+1 until filteredIndMap.size())
                filteredIndMap.removeAt(i)
        }
        adjustMapping()
//        isMappingChanged= true
        notifyDataSetChanged()
    }
/*
    //<29 Juni 2020> => Definisi baru: 4
    fun filter(resetFirst: Boolean= false, func: (pos: Int, data: D) -> Boolean){
        if(dataList.isNullOrEmpty()) return

        loge("filter() MULAI")

        var u= -1
        if(resetFirst){
            filteredIndMap.clear()
            //Pake sortedIndMap karena sortedIndMap ukurannya sama dg dataList dan dg urutan yg benar.
//            val copySort= sortedIndMap.clone()
            for((key, sortVal) in sortedIndMap){
                if(lastMapping == IndexMapping.SORT)
                    sortedIndMap[key]= filteredIndMap[sortVal] //re-mapping sortedIndMap menjadi langsung ke dataList.
                if(func(key, dataList!![sortVal])){
                    filteredIndMap[++u]= key
                    loge("filter() u= $u key= $key")
                }
            }
        } else{
            //re-mapping sortedIndMap menjadi langsung ke dataList.
            if(lastMapping == IndexMapping.SORT)
                for((key, sortVal) in sortedIndMap){
                    val filterVal= filteredIndMap[sortVal]
                    if(filterVal >= 0)
                        sortedIndMap[key]= filterVal
                }

            //Menghasilkan filteredIndMap yg lebih kecil atau sama dengan awal.
            for((key, filterVal) in filteredIndMap){
                if(func(key, dataList!![sortedIndMap[filterVal]])){
                    filteredIndMap[++u]= filterVal
                    loge("filter() u= $u filterVal= $filterVal")
                }
            }
            //Jika filteredIndMap yg dihasilkan lebih kecil dari awal (i akhir dari loop di atas < size()),
            // maka hilangi sisa di ekornya.
            for(i in u+1 until filteredIndMap.size())
                filteredIndMap.removeAt(i)
        }
//        adjustMapping()

        if(lastMapping == IndexMapping.SORT)
            lastMapping= IndexMapping.FILTER
//        isMappingChanged= true
        notifyDataSetChanged()

        loge("filter() SELESAI")
    }
 */
/*
    <29 Juni 2020> => Definisi baru: 2
    fun filter(resetFirst: Boolean= false, func: (pos: Int, data: D) -> Boolean){
//        val newFilter= SparseIntArray()
        if(dataList.isNullOrEmpty()) return

        var i= -1
        if(lastMapping == IndexMapping.FILTER){
            if(resetFirst){
                filteredIndMap.clear()
                for((key, value) in sortedIndMap){
                    if(func(key, dataList!![value]))
                        filteredIndMap[++i]= key //newFilter[++i]= key
                }
            } else{
                //Menghasilkan filteredIndMap yg lebih kecil atau sama dengan awal.
                for((key, value) in filteredIndMap){
                    if(func(key, dataList!![sortedIndMap[value]]))
                        filteredIndMap[++i]= value //newFilter[++i]= key
                }
                //Jika filteredIndMap yg dihasilkan lebih kecil dari awal (i akhir dari loop di atas < size()),
                // maka hilangi sisa di ekornya.
                for(u in i+1 until filteredIndMap.size())
                    filteredIndMap.removeAt(u)
            }
        } else{
            if(!resetFirst){
                //1. Ubah yg sebelumnya mapping dilakukan scr perantara dari sortedIndMap ke filteredIndMap
                //    menjadi langsung ke dataList.
                // Berarti value pada filteredIndMap menuju ke dataList.
                // Proses re-mapping pada sortedIndMap dilakukan scr bersamaan dg proses filter agar
                //   tidak melibatkan loop kedua.
                val newFilter= SparseIntArray() //Untuk tempat menyimpan filter yg baru karena filteredIndMap
                        //tidak dapat dirubah selama loop agar tidak menyebabkan error atau sederhananya hasilnya gak sesuai harapan.
//                var u= -1
                //Knp kok iterasi menggunakan sortedIndMap? Karena tujuannya adalah re-mapping sortedIndMap.
                for(u in 0 until sortedIndMap.size()){
                    val sortVal= sortedIndMap[u]
                    val dataPos= filteredIndMap[sortVal]
                    sortedIndMap[u]= dataPos //value //re-mapping indek sortedIndMap langsung ke dataList.
                    if(func(u, dataList!![dataPos]))
                        newFilter[++i]= u
                }
                filteredIndMap= newFilter
            } else{
                //1. Filter sprti biasa terhadap datalist.
                filteredIndMap.clear()
                for((u, data) in dataList!!.withIndex()){
                    if(func(u, data))
                        filteredIndMap[++i]= u
                }
                //2. <28 Juni 2020> => Sort sblumnya dihilangkan karena msh susah untuk mempertahankannya untuk smtra ini.
                sortedIndMap.clear()
                for(u in 0 until filteredIndMap.size()){
                    sortedIndMap[u]= u
                }
            }
            lastMapping= IndexMapping.FILTER
        }
        notifyDataSetChanged_()
/*
        if(newFilter.isNotEmpty())
            filteredIndMap= newFilter
 */
    }
 */
/*
    <28 Juni 2020> => Diubah menjadi dengan definisi baru.
    fun filter(func: (pos: Int, data: D) -> Boolean){
        dataListFull?.filter { pos, el ->
            func(pos, el)
        }.notNull { list ->
            `access$setDataListSetFromInternal` {
                dataList= list as ArrayList //ArrayList(list)
            }
        }
    }
 */
    /**
     * Menggunakan metode Selection Sorting.
     * @param func return true jika urutan sudah benar,
     *          false jika urutan salah dan perlu dilakukan penukaran tempat.
     *          Penukaran tempat tidak dilakukan langsung ke data, namun ke {@link #sortedIndMap}.
     *        Indeks dari data1 selalu < indeks data2.
     *
     * <28 Juni 2020> => Fungsi ini tidak memiliki param resetFirst karena sort dilakukan hanya
     *                     pada data yg ditampilkan atau dg kata lain yg sebelumnya pernah di filter.
     *
     * <29 Juni 2020> => Fungsi ini hanya digunakan untuk sort dataList scr keseluruhan.
     *                   Fungsi ini tidak terbatas oleh ukuran filteredIndMap.
     *                   Untuk masalah integrasi data, fungsi getShownInd() mengurus masalah itu.
     *                   Pada definisi ini, sortedIndMap selalu berukuran sama dg dataList.
     *
     */
    fun sort(func: (pos1: Int, data1: D, pos2: Int, data2: D) -> Boolean){
        if(dataList.isNullOrEmpty()) return

        //Reset sortedIndMap sehingga menyamai indeks filteredIndMap
        sortedIndMap.clear()
        for(i in 0 until filteredIndMap.size())
            sortedIndMap[i]= i

        for(i in dataList!!.indices)
            for(u in i+1 until dataList!!.size)
                if(!func(i, dataList!![sortedIndMap[i]], u, dataList!![sortedIndMap[u]])){
                    val temp= sortedIndMap[i]
                    sortedIndMap[i]= sortedIndMap[u]
                    sortedIndMap[u]= temp
//                    loge("sort() TUKAR i= $i u= $u \n sortedIndMap[i]= ${sortedIndMap[i]} sortedIndMap[u]= ${sortedIndMap[u]}")
                }
        notifyDataSetChanged()
    }
/*
    fun sort(func: (pos1: Int, data1: D, pos2: Int, data2: D) -> Boolean){
        if(dataList.isNullOrEmpty()) return

        //1. Jika mapping trahir adalah Filter, maka re-mapping dulu indeks pada mapping langsung ke dataList.
        if(lastMapping == IndexMapping.FILTER){
            for(i in 0 until filteredIndMap.size()){
                val filterVal= filteredIndMap[i]
                val dataPos= sortedIndMap[filterVal]
                filteredIndMap[i]= dataPos
            }
            lastMapping= IndexMapping.SORT
        }
        //2. Kemudian lakukan pengurutan.
        //Reset sortedIndMap sehingga menyamai indeks filteredIndMap
        sortedIndMap.clear()
        for(i in 0 until filteredIndMap.size())
            sortedIndMap[i]= i

        for(i in 0 until filteredIndMap.size())
            for(u in i+1 until filteredIndMap.size())
                if(!func(i, dataList!![filteredIndMap[sortedIndMap[i]]],
                        u, dataList!![filteredIndMap[sortedIndMap[u]]])
                ){
                    sortedIndMap[i]= u
                    sortedIndMap[u]= i
                }
/*
        else{
/*
            //Reset sortedIndMap sehingga menyamai indeks dataList
            sortedIndMap.clear()
            for(i in dataList!!.indices)
                sortedIndMap[i]= i

            for(i in dataList!!.indices)
                for(u in i+1 until dataList!!.size)
                    if(!func(i, dataList!![sortedIndMap[i]], u, dataList!![sortedIndMap[u]])){
                        sortedIndMap[i]= u
                        sortedIndMap[u]= i
                    }
 */
        }
 */
        notifyDataSetChanged_()
    }
 */

    /**
     * Untuk menyesuaikan adpPosMap agar fungsi getShownIndex() gak error.
     * Fungsi ini dipanggil hanya oleh filter()
     */
    private fun adjustMapping(){
        var diff= 0
        adpPosMap= Array(itemCount){
            while(filteredIndMap.get(
                    sortedIndMap.get(it +diff, -1),
                    -1
                ) < 0)
                diff++

            it +diff
        }
    }
 */

/*
    /**
     * Fungsi yg memiliki prinsip sama dg filter, yaitu menyaring data yg sudah diurutkan.
     * Data yg disaring dapat berupa data secara utuh maupun data yg sblumnya sudah disaring menggunakan filter().
     *
     * @param onlyShownItem true jika pencarian hanya dilakukan terhadap item yg telah dimapping.
     */
    open fun searchItem(keyword: String, onlyShownItem: Boolean= true){
        if(keyword.isNotEmpty()){
            dataList.notNull { list ->
//                val dataMatch= ArrayList<D>() //<28 Juni 2020> => Definisi lama.
                val newFilter= SparseIntArray() //<28 Juni 2020> => Definisi baru.
                val searchSize= itemCount // => Lebih cocok menggunakan sortedIndMap.size()
                                            // karena berhubungan langsung dg sortedIndMap
                                           // dataList!!.size

                var filterInd= -1
                for(i in 0 until searchSize){
                    val ind= i
                    if(searchFilterFun(dataList!![ind], keyword)) //!!!
                        newFilter[++filterInd]=
                            if(onlyShownItem) filteredIndMap[i]
                            else i // => Lebih tepatnya i, karena i merupakan indeks dari sortedIndMap.
                                  // Jika pakai sortedIndMap[i], maka data tidak terurut.
//                        dataMatch.add(dataList!![ind]) //<28 Juni 2020> => Definisi lama.
                }
                filteredIndMap= newFilter
                notifyDataSetChanged_()
/*
                <28 Juni 2020> => Definisi lama.
                list.forEach { data ->
                    if(searchFilterFun(ctx, data, keyword))
                        dataMatch.add(data)
                }
                if(dataList!!.size != dataMatch.size)
                    internalEdit{
                        dataList= dataMatch
                    }
 */
            }
        } else
            resetDataToInitial()
    }
 */
/*
    fun showOnlySelectedData(isShown: Boolean= true){
        if(!isShown)
            filter { pos, data ->
                selectedItemPos_list?.contains(pos) ?: false
            }
        else
            resetDataToInitial()
    }


    fun resetDataToInitial(){
        //<28 Juni 2020> => Definisi baru.
        sortedIndMap.clear() //= SparseIntArray()
        filteredIndMap.clear() //= SparseIntArray()

        if(dataList != null)
            for(i in dataList!!.indices){
                sortedIndMap[i]= i
                filteredIndMap[i]= i
            }
        adpPosMap= Array(itemCount){ it }
        notifyDataSetChanged_()
/*
        <28 Juni 2020> => Definisi lama.
        internalEdit {
            dataList= dataListFull
        }
 */
    }

    fun resetSortedInd(){
        sortedIndMap.clear() //= SparseIntArray()

        if(dataList != null)
            for(i in dataList!!.indices)
                sortedIndMap[i]= i
        notifyDataSetChanged_()
    }
    fun resetFilteredInd(){
        filteredIndMap.clear() //= SparseIntArray()

        if(dataList != null)
            for(i in dataList!!.indices)
                filteredIndMap[i]= i
        adpPosMap= Array(itemCount){ it }
        notifyDataSetChanged_()
    }
 */


/*
    enum class DataUpdateKind{
        SET, EDIT
    }
    var onUpdateDataListener: OnUpdateDataListener?= null
    abstract inner class OnUpdateDataListener{
        /**
         * @param pos -1 jika update @see dataList scr keseluruhan
         */
        abstract fun onUpdateData(dataArray: List<D>?, pos: Int= -1, kind: DataUpdateKind)
    }
    fun setOnUpdateDataListener(func: (dataArray: List<D>?, pos: Int, kind: DataUpdateKind) -> Unit){
        onUpdateDataListener= object: OnUpdateDataListener(){
            override fun onUpdateData(dataArray: List<D>?, pos: Int, kind: DataUpdateKind) {
                func(dataArray, pos, kind)
            }
        }
    }

    var onItemSelectedFun: ((contentView: View?, pos: Int, data: D) -> Unit)?= null
    var onItemUnSelectedFun: ((contentView: View?, pos: Int, data: D) -> Unit)?= null
    protected open var onItemSelectedListener: OnItemSelectedListener<D>?
        = object : OnItemSelectedListener<D> {
        override fun onSelectItem(v: View?, pos: Int, data: D) {
            showCheckIndicator(isCheckIndicatorShown, pos)
            onItemSelectedFun?.invoke(v, pos, data)
        }

        override fun onUnSelectItem(v: View?, pos: Int, data: D) {
            showCheckIndicator(false, pos)
            onItemUnSelectedFun?.invoke(v, pos, data)
        }
    }
    interface OnItemSelectedListener<D>{
        fun onSelectItem(v: View?, pos: Int, data: D)
        fun onUnSelectItem(v: View?, pos: Int, data: D)
    }

    var onItemClickListener: OnItemClickListener?= null
    abstract inner class OnItemClickListener{
        abstract fun onClickItem(v: View?, pos: Int, data: D)
    }
    fun setOnItemClickListener(l: (v: View?, pos: Int, data: D) -> Unit){
        onItemClickListener= object: OnItemClickListener(){
            override fun onClickItem(v: View?, pos: Int, data: D) {
                l(v, pos, data)
            }
        }
    }

    private val onLayoutCompletedQueue=
        RunQueue<RecyclerView.State?, Unit>()
/*
    private val onLayoutCompletedQueue= ArrayList<
                Pair<(state: RecyclerView.State?) -> Unit_, Boolean>
            >()
 */
    fun addOnLayoutCompletedQueue(runOnce: Boolean= true, f: (state: RecyclerView.State?) -> Unit){
        //onLayoutCompletedQueue.add(Pair(f, runOnce))
        onLayoutCompletedQueue.addRunQueue(runOnce, f)
    }
    private fun iterateOnLayoutCompletedQueue(state: RecyclerView.State?){
        onLayoutCompletedQueue.iterateRunQueue(state)
    }

    private var onLayoutCompletedListener: OnLayoutCompletedListener?= null
    interface OnLayoutCompletedListener{
        fun onLayoutCompletedResp(state: RecyclerView.State?)
    }
    private fun setOnLayoutCompletedListener(l: (state: RecyclerView.State?) -> Unit){
        onLayoutCompletedListener= object: OnLayoutCompletedListener {
            override fun onLayoutCompletedResp(state: RecyclerView.State?) {
                l(state)
            }
        }
        updateLayoutManager { lm ->
//            Log.e("SimpleAbsRVA", "lm is LayoutManagerResp = ${lm is LayoutManagerResp}")
            if(lm is LayoutManagerResp){
                lm.onLayoutCompletedListener= onLayoutCompletedListener
//                Log.e("SimpleAbsRVA", "lm is NOT thrown")
            } else{
                throw TypeExc(this::class.java, "lm is not LayoutManagerResp")
//                Log.e("SimpleAbsRVA", "lm is thrown")
            }
        }
    }

    open fun createEmptyData(): D?{
        return null
    }

    /**
     * <28 Juni 2020> => Data yg dimasukkan tidak akan terlihat scr default.
     *                   Data yg dimasukkan juga tidak terdaftar pada mapping indeks yg ada.
     */
    @CallSuper
    open fun addEmptyData(pos: Int= dataList?.size ?: 0, initValFun: ((initData: D) -> D) ?= null): D?{
        var emptyData= createEmptyData()
        if(initValFun != null && emptyData != null)
            emptyData= initValFun(emptyData)
        return if(emptyData != null){
            addData(emptyData, pos)
//            Log.e("BoundProductSendAdp_Lessee", "_addEmptyData Di dalam")
            emptyData
        } else{
//            Log.e("BoundProductSendAdp_Lessee", "_addEmptyData dataList.size= ${dataList?.size}")
            null
        }
    }
*/

    var onBindViewListener: ArrayList<(holder: SimpleViewHolder, position: Int, data: D) -> Unit>?= null
    fun addOnBindViewListener(l: (holder: SimpleViewHolder, position: Int, data: D) -> Unit){
        if(onBindViewListener == null)
            onBindViewListener= ArrayList()
        onBindViewListener!!.add(l)
    }
    fun removeOnBindViewListener(l: (holder: SimpleViewHolder, position: Int, data: D) -> Unit){
        onBindViewListener?.remove(l)
    }
    fun clearOnBindViewListener(){
        onBindViewListener?.clear()
    }

//    var onViewRecycledListener: ((holder: SimpleViewHolder) -> Unit)?= null
    private var onViewRecycledListener: ArrayList<(holder: SimpleViewHolder) -> Unit>?= null
    @CallSuper
    override fun onViewRecycled(holder: SimpleViewHolder) {
        if(onViewRecycledListener != null){
            for(l in onViewRecycledListener!!)
                l(holder)
        }
    }
    fun addOnViewRecycledListener(l: (holder: SimpleViewHolder) -> Unit){
        if(onViewRecycledListener == null)
            onViewRecycledListener= ArrayList()
        onViewRecycledListener!!.add(l)
    }
    fun removeOnViewRecycledListener(l: (holder: SimpleViewHolder) -> Unit){
        onViewRecycledListener?.remove(l)
    }
    fun clearOnViewRecycledListener(){
        onViewRecycledListener?.clear()
    }

    /**
     * <28 Juni 2020> => Data yg dimasukkan tidak akan terlihat scr default.
     *                   Data yg dimasukkan juga tidak terdaftar pada mapping indeks yg ada.
     */
    fun addData(data: D, pos: Int= dataList?.size ?: 0){
        val dataListInt= if(dataList != null) dataList!!
            else ArrayList()

        dataListInt.add(pos, data)
        internalEdit { dataList= dataListInt }
    }

    @PublishedApi
    internal fun `access$setDataListSetFromInternal`(func: () -> Unit) =
        internalEdit(func)
}
