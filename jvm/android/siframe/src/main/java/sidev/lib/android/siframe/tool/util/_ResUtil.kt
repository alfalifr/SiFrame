package sidev.lib.android.siframe.tool.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat

object _ResUtil{
    fun getString(c: Context, @StringRes res: Int): String{
        return c.resources.getString(res)
    }
    fun getDimen(c: Context, @DimenRes res: Int): Float{
        return c.resources.getDimension(res)
    }
    fun getColor(c: Context, @ColorRes colorId: Int): Int{
        return ContextCompat.getColor(c, colorId)
    }
    fun getDrawable(c: Context, @DrawableRes drwId: Int): Drawable?{
        return ContextCompat.getDrawable(c, drwId)
    }
    fun getStyle(c: Context, @StyleRes styleId: Int, attrib: IntArray): TypedArray {
        return c.obtainStyledAttributes(styleId, attrib)
    }
}