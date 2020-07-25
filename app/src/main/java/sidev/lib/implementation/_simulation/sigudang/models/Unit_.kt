package com.sigudang.android.models

import com.sigudang.android.models.abs.SimplestModel
import java.io.Serializable

data class Unit_(private val id_: String, private val name_: String)
    : SimplestModel(id_, name_), Serializable