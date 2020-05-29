package sidev.lib.android.siframe.util

import android.content.Context
import androidx.annotation.StringRes

object _ResUtil{
    fun getString(c: Context, @StringRes res: Int): String{
        return c.resources.getString(res)
    }
}