package com.sigudang.android.adapters.BoundProductListAdp.nested
///*
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import androidx.core.util.set
import androidx.recyclerview.widget.LinearLayoutManager
import com.sigudang.android._template.util.T_Util
import com.sigudang.android.adapters.BoundProductListAdp.component.BpSendComp_Lessee
import com.sigudang.android.models.BoundProductSendModel
import com.sigudang.android.utilities.view.component.NumberPickerComp
import kotlinx.android.synthetic.main._simul_edu_comp_nav_modul_item.view.*
import kotlinx.android.synthetic.main._simul_edu_comp_nav_modul_item.view.tv
import kotlinx.android.synthetic.main._simul_sigud_component_bar_dropdown.view.*
import kotlinx.android.synthetic.main._simul_sigud_component_bar_fill.view.*
import kotlinx.android.synthetic.main._simul_sigud_content_item_product_bound_send.view.*
import sidev.lib.`val`.Assignment
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.view.comp.data.NumberPickerData
import sidev.lib.check.notNull
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.sigudang.util.ViewUtil

class BpSendAdp_Lessee(c: Context)
    : BpSendAdp_Abs(c){ //RvAdp<BoundProductSendModel, LinearLayoutManager>(c){
    override val itemLayoutId: Int
        get() = R.layout._simul_sigud_content_item_product_bound_send

    lateinit var bpSendComp_Lesse: BpSendComp_Lessee
    var isAmountFixed= false
        set(v){
            field= v
            notifyDataSetChanged_()
        }
//    protected lateinit var bsSendKindFrag: BsSendKindFr

    var maxSendSum= 0
        set(v){
            field= v
            overallDiff= v
            if(dataList != null){
                for((i, data) in dataList!!.withIndex()){
                    overallDiff -= data.amount
                    loge("maxSendSum set i= $i data.amount= ${data.amount} overallDiff= $overallDiff")
                }
                for((i, dataComp) in numberPickerComp.dataIterator().withIndex()){
                    numberPickerComp.setUpperNumberAt(i, dataComp!!.number +overallDiff)
                }
            }
        }
    var overallDiff= 0
        private set

    private val hasInitIndex= SparseBooleanArray()

    protected val numberPickerComp= object: NumberPickerComp<BoundProductSendModel>(c){
        override val compId: Int
            get() = R.id.comp_number_picker

        override fun initData(position: Int, inputData: BoundProductSendModel?): NumberPickerData?
                = NumberPickerData(inputData?.amount ?: 0, 0, (inputData?.amount ?: 0) +overallDiff)
    }

    init{
/*
        addOnBindViewListener { holder, position, data ->
            loge("Hasilnya type ModEt == null => ${holder.itemView.findViewByType_<ModEt>() == null}")
        }
// */
        numberPickerComp.setupWithRvAdapter(this)
        var isEditNumber= false
        var posInEdit= -1
///*
        numberPickerComp.onNumberChangeListener= { pos, old, new, assingment ->
//            if (hasInitIndex[pos, false]) {
            if(assingment != Assignment.INIT){
                val diff = (new - old)
                loge("onNumberChangeListener pos= $pos diff= $diff old= $old new= $new")
                if (posInEdit != pos)
                    getDataAt(pos).notNull {
                        it.amount = new
                        overallDiff -= diff
///*
                        val upper= numberPickerComp.getDataAt(pos)?.upperBorder
                        val lower= numberPickerComp.getDataAt(pos)?.lowerBorder
                        val no= numberPickerComp.getDataAt(pos)?.number
                        loge("onNumberChangeListener Notnull pos= $pos diff= $diff old= $old new= $new overallDiff= $overallDiff upper= $upper lower= $lower no= $no")
// */
                    }
// /*
                if (!isEditNumber) {
                    isEditNumber = true
                    posInEdit = pos
                    for (i in 0 until numberPickerComp.savedDataCount) {
                        loge("posInEdit= $posInEdit pos= $pos i= $i")
                        if (i != pos) {
                            numberPickerComp.setUpperNumberAt(
                                i,
                                numberPickerComp.getDataAt(i)?.number?.plus(overallDiff) ?: -1
                            )
                        }
                    }
                }
                if (posInEdit == pos) {
                    isEditNumber = false
                    posInEdit = -1
                }
                loge("posInEdit= $posInEdit pos= $pos isEditNumber= $isEditNumber")
            }
//            }
/*
            else {
                loge("onNumberChangeListener pos= $pos else old= $old new= $new")
                overallDiff -= new
                hasInitIndex[pos] = true
            }
 */
        }
    }
/*
    var sendKindDataFull: ArrayList<SendKindModel> = sendKindModel_list_full.toCollection(ArrayList())
        set(v){
            field= v
            bsSendKindFrag.dataList= v
        }
 */
/*
    init{
        bsSendKindFrag= BsSendKindFr()
        bsSendKindFrag.setBtnConfirmText("Pilih")
//        sendKindDataFull= sendKindModel_list_full.toCollection(ArrayList())
    }
 */

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: BoundProductSendModel) {
        val v= vh.itemView

        v.comp_bar_fill_send_method.visibility= if(isSendMethodVisible) View.VISIBLE
            else View.GONE

        v.comp_bar_fill_send_address.visibility= if(isSendAddressVisible) View.VISIBLE
            else View.GONE

        Log.e("SendAdp_Lessee", "data.kind?.name= ${data.method?.name} isSendKindVisible= $isSendMethodVisible isSendAddressVisible= $isSendAddressVisible")

        v.comp_bar_fill_send_method.tv.hint= "--Pilih jenis pengirim--"
        v.comp_bar_fill_send_method.tv.text= data.method?.name ?: ""
        v.comp_bar_fill_send_method.setOnClickListener { v ->
//            val selectSendKindBsFrag= BsSendKindFr()
//            Log.e("SendAdp_Lessee", "data.kind?.name= ${data.kind?.name} bsSendKindFrag.dataList?.size= ${bsSendKindFrag.dataList?.size}")
            bpSendComp_Lesse.bsSendKindFr?.selectItem(data.method)
            bpSendComp_Lesse.bsSendKindFr?.onBsRvBtnClickListener= { vPriv, dataPriv, posPriv ->
                val selectedData= dataPriv //adp.getSelectedData()
                Log.e("SendAdp_Lessee", "selectedData?.name= ${selectedData?.name}")
                if(selectedData != null){
                    ViewUtil.setCompData_dropDown(v.comp_bar_fill_send_method, dataPriv)
                    data.method= selectedData
                }
            }
            bpSendComp_Lesse.bsSendKindFr?.show(T_Util.getFM(ctx)!!, "")
        }
/*
        v.comp_bar_fill_send_kind.btn_bg.setOnClickListener{ vInt1 ->
            val sendKindBs= BsSendKindFr()
            sendKindBs.selectItem(data.kind)
            sendKindBs.onBsRvBtnClickListener= { vInt2, dataInt1, pos ->
                ViewUtil.setCompData_dropDown(v.comp_bar_fill_send_kind, dataInt1)
                modifyDataAt(pos){ dataInt2 ->
                    dataInt2.kind= dataInt1
                    dataInt2
                }
            }
            sendKindBs.show(T_Util.getFM(ctx)!!, "")
        }
 */
        val ivSideText= v.comp_bar_fill_send_method.iv_side_txt
        if(data.method != null && data.method!!.img?.resId != null)
            ivSideText.setImageResource(data.method!!.img!!.resId!!)
        else
            ivSideText.setImageBitmap(null)

        v.comp_bar_fill_send_address.et.hint= "--Masukan alamat--"
        v.comp_bar_fill_send_address.et.setText(data.address)
        v.comp_bar_fill_send_address.et.addOnceTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
//                if(vh.adapterPosition == pos)
                    data.address= s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
/*
        val numberPickerComp= getNumberPicker(pos, v.comp_number_picker)
//        numberPickerComp= NumberPickerComp(ctx, v.comp_number_picker)
        numberPickerComp.listener= object: NumberPickerComp.CompListener{
            override fun onCountChange(
                v: View,
                before: Int,
                after: Int,
                direction: NumberPickerComp.Enum
            ) {
                if(vh.isAdpPositionSameWith(pos)){
                    val diff= after -before
                    loge("diff= after -before diff= $diff after= $after before= $before")
                    Log.e("BoundProductSendAdp_Lessee", "diff= after -before diff= $diff after= $after before= $before")
//                    boundProduct!!.amount += diff
                    if(direction == NumberPickerComp.Enum.PLUS
                        || direction == NumberPickerComp.Enum.MINUS
                        || direction == NumberPickerComp.Enum.SET_TEXT){
                        loge("diff= after -before diff= MASUK $diff after= $after before= $before")
                        Log.e("BoundProductSendAdp_Lessee", "diff= after -before diff= MASUK $diff after= $after before= $before")
                        data.amount += diff
                        onSendAmountChangeListener?.invoke(diff, pos)
                    }
                }
            }
        }
 */
//        val initCount= data.amount
        Log.e("sendAdp_Lessee", "BindVH data.amount= ${data.amount} data.method?.name= ${data.method?.name}")
        numberPickerComp.setNumberAt(pos, data.amount) //.setCount(initCount)
        numberPickerComp.setComponentEnabled(pos, enable = !isAmountFixed) //.enableSelection(!isAmountFixed)
    }

    override fun setupLayoutManager(context: Context): LinearLayoutManager {
        return LinearLayoutManager(context)
    }

    /**
     * @return true jika berhasil
     */
    fun setCountAt(pos: Int, count: Int): Boolean{
        return numberPickerComp.setNumberAt(pos, count)
    }
}

// */