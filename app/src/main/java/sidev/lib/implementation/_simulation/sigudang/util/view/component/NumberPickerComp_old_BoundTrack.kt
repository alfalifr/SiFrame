package com.sigudang.android.utilities.view.component
/*
import android.content.Context
import android.view.View
import com.sigudang.android.R
import kotlinx.android.synthetic.main.item_bound_track_amount.view.*

class NumberPickerComp_old_BoundTrack(c: Context, v: View?) : NumberPickerCompOld(c, v){
    override val layoutId: Int
        get() = R.layout.item_bound_track_amount

    var isAmountFixed= true
        set(v){
            field= v
            enableSelection(!v)
            if(view != null){
                var counterBtnVis= View.GONE
                var unitVis= View.VISIBLE
                if(!v){
                    counterBtnVis= View.VISIBLE
                    unitVis= View.GONE
                }
                view!!.iv_minus.visibility= counterBtnVis
                view!!.iv_plus.visibility= counterBtnVis
                view!!.tv_unit.visibility= unitVis
            }
        }
}

 */