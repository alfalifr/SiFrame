package sidev.lib.implementation._simulation.edc.frag

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import sidev.lib.android.siframe.view.tool.dialog.DialogConfirmationView
import kotlinx.android.synthetic.main.bar_top_utility.view.*
import kotlinx.android.synthetic.main.comp_performance_bar.view.*
import org.jetbrains.anko.support.v4.runOnUiThread
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.arch.viewmodel.LifeData
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.NestedTopMiddleBottomBase
import sidev.lib.android.siframe.model.StringId
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.siframe.view.tool.dialog.DialogListView
import sidev.lib.check.asNotNull
import sidev.lib.check.notNull
import sidev.lib.collection.copyGrowExponentially
import sidev.lib.collection.growTimely
import sidev.lib.implementation.R
import sidev.lib.implementation._cob.dum_transaction
import sidev.lib.implementation._simulation.edc.adp.TransacAdp
import sidev.lib.implementation._simulation.edc.model.Transaction
import sidev.lib.implementation._simulation.edc.util.Edc_Const

class TransacFrag : RvFrag<TransacAdp>(), NestedTopMiddleBottomBase {
    override var topContainer: ViewGroup?= null
    override var middleContainer: ViewGroup?= null
    override var bottomContainer: ViewGroup?= null

    override val topLayoutId: Int
        get() = R.layout.bar_top_utility
    override val bottomLayoutId: Int
        get() = R.layout.comp_performance_bar
    override val isBottomContainerNestedInRv: Boolean
        get() = false
    /*
    override val bottomContainerId: Int
        get() = _Config.ID_RL_BOTTOM_CONTAINER_OUTSIDE
 */

    var timeStart= System.currentTimeMillis()
    var timeEnd: Long= 0

    val timeElapsed= LifeData<Double>()
    val totalData= LifeData<Int>()
    val shownData= LifeData<Int>()

    lateinit var dialogSort: DialogListView<StringId>
    lateinit var dialogFilter: DialogListView<StringId>
    lateinit var dialogDuplicate: DialogConfirmationView
//    var sortFun: ((Int, Transaction, Int, Transaction) -> Boolean)?= null
    var sortFun: ((Transaction, Transaction) -> Int)?= null
    var filterFun: ((Transaction) -> Boolean)?= null


    val sortMenu= arrayListOf(
        StringId("1", "Nama Komoditi"),
        StringId("2", "Harga Total"),
        StringId("3", "Status")
    )
    val filterMenu= arrayListOf(
        StringId("1", "Beli"),
        StringId("2", "Jual"),
        StringId("3", "Diterima"),
        StringId("4", "Ditolak")
    )

    var duplicateData= ArrayList<Transaction>()


    override fun initRvAdp(): TransacAdp {
        return TransacAdp(context!!)
    }

    override fun _initView(layoutView: View) {
        __initTopMiddleBottomView(layoutView)
        activity.asNotNull { act: BarContentNavAct ->
            act.actBarViewContainer.findViewById<ImageView>(R.id.iv_action).notNull { iv ->
                initDialog()
                iv.setImageResource(R.drawable.ic_dot_3_vertical)
                _ViewUtil.setColorTintRes(iv, R.color.putih)
                val popup= PopupMenu(context!!, iv)
                popup.inflate(R.menu.popup_rv)
                popup.setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId){
                        R.id.popup_sort -> dialogSort.show()
                        R.id.popup_filter -> dialogFilter.show()
                        R.id.popup_reset -> rvAdp.resetDataToInitial()
                        R.id.popup_reset_sort -> rvAdp.resetSort()
                        R.id.popup_reset_filter -> rvAdp.resetFilter()
                        R.id.duplicate -> dialogDuplicate.show() //rvAdp.resetFilteredInd()
                    }
                    true
                }
                iv.setOnClickListener { popup.show() }
            }
        }
        val transList= dum_transaction.copyGrowExponentially(4) //dum_transaction.toArrayList()
        rvAdp.dataList= transList as ArrayList
        timeEnd= System.currentTimeMillis()
        timeElapsed.value= (timeEnd -timeStart)/1000.toDouble()
        totalData.value= rvAdp.dataList?.size ?: 0
        shownData.value= rvAdp.itemCount
/*
        inflate(R.layout._sif_comp_btn_action).asNotNull { btn: Button ->
            layoutView.asNotNull { vg: ViewGroup -> vg.addView(btn) }
            btn.text= "Tambah data"
            btn.setOnClickListener {
                for((i, valMap) in transList.last().implementedPropertiesValueMapTree.withIndex()){
                    loge("TransacFrag i= $i prop= ${valMap.first} value= ${valMap.second}")
                }
                val oldData= transList.last()
                val newData= oldData.clone()
                newData.commodity?.name= "Beras Uhuy"

//                rvAdp.addData(newData, isAddedVisible = true)
                rvAdp.addEmptyData(newData)
                loge("TransacFrag rvAdp.addData newData= $newData")
                loge("TransacFrag newData.commodity?.name= ${newData.commodity?.name} oldData.commodity?.name= ${oldData.commodity?.name} transList.size= ${transList.size}")
            }
        }
 */

        loge("Current elapsed time= $timeElapsed sec")
    }

    override fun _initTopView(topView: View) {
        loge("TransacFrag _initTopView()")
        topView.btn.asNotNull { btn: Button ->
            btn.text= "Print All Num Comp"
            btn.setOnClickListener {
                for((i, data) in rvAdp.numPickerComp.dataIterator().withIndex()){
                    loge("Transac i= $i data= $data")
                }
            }
        }
    }
    override fun _initMiddleView(middleView: View) {}

    override fun _initBottomView(bottomView: View) {
        timeElapsed.observe(this){
            bottomView.tv_time.text= it.toString()
        }
        shownData.observe(this){
            bottomView.tv_count_shown.text= it.toString()
        }
        totalData.observe(this){
            bottomView.tv_count.text= it.toString()
        }
    }

    fun initDialog(){
        dialogSort= DialogListView(context!!)
        dialogSort.setTitle("Sort")
        dialogSort.dataList= sortMenu
        dialogSort.setOnItemClickListener { v, pos, data ->
            loge("sort data.id= ${data.id}")
            sortFun= when(data.id){
                "1" -> { data1, data2 ->
//                    data1.commodity!!.name!! < data2.commodity!!.name!!
                    data1.commodity!!.name!!.compareTo(data2.commodity!!.name!!)
                }
                "2" -> { data1, data2 ->
                    val price1= data1.commodity!!.price!! *data1.sum!!
                    val price2= data2.commodity!!.price!! *data2.sum!!
//                    loge("sort() LUAR!!! price1= $price1 price2= $price2")
//                    price1 < price2
                    price1.compareTo(price2)
                }
                "3" -> { data1, data2 ->
//                    data1.status!! < data2.status!!
                    data1.status!!.compareTo(data2.status!!)
                }
                else -> null
            }
        }
        dialogSort.btnListener= object: DialogListView.DialogListBtnListener<StringId>{
            override fun onRightBtnClick(dialog: DialogListView<StringId>, v: View) {
                if(sortFun != null){
                    timeStart= System.currentTimeMillis()
                    rvAdp.sort(comparator = sortFun!!)
                    timeEnd= System.currentTimeMillis()
                    timeElapsed.value= (timeEnd -timeStart)/1000.toDouble()
                    shownData.value= rvAdp.itemCount
                    runOnUiThread {
                        loge("post sort() ========")
                        Log.e("SORT", "post sort() ========")
                        for(i in 0 until rvAdp.itemCount){
                            val trans= rvAdp.getDataAt(i)!!
                            Log.e("SORT", "Harga total i= $i ==> ${trans.commodity!!.price!! *trans.sum!!}")
                            loge("Harga total i= $i ==> ${trans.commodity!!.price!! *trans.sum!!}")
                        }
                    }
                }
                dialog.cancel()
            }
            override fun onLeftBtnClick(dialog: DialogListView<StringId>, v: View) { dialog.cancel() }
        }
        dialogSort.showtBtnAction()
        dialogSort.setRightBtnString("Ok")
        dialogSort.setLeftBtnString("Batal")

        dialogFilter= DialogListView(context!!)
        dialogFilter.setTitle("Filter")
        dialogFilter.dataList = filterMenu
        dialogFilter.setOnItemClickListener { v, pos, data ->
            loge("filter data.id= ${data.id}")
            filterFun= when(data.id){
                "1" ->  { data -> data.orderType == Edc_Const.TYPE_TRANSACTION_BUY }
                "2" ->  { data -> data.orderType == Edc_Const.TYPE_TRANSACTION_SELL }
                "3" ->  { data -> data.status == Edc_Const.STATUS_TRANSACTION_APPROVED }
                "4" ->  { data -> data.status == Edc_Const.STATUS_TRANSACTION_REJECTED }
                else -> null
            }
        }
        dialogFilter.btnListener= object: DialogListView.DialogListBtnListener<StringId>{
            override fun onRightBtnClick(dialog: DialogListView<StringId>, v: View) {
                if(filterFun != null){
                    timeStart= System.currentTimeMillis()
                    rvAdp.filter(filter= filterFun!!)
                    timeEnd= System.currentTimeMillis()
                    timeElapsed.value= (timeEnd -timeStart)/1000.toDouble()
                    shownData.value= rvAdp.itemCount
                }
                dialog.cancel()
            }
            override fun onLeftBtnClick(dialog: DialogListView<StringId>, v: View) { dialog.cancel() }
        }
        dialogFilter.showtBtnAction()
        dialogFilter.setRightBtnString("Ok")
        dialogFilter.setLeftBtnString("Batal")

        dialogDuplicate= DialogConfirmationView(context!!)
        dialogDuplicate.showTitle(false)
        dialogDuplicate.setMessage("Duplikasi data?")
        dialogDuplicate.setBtnRightMsg("Iya")
        dialogDuplicate.setBtnLeftMsg("Batal")
        dialogDuplicate.setOnClickListener(DialogConfirmationView.ButtonKind.RIGHT){
            duplicateData= ArrayList()
            try{
                for((i, data) in rvAdp.dataList!!.withIndex()){
                    loge("dialogDuplicate() i= $i")
                    duplicateData.add(data)
                }
            } catch (e: Exception){}
            duplicateData.growTimely(5)
        }
    }
}