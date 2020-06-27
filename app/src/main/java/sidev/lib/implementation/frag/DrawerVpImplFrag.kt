package sidev.lib.implementation.frag

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.comp_drawer_start_iv.view.*
import kotlinx.android.synthetic.main.comp_nav_arrow.view.*
import org.jetbrains.anko.imageResource
import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.fragment.DrawerVpFrag
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.lifecycle.fragment.VpFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R

class DrawerVpImplFrag : DrawerVpFrag<SimpleAbsFrag>(), TopMiddleBottomBase{
    override val startDrawerLayoutId: Int
        get() = R.layout.comp_drawer_start_iv
    override val endDrawerLayoutId: Int
        get() = R.layout.comp_drawer_start_iv

    //    override var isVpTitleFragBased: Boolean= true
    override var vpFragList: Array<SimpleAbsFrag> =
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

    override fun _initStartDrawerView(startDrawerView: View) {
        startDrawerView.iv.imageResource= R.drawable.ic_profile
        startDrawerView.tv.text= "_initStartDrawerView() ${this::class.java.simpleName}"
        _ViewUtil.setColor(startDrawerView.iv, _ColorRes.COLOR_LIGHT)
        _ViewUtil.setBgColor(startDrawerView, R.color.unguTua)
    }

    override fun _initEndDrawerView(endDrawerView: View) {
        endDrawerView.iv.imageResource= R.drawable.ic_cross_circle
        endDrawerView.tv.text= "_initEndDrawerView() ${this::class.java.simpleName}"
        _ViewUtil.setColor(endDrawerView.iv, _ColorRes.RED)
        _ViewUtil.setBgColor(endDrawerView, R.color.ijoRumputMuda)
    }

    override fun _initView(layoutView: View) {
        contentLayoutId == R.layout._sif_page_vp
        loge("_initView() contentLayoutId == R.layout._sif_page_vp => ${contentLayoutId == R.layout._sif_page_vp}")
        loge("_sideBase_view.findViewById<View>(_Config.ID_VP) == null => ${_sideBase_view.findViewById<View>(R.id.vp) == null}")
        loge("_sideBase_view.findViewByType<ViewPager>() == null => ${_sideBase_view.findViewByType<ViewPager>() == null}")
        __initTopMiddleBottomView(layoutView)
    }
}