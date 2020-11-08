package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.lifecycle.fragment.ActBarFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.reflex.clazz

class Frag1 : ActBarFrag(){
    override val actBarId: Int
        get() = R.layout.comp_act_bar
    override val layoutId: Int
        get() = R.layout.frag_txt


    override fun _initActBar(actBarView: View) {}

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag1"
        _ViewUtil.setBgColorTintRes(layoutView, R.color.ijoRumputMuda)
        loge("Frag1 _prop_parentLifecycle?.clazz => ${_prop_parentLifecycle?.clazz}")
    }

    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        loge("Frag1 onActive pos= $pos")
    }
}