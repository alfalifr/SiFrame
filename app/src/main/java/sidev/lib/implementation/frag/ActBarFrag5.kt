package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import org.jetbrains.anko.textColorResource
import sidev.lib.android.siframe._customizable._ColorRes
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.ActBarFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.check.asNotNull
import sidev.lib.implementation.R

class ActBarFrag5 : ActBarFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt
    override val actBarId: Int
        get() = R.layout.comp_act_bar


    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag5 - Act Bar"
        _ViewUtil.setBgColorRes(layoutView, R.color.biruLaut)
        isActBarViewFromFragment= true
    }

    override fun _initActBar(actBarView: View) {
        _ViewUtil.setBgColorRes(actBarView, R.color.kuningUtama)
        actBarView.tv.text= fragTitle
        actBarView.tv.textColorResource= _ColorRes.TEXT_DARK
    }

    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        actSimple.asNotNull { act: BarContentNavAct -> act.setMenu(R.menu.menu_2) }
    }
}