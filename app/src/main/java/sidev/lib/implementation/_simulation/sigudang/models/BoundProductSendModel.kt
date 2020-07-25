package com.sigudang.android.models

import java.io.Serializable

data class BoundProductSendModel(var method: SendMethodModel?, var address: String,
                                 var amount: Int= 0, var receipt: String?= null)
    : Serializable