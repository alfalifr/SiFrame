package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.getExtra

/**
 * Kelas yg properti abstraknya dapat di-lateinit
 */
open class SingleFragDrawerAct_BarContentNav_Simple: SingleFragDrawerAct_BarContentNav(){
    override lateinit var fragment: Fragment
    override var startDrawerLayoutId: Int= _Config.INT_EMPTY
    override var endDrawerLayoutId: Int= _Config.INT_EMPTY

    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {}
//    final override fun _initView(layoutView: View) {}

    override fun __initDrawer(rootView: View) {
        startDrawerLayoutId= _sideBase_intent.getExtra(_SIF_Constant.DRAWER_START_LAYOUT_ID, startDrawerLayoutId)!!
        endDrawerLayoutId= _sideBase_intent.getExtra(_SIF_Constant.DRAWER_END_LAYOUT_ID, endDrawerLayoutId)!!
        super.__initDrawer(rootView)
    }

    override fun _initStartDrawerView(startDrawerView: View) {}
    override fun _initEndDrawerView(endDrawerView: View) {}
}

/*
class SingleFragDrawerAct_BarContentNav_Simple: SingleFragDrawerAct_BarContentNav(){
    override lateinit var fragment: Fragment
    override var startDrawerLayoutId: Int= _Config.INT_EMPTY
    override var endDrawerLayoutId: Int= _Config.INT_EMPTY

    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {}
    override fun _initView(layoutView: View) {}

    override fun _initStartDrawerView(startDrawerView: View) {}
    override fun _initEndDrawerView(endDrawerView: View) {}
}
 */