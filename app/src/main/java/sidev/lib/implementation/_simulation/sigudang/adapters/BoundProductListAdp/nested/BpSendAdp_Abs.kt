package com.sigudang.android.adapters.BoundProductListAdp.nested
///*
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.sigudang.android.models.BoundProductSendModel
//import com.sigudang.android.utilities.receiver.findViewByType_
import sidev.lib.android.siframe.adapter.RvAdp

//import sidev.lib.android.siframe.view.comp.NumberPickerComp

abstract class BpSendAdp_Abs(c: Context)
    : RvAdp<BoundProductSendModel, LinearLayoutManager>(c){
    init{
        isItemClickEnabled= false
    }

    var isSendMethodVisible= true
        set(v){
            field= v
            notifyDataSetChanged_()
        }
    var isSendAddressVisible= true
        set(v){
            field= v
            notifyDataSetChanged_()
        }



    override val selectFilterFun: ((dataFromList: BoundProductSendModel, dataFromInput: BoundProductSendModel, posFromList: Int) -> Boolean)?
            = { dataFromList, dataFromInput, posFromList ->
        val bool= dataFromList.method?.name == dataFromInput.method?.name
        if(bool)
            modifyDataAt(posFromList){ data ->
                dataFromInput
            }
        bool
    }

    override fun setupLayoutManager(context: Context): LinearLayoutManager {
        return LinearLayoutManager(context)
    }


    /**
     * Pake method ini untuk merubah @see isSendKindVisible dan @see isSendAddressVisible
     * agar lebih teratur view per item di adp
     */
    fun setSendKindAddresVisible(isSendKindVisible: Boolean= this.isSendMethodVisible,
                                 isSendAddressVisible: Boolean= this.isSendAddressVisible){
        this.isSendMethodVisible= isSendKindVisible
        this.isSendAddressVisible= isSendAddressVisible
        notifyDataSetChanged_()
    }

    fun getTotalAmount(): Int{
        var total= 0
        dataList?.forEach { data ->
            total += data.amount
        }
        return total
    }

    public override fun createEmptyData(): BoundProductSendModel? {
        return BoundProductSendModel(null, "")
    }

}

// */