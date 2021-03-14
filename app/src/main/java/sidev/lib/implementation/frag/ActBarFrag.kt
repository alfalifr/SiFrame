package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.comp_act_bar.view.*
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFromFragBase
import sidev.lib.android.siframe.lifecycle.fragment.ActBarFrag
import sidev.lib.android.std.`val`._Config
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.console.prine
import sidev.lib.implementation.R

class ActBarFrag : ActBarFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt
    override val actBarId: Int
        get() = R.layout.comp_act_bar


    override fun _initView(layoutView: View) {
        _ViewUtil.setBgColorTintRes(layoutView, R.color.biruLangit)
        /*actSimple.asNotNull { act: ActBarFromFragBase ->
            act.isActBarViewFromFragment= true
        }
         */
        prine("halo")
        loge("ActBarFrag _initView()")
/*
            .asNotNull { act: MultipleActBarViewPagerActBase<*> ->
            loge("act.isVpTitleFragBased= true")
            act.isVpTitleFragBased= true
        }
 */
    }

    override fun _initActBar(actBarView: View) {
        loge("ActBarFrag _initActBar()")
        actBarView.tv.text= ActBarFrag::class.java.simpleName
    }
}