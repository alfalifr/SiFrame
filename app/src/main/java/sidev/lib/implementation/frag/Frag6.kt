package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.implementation.R

class Frag6 : Frag(){
    override val layoutId: Int
        get() = R.layout.frag_txt

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag6"
        _ViewUtil.setBgColorRes(layoutView, R.color.biru)
    }

    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        loge("Frag6 onActive pos= $pos")
        actSimple.asNotNull { act: ViewPagerBase<Frag> ->
            loge("act.getFragPos(this)= ${act.getFragPos(this)}")
//            loge("Frag6 act.vp == act.lateVp => ${act.vp == act.lateVp}")
        }
    }
}