package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.comp_nav_arrow.view.*
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.NestedTopMiddleBottomBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.implementation.adp.StrAdp
import sidev.lib.universal.`fun`.toArrayList
import sidev.lib.universal.tool.util.ThreadUtil

class RvFragImpl : RvFrag<StrAdp>(), NestedTopMiddleBottomBase {
    override var topContainer: View?= null
    override var middleContainer: View?= null
    override var bottomContainer: View?= null

    override val bottomLayoutId: Int
        get() = R.layout.comp_nav_arrow
    override val topLayoutId: Int
        get() = R.layout.comp_nav_arrow

    override val isTopContainerNestedInRv: Boolean
        get() = true
    override val isBottomContainerNestedInRv: Boolean
        get() = true

    /*
        override val topContainerId: Int
            get() = _Config.ID_RL_TOP_CONTAINER_OUTSIDE
    // */
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
            "Hoho"
// /*
            ,"Hihe 8",
            "Hihe",
            "Hihe 10",
            "Hihe 11",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe",
            "Hihe"
// */
        )
        rvAdp.dataList= data.toArrayList() //ArrayList(data.toList())

        showLoading()
        ThreadUtil.delayRun(3000){
            scrollToPosition(6)
            showLoading(false)
        }
    }

    override fun _initBottomView(bottomView: View) {
        loge("_initBottomView() MULAI")
        bottomView.iv_arrow_forth.setOnClickListener {
            loge("this.bottomView != null => ${this.bottomView != null}")
        }
    }
}