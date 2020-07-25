package com.sigudang.android.models

import java.io.Serializable

data class UserBusiness (
    val id: Long,
    val name: String,
    val desc: String? = null,
    val siteUrl: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val locLat: String? = null,
    val locLong: String? = null,
    val businessTypeId: Int? = null,
    val villageId: Int? = null
) : Serializable