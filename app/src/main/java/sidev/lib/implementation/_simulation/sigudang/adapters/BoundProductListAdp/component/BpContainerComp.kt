package com.sigudang.android.adapters.BoundProductListAdp.component
///*
import android.content.Context
import android.view.View
import com.sigudang.android.adapters.BoundProductListAdp.nested.BpContainerAdp
import com.sigudang.android.models.BoundProduct
import kotlinx.android.synthetic.main._simul_sigud_item_product_bound_with_addition.view.*
//import sidev.lib.android.siframe.arch.value.BoxedVal
import sidev.lib.android.siframe.view.comp.ViewComp
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.structure.data.BoxedVal

//import com.sigudang.android.utilities.view.component.ViewComp

class BpContainerComp(c: Context) : ViewComp<BpContainerAdp, BoundProduct>(c){
    override val viewLayoutId: Int
        get() = R.layout._simul_sigud_item_product_bound_with_addition
    override val compId: Int
        get() = R.id.ll_container_container
    override val isDataRecycled: Boolean
        get() = true
    override val isViewSaved: Boolean
        get() = true

    override fun bindComponent(
        adpPos: Int,
        v: View,
        valueBox: BoxedVal<BpContainerAdp>,
        additionalData: Any?,
        inputData: BoundProduct?
    ) {
        valueBox.value.notNull { adp ->
            adp.dataList= inputData?.boundContainer
            adp.rv= v.rv_container
            adp.rv!!.recycledViewPool.setMaxRecycledViews(adp.itemLayoutId, 6)
            adp.maxContainerSum= inputData?.amount ?: -1
        }
    }

    override fun initData(dataPos: Int, inputData: BoundProduct?): BpContainerAdp? {
        return BpContainerAdp(ctx)
    }
}

// */