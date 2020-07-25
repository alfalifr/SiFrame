package com.sigudang.android.Model

import java.io.Serializable

class ViewDimension : Serializable {
    var height: Int = -1
    var width : Int = -1
    var marginLeft : Int = -1
    var marginRight : Int = -1
    var marginTop : Int = -1
    var marginBottom : Int = -1
    var paddingLeft : Int = -1
    var paddingRight : Int = -1
    var paddingTop : Int = -1
    var paddingBottom : Int = -1

    constructor()
}