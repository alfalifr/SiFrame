package com.sigudang.android.models

import com.sigudang.android.models.abs.SimplestModel
import java.io.Serializable

data class Category(private var id_: String, private var name_: String, var type: String= "")
    : SimplestModel(id_, name_), Serializable