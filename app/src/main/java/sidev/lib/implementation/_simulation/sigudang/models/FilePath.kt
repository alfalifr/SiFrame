package com.sigudang.android.models

import java.io.File
import java.io.Serializable

data class FilePath(val file: File? = null, val path: String? = null) : Serializable