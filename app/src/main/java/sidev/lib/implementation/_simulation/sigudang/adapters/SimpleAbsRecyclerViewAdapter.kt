package sidev.kuliah.agradia.template.adp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.sigudang.android._template.adapter.layoutmanager.LayoutManagerResp
import sidev.lib.android.siframe.tool.RunQueue
import sidev.lib.check.notNull
import sidev.lib.collection.copy
import sidev.lib.exception.Exc
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.sigudang.util.RequestUtil
import sidev.lib.implementation._simulation.sigudang.util.filter

//!!!!!!@@ 18 Jan 2020
abstract class SimpleAbsRecyclerViewAdapter <D, LM: RecyclerView.LayoutManager> (
    val ctx: Context, dataList: ArrayList<D>?
    )
    : RecyclerView.Adapter<SimpleAbsRecyclerViewAdapter<D, LM>.SimpleViewHolder>(){

    protected var isInternalEdit= false
    protected var isDataListInternalEdit= false
    /**
     * List data yang akan dipake untuk ditampilkan
     */
    var dataList: ArrayList<D>?= null
        set(v){
            field= v
            if(!isDataListInternalEdit)
                dataListFull= v

            updateData_int(v, isDataListInternalEdit)

            val copiedList= if(dataList != null) v?.copy() //ArrayList(dataList)
                else ArrayList()
            onUpdateDataListener?.onUpdateData(copiedList, -1, DataUpdateKind.SET)
            notifyDataSetChanged_()
        }
    var dataListFull: ArrayList<D>?= null
        protected set(v){
            field= v
            if(isDataListInternalEdit){
                dataList= v
//                updateData_int(containerView)
            }
        }

    /**
     * RequestUtil digunakan jika ada request yang berasal dari server
     */

    var reqUtil: RequestUtil? = null

    /**
     * Kenapa menggunakan lambda? Karena lebih fleksibel
     * saat mengganti kondisi search, yaitu dengan mengganti dg lambda lainnya pada
     * file constants
     */
    open val searchFilterFun: (c: Context, data: D, keyword: String) -> Boolean= { _, _, _ -> true}
    open val selectFilterFun: ((dataFromList: D, dataFromInput: D, posFromList: Int) -> Boolean) ?= null
    init{
        this.dataList= dataList
//        reqUtil = RequestUtil(ctx)
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
    val itemContainerLayoutId= R.layout._simul_sigud__t_item_adp_container

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

    abstract fun bindVH(vh: SimpleViewHolder, pos: Int, data: D)
    abstract fun setupLayoutManager(): LM
    @CallSuper
    open fun bindVH_internal(vh: SimpleViewHolder, pos: Int, data: D){
        val v= vh.itemView

        val proceedVis= if(!isMultiSelectionEnabled) pos == selectedItemPos_single
            else (selectedItemPos_list?.indexOf(pos) ?: -1) >= 0

        if(proceedVis){
            v.findViewById<ImageView>(R.id.iv_check)
                ?.visibility=
                    if(isCheckIndicatorShown) View.VISIBLE
                    else View.GONE
            v.findViewById<ImageView>(R.id.iv_overlay)
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
        v.findViewById<LinearLayout>(R.id.ll_content_container)
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
        Log.e("SImpleAbsRVA", "bindVh() position= $position name= ${this::class.java.simpleName}")
        selectedItemView= holder.itemView
        selectedItemView?.findViewById<ImageView>(R.id.iv_check)
            ?.visibility= if(isCheckIndicatorShown && position == selectedItemPos_single) View.VISIBLE
            else View.GONE
        bindVH_internal(holder, position, dataList!![position])
        bindVH(holder, position, dataList!![position])
        holder.itemView.setOnClickListener { v ->
            selectItem(position)
            onItemClickListener?.onClickItem(v, holder.adapterPosition, dataList!![position])
        }
    }

    inline fun notifyDataSetChanged_(f: (() -> Unit)= {}){
        val lm= rv?.layoutManager as LM?
        if(lm != null){
            val recyclerViewState= rv!!.layoutManager?.onSaveInstanceState()
            f()
            notifyDataSetChanged()
            Log.e(this::class.java.simpleName, "notifyDatasetChenged!!! name= ${this::class.java.simpleName}")
            rv!!.layoutManager?.onRestoreInstanceState(recyclerViewState)
        }
    }

    protected open fun initAdp(){}

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

    /**
     * Dg anggapan bahwa elemen di dalam
     * @param list tidak boleh null
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
                            Log.e("SimpleAbsRVA", "i= $i posFromList= $posFromList isListAlreadyFiltered[posFromList]= ${isListAlreadyFiltered[posFromList]}")
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
    open fun selectItem(pos: Int, v: View?= null){
        if(!isMultiSelectionEnabled){
            val isSelectedBefore= selectedItemPos_single >= 0
            var selectedItemView_before: View?= null
            if(isSelectedBefore){
                if(getDataAt(selectedItemPos_single) != null)
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
                    selectedItemView= v ?: getItem(pos)
                    selectedItemPos_single= pos
                    Log.e("SIMPLE_RV_ADP", "selectedItemView == null = ${selectedItemView == null}")
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
                selectedItemView?.findViewById<ImageView>(R.id.iv_check)
                    ?.visibility= View.VISIBLE
                selectedItemView_before?.findViewById<ImageView>(R.id.iv_check)
                    ?.visibility= View.GONE
            }
            if(isOverlayShown){
                selectedItemView?.findViewById<ImageView>(R.id.iv_overlay)
                    ?.visibility= View.VISIBLE
                selectedItemView_before?.findViewById<ImageView>(R.id.iv_overlay)
                    ?.visibility= View.GONE
            }
        } else{
            if(selectedItemPos_list == null)
                selectedItemPos_list= ArrayList()

            selectedItemView= v ?: getItem(pos)

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
                selectedItemView?.findViewById<ImageView>(R.id.iv_check)
                    ?.visibility=
                        if(isPosNotExisting) View.VISIBLE
                        else View.GONE
            }
            if(isOverlayShown){
                selectedItemView?.findViewById<ImageView>(R.id.iv_overlay)
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
        getItem(pos)?.findViewById<ImageView>(R.id.iv_check)
            ?.visibility=
                if(isShown) View.VISIBLE
                else View.GONE
    }

    protected fun showOverlay(isShown: Boolean= true){
        val vis= if(isShown) View.VISIBLE
            else View.GONE
        if(!isMultiSelectionEnabled){
            selectedItemView?.findViewById<ImageView>(R.id.iv_overlay)
                ?.visibility= vis
        } else{
            for(selectedPos in selectedItemPos_list!!){
                getItem(selectedPos)
            }
        }
    }


    fun getItem(pos: Int): View?{
        return rv?.layoutManager?.findViewByPosition(pos)
    }

    fun getDataAt(pos: Int): D?{
        return if(pos in 0 until (dataList?.size ?: 0))
            dataList?.get(pos)
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

    fun resetDataToInitial(){
        setDataListSetFromInternal {
            dataList= dataListFull
        }
    }

    protected inline fun setDataListSetFromInternal(func: () -> Unit){
        val isDataListSetFromInternal_init= isDataListInternalEdit
        isDataListInternalEdit= true
        func()
        isDataListInternalEdit= isDataListSetFromInternal_init
    }

    open fun deleteItemAt(pos: Int): D?{
        val e= dataListFull?.removeAt(pos)
        dataList?.remove(e)
        notifyDataSetChanged_()
        return e
    }
    fun clearData(){
        setDataListSetFromInternal {
            dataListFull= null
        }
    }

    @CallSuper
    open fun searchItem(keyword: String){
        if(keyword.isNotEmpty()){
            dataListFull.notNull { list ->
                val dataMatch= ArrayList<D>()
                list.forEach { data ->
                    if(searchFilterFun(ctx, data, keyword))
                        dataMatch.add(data)
                }
                if(dataList!!.size != dataMatch.size)
                    setDataListSetFromInternal{
                        dataList= dataMatch
                    }
            }
        } else
            resetDataToInitial()
    }

    inline fun modifyDataAt(ind: Int, func: (data: D) -> D){
        dataList.notNull { list ->
            val data= list.getOrNull(ind)
            if(data != null){
                val indInFull= dataListFull!!.indexOf(data)
                val dataNew= func(data)
                dataList!![ind]= dataNew
                dataListFull!![indInFull]= dataNew
                onUpdateDataListener?.onUpdateData(dataList, ind, DataUpdateKind.EDIT)
            }
        }
    }

    inline fun modifyDataInnerVarAt(ind: Int, func: (data: D) -> Unit){
        dataList.notNull { list ->
            val data= list.getOrNull(ind)
            if(data != null)
                func(data)
        }
    }

    inline fun filter(func: (pos: Int, data: D) -> Boolean){
        dataListFull?.filter { pos, el ->
            func(pos, el)
        }.notNull { list ->
            `access$setDataListSetFromInternal` {
                dataList= ArrayList(list)
            }
        }
    }

    fun showOnlySelectedData(isShown: Boolean= true){
        if(!isShown)
            filter { pos, data ->
                selectedItemPos_list?.contains(pos) ?: false
            }
        else
            resetDataToInitial()
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
        = object : OnItemSelectedListener<D>{
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

    private val onLayoutCompletedQueue= RunQueue<RecyclerView.State?, Unit>()
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
        onLayoutCompletedListener= object: OnLayoutCompletedListener{
            override fun onLayoutCompletedResp(state: RecyclerView.State?) {
                l(state)
            }
        }
        updateLayoutManager { lm ->
            Log.e("SimpleAbsRVA", "lm is LayoutManagerResp = ${lm is LayoutManagerResp}")
            if(lm is LayoutManagerResp){
                lm.onLayoutCompletedListener= onLayoutCompletedListener
                Log.e("SimpleAbsRVA", "lm is NOT thrown")
            } else{
                throw Exc(this::class, "lm is not LayoutManagerResp")
                Log.e("SimpleAbsRVA", "lm is thrown")
            }
        }
    }

    open fun createEmptyData(): D?{
        return null
    }
    @CallSuper
    open fun addEmptyData(pos: Int= dataList?.size ?: 0, initValFun: ((initData: D) -> D) ?= null): D?{
        var emptyData= createEmptyData()
        if(initValFun != null && emptyData != null)
            emptyData= initValFun(emptyData)
        return if(emptyData != null){
            addData(emptyData, pos)
            Log.e("BoundProductSendAdp_Lessee", "_addEmptyData Di dalam")
            emptyData
        } else{
            Log.e("BoundProductSendAdp_Lessee", "_addEmptyData dataList.size= ${dataList?.size}")
            null
        }
    }
    fun addData(data: D, pos: Int= dataList?.size ?: 0){
        val dataListInt= if(dataList != null) dataList!!
            else ArrayList()

        dataListInt.add(pos, data)
        dataList= dataListInt
    }

    @PublishedApi
    internal fun `access$setDataListSetFromInternal`(func: () -> Unit) =
        setDataListSetFromInternal(func)
}
