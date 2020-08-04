package com.sigudang.android.fragments.BoundProcessFrag.viewpager_new.lessee.inbound

import android.view.View
import com.sigudang.android.fragments.BoundProcessFrag.BoundVpProcessFrag
import com.sigudang.android.fragments.BoundProcessFrag.singleProcess.lessee.inbound.BoundFrag_Lessee_In0Create_1ProductSelect_
import com.sigudang.android.fragments.BoundProcessFrag.singleProcess.lessee.inbound.BoundFrag_Lessee_In0Create_2WarehouseSelect_
import com.sigudang.android.fragments.BoundProcessFrag.singleProcess.lessee.inbound.BoundFrag_Lessee_In0Create_3Checkout_
import sidev.lib.android.siframe.lifecycle.fragment.Frag

class BoundVp_Lessee_In0Create : BoundVpProcessFrag(){
    override var vpFragList: Array<Frag>
        = arrayOf(
        BoundFrag_Lessee_In0Create_1ProductSelect_(),
        BoundFrag_Lessee_In0Create_2WarehouseSelect_(),
        BoundFrag_Lessee_In0Create_3Checkout_()
    )

    override fun _initView(layoutView: View) {}
}