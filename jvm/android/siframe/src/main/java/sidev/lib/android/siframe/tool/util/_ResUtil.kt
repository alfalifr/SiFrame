package sidev.lib.android.siframe.tool.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import sidev.lib.android.siframe.R
import java.util.*

object _ResUtil{
    enum class Type{
        Id,
        Layout,
        Drawable,
        Color,
        String,
        Dimen,
        Style,
        Font,
        Anim,
        Animator,
        Attr,
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

/**
 * Fungsi infix sederhana untuk mengecek apakah this [Int] merupakan res dg type tertentu.
 */
infix fun Int.isIdIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.Id)
infix fun Int.isLayoutIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.Layout)
infix fun Int.isDrawableIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.Drawable)
infix fun Int.isColorIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.Color)
infix fun Int.isStringIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.String)
infix fun Int.isDimenIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.Dimen)
infix fun Int.isStyleIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.Style)
infix fun Int.isFontIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.Font)
infix fun Int.isAnimIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.Anim)
infix fun Int.isAnimatorIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.Animator)
infix fun Int.isAttrIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.Attr)

/**
 * Fungsi infix sederhana untuk mengambil resource yg ada dg id [res] dari [Context].
 */
infix fun Context.getStringOf(@StringRes res: Int): String = _ResUtil.getString(this, res)
infix fun Context.getDimenOf(@DimenRes res: Int): Float = _ResUtil.getDimen(this, res)
infix fun Context.getColorOf(@ColorRes res: Int): Int = _ResUtil.getColor(this, res)
infix fun Context.getDrawableOf(@DrawableRes res: Int): Drawable? = _ResUtil.getDrawable(this, res)
infix fun Context.getFontOf(@FontRes res: Int): Typeface? = _ResUtil.getFont(this, res)

/**
 * Fungsi infix sederhana untuk mengambil resource yg ada dg resId this [Int].
 * Fungsi ini sama dg rumpun fungsi di atas ([getStringOf]).
 */
infix fun Int.asStringIn(c: Context): String = _ResUtil.getString(c, this)
infix fun Int.asDimenIn(c: Context): Float = _ResUtil.getDimen(c, this)
infix fun Int.asColorIn(c: Context): Int = _ResUtil.getColor(c, this)
infix fun Int.asDrawableIn(c: Context): Drawable? = _ResUtil.getDrawable(c, this)
infix fun Int.asFontIn(c: Context): Typeface? = _ResUtil.getFont(c, this)

infix fun Int.asResNameBy(c: Context): String = _ResUtil.getResName(c, this)
infix fun Int.asResPackageBy(c: Context): String = _ResUtil.getResPackageName(c, this)
infix fun Int.asResTypeBy(c: Context): String = _ResUtil.getResTypeName(c, this)
infix fun Int.asResEntryBy(c: Context): String = _ResUtil.getResEntryName(c, this)


/**
 * Properti ini tidak akan @throws [Resources.NotFoundException].
 * @return null jika this [View] tidak memiliki [View.getId].
 */
val View.idResName: String?
    get()= try{ _ResUtil.getResName(this.context, this.id) }
    catch (e: Resources.NotFoundException){ null }

/**
 * Properti ini tidak akan @throws [Resources.NotFoundException].
 * @return null jika this [View] tidak memiliki [View.getId].
 */
val View.idPackage: String?
    get()= try{ _ResUtil.getResPackageName(this.context, this.id) }
    catch (e: Resources.NotFoundException){ null }

/**
 * Properti ini tidak akan @throws [Resources.NotFoundException].
 * @return null jika this [View] tidak memiliki [View.getId].
 */
val View.idName: String?
    get()= try{ _ResUtil.getResEntryName(this.context, this.id) }
    catch (e: Resources.NotFoundException){ null }



/*
infix fun Int.asResPackageBy(c: Context): String = _ResUtil.getResPackageName(c, this)
infix fun Int.asResTypeBy(c: Context): String = _ResUtil.getResTypeName(c, this)
infix fun Int.asResEntryBy(c: Context): String = _ResUtil.getResEntryName(c, this)
 */