package com.sigudang.android.fragments.BoundProcessFrag.singleProcess.lessee.inbound

import android.view.View
import android.widget.Button
import com.sigudang.android.adapters.BoundProductListAdp.BoundProductListAdp
import com.sigudang.android.fragments.BoundProcessFrag.BottomViewIndex
import com.sigudang.android.fragments.BoundProcessFrag.BoundSingleProcessAbsFrag
import com.sigudang.android.fragments.BoundProcessFrag.OverviewIndex
import com.sigudang.android.fragments.BoundProcessFrag.ProcessBottomNavigationFrag
import kotlinx.android.synthetic.main._simul_sigud_fragment_bound_proses_bottom_btn.view.*
import org.jetbrains.anko.support.v4.toast

class BoundFrag_Lessee_In0Create_3Checkout_ : BoundSingleProcessAbsFrag(){
    override val isOverviewVisible: Boolean
        get() = true
    override val shownColumnInd: Array<OverviewIndex>
        = arrayOf(
            OverviewIndex.DATE,
            OverviewIndex.WAREHOUSE,
            OverviewIndex.PRODUCT
    )
    override val shownBottomInd: Array<BottomViewIndex> = arrayOf(BottomViewIndex.CONFIRM)
    override val shownAdpComponent: Array<AdpCompIndex> = arrayOf()

    override fun initBottomBtnView(bottomView: View) {
        (bottomView.comp_btn_confirm as Button).text= "Kirim permintaan"
        bottomView.comp_btn_confirm.setOnClickListener { toast("Mantab Bro") }
    }

    override fun initBoundProductListAdp(adp: BoundProductListAdp) {}
    override fun _initView(layoutView: View) {}
}