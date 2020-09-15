package com.sigudang.android.fragments.BoundProcessFrag.singleProcess.lessee.inbound

import android.view.View
import android.widget.Button
import com.sigudang.android.adapters.BoundProductListAdp.BoundProductListAdp
import com.sigudang.android.fragments.BoundProcessFrag.*
import kotlinx.android.synthetic.main._simul_sigud_fragment_bound_proses_bottom_btn.view.*
//import kotlinx.android.synthetic.main.fragment_bound_proses_bottom_btn.view.*
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.check.asNotNull

class BoundFrag_Lessee_In0Create_1ProductSelect_: BoundSingleProcessAbsFrag(){
//    override val isOverviewVisible: Boolean
//        get() = false
    override val shownColumnInd: Array<OverviewIndex> = arrayOf(OverviewIndex.PRODUCT_SEARCH, OverviewIndex.WAREHOUSE)
    override val shownBottomInd: Array<BottomViewIndex> = arrayOf(BottomViewIndex.SUMMARY, BottomViewIndex.CONFIRM)
    override val shownAdpComponent: Array<AdpCompIndex> = arrayOf(AdpCompIndex.PRODUCT_COUNT)


    override fun _initView(layoutView: View) {}

    override fun initBottomBtnView(bottomView: View) {
        (bottomView.comp_btn_confirm as Button).text= "Lanjut"
        bottomView.comp_btn_confirm.setOnClickListener {
            _prop_parentLifecycle.asNotNull { vpFrag: ViewPagerBase<*> -> vpFrag.pageForth() }
        }
    }

    override fun initBoundProductListAdp(adp: BoundProductListAdp) {}
}