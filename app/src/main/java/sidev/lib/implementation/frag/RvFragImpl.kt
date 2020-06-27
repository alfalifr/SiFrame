package sidev.lib.implementation.frag

import android.view.View
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.implementation.adp.StrAdp

class RvFragImpl : RvFrag<StrAdp>(), TopMiddleBottomBase{
    override var topContainer: View?= null
    override var middleContainer: View?= null
    override var bottomContainer: View?= null
    override val bottomLayoutId: Int
        get() = R.layout.comp_nav_arrow

    override fun ___initSideBase() {}
    override fun _initTopView(topView: View) {}
    override fun _initMiddleView(middleView: View) {}

    override fun initRvAdp(): StrAdp = StrAdp(context!!, null)

    override fun _initView(layoutView: View) {
        __initTopMiddleBottomView(layoutView)
        val data= arrayOf(
            "Data 1",
            "Data 2",
            "Data 3",
            "Data 4",
            "Halo",
            "Bro",
            "Hoho",
// /*
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe"
// */
        )
        rvAdp.dataList= ArrayList(data.toList())
    }

    override fun _initBottomView(bottomView: View) {
        loge("_initBottomView() MULAI")
    }
}