package com.sigudang.android.adapters.BoundProductListAdp.component
///*
import android.content.Context
import android.view.View
import com.sigudang.android.adapters.BoundProductListAdp.nested.BpSendAdp_Lessee
import com.sigudang.android.fragments.bottomsheet.BsSendKindFr
import com.sigudang.android.models.BoundProduct
import com.sigudang.android.models.SendMethodModel
import com.sigudang.android.utilities.view.component.ViewComp_
import kotlinx.android.synthetic.main._simul_sigud_item_product_bound_with_addition.view.*
import sidev.lib.check.notNull
import sidev.lib.implementation.R
import sidev.lib.structure.data.value.Val

//import sidev.lib.android.siframe.arch.value.BoxedVal

//import com.sigudang.android.utilities.view.component.ViewComp

class BpSendComp_Lessee(c: Context)
    : ViewComp_<BpSendAdp_Lessee, BoundProduct>(c){
    override val viewLayoutId: Int
        get() = R.layout._simul_sigud_item_product_bound_with_addition
    override val compId: Int
        get() = R.id.ll_send_lessee_container
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


    var bsSendKindFr: BsSendKindFr?= null
        private set
    var sendKindDataFull: ArrayList<SendMethodModel>? = null //sendKindModel_list_full.toCollection(ArrayList())
        set(v){
            field= v
            bsSendKindFr= if(v != null)BsSendKindFr()
            else null
            bsSendKindFr?.dataList= v
        }

    override fun bindComponent(
        adpPos: Int,
        v: View,
        valueBox: Val<BpSendAdp_Lessee>,
        additionalData: Any?,
        inputData: BoundProduct?
    ) {
        valueBox.value.notNull { adp ->
            adp.dataList= inputData?.boundSend
            adp.rv= v.rv_send_lessee
            adp.rv!!.recycledViewPool.setMaxRecycledViews(adp.itemLayoutId, 6)
            adp.bpSendComp_Lesse= this
            adp.maxSendSum= inputData?.amount ?: -1
            adp.isSendMethodVisible= isSendMethodVisible
            adp.isSendAddressVisible= isSendAddressVisible
        }
    }

    override fun initData(dataPos: Int, inputData: BoundProduct?): BpSendAdp_Lessee? {
        val adp= BpSendAdp_Lessee(ctx)
        adp.bpSendComp_Lesse= this
        return adp
    }
}

// */