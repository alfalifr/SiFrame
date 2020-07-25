package com.sigudang.android.models.db

import java.io.Serializable

data class WarehouseContainer(
    val id: String? = null,
    val code: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val wh_id: String? = null,
    val wh_container_type: String? = null,
    val wh_location: String? = null
) : Serializable