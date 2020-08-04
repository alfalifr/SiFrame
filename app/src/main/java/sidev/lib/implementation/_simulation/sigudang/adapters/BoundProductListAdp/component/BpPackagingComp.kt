package com.sigudang.android.adapters.BoundProductListAdp.component
///*
import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.sigudang.android.adapters.BoundProductListAdp.nested.BpPackagingAdp
import com.sigudang.android.models.BoundProduct
import kotlinx.android.synthetic.main._simul_sigud_item_product_bound_with_addition.view.*
//import sidev.lib.android.siframe.arch.value.BoxedVal
import sidev.lib.android.siframe.tool.util.`fun`.findView
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.view.comp.ViewComp
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.structure.data.BoxedVal

//import com.sigudang.android.utilities.view.component.ViewComp

class BpPackagingComp(c: Context) : ViewComp<BpPackagingAdp, BoundProduct>(c){
    override val viewLayoutId: Int
        get() = R.layout._simul_sigud_item_product_bound_with_addition
    override val compId: Int
        get() = R.id.ll_packaging_container
    override val isDataRecycled: Boolean
        get() = true

    override fun bindComponent(
        adpPos: Int,
        v: View,
        valueBox: BoxedVal<BpPackagingAdp>,
        additionalData: Any?,
        inputData: BoundProduct?
    ) {
        loge("inputData != null => ${inputData != null}")
        valueBox.value.notNull { adp ->
            adp.dataList= inputData?.boundPackaging
            adp.rv= v.rv_packaging
            adp.rv!!.recycledViewPool.setMaxRecycledViews(adp.itemLayoutId, 6)
        }
    }

    override fun initData(dataPos: Int, inputData: BoundProduct?): BpPackagingAdp? {
        return BpPackagingAdp(ctx)
    }

    override fun setComponentEnabled(adpPos: Int, v: View?, enable: Boolean) {
        v?.findView<CheckBox>(compId).notNull { it.isEnabled= enable }
        dataIterator().forEach { it!!.isCheckEnabled= enable }
    }
}

// */