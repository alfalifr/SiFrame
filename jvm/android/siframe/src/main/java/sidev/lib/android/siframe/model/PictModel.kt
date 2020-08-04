package sidev.lib.android.siframe.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File
import java.io.Serializable

/**
 * Data class yang digunakan di dalam proyek ini untuk merepresentasikan gambar.
 * Gambar dalam app ini memiliki bitmap, direktori, dan file.
 */
data class PictModel(var bm: Bitmap?= null, var dir: String?= null, var file: File?= null, var resId: Int? = null, var uri: Uri? = null): Serializable