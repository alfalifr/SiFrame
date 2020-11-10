package com.sigudang.android.adapters.BoundProductListAdp.nested
///*
import android.content.Context
import android.util.SparseBooleanArray
import androidx.core.util.set
import androidx.recyclerview.widget.LinearLayoutManager
import com.sigudang.android.models.BoundContainerModel
import com.sigudang.android.utilities.view.component.NumberPickerComp
//import com.sigudang.android.utilities.view.component.NumberPickerCompOld
import kotlinx.android.synthetic.main._simul_sigud_item_bound_track_amount.view.*
///import com.sigudang.android.utilities.view.component.NumberPickerComp
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.siframe.view.comp.data.NumberPickerData
import sidev.lib.check.notNull
import sidev.lib.implementation.R

//import sidev.lib.android.siframe.view.comp.NumberPickerComp

class BpContainerAdp(c: Context) //, dataList: ArrayList<BoundContainerModel>?)
    : RvAdp<BoundContainerModel, LinearLayoutManager>(c){ //TrackProgresAdp<BoundContainerModel>(c, dataList){ //Str2IntAdp<BoundContainerModel>(c, dataList){
    override val itemLayoutId: Int
        get() = R.layout._simul_sigud_item_bound_track_amount
///*
    var maxContainerSum= 3
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
    var overallDiff: Int= 0
        private set
// */
    var isAmountContainerFixed= true
        set(v){
            field= v
            notifyDataSetChanged_()
//            notifyDatasetChanged_()
        }
    private val hasInitIndex= SparseBooleanArray()
    val numberPickerComp= object: NumberPickerComp<BoundContainerModel>(ctx){
        override fun initData(position: Int, inputData: BoundContainerModel?): NumberPickerData?
            = NumberPickerData(inputData?.amount ?: 0, 0, overallDiff)
    }
    init{
        numberPickerComp.setupWithRvAdapter(this)
        var isEditNumber= false
        var posInEdit= -1
///*
        numberPickerComp.onNumberChangeListener= { pos, old, new, assign ->
            if(hasInitIndex[pos, false]){
                val diff= (new -old)
                loge("onNumberChangeListener pos= $pos diff= $diff old= $old new= $new")
                if(posInEdit != pos)
                    getDataAt(pos).notNull {
                        it.amount= new
                        overallDiff -= diff
/*
                        val upper= numberPickerComp.getDataAt(pos)?.upperBorder
                        val lower= numberPickerComp.getDataAt(pos)?.lowerBorder
                        val no= numberPickerComp.getDataAt(pos)?.number
                        loge("onNumberChangeListener Notnull pos= $pos diff= $diff old= $old new= $new overallDiff= $overallDiff upper= $upper lower= $lower no= $no")
 */
                    }
// /*
                if(!isEditNumber){
                    isEditNumber= true
                    posInEdit= pos
                    for(i in 0 until numberPickerComp.savedDataCount){
                        loge("posInEdit= $posInEdit pos= $pos i= $i")
                        if(i != pos){
                            numberPickerComp.setUpperNumberAt(i, numberPickerComp.getDataAt(i)?.number?.plus(overallDiff) ?: -1)
                        }
                    }
                }
                if(posInEdit == pos){
                    isEditNumber= false
                    posInEdit= -1
                }
                loge("posInEdit= $posInEdit pos= $pos isEditNumber= $isEditNumber")
            } else{
                overallDiff -= new
                hasInitIndex[pos]= true
            }
// */
        }
// */
    }

/*
        set(v) {
            field= v
            for(i in 0 until itemCount)
                numberPickerComp.setUpperNumberAt(
                    i,
                    numberPickerComp.getDataAt(i)?.upperBorder?.plus(v)
                        ?: numberPickerComp.defaultUpperBorder
                )
        }
 */

/*
    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: BoundContainerModel) {
        super.bindVH(vh, pos, data)
        val comp= getViewComp(pos)//?.isAmountFixed= isAmountContainerFixed
        comp?.isAmountFixed= isAmountContainerFixed
        comp?.setCount(data.int, true)
        comp?.numberUpperBorder= (comp?.getCount() ?: 0) +overallDiff
//        comp?.enableUp(!isTotalAmountMax, comp.colorDisabled)
    }
// */

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: BoundContainerModel) {
        val v= vh.itemView
        v.tv_title.text= data.name
        numberPickerComp.setNumberAt(pos, data.amount)
        numberPickerComp.setUpperNumberAt(pos, data.amount +overallDiff)
        numberPickerComp.setLowerNumberAt(pos, 0)
        numberPickerComp.setComponentEnabled(pos, v, isAmountContainerFixed)
    }

    override fun setupLayoutManager(context: Context): LinearLayoutManager = LinearLayoutManager(context)

    override fun createEmptyData(): BoundContainerModel? {
        val count= itemCount
        return BoundContainerModel("id$count", "Container DumDum $count")
    }
}

// */