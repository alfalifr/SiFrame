package sidev.lib.android.siframe.adapter

import android.content.Context
import android.util.SparseIntArray
import android.view.View
import android.widget.ImageView
import androidx.annotation.CallSuper
import androidx.core.util.set
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.adapter.layoutmanager.LayoutManagerResp
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.universal.exception.TypeExc
import sidev.lib.android.siframe.intfc.listener.Listener
import sidev.lib.android.siframe.tool.RunQueue
import sidev.lib.android.siframe.tool.RvAdpContentArranger
import sidev.lib.android.siframe.tool.util.`fun`.addLast
import sidev.lib.android.siframe.tool.util.`fun`.iterator
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.notNull
import java.lang.Exception
import java.lang.IndexOutOfBoundsException

//<8 Juli 2020> => Definisi lama.
//!!!!!!@@ 18 Jan 2020
abstract class RvAdp <D, LM: RecyclerView.LayoutManager> (ctx: Context)
    : SimpleRvAdp<D, LM>(ctx) { //RecyclerView.Adapter<RvAdp<D, LM>.SimpleViewHolder>(), Adp{

    //<27 Juni 2020> => konstruktor dg param dataList jadi konstruktor sekunder agar menghemat waktu ngoding.
    constructor(ctx: Context, dataList: ArrayList<D>?): this(ctx){
        this.dataList= dataList
    }

//    protected var isInternalEdit= false
//    protected var isInternalEdit= false
    /**
     * List data yang akan dipake untuk ditampilkan
     *
     * <9 Juli 2020> => Properti tidak jadi final agar dapat dioverride sesuai kebutuhan.
     */
    override var dataList: ArrayList<D>?= null
        set(v){
            field= v
            if(!isInternalEdit)
                resetDataToInitial()
//                dataListFull= v
            updateData_int(v, isInternalEdit)

            val copiedList=
                if(dataList != null) ArrayList(dataList!!)
                else ArrayList()
            onUpdateDataListener?.onUpdateData(copiedList, -1, DataUpdateKind.SET)
//            notifyDataSetChanged_()
        }
    /**
     * Berguna saat [dataList] tidak dipakai namun, programmer tetap ingin menunjukan
     * bahwa rvAdp ini memiliki data yg lain.
     *
     * Properti ini dipakai oleh [RvAdpContentArranger] sbg definisi jml data scr utuh
     * saat dilakukan [RvAdpContentArranger.reset].
     */
    open val dataListCount: Int
        get()= dataList?.size ?: 0

    private var contentArranger= RvAdpContentArranger<D>()
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
 */

/*
    var dataListFull: ArrayList<D>?= null
        protected set(v){
            field= v
            if(isInternalEdit){
                dataList= v
//                updateData_int(containerView)
            }
        }
 */
    /**
     * Kenapa menggunakan lambda? Karena lebih fleksibel
     * saat mengganti kondisi search, yaitu dengan mengganti dg lambda lainnya pada
     * file constants
     */
    open val searchFilterFun: (data: D, keyword: String) -> Boolean= { _, _ -> true }
    open val selectFilterFun: ((dataFromList: D, dataFromInput: D, posFromList: Int) -> Boolean) ?= null
    init{
        contentArranger.rvAdp= this
        contentArranger.reset()
        setOnLayoutCompletedListener { state ->
            iterateOnLayoutCompletedQueue(state)
        }
    }
/*
    var rv: RecyclerView?= null
        set(v){
            field?.adapter= null
            field= v
            setupRv()
        }
 */
    var isMultiSelectionEnabled= false
    /**
     * Indeks [dataList] scr langsung tanpa pemrosesan.
     */
    var selectedItemPos_list: ArrayList<Int>?= null
        protected set
    /**
     * Indeks [dataList] scr langsung tanpa pemrosesan.
     */
    var selectedItemPos_single= -1
        protected set

    protected var selectedDataList: ArrayList<D>?= null

    /**
     * Akan menjadi krg reliable jika isMultiSelectionEnabled = true.
     * Gunakan getItem(Int) sebagai gantinya
     */
    var selectedItemView: View?= null
        protected set

//    abstract val itemLayoutId: Int
//    val itemContainerLayoutId= _Config.LAYOUT_ITEM_ADP_CONTAINER //R.layout._t_item_adp_container

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

//    override fun getItemId(position: Int): Long = super<RecyclerView.Adapter>.getItemId(position)
//    override fun getItemViewType(position: Int): Int = super<RecyclerView.Adapter>.getItemViewType(position)
    //    override fun hasStableIds() = super<RecyclerView.Adapter>.hasStableIds()


//    abstract fun bindVH(vh: SimpleViewHolder, pos: Int, data: D)
//    abstract fun setupLayoutManager(): LM
    override fun __bindVH(vh: SimpleViewHolder, pos: Int, data: D){
        super.__bindVH(vh, pos, data)
        val v= vh.itemView

        val proceedVis= if(!isMultiSelectionEnabled) pos == selectedItemPos_single
            else (selectedItemPos_list?.indexOf(pos) ?: -1) >= 0

        if(proceedVis){
            v.findViewById<ImageView>(_Config.ID_IV_CHECK) //R.id.iv_check
                ?.visibility=
                    if(isCheckIndicatorShown) View.VISIBLE
                    else View.GONE
            v.findViewById<ImageView>(_Config.ID_IV_OVERLAY) //R.id.iv_overlay
                ?.visibility=
                    if(isOverlayShown) View.VISIBLE
                    else View.GONE
        }
    }
/*
    open inner class SimpleViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun isAdpPositionSameWith(bindPos: Int): Boolean{
            return adapterPosition == bindPos
        }
    }
 */
/*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val v= LayoutInflater.from(ctx).inflate(itemContainerLayoutId, parent, false)
        val contentV= LayoutInflater.from(ctx).inflate(itemLayoutId, parent, false)
        v.findViewById<LinearLayout>(_Config.ID_VG_CONTENT_CONTAINER) //R.id.ll_content_container
            .addView(contentV)
        return SimpleViewHolder(v)
    }
 */
    override fun getItemCount(): Int{
        var count= contentArranger.resultInd.size()
        if(headerView != null) count++
        if(footerView != null) count++
        return count
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val dataInd= getDataShownIndex(position)
        getDataAt(position).notNull { data ->
            loge("data != null position= $position dataInd= $dataInd headerView == null= ${headerView == null}")
            //<10 Juli 2020> => Dibuat jadi .notNull() agar saat bind header atau footer tidak dilakukan
            //  di dalam RvAdp ini.
            holder.itemView.findViewById<ImageView>(_Config.ID_IV_CHECK) //R.id.iv_check
                ?.visibility= if(isCheckIndicatorShown && dataInd == selectedItemPos_single) View.VISIBLE
            else View.GONE

            if(!isItemClickEnabled)
                holder.itemView.isClickable= false

            __bindVH(holder, position, data) //dataInd
            bindVH(holder, position, data)

            if(isItemClickEnabled)
                holder.itemView.setOnClickListener { v ->
                    selectItem(position, onlyShownItem = true) //jika true, maka [dataInd] akan diproses lagi, yg mungkin dapat menyebabkan error.
                    onItemClickListener?.onClickItem(v, holder.adapterPosition, data)
                }
        } //dataList!![dataInd]
                //<9 Juli 2020> => Pakai fungsi [getDataAt] agar definisi diperolehnya data bisa dioverride.
                // Knp kok pake [position] bkn [dataInd]? Karena di fungsi [getDataAt] sudah diberi filter.
//        loge("bindVh() position= $position dataInd= $dataInd name= ${this::class.java.simpleName}")
//        selectedItemView= holder.itemView
    }
/*
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
 */

    /*
    protected open fun setupRv(){
        if(rv != null){
            rv!!.adapter= this
            val lm= setupLayoutManager()
            if(lm is LayoutManagerResp)
                lm.onLayoutCompletedListener= onLayoutCompletedListener
            rv!!.layoutManager= lm
/*
            if(lm is LinearLayoutManager)
                rv!!.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    val firstVisItemPos= lm.findFirstVisibleItemPosition()
                    val lastVisItemPos= lm.findLastCompletelyVisibleItemPosition()
                    Log.e("SimpleAbsRVA", "firstVisItemPos= $firstVisItemPos lastVisItemPos= $lastVisItemPos lastVisItemPos == itemCount -1 = ${lastVisItemPos == itemCount -1}")
                    if(lastVisItemPos == itemCount -1){

                    }
                }
 */
        }
    }
    fun updateLayoutManager(func: (lm: LM) -> Unit){
        if(rv != null)
            func(rv!!.layoutManager as LM)
    }
 */
    override fun initLayoutManager(layoutManager: LM) {
        if(layoutManager is LayoutManagerResp)
            layoutManager.onLayoutCompletedListener= onLayoutCompletedListener
    }

    /**
     * Fungsi yg mengembalikan indeks dari [dataList] yg dipengaruhi oleh sort atau filter
     * yg dilakukan oleh [contentArranger].
     *
     * @param adpPos merupakan index itemView yg ditampilkan di adapter, bkn index dari {@link #dataList}.
     *
     * @return posisi index sebenarnya dari [dataList], -1 jika ternyata [adpPos]
     */
    fun getDataShownIndex(adpPos: Int): Int?{
        try{
            return try{ contentArranger.resultInd[getDataIndex(adpPos)!!, -1] }
            catch (e: KotlinNullPointerException){ null }
        } catch (e: IndexOutOfBoundsException){
            throw IndexOutOfBoundsException("itemPos ($adpPos) melebihi itemCount ($itemCount)")
        }
    }

    /**
     * Fungsi yg digunakan untuk mendapatkan index yg sudah diproses sehingga programmer
     * tidak perlu lagi melakukan pengecekan terhadap [onlyShownItem]. FUngsi ini mengurus
     * masalah itu.
     *
     * @param adpPos dapat berupa posisi adapter (data yg terlihat) maupun indeks [dataList] scr langsung.
     */
    fun getDataProcessedIndex(adpPos: Int, onlyShownItem: Boolean): Int?{
        return if(onlyShownItem) getDataShownIndex(adpPos)
            else getDataIndex(adpPos)
    }

    /**
     * @return posisi data yg ditampilkan oleh adp [SimpleRvAdp] ini,
     *   null jika [dataPos] IndexOutOfBound.
     */
    final override fun getRawAdpPos(dataPos: Int): Int? {
        return if(dataPos in 0 until itemCount){
            if(dataPos == itemCount -1 && footerView != null) return null

            //1. Mencari posisi yg ditampilkan berdasarkan [contentArranger.resultInd].
            var foundAdpPos= 0
            for((adpPos, innerDataPos) in contentArranger.resultInd){
                if(innerDataPos == dataPos){
                    foundAdpPos= adpPos
                    break
                }
            }
            //2. Seperti pada superclass, jika headerView != null, maka posisi data yg ditampilkan bergeser 1.
            super.getRawAdpPos(foundAdpPos)
        } else null
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
                                    selectItem(posFromList, isIndexProcessed = true)
                                    isListAlreadyFiltered[posFromList]= true
                                    break
                                }
                        }
                } else{
                    for(data in list){
                        val pos= dataList!!.indexOf(data)
                        if(pos >= 0)
                            selectItem(pos, isIndexProcessed = true)
                    }
                }
            } else {
                selectItem(list.firstOrNull())
            }
        }
    }
    /**
     * <28 Juni 2020> => Fungsi hanya untuk data yg terlihat karena erat kaitannya dg view.
     *
     * @return indeks dari view di mana [data] ditampilkan pada layar.
     *   Nilai yg dikembalikan bkn merupakan indeks pada [dataList] scr utuh.
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
/*
        var viewIndex= pos //Index dari view yg ditampilkan, bkn [dataList] scr utuh.
        for((key, value) in contentArranger.resultInd){
            if(value == pos)
                viewIndex= key
        }
 */
        selectItem(pos, isIndexProcessed = true)
        return pos
    }
    /**
     * @param pos adalah posisi mentah dari keseluruhan view yg tampil di adapter.
     * @param isIndexProcessed true jika @param [pos] yg diinputkan adalah posisi yg sudah menunjukan
     *   indeks [dataList] scr langsung
     */
    open fun selectItem(pos: Int, v: View?= null, onlyShownItem: Boolean= true, isIndexProcessed: Boolean= false){
        val dataInd= if(isIndexProcessed) pos
            else getDataProcessedIndex(pos, onlyShownItem) ?: return
/*
        val dataInd= (if(onlyShownItem) getDataShownIndex(pos)
            else getDataIndex(pos)) ?: return
 */

        if(!isMultiSelectionEnabled){
            val isSelectedBefore= selectedItemPos_single >= 0
            var selectedItemView_before: View?= null
            if(isSelectedBefore){
                onItemSelectedListener?.onUnSelectItem(
                    selectedItemView,
                    pos, //<14 Juli 2020> => untuk menunjukan pos sebenarnya di adapter.
                    getDataAtProcessedIndex(selectedItemPos_single)!!
//                    selectedItemPos_single,
//                    getDataAt(selectedItemPos_single, false)!!
                )
                selectedItemView_before= selectedItemView
                selectedItemView= null
            }
            if(dataInd in 0 until (dataList?.size ?: 0)){
                /**
                 * Anggapannya pada umumnya jika user menekan item yang sama,
                 * maka berarti bahwa user tersebut ingin me-unselect item tersebut.
                 */
                if(selectedItemPos_single != dataInd){ //pos <9 Juli 2020> => agar [selectedItemPos_single] sesuai [dataList] scr full.
                    selectedItemView= v ?: getView(pos) //untuk menunjukan pos sebenarnya di adapter.
                    selectedItemPos_single= dataInd
//                    Log.e("SIMPLE_RV_ADP", "selectedItemView == null = ${selectedItemView == null}")
//                    loge("pos= $pos dataInd= $dataInd getDataAt(dataInd, false) = ${getDataAt(dataInd, false)} getDataAtProcessedIndex(pos) = ${getDataAtProcessedIndex(dataInd)}")
                    onItemSelectedListener?.onSelectItem(
                        selectedItemView,
                        pos, //<14 Juli 2020> => untuk menunjukan pos sebenarnya di adapter.
//                        dataInd,
                        getDataAtProcessedIndex(dataInd)!!
//                        getDataAt(dataInd, false)!!
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

            val isPosNotExisting= selectedItemPos_list!!.indexOf(dataInd) < 0
            if(isPosNotExisting){
                selectedItemPos_list!!.add(dataInd)
                onItemSelectedListener?.onSelectItem(
                    selectedItemView,
                    pos, //untuk menunjukan pos sebenarnya di adapter.
                    getDataAtProcessedIndex(dataInd)!!
//                    dataInd,
//                    getDataAt(dataInd, false)!!
                )
            } else{
                selectedItemPos_list!!.removeAt(dataInd)
                onItemSelectedListener?.onUnSelectItem(
                    selectedItemView,
                    pos, //untuk menunjukan pos sebenarnya di adapter.
                    getDataAtProcessedIndex(dataInd)!!
//                    dataInd,
//                    getDataAt(dataInd, false)!!
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
        if(!isMultiSelectionEnabled){
            if(selectedItemPos_single >= 0){
                onItemSelectedListener?.onUnSelectItem(
                    selectedItemView,
                    selectedItemPos_single,
                    getDataAt(selectedItemPos_single)!!
                )
                selectedItemPos_single= -1
                selectedItemView= null
            }
        } else{
            //<14 Juli 2020> <selesai:0> => Msh blum ditest!!!.
            if(selectedItemPos_list != null){
                for(selectedInd in selectedItemPos_list!!){
                    onItemSelectedListener?.onUnSelectItem(
                        getView(selectedInd),
                        selectedInd,
                        getDataAtProcessedIndex(selectedInd)!!
                    )
                }
                selectedItemPos_list!!.clear()
            }
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
     *
     * @param pos adalah indeks view yg terlihat saja, bkn indeks [dataList] scr utuh.
     */
    override fun getView(pos: Int): View?{
        return rv?.layoutManager?.findViewByPosition(pos)
    }

    protected fun getDataAtProcessedIndex(pos: Int): D?{
        return try{ dataList?.get(pos) } catch (e: Exception){ null }
    }
    override fun getDataAt(pos: Int, onlyShownItem: Boolean, isIndexProcessed: Boolean): D?{
        return if(!isIndexProcessed){
            if(pos in 0 until itemCount) //(dataList?.size ?: 0))
                try{
                    dataList?.get(
                        getDataProcessedIndex(pos, onlyShownItem)!!
/*
                    <14 Juli 2020> => Definisi lama.
                    if(!onlyShownItem) getDataIndex(pos)!!
                    else getDataShownIndex(pos)!!
 */
                    )
                } catch (e: Exception){ null }
            else null
        } else dataList?.get(pos)
    }

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

//    protected open fun updateData_int(dataList: ArrayList<D>?, isInternalEdit: Boolean) {}

/*
    protected inline fun internalEdit(func: () -> Unit){
        val isInternalEdit_init= isInternalEdit
        isInternalEdit= true
        func()
        isInternalEdit= isInternalEdit_init
    }
 */

    override fun deleteItemAt(pos: Int, onlyShownItem: Boolean): D?{
        val ind = (if(onlyShownItem) getDataShownIndex(pos)
            else pos)
            ?: return null //dataListFull?.removeAt(pos)

        val e= dataList?.removeAt(ind)
        notifyDataSetChanged_()
        return e
    }

    override fun modifyDataAt(ind: Int, onlyShownItem: Boolean, func: (data: D) -> D){
        dataList.notNull { list ->
            (if(!onlyShownItem) ind
            else getDataShownIndex(ind)).notNull{ dataInd ->
                val data= list.getOrNull(dataInd)
                if(data != null){
//                val indInFull= dataList!!.indexOf(data)
                    val dataNew= func(data)
                    dataList!![dataInd]= dataNew
//                dataListFull!![indInFull]= dataNew
                    onUpdateDataListener?.onUpdateData(dataList, dataInd, DataUpdateKind.EDIT)
                }
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
        if(resetFirst)
            contentArranger.reset()
        contentArranger.filter(func)
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
     *                   Untuk masalah integrasi data, fungsi [getShownInd]() mengurus masalah itu.
     *                   Pada definisi ini, sortedIndMap selalu berukuran sama dg dataList.
     *
     */
    fun sort(resetFirst: Boolean= false, func: (pos1: Int, data1: D, pos2: Int, data2: D) -> Boolean){
        if(resetFirst)
            contentArranger.reset()
        contentArranger.sort(func)
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
/*
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


    /**
     * Fungsi yg memiliki prinsip sama dg filter, yaitu menyaring data yg sudah diurutkan.
     * Data yg disaring dapat berupa data secara utuh maupun data yg sblumnya sudah disaring menggunakan filter().
     *
     * @param onlyShownItem true jika pencarian hanya dilakukan terhadap item yg telah dimapping.
     */
    @CallSuper
    open fun searchItem(keyword: String, onlyShownItem: Boolean= true){
        if(keyword.isNotEmpty()){
            dataList.notNull { list ->
//                val dataMatch= ArrayList<D>() //<28 Juni 2020> => Definisi lama.
                val newFilter= SparseIntArray() //<28 Juni 2020> => Definisi baru.
                val searchSize=
                    if(onlyShownItem) itemCount
                    else dataList?.size ?: 0

                var filterInd= -1
                for(i in 0 until searchSize){
                    (if(onlyShownItem) getDataShownIndex(i)
                    else i).notNull { ind ->
                        if(searchFilterFun(dataList!![ind], keyword)) //!!!
                            newFilter[++filterInd]= ind
                    }
                }
                contentArranger.resultInd= newFilter
                notifyDataSetChanged_()
            }
        } else
            resetDataToInitial()
    }

    fun showOnlySelectedData(isShown: Boolean= true){
        if(!isShown)
            filter { pos, data ->
                selectedItemPos_list?.contains(pos) ?: false
            }
        else
            resetDataToInitial()
    }


    fun resetDataToInitial(){
        contentArranger.reset()
        notifyDataSetChanged_()
    }

    fun resetSort(){
        contentArranger.resetSort()
        notifyDataSetChanged_()
    }
    fun resetFilter(){
        contentArranger.resetFilter()
        notifyDataSetChanged_()
    }



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
        override var tag: Any?= null
        override fun onSelectItem(v: View?, pos: Int, data: D) {
            showCheckIndicator(isCheckIndicatorShown, pos)
            onItemSelectedFun?.invoke(v, pos, data)
        }

        override fun onUnSelectItem(v: View?, pos: Int, data: D) {
            showCheckIndicator(false, pos)
            onItemUnSelectedFun?.invoke(v, pos, data)
        }
    }
    interface OnItemSelectedListener<D>: Listener{
        /**
         * @param pos merupakan posisi yg terlihat pada adapter.
         *   [pos] belum tentu menunjukan indeks sebenarnya dari [dataList].
         */
        fun onSelectItem(v: View?, pos: Int, data: D)
        /**
         * @param pos merupakan posisi yg terlihat pada adapter.
         *   [pos] belum tentu menunjukan indeks sebenarnya dari [dataList].
         */
        fun onUnSelectItem(v: View?, pos: Int, data: D)
    }

    var isItemClickEnabled= true
        set(v){
            field= v
            notifyDataSetChanged_()
        }
    var onItemClickListener: OnItemClickListener?= null
    abstract inner class OnItemClickListener: Listener{
        override var tag: Any?= null
        /**
         * @param pos merupakan posisi yg terlihat pada adapter.
         *   [pos] belum tentu menunjukan indeks sebenarnya dari [dataList].
         */
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

    open fun createEmptyData(): D? = null

    /** <20 Juli 2020> => Data yg ditambah dapat ditampilkan atau tidak tergantung [isAddedVisible]. */
    fun addData(data: D, pos: Int= dataList?.size ?: 0, isAddedVisible: Boolean){
        super.addData(data, pos)
        if(isAddedVisible){
            contentArranger.resultInd.addLast(pos)
            notifyDataSetChanged_()
        }
    }

    /**
     * <28 Juni 2020> => Data yg dimasukkan tidak akan terlihat scr default.
     *                   Data yg dimasukkan juga tidak terdaftar pada mapping indeks yg ada.
     *
     * <20 Juli 2020> => Data yg ditambah dapat ditampilkan atau tidak tergantung [isAddedVisible].
     */
    @CallSuper
    open fun addEmptyData(defaultData: D?= null, pos: Int= dataList?.size ?: 0, isAddedVisible: Boolean= true): D?{
        val emptyData= createEmptyData() ?: defaultData
        return if(emptyData != null){
            addData(emptyData, pos, isAddedVisible)
            notifyDataSetChanged_()
            emptyData
        } else {
            loge("addEmptyData() data yg ditambahkan null, sehingga diabaikan")
            null
        }
    }
/*
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
 */
}