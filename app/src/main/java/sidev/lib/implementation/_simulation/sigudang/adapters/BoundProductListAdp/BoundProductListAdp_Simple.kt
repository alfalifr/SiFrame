package com.sigudang.android.adapters.BoundProductListAdp
/*
import android.content.Context
import android.view.View
import com.sigudang.android.models.BoundProduct
import com.sigudang.android.utilities.view.component.BoundProductItemCompOld

class BoundProductListAdp_Simple(ctx: Context, dataList: ArrayList<BoundProduct>?)
    : BoundProductListAdp_Abs<BoundProductItemCompOld>(ctx, dataList){
/*
    init{
        isPackagingVisible= false
        isSendVisible_Lessee= false
        isTrackVisible= false
        isContainerVisible= false
    }
 */

    override fun initAdp() {
        super.initAdp()
        setPackagingSendVisible_Lessee(false, false)
        setTrackContainerVisible(false, false)
    }

    override fun createViewComp(c: Context, view: View?): BoundProductItemCompOld {
        return BoundProductItemCompOld(c, view)
    }
}

 */