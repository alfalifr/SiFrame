package com.sigudang.android.models

import com.sigudang.android._template.model.PictTextModel
import sidev.lib.implementation._simulation.sigudang.dummy.CourierType
import sidev.lib.implementation._simulation.sigudang.models.PictModel
import java.io.Serializable

data class SendMethodModel(var name: String, var img: PictModel?, var id: String? = null, var type: CourierType = CourierType.THIRD_PARTY)
    : Serializable
//    : PictTextModel(img, name)