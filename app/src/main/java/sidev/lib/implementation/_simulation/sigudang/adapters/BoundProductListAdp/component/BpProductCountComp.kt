package com.sigudang.android.adapters.BoundProductListAdp.component

import android.content.Context
import android.view.View
import android.widget.Button
import com.sigudang.android.adapters.BoundProductListAdp.BoundProductListAdp
import com.sigudang.android.models.BoundProduct
import com.sigudang.android.utilities.view.component.NumberPickerComp
import kotlinx.android.synthetic.main._simul_sigud_content_item_product_bound.view.*
import sidev.lib.`val`.Assignment
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.siframe.view.comp.data.NumberPickerData
import sidev.lib.check.asNotNull
import sidev.lib.implementation.R
import sidev.lib.structure.data.value.Val

class BpProductCountComp(c: Context) : NumberPickerComp<BoundProduct>(c){
    override val compId: Int
        get() = R.id.content_product_info

    init{
        onNumberChangeListener= { pos, old, new, assign ->
            loge("BpProductCountComp onNumberChangeListener pos= $pos old= $old new= $new assign= $assign")
            if(assign != Assignment.INIT){
                rvAdp.asNotNull { adp: BoundProductListAdp -> adp.setProductAmountAt(pos, new) }
                rvAdp?.getView(pos)?.tv_amount_number?.text= new.toString()
            }
        }
    }


    override fun getDefaultInitNumber(dataPos: Int, inputData: BoundProduct?): Int = inputData?.amount ?: 0

    override fun bindComponent(
        adpPos: Int,
        v: View,
        valueBox: Val<NumberPickerData>,
        additionalData: Any?,
        inputData: BoundProduct?
    ) {
        super.bindComponent(adpPos, v, valueBox, additionalData, inputData)
        val isProductSelected= additionalData == true
                || valueBox.value?.number ?: 0 > 0
        v.comp_btn_add_amount.asNotNull { btn: Button ->
            btn.text= "Tambah"
            btn.visibility= if(isCompVisible){
                if(isProductSelected) View.GONE else View.VISIBLE
            } else View.GONE
            setAdditionalDataAt(getDataPosition(adpPos), !isProductSelected)
        }
        v.comp_number_picker.visibility= if(isCompVisible){
            if(isProductSelected) View.VISIBLE else View.GONE
        } else View.GONE
    }

    override fun setComponentEnabled(position: Int, v: View?, enable: Boolean) {
        super.setComponentEnabled(position, v, enable)
        val vis= if(enable) View.VISIBLE else View.GONE

        v?.comp_number_picker?.visibility= vis
        v?.comp_btn_add_amount?.visibility= vis
    }

    override fun setComponentVisible(adpPos: Int, v: View?, visible: Boolean) {
        val vis= if(visible) View.VISIBLE else View.GONE
        loge("v?.comp_number_picker == null => ${v?.comp_number_picker == null} visible= $visible adpPos= $adpPos")
        v?.comp_number_picker?.visibility= vis
        v?.comp_btn_add_amount?.visibility= vis
    }
}