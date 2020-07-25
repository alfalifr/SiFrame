package com.sigudang.android.models

import android.view.View
import java.io.Serializable

data class UpperInfoModel(var left: String, var right: String,
                          var onClickEdit: View.OnClickListener?= null)
    : Serializable