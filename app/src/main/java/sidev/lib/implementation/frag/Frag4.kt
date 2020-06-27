package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import org.jetbrains.anko.textColorResource
import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.SingleFragDrawerActBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull

class Frag4 : SimpleAbsFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag4"
        _ViewUtil.setBgColor(layoutView, R.color.kuningMuda)

        if(actSimple is SingleFragDrawerActBase){
            (actSimple as SingleFragDrawerActBase)._reinitStartDrawerView { drawer, startDrawer ->
                startDrawer.tv.text= "Ini teks sebenarnya StartDrawer dari Frag4"
                startDrawer.tv.textColorResource= _ColorRes.TEXT_LIGHT
                _ViewUtil.setBgColor(startDrawer.parent as View, R.color.ijo)
            }
        }
    }

    override fun onActive(parentView: View, callingLifecycle: LifecycleViewBase?, pos: Int) {
        callingLifecycle.asNotNull { act: ViewPagerBase<*> ->
            act.isVpTitleFragBased= true
            loge("onActive() act.isVpTitleFragBased= true")
        }
        actSimple.asNotNull { act: SimpleAbsBarContentNavAct ->
            act.setActBarTitle("Frag 4 bro!!")
        }
    }
}