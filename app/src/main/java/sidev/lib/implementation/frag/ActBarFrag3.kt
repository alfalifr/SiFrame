package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import org.jetbrains.anko.textColorResource
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerBase
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.ActBarFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.notNull

class ActBarFrag3 : ActBarFrag(){
    override val layoutId: Int
        get() = R.layout.frag_txt
    override val actBarId: Int
        get() = R.layout.comp_act_bar


    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag3"
        _ViewUtil.setBgColor(layoutView, R.color.merah)
        isActBarViewFromFragment= true
    }

    override fun _initActBar(actBarView: View) {
        _ViewUtil.setBgColor(actBarView, R.color.biruLangit)
        actBarView.tv.text= fragTitle
    }

    override fun onActive(parentView: View, callingLifecycle: LifecycleViewBase?, pos: Int) {
        actSimple.asNotNull { act: BarContentNavAct ->
            act.setMenu(R.menu.menu_1)
            act.setActBarTitle("$fragTitle onActive()")
        }
        actSimple.asNotNull { act: DrawerActBase ->
            context!!.inflate(R.layout.comp_drawer_start, act.endDrawerContainer).notNull { v ->
                v.tv.textColorResource= R.color.putih
                _ViewUtil.setBgColor(v, R.color.hitam)
                _ViewUtil.setBgColor(act.endDrawerContainer, R.color.unguTua)
                act.setDrawerView(DrawerBase.Type.DRAWER_END, v)
            }
            _ViewUtil.setBgColor(act.startDrawerContainer, R.color.hitam)
        }
    }
}