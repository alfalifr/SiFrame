package sidev.lib.implementation._simulation.edc.frag

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import id.go.surabaya.ediscont.utilities.customview.DialogConfirmationView
import kotlinx.android.synthetic.main.comp_performance_bar.view.*
import org.jetbrains.anko.support.v4.runOnUiThread
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.arch.viewmodel.LifeData
import sidev.lib.android.siframe.model.StringId
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.view.tool.dialog.DialogListView
import sidev.lib.implementation.R
import sidev.lib.implementation._cob.dum_transaction
import sidev.lib.implementation._simulation.edc.adp.TransacAdp
import sidev.lib.implementation._simulation.edc.model.Transaction
import sidev.lib.implementation._simulation.edc.util.Edc_Const
import sidev.lib.universal.`fun`.*

class TransacFrag : RvFrag<TransacAdp>(), TopMiddleBottomBase{
    override var topContainer: View?= null
    override var middleContainer: View?= null
    override var bottomContainer: View?= null

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

    lateinit var dialogSort: DialogListView
    lateinit var dialogFilter: DialogListView
    lateinit var dialogDuplicate: DialogConfirmationView
    var sortFun: ((Int, Transaction, Int, Transaction) -> Boolean)?= null
    var filterFun: ((Int, Transaction) -> Boolean)?= null


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
                _ViewUtil.setColor(iv, R.color.putih)
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
        val transList= dum_transaction.copyGrowExponentially(8) //dum_transaction.toArrayList()
        rvAdp.dataList= transList as ArrayList
        timeEnd= System.currentTimeMillis()
        timeElapsed.value= (timeEnd -timeStart)/1000.toDouble()
        totalData.value= rvAdp.dataList?.size ?: 0
        shownData.value= rvAdp.itemCount

        loge("Current elapsed time= $timeElapsed sec")
    }

    override fun _initTopView(topView: View) {}
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
        dialogSort.updateData(sortMenu)
        dialogSort.setOnItemClickListener { v, pos, data ->
            loge("sort data.id= ${data.id}")
            sortFun= when(data.id){
                "1" -> { pos1, data1, pos2, data2 ->
                    data1.commodity!!.name!! < data2.commodity!!.name!!
                }
                "2" -> { pos1, data1, pos2, data2 ->
                    val price1= data1.commodity!!.price!! *data1.sum!!
                    val price2= data2.commodity!!.price!! *data2.sum!!
//                    loge("sort() LUAR!!! price1= $price1 price2= $price2")
                    price1 < price2
                }
                "3" -> { pos1, data1, pos2, data2 ->
                    data1.status!! < data2.status!!
                }
                else -> null
            }
        }
        dialogSort.btnListener= object: DialogListView.DialogListBtnListener{
            override fun onRightBtnClick(dialog: DialogListView, v: View) {
                if(sortFun != null){
                    timeStart= System.currentTimeMillis()
                    rvAdp.sort(func = sortFun!!)
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
            override fun onLeftBtnClick(dialog: DialogListView, v: View) { dialog.cancel() }
        }
        dialogSort.showtBtnAction()
        dialogSort.setRightBtnString("Ok")
        dialogSort.setLeftBtnString("Batal")

        dialogFilter= DialogListView(context!!)
        dialogFilter.setTitle("Filter")
        dialogFilter.updateData(filterMenu)
        dialogFilter.setOnItemClickListener { v, pos, data ->
            loge("filter data.id= ${data.id}")
            filterFun= when(data.id){
                "1" ->  { pos, data -> data.orderType == Edc_Const.TYPE_TRANSACTION_BUY }
                "2" ->  { pos, data -> data.orderType == Edc_Const.TYPE_TRANSACTION_SELL }
                "3" ->  { pos, data -> data.status == Edc_Const.STATUS_TRANSACTION_APPROVED }
                "4" ->  { pos, data -> data.status == Edc_Const.STATUS_TRANSACTION_REJECTED }
                else -> null
            }
        }
        dialogFilter.btnListener= object: DialogListView.DialogListBtnListener{
            override fun onRightBtnClick(dialog: DialogListView, v: View) {
                if(filterFun != null){
                    timeStart= System.currentTimeMillis()
                    rvAdp.filter(func= filterFun!!)
                    timeEnd= System.currentTimeMillis()
                    timeElapsed.value= (timeEnd -timeStart)/1000.toDouble()
                    shownData.value= rvAdp.itemCount
                }
                dialog.cancel()
            }
            override fun onLeftBtnClick(dialog: DialogListView, v: View) { dialog.cancel() }
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