package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.tv
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerBase
import sidev.lib.android.siframe.lifecycle.fragment.ActBarFrag
import sidev.lib.android.std.`val`._Config
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.check.asNotNull
import sidev.lib.implementation.R

class ActBarFrag2 : ActBarFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt
    override val actBarId: Int
        get() = _Config.INT_EMPTY //R.layout.comp_act_bar


    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag2"
        _ViewUtil.setBgColorTintRes(layoutView, R.color.ijoRumputMuda)
        isActBarViewFromFragment= true
    }

    override fun _initActBar(actBarView: View) {
        _ViewUtil.setBgColorTintRes(actBarView, R.color.ijo)
        actBarView.tv.text= fragTitle
    }

    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        actSimple.asNotNull { act: DrawerActBase ->
            act.setDrawerView(DrawerBase.Type.DRAWER_END, null)
            _ViewUtil.setBgColorTintRes(act.startDrawerContainer, R.color.biruLaut)
        }
    }
}