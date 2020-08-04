package com.sigudang.android.adapters.BoundProductListAdp.nested
///*
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
//import com.sigudang.android.adapters.BoundProductSendAdp_WarehouseOld
import com.sigudang.android.models.BoundProductSendModel
import kotlinx.android.synthetic.main._simul_sigud_component_bar_fill.view.*
import kotlinx.android.synthetic.main._simul_sigud_content_item_product_bound_send_overview.view.*
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.isZero

/**
 * <9 Juli 2020> => Copy-an dari [BoundProductSendAdp_WarehouseOld].
 */
class BpSendAdp_Warehouse(c: Context)
    : BpSendAdp_Abs(c){ //: SimpleAbsRecyclerViewAdapter<BoundProductSendModel, LinearLayoutManager>(c, dataList){
    override val itemLayoutId: Int
        get() = R.layout._simul_sigud_content_item_product_bound_send_overview

    var isReceiptVIsible= false
        set(v){
            field= v
            notifyDataSetChanged_()
        }

    init{
        setOnUpdateDataListener { dataArray, pos, kind ->
            val receiptStatusList_old= receiptStatusNotBlankList
            receiptStatusNotBlankList_falseNumber= 0
            receiptStatusNotBlankList= Array(itemCount){
                val bool= try{ receiptStatusList_old[it] }
                catch (e: ArrayIndexOutOfBoundsException){ false }
                if(!bool)
                    receiptStatusNotBlankList_falseNumber++
                bool
            }
        }
    }

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: BoundProductSendModel) {
        val v= vh.itemView

        v.ll_address_container.visibility= if(isSendAddressVisible) View.VISIBLE
            else View.GONE

        v.ll_kind_container.visibility= if(isSendMethodVisible) View.VISIBLE
            else View.GONE

        Log.e("SendAdp_Warehouse", "data.kind?.name= ${data.method?.name} isSendKindVisible= $isSendMethodVisible isSendAddressVisible= $isSendAddressVisible")

        v.tv_address.text= data.address
        v.tv_kind.text= data.method?.name
        if(data.method?.img?.resId != null)
            v.iv_kind.setImageResource(data.method?.img!!.resId!!)
        v.tv_amount_number.text= data.amount.toString()

        v.comp_fill_receipt.visibility=
            if(isReceiptVIsible) View.VISIBLE
            else View.GONE
        v.comp_fill_receipt.et.hint= "Masukan No Resi"
        v.comp_fill_receipt.et.addOnceTextChangedListener (object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
//                modifyDataInnerVarAt(pos){ dataPriv ->
                    data.receipt= s?.toString()
                    val receiptNotBlank= data.receipt?.isNotBlank() ?: false
                    if(receiptNotBlank != receiptStatusNotBlankList[pos]){
                        receiptStatusNotBlankList[pos]= receiptNotBlank
                        if(!receiptNotBlank)
                            receiptStatusNotBlankList_falseNumber++
                        else
                            receiptStatusNotBlankList_falseNumber--
                    }
                    onReceiptChangeListener?.invoke(data.receipt ?: "", pos, !receiptNotBlank, receiptStatusNotBlankList_falseNumber.isZero())
//                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        v.tv_amount_number.text= data.amount.toString()
    }

    var receiptStatusNotBlankList_falseNumber= 0
    var receiptStatusNotBlankList= Array(itemCount){dataList!![it].receipt?.isNotBlank() ?: false}

    var onReceiptChangeListener: ((receipt: String, pos: Int, isBlank: Boolean, isNotBlank_overall: Boolean) -> Unit)?= null

    override fun setupLayoutManager(context: Context): LinearLayoutManager = LinearLayoutManager(context)
}

// */