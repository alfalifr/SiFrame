package com.sigudang.android.models.db

import java.io.Serializable

data class ShippingMethod(
    val id: String? = null,
    val name: String? = null,
    val imgUrl: String? = null
) : Serializable