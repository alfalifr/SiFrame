package sidev.lib.implementation.frag

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFromFragBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerActBase
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.lifecycle.fragment.SimpleActBarFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.notNull

class ActBarFrag4 : SimpleActBarFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt
    override val actBarId: Int
        get() = R.layout.comp_act_bar


    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag4 - Act"
        _ViewUtil.setBgColor(layoutView, R.color.ijoBiruMuda)
        isActBarViewFromFragment= true
    }

    override fun _initActBar(actBarView: View) {
        _ViewUtil.setBgColor(actBarView, R.color.unguTua)
        actBarView.tv.text= fragTitle
    }

    override fun onActive(parentView: View, callingLifecycle: LifecycleBase?, pos: Int) {
        if(callingLifecycle != null){
            val callName= callingLifecycle::class.java.simpleName
            loge("onActive() callName= $callName")
        }
        actSimple.asNotNull { act: SimpleAbsBarContentNavAct -> act.setMenu(null) }
        actSimple.asNotNull { act: ViewPagerActBase<SimpleAbsFrag> ->
            loge("act.getFragPos(this)= ${act.getFragPos(this)}")
        }
    }
}