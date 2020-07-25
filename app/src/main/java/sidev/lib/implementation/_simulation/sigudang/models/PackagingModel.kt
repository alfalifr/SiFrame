package com.sigudang.android.models


import java.io.Serializable

data class PackagingModel(var name: String, var id: Int= 0, var isSelected: Boolean= false)
    : Serializable