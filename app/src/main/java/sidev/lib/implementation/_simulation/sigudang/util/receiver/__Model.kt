package com.sigudang.android.utilities.receiver

import com.sigudang.android.Model.User
import com.sigudang.android.Model.Warehouse
import com.sigudang.android.models.Bound
import com.sigudang.android.models.BoundProduct
import com.sigudang.android.models.CrossdockingModel
import com.sigudang.android.models.UserBusiness


/*
fun CrossdockingModel.renewBoundData(){
    val c= this::class.java

    val outboundField= c.getDeclaredField("outbound")
    outboundField.isAccessible= true
    outboundField.set(this, Bound(invoice, date, lessee, warehouse, productList?.toCollection(ArrayList()), status, sender))
    outboundField.isAccessible= false

    val inboundField= c.getDeclaredField("inbound")
    inboundField.isAccessible= true
    inboundField.set(this, Bound(invoice, date, lessee, warehouse, productList?.toCollection(ArrayList()), status, sender))
    inboundField.isAccessible= false

    if(productList != null)
        for(i in productList!!.indices){
            inbound!!.productList!![i].amount= amountIn!![i]
            outbound!!.productList!![i].amount= amountOut!![i]
        }
}

 */

fun Warehouse.toUserBusiness(): UserBusiness{
    return UserBusiness(id, name, address= address, locLat = latitude.toString(), locLong = longtitude.toString())
}
/*
    val id: Long,
    val name: String,
    val desc: String? = null,
    val siteUrl: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val locLat: String? = null,
    val locLong: String? = null,
    val businessTypeId: Int? = null,
    val villageId: Int? = null
 */


/**
 * Digunakan untuk menambah jumlah di tiap boundSend
 * agar lebih teratur
 */
fun BoundProduct.addAmountAt(pos: Int= -1, amount: Int){
    this.amount += amount
    if(boundSend != null && pos >= 0)
        boundSend!![pos].amount += amount
}

/**
 * Digunakan untuk mengurangi jumlah di tiap boundSend
 * agar lebih teratur
 */
fun BoundProduct.decAmountAt(pos: Int= -1, amount: Int){
    this.amount -= amount
    if(boundSend != null && pos >= 0)
        boundSend!![pos].amount -= amount
}