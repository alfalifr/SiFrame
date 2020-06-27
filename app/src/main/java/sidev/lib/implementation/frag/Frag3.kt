package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.notNull

class Frag3 : SimpleAbsFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag3"
        _ViewUtil.setBgColor(layoutView, R.color.merahMuda)
    }

    override fun onActive(parentView: View, callingLifecycle: LifecycleViewBase?, pos: Int) {
        callingLifecycle.notNull { act ->
            val callName= act::class.java.simpleName
            loge("onActive() callName= $callName")
            for((i, stack) in Thread.currentThread().stackTrace.withIndex()){
                loge("Frag3 onActive() i= $i stack= ${stack.methodName}")
            }
        }
        actSimple.asNotNull { act: SimpleAbsBarContentNavAct ->
            act.setActBarTitle("Frag 3 bro!!")
        }
        actSimple.asNotNull { act: ViewPagerBase<SimpleAbsFrag> ->
            loge("act.getFragPos(this)= ${act.getFragPos(this)}")
//            loge("Frag3 act.vp == act.lateVp => ${act.vp == act.lateVp}")
        }
    }
}