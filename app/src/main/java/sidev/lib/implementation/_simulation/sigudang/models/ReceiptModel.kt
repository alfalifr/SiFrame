package com.sigudang.android.models

import android.net.Uri
import sidev.lib.implementation._simulation.sigudang.models.PictModel
import java.io.Serializable

data class ReceiptModel(var receipt: String, var img: PictModel?= null): Serializable