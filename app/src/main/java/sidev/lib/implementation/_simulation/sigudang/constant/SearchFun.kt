package com.sigudang.android.utilities.constant

import android.content.Context
import com.sigudang.android.models.Bound
import com.sigudang.android.models.BoundProduct
//import com.sigudang.android.utilities.Util

val searchBoundFun= { c: Context, bound: Bound, keyword: String ->
    if(bound.invoice?.contains(keyword, true) == true) true
    else{
        if(1 == Constants.STATE_ROLE_LESSEE)
            bound.warehouse?.name?.contains(keyword, true) ?: false
        else
            bound.lessee?.name?.contains(keyword, true) ?: false
    }
}


val searchBoundProductFun= { c: Context, boundProduct: BoundProduct, keyword: String ->
    boundProduct.product.name.contains(keyword, true)
            || boundProduct.product.category?.name?.contains(keyword, true) ?: false
}