package com.sigudang.android.models

import java.io.Serializable

data class Bound(var id: Long,
                 var invoice: String?, var date: String,
                 var lessee: UserBusiness?= null,
                 var warehouse: UserBusiness?= null,
                 var productList: ArrayList<BoundProduct>? = null,
/*
                =========================
                GAK JADI. Jadinya di dalam productList-nya
                 /**
                  * Digunakan hanya pada crossdocking
                  * size nya harus sama dengan productList
                  */
                 var amountIn: ArrayList<Int>? = null,
                 var amountOut: ArrayList<Int>?= null,
                 //==================
 */
                 var status: Int= 0,
                 var shipper: ShipperModel?= null, // could be sender, receiver, or picker
                 var send: BoundSendModel?= null)
    : Serializable