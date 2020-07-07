package sidev.lib.android.siframe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.intfc.adp.Adp
import sidev.lib.android.siframe.tool.RunQueue
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.filter
import sidev.lib.universal.`fun`.notNull

//!!!!!!@@ 18 Jan 2020
abstract class RvAdp_Old <D, LM: RecyclerView.LayoutManager> (
    val ctx: Context //, dataList: ArrayList<D>? <27 Juni 2020> => param konstruktor primer dataList jadi opsional agar menghemat waktu ngoding.
    )
    : RecyclerView.Adapter<RvAdp_Old<D, LM>.SimpleViewHolder>(), Adp{

    //<27 Juni 2020> => konstruktor dg param dataList jadi konstruktor sekunder agar menghemat waktu ngoding.
    constructor(ctx: Context, dataList: ArrayList<D>?): this(ctx){
        this.dataList= dataList
    }

//    protected var isInternalEdit= false
    protected var isInternalEdit= false
    /**
     * List data yang akan dipake untuk ditampilkan
     */
    var dataList: ArrayList<D>?= null
        set(v){
            field= v
            if(!isInternalEdit)
                dataListFull= v
            updateData_int(v, isInternalEdit)

            val copiedList=
                if(dataList != null) ArrayList(dataList!!)
                else ArrayList()
            onUpdateDataListener?.onUpdateData(copiedList, -1, DataUpdateKind.SET)
            notifyDataSetChanged_()
        }
/*
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

    var dataListFull: ArrayList<D>?= null
        protected set(v){
            field= v
            if(isInternalEdit){
                dataList= v
//                updateData_int(containerView)
            }
        }
    /**
     * Kenapa menggunakan lambda? Karena lebih fleksibel
     * saat mengganti kondisi search, yaitu dengan mengganti dg lambda lainnya pada
     * file constants
     */
    open val searchFilterFun: (data: D, keyword: String) -> Boolean= { _, _ -> true }
    open val selectFilterFun: ((dataFromList: D, dataFromInput: D, posFromList: Int) -> Boolean) ?= null
    init{
//        this.dataList= dataList
        setOnLayoutCompletedListener { state ->
            iterateOnLayoutCompletedQueue(state)
        }
        initAdp()
    }

    var rv: RecyclerView?= null
        set(v){
            field?.adapter= null
            field= v
            setupRv()
        }
    var isMultiSelectionEnabled= false
    var selectedItemPos_list: ArrayList<Int>?= null
        protected set
    var selectedItemPos_single= -1
        protected set

    protected var selectedDataList: ArrayList<D>?= null

    /**
     * Akan menjadi krg reliable jika isMultiSelectionEnabled = true.
     * Gunakan getItem(Int) sebagai gantinya
     */
    var selectedItemView: View?= null
        protected set

    abstract val itemLayoutId: Int
    val itemContainerLayoutId= _Config.LAYOUT_ITEM_ADP_CONTAINER //R.layout._t_item_adp_container

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

    override fun getItemId(position: Int): Long = super<RecyclerView.Adapter>.getItemId(position)
    override fun getItemViewType(position: Int): Int = super<RecyclerView.Adapter>.getItemViewType(position)
    //    override fun hasStableIds() = super<RecyclerView.Adapter>.hasStableIds()


    abstract fun bindVH(vh: SimpleViewHolder, pos: Int, data: D)
    abstract fun setupLayoutManager(): LM
    @CallSuper
    open fun __bindVH(vh: SimpleViewHolder, pos: Int, data: D){
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

    open inner class SimpleViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun isAdpPositionSameWith(bindPos: Int): Boolean{
            return adapterPosition == bindPos
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val v= LayoutInflater.from(ctx).inflate(itemContainerLayoutId, parent, false)
        val contentV= LayoutInflater.from(ctx).inflate(itemLayoutId, parent, false)
        v.findViewById<LinearLayout>(_Config.ID_VG_CONTENT_CONTAINER) //R.id.ll_content_container
            .addView(contentV)
        return SimpleViewHolder(v)
    }

    override fun getItemCount(): Int {
        return when(dataList){
            null -> 0
            else -> dataList!!.size
        }
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        val dataInd= position //getShownIndex(position)
        val data= dataList!![dataInd]
        loge("bindVh() position= $position dataInd= $dataInd name= ${this::class.java.simpleName}")
//        selectedItemView= holder.itemView
        holder.itemView.findViewById<ImageView>(_Config.ID_IV_CHECK) //R.id.iv_check
            ?.visibility= if(isCheckIndicatorShown && position == selectedItemPos_single) View.VISIBLE
            else View.GONE
        __bindVH(holder, position, data)
        bindVH(holder, position, data)
        holder.itemView.setOnClickListener { v ->
            selectItem(position)
            onItemClickListener?.onClickItem(v, holder.adapterPosition, data)
        }
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

    protected open fun initAdp(){}

    protected open fun setupRv(){
        if(rv != null){
            rv!!.adapter= this
            val lm= setupLayoutManager()
//            if(lm is LayoutManagerResp)
//                lm.onLayoutCompletedListener= onLayoutCompletedListener
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
 */

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

    /**
     * Mengembalikan data dari dataset scr keseluruhan.
     * Jika pos IndexOutOfBound, maka return pos.
     */
    override fun getItem(pos: Int): Any{
        return try{ dataList!![pos]!! }
        catch (e: KotlinNullPointerException){ pos }
    }

    /**
     * Bentuk spesifik untuk mendapat itemView.
     * <28 Juni 2020> => Hanya untuk view yg terlihat, bkn semuanya.
     */
    override fun getView(pos: Int): View?{
        return rv?.layoutManager?.findViewByPosition(pos)
    }


    fun getDataAt(pos: Int): D?{
        return if(pos in 0 until (dataList?.size ?: 0))
            dataList?.get(pos)//.get(if(!onlyShownItem) pos else getShownIndex(pos))
        else
            null
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

    protected open fun updateData_int(dataList: ArrayList<D>?, isInternalEdit: Boolean) {}


    protected inline fun internalEdit(func: () -> Unit){
        val isInternalEdit_init= isInternalEdit
        isInternalEdit= true
        func()
        isInternalEdit= isInternalEdit_init
    }

    open fun deleteItemAt(pos: Int): D?{
        val ind = pos //if(onlyShownItem) getShownIndex(pos)
            //else pos //dataListFull?.removeAt(pos)
        val e= dataList?.removeAt(ind)
        notifyDataSetChanged_()
        return e
    }
    fun clearData(){
        dataList= null
/*
        <28 Juni 2020> => Definisi lama.
        internalEdit {
            dataListFull= null
        }
 */
    }

    fun modifyDataAt(ind: Int, func: (data: D) -> D){
        dataList.notNull { list ->
            val ind= ind //if(!onlyShownItem) ind
                //else getShownIndex(ind)
            val data= list.getOrNull(ind)
            if(data != null){
//                val indInFull= dataList!!.indexOf(data)
                val dataNew= func(data)
                dataList!![ind]= dataNew
//                dataListFull!![indInFull]= dataNew
                onUpdateDataListener?.onUpdateData(dataList, ind, DataUpdateKind.EDIT)
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

    fun filter(func: (pos: Int, data: D) -> Boolean){
        dataListFull?.filter { pos, el ->
            func(pos, el)
        }.notNull { list ->
            `access$setDataListSetFromInternal` {
                dataList= list as ArrayList //ArrayList(list)
            }
        }
    }

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

        loge("sort() MULAI")

        for(i in dataList!!.indices)
            for(u in i+1 until dataList!!.size)
                if(!func(i, dataList!![i], u, dataList!![u])){
                    val temp= dataList!![i]
                    dataList!![i]= dataList!![u]
                    dataList!![u]= temp
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
     * Fungsi yg memiliki prinsip sama dg filter, yaitu menyaring data yg sudah diurutkan.
     * Data yg disaring dapat berupa data secara utuh maupun data yg sblumnya sudah disaring menggunakan filter().
     *
     * @param onlyShownItem true jika pencarian hanya dilakukan terhadap item yg telah dimapping.
     */
    @CallSuper
    open fun searchItem(keyword: String){
        if(keyword.isNotEmpty()){
            dataList.notNull { list ->
                val dataMatch= ArrayList<D>()
                list.forEach { data ->
                    if(searchFilterFun(data, keyword))
                        dataMatch.add(data)
                }
                if(dataList!!.size != dataMatch.size)
                    internalEdit{
                        dataList= dataMatch
                    }
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
        internalEdit {
            dataList= dataListFull
        }
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
/*
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
 */
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
