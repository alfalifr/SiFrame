package com.sigudang.android.models

import java.io.Serializable

data class ShipperModel(var name: String, var phone: String, var token: String? = null, var type: Int? = null)
    : Serializable {
    object Type {
        val SENDER = 1
        val RECEIVER = 2
        val PICKER = 3
    }
}