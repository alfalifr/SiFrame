package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import org.jetbrains.anko.textColorResource
import sidev.lib.android.std._val._ColorRes
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.DrawerFrag
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.implementation.R

class Frag4 : DrawerFrag(){
///*
    override val contentLayoutId: Int
        get() = R.layout.frag_txt
    override val startDrawerLayoutId: Int
        get() = R.layout.comp_drawer_start_iv
    override val endDrawerLayoutId: Int
        get() = DrawerBase.DRAWER_LAYOUT_SAME_AS_EXISTING //_Config.INT_EMPTY

    override fun _initStartDrawerView(startDrawerView: View) {
        startDrawerView.tv.text= "Ini teks sebenarnya StartDrawer dari Frag4"
        startDrawerView.tv.textColorResource= _ColorRes.TEXT_LIGHT
        _ViewUtil.setBgColorTintRes(startDrawerView.parent as View, R.color.ijo)
    }

    override fun _initEndDrawerView(endDrawerView: View) {
        endDrawerView.tv.text= "Ini teks sebenarnya EndDrawer dari Frag4"
        endDrawerView.tv.textColorResource= _ColorRes.TEXT_LIGHT
        _ViewUtil.setBgColorTintRes(endDrawerView.parent as View, R.color.merah)
    }
// */
/*
    override val layoutId: Int
        get() = R.layout.frag_txt
// */

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag4"
        _ViewUtil.setBgColorTintRes(layoutView, R.color.kuningMuda)
/*
        if(actSimple is SingleFragDrawerActBase){
            (actSimple as SingleFragDrawerActBase)._reinitEndDrawerView { drawer, startDrawer ->
                startDrawer.tv.text= "Ini teks sebenarnya StartDrawer dari Frag4"
                startDrawer.tv.textColorResource= _ColorRes.TEXT_LIGHT
                _ViewUtil.setBgColor(startDrawer.parent as View, R.color.ijo)
            }
        }
// */
    }

    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        loge("Frag4 onActive pos= $pos")
///*
        actSimple.asNotNull { act: BarContentNavAct ->
            act.setActBarTitle("Frag 4 bro!!")
        }
// */
        callingLifecycle.asNotNull { act: ViewPagerBase<*> ->
            act.isVpTitleFragBased= true
            loge("onActive() act.isVpTitleFragBased= true")
        }
    }
}