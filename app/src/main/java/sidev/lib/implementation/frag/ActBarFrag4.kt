package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.lifecycle.fragment.ActBarFrag
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.implementation.R

class ActBarFrag4 : ActBarFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt
    override val actBarId: Int
        get() = R.layout.comp_act_bar


    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag4 - Act"
        _ViewUtil.setBgColorTintRes(layoutView, R.color.ijoBiruMuda)
        isActBarViewFromFragment= true
    }

    override fun _initActBar(actBarView: View) {
        _ViewUtil.setBgColorTintRes(actBarView, R.color.unguTua)
        actBarView.tv.text= "$fragTitle _initActBar()"
    }

    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        if(callingLifecycle != null){
            val callName= callingLifecycle::class.java.simpleName
            loge("onActive() callName= $callName")
        }
        actSimple.asNotNull { act: BarContentNavAct ->
            act.setMenu(null)
//            act.setActBarTitle("$fragTitle onActive()")
        }
        actSimple.asNotNull { act: ViewPagerBase<Frag> ->
            loge("act.getFragPos(this)= ${act.getFragPos(this)}")
        }
    }
}