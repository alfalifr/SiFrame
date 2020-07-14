package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.lifecycle.fragment.ActBarFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.implementation.R

class Frag1 : ActBarFrag(){
    override val actBarId: Int
        get() = R.layout.comp_act_bar
    override val layoutId: Int
        get() = R.layout.frag_txt


    override fun _initActBar(actBarView: View) {}

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag1"
        _ViewUtil.setBgColorRes(layoutView, R.color.ijoRumputMuda)
        arguments
    }
}