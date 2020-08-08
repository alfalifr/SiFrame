package com.sigudang.android.fragments.BoundProcessFrag
///*
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.CallSuper
import com.sigudang.android.adapters.BoundProductListAdp.BoundProductListAdp
import com.sigudang.android.fragments.bottomsheet.BsPickDateFrag
import com.sigudang.android.fragments.bottomsheet.BsWarehouseSelectFr
import com.sigudang.android.models.Bound
import com.sigudang.android.utilities.receiver.createPopupDatePicker
import com.sigudang.android.utilities.receiver.createPopupWarehousePicker
import org.jetbrains.anko.support.v4.act
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.arch.intent_state.IntentResult
//import kotlinx.android.synthetic.main.fragment_bound_proses_overview.view.*
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.intfc.`fun`.InitPropFun
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.NestedTopMiddleBottomBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.NestedTopMiddleBottomFragmentBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.android.siframe.tool.util.idName
import sidev.lib.android.viewrap.AnimatedGradientViewWrapper
import sidev.lib.android.viewrap.wrapChildWithBuffer
import sidev.lib.universal.`fun`.*
import sidev.lib.universal.tool.util.ThreadUtil
import kotlin.Exception


typealias OverviewIndex= BoundProcessOverviewFrag.OverviewIndex
typealias BottomViewIndex= ProcessBottomNavigationFrag.BottomViewIndex
typealias EditableIndex= BoundProcessOverviewFrag.EditableIndex

/**
 * Fragment untuk tampilan semua proses pada bound, baik in, out, crossdocking.
 * Fragment ini juga digunakan untuk menampilkan product list pada bound.
 */
abstract class BoundSingleProcessAbsFrag
    : RvFrag<BoundProductListAdp>(), BoundProcessRootFrag, //
    NestedTopMiddleBottomFragmentBase, InitPropFun { //<BpListAdp, BpSingleProcessState, BpSingleProcessIntent>(), TopMiddleBottomBase{
    final override var isInit: Boolean= false

    //data yg sama untuk semua BoundSingleProcessAbsFrag yg berjalan
    // titik .copy() berada pada entry point saat startAct()

//=================Bound Data==============
    var boundData: Bound?= null
        set(v){
            field= v
            loge("boundData != null =. ${boundData != null}")
            if(v != null){
                typedTopFragment.boundData= v
                typedBottomFragment.boundData= v
            }
        }

    var isSendMethodSame= false
        set(v){
            field= v
            rvAdp.sendComp_lessee?.isCompVisible= !v || !isSendAddressSame
            rvAdp.sendComp_warehouse?.isCompVisible= !v || !isSendAddressSame

            rvAdp.sendComp_lessee?.isSendMethodVisible= !v
            rvAdp.sendComp_warehouse?.isSendMethodVisible= !v
            typedTopFragment.isSendMethodSame= v
        }
    var isSendAddressSame= false
        set(v){
            field= v
            rvAdp.sendComp_lessee?.isCompVisible= !v || !isSendMethodSame
            rvAdp.sendComp_warehouse?.isCompVisible= !v || !isSendMethodSame

            rvAdp.sendComp_lessee?.isSendAddressVisible= !v
            rvAdp.sendComp_warehouse?.isSendAddressVisible= !v
            typedTopFragment.isSendAddressSame= v
        }

/*
    val boundTotalAmount: Int
        get(){
            var total= 0
            if(boundData.productList != null)
                for(product in boundData.productList!!){
                    total += product.amount
                }
            return total
        }

    val boundTotalVolume: Double
        get(){
            var total= 0.0
            if(boundData.productList != null)
                for(product in boundData.productList!!){
                    total += (product.amount *product.product.volPerUnit)
                }
            return total
        }
 */

    /*
    final override var topLayoutId: Int= R.layout.fragment_bound_proses_overview_upper
        private set
    final override val bottomLayoutId: Int
        get() = R.layout.fragment_bound_proses_bottom_btn
 */
//============Layout component===========
    override var topFragment: Frag?= BoundProcessOverviewFrag()
    override var bottomFragment: Frag?= ProcessBottomNavigationFrag()
    val typedTopFragment: BoundProcessOverviewFrag get()= topFragment as BoundProcessOverviewFrag
    val typedBottomFragment: ProcessBottomNavigationFrag get()= bottomFragment as ProcessBottomNavigationFrag

    final override val isTopContainerNestedInRv: Boolean get() = true
    override val isBottomContainerNestedInRv: Boolean get() = true

    override var bottomContainer: ViewGroup?= null
    override var middleContainer: ViewGroup?= null
    override var topContainer: ViewGroup?= null

//    lateinit var viewBufferWrapper: List<AnimatedGradientViewWrapper>//?= null


//============Layout component data===========
    open val isOverviewVisible: Boolean
        get()= true

    abstract val shownColumnInd: Array<OverviewIndex> //= InfoIndex.INVOICE.toArray { it.ordinal } //Array(columnViewList.size){it} //setShownColumnInd()
    abstract val shownBottomInd: Array<BottomViewIndex>
    open val editableColumnInd: Array<EditableIndex> = arrayOf() //setShownColumnInd()

    var shownColumnInd_int: Array<Int>
        set(v){ typedTopFragment.shownColumnInd_int= v }
        get()= typedTopFragment.shownColumnInd_int
    val columnViewList: ArrayList<View> get()= typedTopFragment.columnViewList

    val editableViewList: ArrayList<View> get()= typedTopFragment.editableViewList
    var editableColumnInd_int: Array<Int>
        set(v){ typedTopFragment.editableColumnInd_int= v }
        get()= typedTopFragment.editableColumnInd_int

    val bottomViewList: ArrayList<View> get()= typedBottomFragment.bottomViewList
    var shownBottomInd_int: Array<Int>
        set(v){ typedBottomFragment.shownBottomInd_int= v }
        get()= typedBottomFragment.shownBottomInd_int



//============Adapter component data===========
    enum class AdpCompIndex{
        PRODUCT_COUNT, CONTAINER_VISIBLE, PACKAGING_VISIBLE,
        SEND_LESSEE_VISIBLE, SEND_WAREHOUSE_VISIBLE, TRACKING_VISIBLE,
    }
    abstract val shownAdpComponent: Array<AdpCompIndex>
    var shownAdpComponent_int: Array<AdpCompIndex> = arrayOf()
        set(v){
            field= v
            rvAdp.isProductCountFixed= true
            rvAdp.isContainerVisible= false
            rvAdp.isPackagingVisible= false
            rvAdp.isSendLesseeVisible= false
            rvAdp.isSendWarehouseVisible= false
            rvAdp.isTrackingVisible= false

            for(ind in v){
                when(ind){
                    AdpCompIndex.PRODUCT_COUNT -> rvAdp.isProductCountFixed= false
                    AdpCompIndex.CONTAINER_VISIBLE -> rvAdp.isContainerVisible= true
                    AdpCompIndex.PACKAGING_VISIBLE -> rvAdp.isPackagingVisible= true
                    AdpCompIndex.SEND_LESSEE_VISIBLE -> rvAdp.isSendLesseeVisible= true
                    AdpCompIndex.SEND_WAREHOUSE_VISIBLE -> rvAdp.isSendWarehouseVisible= true
                    AdpCompIndex.TRACKING_VISIBLE -> rvAdp.isTrackingVisible= true
                }
            }
        }



    @CallSuper
    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
//        viewBufferWrapper.forEach { it.showBuffer(false) }
        super<RvFrag>.onActive(parentView, callingLifecycle, pos)
        ThreadUtil.printCurrentStackTrace()
        for((i, stack) in Thread.currentThread().stackTrace.withIndex())
            loge("i= $i stack= $stack")
        loge("onActive() callingLifecycle= ${callingLifecycle?.clazz}")
        boundData= getBoundDataFromParent()!! //callingLifecycle.asNotNullTo { vp: BoundVpProcessFrag -> vp.getBoundData(this) }!!
        rvAdp.dataList= boundData?.productList
        for(i in 0 until rvAdp.itemCount){
            loge("rvAdp.itemCount iteraste i= $i rvAdp.getDataAt(i)= ${rvAdp.getDataAt(i)} rvAdp.getDataProcessedIndex(i, true)= ${rvAdp.getDataProcessedIndex(i, true)}")
        }

        val isSendMethodSame= boundData?.send?.method != null
        this.isSendMethodSame= isSendMethodSame

        val isSendAddressSame= boundData?.send?.address?.isNotBlank() == true
        this.isSendAddressSame= isSendAddressSame

        readData()
    }

    @CallSuper
    override fun __initView(layoutView: View) {
        super<RvFrag>.__initView(layoutView)
/*
        if(!isOverviewVisible)
            topLayoutId= _Config.INT_EMPTY
*/
        __initTopMiddleBottomView(layoutView)
        loge("${this::class} __initView()")
//        viewBufferWrapper= layoutView.wrapChildWithBuffer().also { it.forEach { it.showBuffer(keepView = true) } }

        rv.addOnGlobalLayoutListener {
            loge("rv.yEndInWindow= ${rv.yEndInWindow} typedTopFragment.layoutView.size.string= ${try{ typedTopFragment.layoutView.size.string} catch (e: Exception){ null }}")
        }

        rv.recycledViewPool.setMaxRecycledViews(rvAdp.itemLayoutId, 4)
        shownAdpComponent_int= shownAdpComponent
        loge("shownAdpComponent_int= ${shownAdpComponent_int.string}")

        initBoundProductListAdp(rvAdp)
    }


    final override fun _initTopView(topView: View) {
        Log.e(this::class.qualifiedName, "_initTopView()")
        shownColumnInd_int= shownColumnInd.toArrayOf { it.ordinal }
        editableColumnInd_int= editableColumnInd.toArrayOf { it.ordinal }
        if(isOverviewVisible) typedTopFragment.readData()
    }

    final override fun _initBottomView(bottomView: View) {
        Log.e(this::class.qualifiedName, "_initBottomView() BoundSingleProcessAbsFrag")
        shownBottomInd_int= shownBottomInd.toArrayOf { it.ordinal }
        rvAdp.onProductAmountChange= { product, oldAmount, newAmount ->
            val diff= newAmount -oldAmount
            typedBottomFragment.setProductAmount(product, diff)
//            bottomView.tv_summary_amount_number.addTxtNumberBy(diff)
//            val volum= bottomView.tv_summary_volume_number.addTxtNumberBy(diff *product.volPerUnit, -2)
            loge("productAmountChange diff= $diff")
        }
        typedBottomFragment.readData()
        initBottomBtnView(bottomView)
    }
    abstract fun initBottomBtnView(bottomView: View)


    final override fun initRvAdp(): BoundProductListAdp{
        val adp= BoundProductListAdp(context!!)
/*
        adp.isProductCountFixed= false
        adp.isContainerVisible= false
        adp.isPackagingVisible= false
        adp.isSendLesseeVisible= true
        adp.isSendWarehouseVisible= true
        adp.isTrackingVisible= false
// */
        return adp
    }
    abstract fun initBoundProductListAdp(adp: BoundProductListAdp)


    fun readData(){
        if(isOverviewVisible){
            typedTopFragment.readData()
/*
            val topView= topView!!
            topView.tv_invoice.text= boundData?.invoice
            loge("layoutView.tv_invoice.text= ${topView.tv_invoice.text}")
            topView.tv_date.text= boundData?.date
            topView.tv_warehouse.text= boundData?.warehouse?.name
            topView.tv_lessee.text= boundData?.lessee?.name

            val sendData= boundData?.send
            val senderData= boundData?.shipper
//        if(sendData != null){
            topView.tv_send_kind.text= sendData?.method?.name
            topView.tv_send_address.text= sendData?.address
            topView.tv_receipt.text= sendData?.receipt?.receipt
//        }

            loge("senderData.phone = ${senderData?.phone}")
//        if(senderData!= null){
            topView.tv_sender.text= senderData?.name
            topView.tv_sender_no.text= senderData?.phone
            topView.tv_sender.visibility= View.VISIBLE
            topView.tv_sender_no.visibility= View.VISIBLE
            if(senderData == null){
                topView.tv_sender.visibility= View.GONE
                topView.tv_sender_no.visibility= View.GONE
            }
            topView.comp_cb_send_kind_same.cb.isChecked= isSendMethodSame
            topView.comp_cb_send_address_same.cb.isChecked= isSendAddressSame
 */
        }
        typedBottomFragment.readData()
/*
        bottomView.notNull { bottomView ->
            bottomView.tv_summary_amount_number.text= boundTotalAmount.toString()
            bottomView.tv_summary_volume_number.text= boundTotalVolume.round(-2).toString()
        }.isNull { //Jika bottoView msh null, maka tunggu saja hingga dipasang ke screen
            layoutView.addOnGlobalLayoutListener {
                bottomView!!.tv_summary_amount_number.text= boundTotalAmount.toString()
                bottomView!!.tv_summary_volume_number.text= boundTotalVolume.round(-2).toString()
            }
        }
 */
    }
/*
    /**
     * @return true jika berhasil.
     */
    fun setProductAmount(product: Product, amount: Int): Boolean{
        return boundData.productList?.findIndexed { it.value.product.id == product.id }
            .notNull {
                val bound= it.value
                val diff= amount -bound.amount
                bound.amount= amount
                bottomView!!.tv_summary_amount_number.addTxtNumberBy(diff)

                rvAdp.setProductAmountAt(rvAdp.getRawAdpPos(it.index) ?: it.index, amount)
            } != null
    }
 */
/*
    fun sendMethodSame(same: Boolean){
//        isSendMethodSame= same
        rvAdp.sendComp_lessee?.isSendMethodVisible= same
        rvAdp.sendComp_warehouse?.isSendMethodVisible= same

        val vis= if(same) View.VISIBLE else View.GONE
        topView!!.ll_send_kind_container!!.visibility= vis
        topView!!.ll_receipt_container!!.visibility= vis
    }
    fun sendAddressSame(same: Boolean){
        rvAdp.sendComp_lessee?.isSendAddressVisible= same
        rvAdp.sendComp_warehouse?.isSendAddressVisible= same
        topView!!.ll_send_address_container!!.visibility=
            if(same) View.VISIBLE else View.GONE
    }
 */

    /**
     * Digunakan untuk menyelesaikan masalah apakah metode dan alamat pengiriman
     * sama semua atau beda".
     */
    fun resolveSendMethodAndAddress(){
        //Sesuatu
    }
/*
    private fun proceedColumnVisibility(){
        for((i, view) in columnViewList.withIndex()){
            loge("i= $i")
            view.visibility= View.GONE
        }

        for(ind in shownColumnInd_int){
            loge("ind= $ind")
            try{ columnViewList[ind].visibility= View.VISIBLE }
            catch (e: Exception){ /*ignore*/ }
        }
    }
 */
/*
    private fun proceedColumnEditable(){
        for((i, view) in columnViewList.withIndex()){
            loge("i= $i")
            view.visibility= View.GONE
        }

        for(ind in editableColumnInd_int){
            loge("ind= $ind")
            try{ columnViewList[ind].visibility= View.VISIBLE }
            catch (e: Exception){ /*ignore*/ }
        }
    }
 */

    //Hanya sbg implementasi abstraksi
    override fun _initMiddleView(middleView: View) {}

/*
    fun showColumn(isShown: Boolean= true, vararg ind: Int, range: IntRange?= null){
        val vis= if(isShown) View.VISIBLE
        else View.GONE
        for(i in ind)
            columnViewList[i].visibility= vis
        if(range != null)
            for(i in range)
                columnViewList[i].visibility= vis
    }
 */
}


class BpSingleProcConf{
    sealed class State<R: Result>: ViewState<R>(){

    }

    sealed class Intent: ViewIntent(){

    }

    sealed class Result: IntentResult(){

    }
}

// */