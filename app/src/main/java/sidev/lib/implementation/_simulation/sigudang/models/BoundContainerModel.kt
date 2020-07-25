package com.sigudang.android.models

import com.sigudang.android._template.model.Str2IntModel
import java.io.Serializable

data class BoundContainerModel(var id: String, var name: String, var amount: Int= 0)
    : Serializable
//    : Str2IntModel(name, "", amount)