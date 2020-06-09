package sidev.lib.implementation.act

import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.act_drawer.view.*
import kotlinx.android.synthetic.main.comp_nav_arrow.view.*
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.customizable.view.intfc.ModableView
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.activity.DrawerBarContentNavAct_ViewPager
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.implementation.R
import sidev.lib.implementation.frag.*

class VpDrawerAct : DrawerBarContentNavAct_ViewPager<SimpleAbsFrag>(), TopMiddleBottomBase{
    override var vpFragList: Array<SimpleAbsFrag> =
        arrayOf(
            Frag1(),
            Frag2(),
            Frag3(),
            Frag4(),
            Frag5(),
            Frag6()
        )
    override var vpFragListStartMark: Array<Int> =
        arrayOf(0)

    override val startDrawerLayoutId: Int
        get() = R.layout.comp_drawer_start
    override val endDrawerLayoutId: Int
        get() = _Config.INT_EMPTY

    override var topContainer: View?= null
    override var middleContainer: View?= null
    override var bottomContainer: View?= null

    override val bottomLayoutId: Int
        get() = R.layout.comp_nav_arrow

    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {}
    override fun _initView(layoutView: View) {
        (vp as ModableView).isTouchable= false
        (vp as ModableView).isTouchInterceptable= false
    }

    override fun _initTopView(topView: View) {}
    override fun _initMiddleView(middleView: View) {}
    override fun _initBottomView(bottomView: View) {
        bottomView.iv_arrow_back.setOnClickListener { pageBackward() }
        bottomView.iv_arrow_forth.setOnClickListener { pageForth() }
    }

    override fun __initViewFlow(rootView: View) {
        super.__initViewFlow(rootView)
        __initTopMiddleBottomView(rootView)
    }

    override fun _initStartDrawerView(startDrawerView: View) {
        startDrawerView.tv.text= "Ini teks sebenarnya dari StarDrawer"
        Log.e("VpDrawerAct", "_initStartDrawerView")
    }
    override fun _initEndDrawerView(endDrawerView: View) {
        Log.e("VpDrawerAct", "_initEndDrawerView")
    }
}