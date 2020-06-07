package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.implementation.R

class Frag6 : SimpleAbsFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt

    override fun initView(layoutView: View) {
        layoutView.tv.text= "Frag6"
        _ViewUtil.setBgColor(layoutView, R.color.biru)
    }
}