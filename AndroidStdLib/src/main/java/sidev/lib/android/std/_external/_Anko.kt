package sidev.lib.android.std._external

import android.os.Handler
import android.os.Looper


object ContextHelper {
    val handler = Handler(Looper.getMainLooper())
}