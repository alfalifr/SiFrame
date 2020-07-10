package sidev.lib.android.siframe.adapter.decoration

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

/**
 * Simple SmoothScroller untuk Rv.
 */
open class RvSmoothScroller(c: Context): LinearSmoothScroller(c) {
    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }
}