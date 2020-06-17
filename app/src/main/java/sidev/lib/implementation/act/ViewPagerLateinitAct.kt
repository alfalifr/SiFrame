package sidev.lib.implementation.act

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.comp_nav_arrow.view.*
import sidev.lib.android.siframe.customizable.view.intfc.ModableView
//import sidev.lib.android.siframe.intfc.customview.ModableView
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct_ViewPager
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.implementation.R
import sidev.lib.implementation.frag.*
import sidev.lib.universal.tool.util.ThreadUtil

class ViewPagerLateinitAct : SimpleAbsBarContentNavAct_ViewPager<SimpleAbsFrag>(), TopMiddleBottomBase {
    override var vpFragList: Array<SimpleAbsFrag> =
        arrayOf()
    override var vpFragListStartMark: Array<Int> =
        arrayOf(0, 3, 4)

    override var topContainer: View?= null
    override var middleContainer: View?= null
    override var bottomContainer: View?= null

    override val bottomLayoutId: Int
        get() = R.layout.comp_nav_arrow


    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {}
    override fun _initView(layoutView: View) {
        __initTopMiddleBottomView(layoutView)
        ThreadUtil.delayRun(2000){
            setFragList(arrayOf(
                ActBarFrag(),
                ActBarFrag2(),
                Frag3(),
                Frag4(),
                Frag5(),
                ActBarFrag6()
            ))
        }
        ThreadUtil.delayRun(7000){
            setFragList(arrayOf(
                ActBarFrag4(),
                Frag3(),
                ActBarFrag6(),
                Frag4()
            ))
        }
    }

    override fun _initBottomView(bottomView: View) {
        bottomView.iv_arrow_back.setOnClickListener { pageBackward() }
        bottomView.iv_arrow_forth.setOnClickListener { pageForth() }
    }
    override fun _initTopView(topView: View) {}
    override fun _initMiddleView(middleView: View) {}
}
