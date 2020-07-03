package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.comp_nav_arrow.view.*
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.lifecycle.fragment.VpFrag
import sidev.lib.implementation.R

class VpImpl2Frag : VpFrag<Frag>(), TopMiddleBottomBase{
//    override var isVpTitleFragBased: Boolean= true
    override var vpFragList: Array<Frag> =
        arrayOf(
            ActBarFrag(),
            ActBarFrag2(),
            ActBarFrag3(),
            ActBarFrag4(),
            ActBarFrag5(),
            ActBarFrag6()
        )
    override var vpFragListStartMark: Array<Int>
        = arrayOf(0, 3, 4)

    override var topContainer: View?= null
    override var middleContainer: View?= null
    override var bottomContainer: View?= null

    override val bottomLayoutId: Int
        get() = R.layout.comp_nav_arrow

    override fun _initTopView(topView: View) {}
    override fun _initMiddleView(middleView: View) {}
    override fun _initBottomView(bottomView: View) {
        bottomView.iv_arrow_back.setOnClickListener { pageBackward() }
        bottomView.iv_arrow_forth.setOnClickListener { pageForth() }
    }

    override fun _initView(layoutView: View) {
        __initTopMiddleBottomView(layoutView)
    }
}