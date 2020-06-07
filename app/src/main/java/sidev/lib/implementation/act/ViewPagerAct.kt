package sidev.lib.implementation.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.comp_nav_arrow.view.*
import sidev.lib.android.siframe.adapter.ViewPagerFragAdp
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct_ViewPager
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.implementation.R
import sidev.lib.implementation.frag.*

class ViewPagerAct : SimpleAbsBarContentNavAct_ViewPager<SimpleAbsFrag>(), TopMiddleBottomBase {
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
        arrayOf(0, 3, 4)

    override var topContainer: View?= null
    override var middleContainer: View?= null
    override var bottomContainer: View?= null

    override val bottomLayoutId: Int
        get() = R.layout.comp_nav_arrow


    override fun initActBar(actBarView: View) {}
    override fun initNavBar(navBarView: BottomNavigationView) {}
    override fun initView(layoutView: View) {
        _initTopMiddleBottomView(layoutView)
    }

    override fun initBottomView(bottomView: View) {
        bottomView.iv_arrow_back.setOnClickListener { pageBackward() }
        bottomView.iv_arrow_forth.setOnClickListener { pageForth() }
    }
}
