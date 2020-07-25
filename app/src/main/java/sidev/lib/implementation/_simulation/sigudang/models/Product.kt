package com.sigudang.android.Model

//import com.sigudang.android.R
//import com.sigudang.android._template.model.PictModel
import com.sigudang.android.models.Category
import com.sigudang.android.models.Unit_
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.sigudang.models.PictModel
import java.io.Serializable

data class Product(
    var id: String= "",
    var name: String = "",
//    var category = ""
    var category: Category?= null,
//    var unit= ""
    var unit: Unit_?= null,
    var l: Double= 0.0,
//            private set
    var w: Double= 0.0,
//            private set
    var h: Double= 0.0,
//            private set
    var img: PictModel = PictModel(resId = R.drawable.bg_default),
    var content: String = "") : Serializable {

    var volPerUnit: Double= 0.0
        private set
/*
    constructor(id: String, name: String,
                category: Category, unit: Unit_,
                l: Double, w: Double, h: Double, //volPerUnit: Double,
                imgRes: Int= R.drawable.img_city_search_1, photoUrl: String= "",
                content: String= ""): this(){
        this.id= id
        this.name= name
        this.category= category
        this.unit= unit
        setDimension(l, w, h)
        this.imgRes= imgRes
        this.photoUrl= photoUrl
        this.content= content
    }
 */

    init{
        volPerUnit= l *w *h
    }

    fun setDimension(l: Double, w: Double, h: Double){
        this.l= l
        this.w= w
        this.h= h
        volPerUnit= l *w *h
    }
}