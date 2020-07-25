package sidev.lib.implementation.frag

import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.`fun`.startSingleFragAct_config
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull

class Frag5 : Frag(){
    override val layoutId: Int
        get() = R.layout.frag_txt

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag5"
        _ViewUtil.setBgColorRes(layoutView, R.color.ijoRumput)
        (layoutView.btn as Button).text= "Ke CrashFrag"
        layoutView.btn.visibility= View.VISIBLE
        layoutView.btn.setOnClickListener { startSingleFragAct_config<CrashFrag>() }
    }

    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        loge("Frag5 onActive pos= $pos")
        callingLifecycle.asNotNull { act: ViewPagerBase<*> ->
            act.isVpTitleFragBased= false
            loge("onActive() act.isVpTitleFragBased= false")
        }
    }
}