package sidev.lib.android.std.tool.util.`fun`

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import sidev.lib.android.std.tool.util._ResUtil
import java.lang.Exception


/**
 * Fungsi infix sederhana untuk mengecek apakah this [Int] merupakan res dg type tertentu.
 */
infix fun Int.isIdIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.ID)
infix fun Int.isLayoutIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.LAYOUT)
infix fun Int.isDrawableIn(c: Context): Boolean =
    _ResUtil.isResType(c, this, _ResUtil.Type.DRAWABLE)
infix fun Int.isColorIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.COLOR)
infix fun Int.isStringIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.STRING)
infix fun Int.isDimenIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.DIMEN)
infix fun Int.isStyleIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.STYLE)
infix fun Int.isFontIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.FONT)
infix fun Int.isAnimIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.ANIM)
infix fun Int.isAnimatorIn(c: Context): Boolean =
    _ResUtil.isResType(c, this, _ResUtil.Type.ANIMATOR)
infix fun Int.isAttrIn(c: Context): Boolean = _ResUtil.isResType(c, this, _ResUtil.Type.ATTR)

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

infix fun Int.asResNameOrNullBy(c: Context): String? =
    try { _ResUtil.getResName(c, this) } catch (e: Resources.NotFoundException){ null }
infix fun Int.asResPackageOrNullBy(c: Context): String? =
    try { _ResUtil.getResPackageName(c, this) } catch (e: Resources.NotFoundException){ null }
infix fun Int.asResTypeOrNullBy(c: Context): String? =
    try { _ResUtil.getResTypeName(c, this) } catch (e: Resources.NotFoundException){ null }
infix fun Int.asResEntryOrNullBy(c: Context): String? =
    try { _ResUtil.getResEntryName(c, this) } catch (e: Resources.NotFoundException){ null }

fun Int.isIdDuplicatedInView(v: View): Boolean{
    var isIdFound= false
    for(innerV in v.childrenTree)
        if(this == innerV.id){
            if(isIdFound) return true
            isIdFound= true
        }
    return false
}

//val Number.dp get()= _ViewUtil.dpToPx(this.toFloat())

/**
 * Properti ini tidak akan @throws [Resources.NotFoundException].
 * @return null jika this [View] tidak memiliki [View.getId].
 */
val View.idResName: String?
    get()= try{
        _ResUtil.getResName(this.context, this.id)
    }
    catch (e: Resources.NotFoundException){ null }

/**
 * Properti ini tidak akan @throws [Resources.NotFoundException].
 * @return null jika this [View] tidak memiliki [View.getId].
 */
val View.idPackage: String?
    get()= try{
        _ResUtil.getResPackageName(this.context, this.id)
    }
    catch (e: Resources.NotFoundException){ null }

/**
 * Properti ini tidak akan @throws [Resources.NotFoundException].
 * @return null jika this [View] tidak memiliki [View.getId].
 */
val View.idName: String?
    get()= try{
        _ResUtil.getResEntryName(this.context, this.id)
    }
    catch (e: Resources.NotFoundException){ null }



/*
infix fun Int.asResPackageBy(c: Context): String = _ResUtil.getResPackageName(c, this)
infix fun Int.asResTypeBy(c: Context): String = _ResUtil.getResTypeName(c, this)
infix fun Int.asResEntryBy(c: Context): String = _ResUtil.getResEntryName(c, this)
 */