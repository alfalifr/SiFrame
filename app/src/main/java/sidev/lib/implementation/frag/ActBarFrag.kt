package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.comp_act_bar.view.*
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFromFragBase
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct_BarContentNav
import sidev.lib.android.siframe.lifecycle.fragment.SimpleActBarFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull

class ActBarFrag : SimpleActBarFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt
    override val actBarId: Int
        get() = R.layout.comp_act_bar


    override fun _initView(layoutView: View) {
        _ViewUtil.setBgColor(layoutView, R.color.biruLangit)
        actSimple.asNotNull { act: ActBarFromFragBase ->
            act.isActBarViewFromFragment= true
        }
    }

    override fun _initActBar(actBarView: View) {
        actBarView.tv.text= ActBarFrag::class.java.simpleName
    }
}