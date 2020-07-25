package com.sigudang.android.Model

import sidev.lib.implementation.R
import java.io.Serializable

data class Warehouse(
    var name: String = "",
    var address: String = "",
    var price: Long = 0,
    var distance: Double = 3.4,
    var totalRating: Double = 0.0,
    var totalVolume: Double = 0.0,
    var usedVolume: Double = 0.0,
    var imgRes: Int= R.drawable.bg_default,
    var photoUrl: String = "",
    var longtitude: Double = 0.0,
    var latitude: Double = 0.0,
    var id: Long= 0) : Serializable {
/*
    constructor(
        name: String,
        address: String,
        price: Long,
        distance: Double,
        totalRating: Double,
        totalVolume: Double,
        usedVolume: Double,
        imgRes: Int= R.drawable.img_product_1,
        photoUrl: String = "",
        longtitude: Double = 0.0,
        latitude: Double = 0.0
    ): this(){
        this.name= name
        this.address= address
        this.price= price
        this.distance= distance
        this.totalRating= totalRating
        this.totalVolume= totalVolume
        this.usedVolume= usedVolume
        this.imgRes= imgRes
        this.photoUrl= photoUrl
        this.longtitude= longtitude
        this.latitude= latitude
    }

 */
}