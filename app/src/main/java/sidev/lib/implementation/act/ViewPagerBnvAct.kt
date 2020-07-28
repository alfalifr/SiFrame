package sidev.lib.implementation.act

import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.comp_nav_arrow.view.*
//import sidev.lib.android.siframe.intfc.customview.ModableView
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct_ViewPager
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.implementation.R
import sidev.lib.implementation.frag.*

class ViewPagerBnvAct : BarContentNavAct_ViewPager<Frag>(), TopMiddleBottomBase {
    override var vpFragList: Array<Frag> =
        arrayOf(
            Frag1(),
            Frag2(),
            Frag3(),
            Frag4()
        )
    override var vpFragListStartMark: Array<Int> =
        arrayOf(0, 3, 4)

    override var topContainer: ViewGroup?= null
    override var middleContainer: ViewGroup?= null
    override var bottomContainer: ViewGroup?= null

    override var menuId: Int?= R.menu.menu_3

    override val bottomLayoutId: Int
        get() = R.layout.comp_nav_arrow


    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {
        setupVpWithNavBar(navBarView)
    }
    override fun _initView(layoutView: View) {
        __initTopMiddleBottomView(layoutView)
    }

    override fun _initBottomView(bottomView: View) {
        bottomView.iv_arrow_back.setOnClickListener { pageBackward() }
        bottomView.iv_arrow_forth.setOnClickListener { pageForth() }
    }
    override fun _initTopView(topView: View) {}
    override fun _initMiddleView(middleView: View) {}
}
