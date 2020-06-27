package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull

class Frag5 : SimpleAbsFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag5"
        _ViewUtil.setBgColor(layoutView, R.color.ijoRumput)
    }

    override fun onActive(parentView: View, callingLifecycle: LifecycleViewBase?, pos: Int) {
        callingLifecycle.asNotNull { act: ViewPagerBase<*> ->
            act.isVpTitleFragBased= false
            loge("onActive() act.isVpTitleFragBased= false")
        }
    }
}