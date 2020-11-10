package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.check.notNull
import sidev.lib.implementation.R

class Frag3 : Frag(){
    override val layoutId: Int
        get() = R.layout.frag_txt

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag3"
        _ViewUtil.setBgColorTintRes(layoutView, R.color.merahMuda)
    }

    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        loge("Frag3 onActive pos= $pos")
        callingLifecycle.notNull { act ->
            val callName= act::class.java.simpleName
            loge("onActive() callName= $callName")
            for((i, stack) in Thread.currentThread().stackTrace.withIndex()){
                loge("Frag3 onActive() i= $i stack= ${stack.methodName}")
            }
        }
        actSimple.asNotNull { act: BarContentNavAct ->
            act.setActBarTitle("Frag 3 bro!!")
        }
        actSimple.asNotNull { act: ViewPagerBase<Frag> ->
            loge("act.getFragPos(this)= ${act.getFragPos(this)}")
//            loge("Frag3 act.vp == act.lateVp => ${act.vp == act.lateVp}")
        }
        loge("_prop_parentLifecycle == parentFragment => ${_prop_parentLifecycle == parentFragment} _prop_parentLifecycle= $_prop_parentLifecycle parentFragment= $parentFragment")
    }
}