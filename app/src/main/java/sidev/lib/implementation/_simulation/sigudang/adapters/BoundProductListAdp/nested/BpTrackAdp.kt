package com.sigudang.android.adapters.BoundProductListAdp.nested
///*
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sigudang.android._template.model.Str2IntModel
import com.sigudang.android.utilities.view.component.NumberPickerComp
import kotlinx.android.synthetic.main._simul_sigud_item_bound_track_amount.view.*
import org.jetbrains.anko.textColorResource
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.`fun`.txt
import sidev.lib.android.siframe.view.comp.data.NumberPickerData
import sidev.lib.check.notNull
import sidev.lib.implementation.R

//import sidev.lib.android.siframe.view.comp.NumberPickerComp

class BpTrackAdp(c: Context) : RvAdp<Str2IntModel, LinearLayoutManager>(c){
    override val itemLayoutId: Int
        get() = R.layout._simul_sigud_item_bound_track_amount

    override var dataList: ArrayList<Str2IntModel>?
        get() = fixedDatalist
        set(v) {}

    var upperInt= 10
        set(v){
            field= v
            fixedDatalist[0].int= v
            fixedDatalist[2].int= v -progressInt
            numberPicker.defaultUpperBorder= v
            for((i, data) in numberPicker.dataIterator().withIndex()){
                numberPicker.setUpperNumberAt(i, v)
            }
            numberPicker.getNumberAt(1).notNull {
                numberPicker.setNumberAt(2, v-it)
            }
            numberPicker.setNumberAt(0, v)

        }
    var progressInt= 0 //Atau middleInt
        set(v){
            field= v
            fixedDatalist[1].int= v
//            fixedDatalist[2].int= upperInt -v
        }

    var upperTxt= "outbound"
        set(v){
            field= v
            fixedDatalist[0].txt1= v
        }
    var middleTxt= "keluar"
        set(v){
            field= v
            fixedDatalist[1].txt1= v
        }

    private val fixedColorId= arrayOf(R.color.colorPrimaryDark, R.color.ijo, R.color.merah)
    private val fixedDatalist= ArrayList<Str2IntModel>()
    init{
//        loge("upperInt= $upperInt fixedDatalist.first().int= ${fixedDatalist.first().int}")
        fixedDatalist +
                Str2IntModel(upperTxt, "", upperInt) +
                Str2IntModel(middleTxt, "", progressInt) +
                Str2IntModel("selisih", "", upperInt -progressInt)
        resetDataToInitial()
    }

    private val numberPicker= object: NumberPickerComp<Any>(ctx){
        override fun initData(dataPos: Int, inputData: Any?): NumberPickerData?
            = NumberPickerData(fixedDatalist[dataPos].int, 0, upperInt)
    }

    var onNumberChangeListener: ((pos: Int, old: Int, new: Int) -> Unit)?= null
    init{
        numberPicker.setupWithRvAdapter(this)
        numberPicker.onNumberChangeListener= { pos, old, new, assign ->
            if(pos == 1){
                numberPicker.setNumberAt(2, upperInt -new)
                progressInt= new
            }
            fixedDatalist[pos].int= new
            onNumberChangeListener?.invoke(pos, old, new)
        }
    }

    override fun getItemCount(): Int {
        return fixedDatalist.size
    }

// /*
    override fun getDataAt(pos: Int, onlyShownItem: Boolean, isIndexProcessed: Boolean): Str2IntModel?{
        return fixedDatalist[pos]
    }
// */

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: Str2IntModel) {
        val v= vh.itemView
        v.tv_title.text= data.txt1
        v.tv_title.textColorResource= fixedColorId[pos]
        v.et_number.txt= data.int.toString()

        numberPicker.setUpperNumberAt(pos, upperInt)
        numberPicker.setNumberAt(pos, data.int)
        numberPicker.setComponentEnabled(pos, v, enable = pos == 1)

        v.tv_unit.visibility= if(pos == 2) View.VISIBLE
            else View.GONE
    }

    override fun setupLayoutManager(context: Context): LinearLayoutManager= LinearLayoutManager(context)
}

// */