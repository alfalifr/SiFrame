package com.sigudang.android.adapters.BoundProductListAdp.component
///*
import android.content.Context
import android.util.Log
import android.view.View
import com.sigudang.android.adapters.BoundProductListAdp.nested.BpSendAdp_Warehouse
import com.sigudang.android.models.BoundProduct
import kotlinx.android.synthetic.main._simul_sigud_item_product_bound_with_addition.view.*
import sidev.lib.android.siframe.tool.util.idName
import sidev.lib.android.siframe.view.comp.ViewComp
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.structure.data.BoxedVal

//import com.sigudang.android.utilities.view.component.ViewComp

class BpSendComp_Warehouse(c: Context)
    : ViewComp<BpSendAdp_Warehouse, BoundProduct>(c){
    override val viewLayoutId: Int
        get() = R.layout._simul_sigud_item_product_bound_with_addition
    override val compId: Int
        get() = R.id.ll_send_warehouse_container
    override val isDataRecycled: Boolean
        get() = true

    var isSendMethodVisible= true
        set(v){
            if(v != field)
                for(adp in dataIterator()){
                    adp!!.isSendMethodVisible= v
                }
            field= v
        }
    var isSendAddressVisible= true
        set(v){
            if(v != field)
                for(adp in dataIterator()){
                    adp!!.isSendAddressVisible= v
                }
            field= v
        }
/*
    var bsSendKindFr: BsSendKindFr?= null
    var sendKindDataFull: ArrayList<SendKindModel> = sendKindModel_list_full.toCollection(ArrayList())
        set(v){
            field= v
            bsSendKindFr?.dataList= v
        }
// */

    override fun bindComponent(
        adpPos: Int,
        v: View,
        valueBox: BoxedVal<BpSendAdp_Warehouse>,
        additionalData: Any?,
        inputData: BoundProduct?
    ) {
//        loge("BpSendComp_Warehouse bindComponent() adpPos= $adpPos v.idName= ${v.idName}")
//        Log.e("BpSendComp_Warehouse", "BpSendComp_Warehouse bindComponent() adpPos= $adpPos v.idName= ${v.idName} v.visibility == View.VISIBLE => ${v.visibility == View.VISIBLE}")
//        v.visibility= View.VISIBLE
        valueBox.value.notNull { adp ->
            adp.dataList= inputData?.boundSend
            adp.rv= v.rv_send_warehouse
            adp.rv!!.recycledViewPool.setMaxRecycledViews(adp.itemLayoutId, 6)
            adp.isSendMethodVisible= isSendMethodVisible
            adp.isSendAddressVisible= isSendAddressVisible
        }
    }

    override fun initData(dataPos: Int, inputData: BoundProduct?): BpSendAdp_Warehouse? {
        return BpSendAdp_Warehouse(ctx)
    }
/*
    override fun setComponentVisible(adpPos: Int, v: View?, visible: Boolean) {
        super.setComponentVisible(adpPos, v, visible)
        loge("setComponentVisible adpPos= $adpPos visible= $visible")
        v?.visibility= View.VISIBLE
    }
*/
}

// */