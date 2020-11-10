package sidev.lib.android.std.tool.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
//import sidev.lib.android.siframe.tool.util.`fun`.childrenTree
import java.util.*

object _ResUtil{
    enum class Type{
        ID,
        LAYOUT,
        DRAWABLE,
        COLOR,
        STRING,
        DIMEN,
        STYLE,
        FONT,
        ANIM,
        ANIMATOR,
        ATTR,
    }

    fun getString(c: Context, @StringRes res: Int): String = c.resources.getString(res)

    @SuppressLint("ResourceType")
    fun getDimen(c: Context, @DimenRes res: Int): Float
            = try{ c.resources.getDimension(res) }
            catch (resNotFoundExc: Resources.NotFoundException){
                //Tujuan dari [Resources.getString] adalah jika dimen-nya gak punya satuan,
                // maka scr otomatis dianggap sbg string oleh Android.
                try{ c.resources.getString(res).toFloat() }
                catch (numFormatExc: NumberFormatException){ throw resNotFoundExc }
            }

    fun getColor(c: Context, @ColorRes colorId: Int): Int = ContextCompat.getColor(c, colorId)

    fun getDrawable(c: Context, @DrawableRes drwId: Int): Drawable? = ContextCompat.getDrawable(c, drwId)

    fun getFont(c: Context, @FontRes fontId: Int): Typeface? = ResourcesCompat.getFont(c, fontId)

    fun getStyle(c: Context, @StyleRes styleId: Int, attrib: IntArray): TypedArray
            = c.obtainStyledAttributes(styleId, attrib)


    /**
     * Fungsi penghubung untuk mengambil nama dari resource dg id [res].
     * @throws Resources.NotFoundException
     */
    fun getResName(c: Context, res: Int): String
            = c.resources.getResourceName(res)

    fun getResPackageName(c: Context, res: Int): String
        = c.resources.getResourcePackageName(res)

    fun getResTypeName(c: Context, res: Int): String
        = c.resources.getResourceTypeName(res)

    fun getResEntryName(c: Context, res: Int): String
        = c.resources.getResourceEntryName(res)


    fun isResType(c: Context, res: Int, type: Type): Boolean
            = isResType(c, res, type.name.toLowerCase(Locale.ROOT))

    fun isResType(c: Context, res: Int, type: String): Boolean{
        return try{
            val typeStr= getResTypeName(c, res)
            typeStr == type
        }
        catch (e: Resources.NotFoundException){ false }
    }
}