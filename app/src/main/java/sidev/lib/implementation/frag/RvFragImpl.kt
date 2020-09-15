package sidev.lib.implementation.frag

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.comp_nav_arrow.view.*
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.NestedTopMiddleBottomBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.NestedTopMiddleBottomFragmentBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.android.siframe.tool.util._ThreadUtil
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.collection.string
import sidev.lib.collection.toArrayList
import sidev.lib.implementation.R
import sidev.lib.implementation.adp.StrAdp

class RvFragImpl : RvFrag<StrAdp>(), NestedTopMiddleBottomFragmentBase {
    override var topContainer: ViewGroup?= null
    override var middleContainer: ViewGroup?= null
    override var bottomContainer: ViewGroup?= null

/*
    override val topLayoutId: Int
        get() = R.layout.comp_nav_arrow
    override val bottomLayoutId: Int
        get() = R.layout.comp_nav_arrow_tall
// */
///*
    override var topFragment: Frag?= TopViewFrag()
    override var bottomFragment: Frag?= BottomViewFrag()
// */
    override val isTopContainerNestedInRv: Boolean
        get() = true
    override val isBottomContainerNestedInRv: Boolean
        get() = true

    /*
        override val topContainerId: Int
            get() = _Config.ID_RL_TOP_CONTAINER_OUTSIDE
    // */
    override fun ___initSideBase() {}
    override fun _initTopView(topView: View) { loge("_initTopView() MULAI") }
    override fun _initMiddleView(middleView: View) {}

    override fun initRvAdp(): StrAdp = StrAdp(context!!, null)

    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        super.onActive(parentView, callingLifecycle, pos)

        rv.addOnGlobalLayoutListener {
            loge("onActive() rv.yEndInWindow= ${rv.yEndInWindow} typedTopFragment.layoutView.size.string= ${try{ topFragment?.layoutView?.size?.string} catch (e: Exception){ null }} bottomFragment?.layoutView?.size?.string= ${try{ bottomFragment?.layoutView?.size?.string} catch (e: Exception){ null }}")
        }
    }

    override fun _initView(layoutView: View) {
        rv.addOnGlobalLayoutListener {
            loge("_initView() AWAL rv.yEndInWindow= ${rv.yEndInWindow} typedTopFragment.layoutView.size.string= ${try{ topFragment?.layoutView?.size?.string} catch (e: Exception){ null }} bottomFragment?.layoutView?.size?.string= ${try{ bottomFragment?.layoutView?.size?.string} catch (e: Exception){ null }}")
        }
        __initTopMiddleBottomView(layoutView)
        rv.addOnGlobalLayoutListener {
            loge("_initView() rv.yEndInWindow= ${rv.yEndInWindow} typedTopFragment.layoutView.size.string= ${try{ topFragment?.layoutView?.size?.string} catch (e: Exception){ null }} bottomFragment?.layoutView?.size?.string= ${try{ bottomFragment?.layoutView?.size?.string} catch (e: Exception){ null }}")
        }
        val data= arrayOf(
            "Data 1",
            "Data 2"
///*
            ,"Data 3",
            "Data 4"
///*
            ,"Halo"
/*
            ,"Bro"
/// *
            ,"Hoho"
/// *
            ,"Hihe 8"
/// *
            ,"Hihe",
            "Hihe 10",
            "Hihe 11",
            "Hihe",
            "Hihe"
/ *
            ,"Hihe"
/ *
            ,"Hihe",
            "Hihe",
            "Hihe",
            "Hihe"
// */
        )
        rvAdp.dataList= data.toArrayList() //ArrayList(data.toList())

        layoutView.addOnGlobalLayoutListener {
            loge("globalLayoutListener RVFragImpl")

            for(i in 0 until rvAdp.itemCount){
                loge("rvAdp.itemCount iteraste i= $i rvAdp.getDataAt(i)= ${rvAdp.getDataAt(i)} rvAdp.getDataProcessedIndex(i, true)= ${rvAdp.getDataProcessedIndex(i, true)}")
            }
/*
            for((i, dataInd) in rvAdp.contentArranger.resultInd){
                loge("rvAdp.itemCount iteraste i= $i dataInd = $dataInd")
            }
 */
            for(i in 0 until rvAdp.itemCount){
                val dataPos= rvAdp.getDataProcessedIndex(i, true)
                val rawPos= if(dataPos != null) rvAdp.getRawAdpPos(dataPos) else null
                loge("rvAdp.itemCount iteraste i= $i dataPos= $dataPos rawPos= $rawPos rawPos == i => ${rawPos == i}")
            }
            loge("rvAdp.footerView == null => ${rvAdp.footerView == null}")
        }

        showLoading()
        _ThreadUtil.delayRun(3000){
            scrollToPosition(6)
            showLoading(false)
        }
    }

    override fun _initBottomView(bottomView: View) {
        loge("_initBottomView() MULAI")
        bottomView.iv_arrow_forth.setOnClickListener {
            loge("this.bottomView != null => ${this.bottomView != null} rvAdp.footerView == null => ${rvAdp.footerView == null} (bottomContainer as? ViewGroup)?.childCount= ${(bottomContainer as? ViewGroup)?.childCount}")
        }
    }
}