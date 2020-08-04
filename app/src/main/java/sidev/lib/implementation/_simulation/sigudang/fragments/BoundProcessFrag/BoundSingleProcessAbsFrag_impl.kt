package com.sigudang.android.fragments.BoundProcessFrag
///*
import android.view.View
import com.sigudang.android.adapters.BoundProductListAdp.BoundProductListAdp
import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.implementation._simulation.sigudang.dummy.sendKindModel_list_full
import sidev.lib.universal.tool.util.ThreadUtil

class BoundSingleProcessAbsFrag_impl : BoundSingleProcessAbsFrag(){
    override val isOverviewVisible: Boolean get() = true
    override val shownColumnInd: Array<OverviewIndex> = OverviewIndex.values()
    override val shownBottomInd: Array<BottomViewIndex> = BottomViewIndex.values()
    override val shownAdpComponent: Array<AdpCompIndex> = arrayOf(AdpCompIndex.PRODUCT_COUNT) //AdpCompIndex.values()
    override val editableColumnInd: Array<EditableIndex> = arrayOf(EditableIndex.WAREHOUSE)

    override fun initBottomBtnView(bottomView: View) {}

    override fun initBoundProductListAdp(adp: BoundProductListAdp) {
        adp.sendComp_lessee!!.sendKindDataFull= sendKindModel_list_full.toCollection(ArrayList())
/*
        ThreadUtil.delayRun(7000){
            adp.containerComp?.isCompVisible= true
            loge("adp.containerComp?.isCompVisible= ${adp.containerComp?.isCompVisible}")
        }
*/
//        adp.packagingComp!!.isEnabled= true
    }
    override fun _initView(layoutView: View) {}
}

// */