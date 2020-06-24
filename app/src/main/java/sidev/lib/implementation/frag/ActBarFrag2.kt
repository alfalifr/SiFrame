package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.comp_act_bar.view.*
import kotlinx.android.synthetic.main.frag_txt.view.*
import kotlinx.android.synthetic.main.frag_txt.view.tv
import org.jetbrains.anko.textColorResource
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFromFragBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.lifecycle.fragment.SimpleActBarFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.notNull

class ActBarFrag2 : SimpleActBarFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt
    override val actBarId: Int
        get() = _Config.INT_EMPTY //R.layout.comp_act_bar


    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag2"
        _ViewUtil.setBgColor(layoutView, R.color.ijoRumputMuda)
        isActBarViewFromFragment= true
    }

    override fun _initActBar(actBarView: View) {
        _ViewUtil.setBgColor(actBarView, R.color.ijo)
        actBarView.tv.text= fragTitle
    }

    override fun onActive(parentView: View, callingLifecycle: LifecycleBase?, pos: Int) {
        actSimple.asNotNull { act: DrawerActBase ->
            act.setDrawerView(DrawerActBase.Type.DRAWER_END, null)
            _ViewUtil.setBgColor(act.startDrawerContainer, R.color.biruLaut)
        }
    }
}