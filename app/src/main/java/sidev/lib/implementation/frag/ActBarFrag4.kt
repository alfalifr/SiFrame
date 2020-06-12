package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFromFragBase
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.lifecycle.fragment.SimpleActBarFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull

class ActBarFrag4 : SimpleActBarFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt
    override val actBarId: Int
        get() = R.layout.comp_act_bar


    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag4 - Act"
        _ViewUtil.setBgColor(layoutView, R.color.ijoBiruMuda)
        isActBarViewFromFragment= true
    }

    override fun _initActBar(actBarView: View) {
        _ViewUtil.setBgColor(actBarView, R.color.unguTua)
        actBarView.tv.text= fragTitle
    }
}