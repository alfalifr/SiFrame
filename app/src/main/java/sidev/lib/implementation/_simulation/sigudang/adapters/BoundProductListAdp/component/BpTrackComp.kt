package com.sigudang.android.adapters.BoundProductListAdp.component
///*
import android.content.Context
import android.util.Log
import android.util.SparseIntArray
import android.view.View
import android.widget.Button
import androidx.core.util.set
import com.sigudang.android.adapters.BoundProductListAdp.nested.BpTrackAdp
import com.sigudang.android.models.BoundProduct
//import com.sigudang.android.utilities.view.component.NumberPickerCompOld
import kotlinx.android.synthetic.main._simul_sigud_item_product_bound_with_addition.view.*
//import com.sigudang.android.utilities.view.component.NumberPickerComp_old
import sidev.lib.android.siframe.view.comp.ViewComp
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.structure.data.BoxedVal


class BpTrackComp(c: Context) : ViewComp<BpTrackAdp, BoundProduct>(c){
    override val viewLayoutId: Int
        get() = R.layout._simul_sigud_item_product_bound_with_addition
    override val compId: Int
        get() = R.id.rl_track_container
    override val isDataRecycled: Boolean
        get() = true

    val progressList= SparseIntArray()

    override fun bindComponent(
        adpPos: Int,
        v: View,
        valueBox: BoxedVal<BpTrackAdp>,
        additionalData: Any?,
        inputData: BoundProduct?
    ) {
        valueBox.value.notNull { trackAdp ->
//            trackAdp= BoundTrackAmountAdp(ctx)
            trackAdp.rv= v.rv_track
            trackAdp.rv!!.recycledViewPool.setMaxRecycledViews(trackAdp.itemLayoutId, 6)
            trackAdp.upperInt= inputData?.amount ?: 0 //.setIntBorder(inputData?.amount ?: 0, 0, 0)
//            trackAdp.numberPicker
/*
                .onNumberChangeListener= { pos, old, new ->
                progressList[position]= new
                inputData?.boundTrack?.progres= new
            }
 */
 /*
            trackAdp.setOnAmountTrackChangeListener_Comp { changeAmount, totalAmount, pos, direction ->
                if(pos == 1){
                    Log.e("BoundProductItemComp", "data?.product?.name = ${inputData?.product?.name} direction= $direction changeAmount= $changeAmount, totalAmount= $totalAmount, pos= $pos,")
                    if(direction != NumberPickerCompOld.Enum.INIT){ // || isTrackForceSetNumber){
                        progressList[position]= totalAmount
//                        data?.boundTrack?.progres= totalAmount //+1 // --> Msh blum tau knp kok bisa berkurang 1
//                        onTrackSingleChangeListener?.onTrackSingleChange(changeAmount, totalAmount)
//                        loge(isTrackSealed, "isTrackSealed")
                        Log.e("BoundProductItemComp", "totalAmount= $totalAmount data?.boundTrack? (progres= ${inputData?.boundTrack?.progres} total= ${inputData?.boundTrack?.total} diff= ${inputData?.boundTrack?.diff})")
                    }
/*
                    if(isTrackSealed && totalAmount > 0){
                        isTrackSealed= false
                        loge(totalAmount, "totalAmount")
                    }
// */
                }
            }
// */
            val trackAddBtn= v.findViewById<Button>(R.id.comp_btn_add_track)
//            isAmountTrackFixed= isAmountTrackFixed
//            isTrackVisible= isTrackVisible
/*
            trackAddBtn.setOnClickListener { v ->
                ctx.toast("trackAddBtn clicked")
                when(ctx){
                    is Activity -> ctx.finish()
                    is Fragment -> ctx.activity!!.finish()
                }
            }
// */
            trackAddBtn.text= "Scan Produk"
            trackAddBtn.visibility= View.GONE
//            isTrackSealed= isTrackSealed
        }
    }

    override fun initData(dataPos: Int, inputData: BoundProduct?): BpTrackAdp? {
        return BpTrackAdp(ctx)
    }
}

// */