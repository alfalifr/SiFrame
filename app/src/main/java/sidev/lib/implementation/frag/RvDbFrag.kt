package sidev.lib.implementation.frag

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.implementation._cob.pageList
import sidev.lib.implementation.adp.PageAdp
import sidev.lib.implementation.model.Page
import sidev.lib.universal.`fun`.*

class RvDbFrag : RvFrag<PageAdp>(){
    override fun initRvAdp(): PageAdp {
        return PageAdp(context!!, null)
    }

    override fun _initView(layoutView: View) {
        rvAdp.dataList= pageList.copyGrowTimely(5) as ArrayList

        inflate(R.layout._sif_comp_btn_action_sm).asNotNull { btn: Button ->
            layoutView.asNotNull { vg: ViewGroup ->
                val lp= RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.setMargins(40, 40, 40, 40)
                btn.layoutParams= lp
                btn.setOnClickListener {
///*
                    for(i in 0 until rvAdp.itemCount){
                        val number= rvAdp.numberPicker.getNumberAt(i)
                        loge("number print numAt i= $i num= $number")
                    }
 // */
                }
                btn.text= "Print Number Picker"
                vg.addView(btn)
            }
        }
    }
}