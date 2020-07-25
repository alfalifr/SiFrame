package com.sigudang.android.models

import com.sigudang.android.Model.Product
//import com.sigudang.android._template.util.ReceiverFun.isNotZero
import java.io.Serializable
import java.lang.Exception

data class BoundProduct(var product: Product,
/*
    var name: String, var category: String,
                        var unit: String, var volPerUnit: Double,
                        var imgRes: Int= R.drawable.img_city_search_1,
 */
                        /**
                         * Berguna sbg penunjuk jml produk pada In-OutBound, bkn Crossdocking.
                         * Jika pada crossdocking, @code amount berfungsi sbg penunjuk jml produk masuk (Inbound)
                         */
                        var amount: Int= 0,
                        /**
                         * Berguna sbg penunjuk jml produk keluar (outbound) pada Crossdocking.
                         * Hanya terpakai saat Crossdocking
                         */
                        var amountOut: Int= 0,
                        var boundPackaging: ArrayList<PackagingModel>?= null,
                        var boundSend: ArrayList<BoundProductSendModel>?= null,
                        var boundContainer: ArrayList<BoundContainerModel>?= null,
                        var orderItemId: Long? = null,
                        var boundTrack: TrackingModel= TrackingModel(amount))
    : Serializable