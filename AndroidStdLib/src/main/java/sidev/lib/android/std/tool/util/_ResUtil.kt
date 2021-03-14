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
import sidev.lib.exception.IllegalArgExc
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

    /**
     * Pola resource identifier lengkap [pkg]:[type]/[entry].
     * Return Int resource identifier (R.[type].[entry]) atau 0 jika tidak ditemukan resource identifiernya.
     */
    fun getResId(c: Context, type: String, entry: String, pkg: String = c.packageName): Int
        = c.resources.getIdentifier(entry, type, pkg)

    /**
     * Pola resource identifier lengkap [pkg]:[type]/[entry].
     * Return Int resource identifier (R.[type].[entry]) atau 0 jika tidak ditemukan resource identifiernya.
     */
    fun getResId(c: Context, resName: String): Int {
        val isWithPkg= '.' in resName
        var pkg: String?= null
        val pkgSeps= listOf('@', ':')
        var pkgSepIndex: Int= -1

        if(isWithPkg){
            var foundSep: Char?= null
            for(sep in pkgSeps){
                pkgSepIndex= resName.indexOf(sep)
                if(pkgSepIndex >= 0){
                    foundSep= sep
                    break
                }
            }
            if(foundSep == null) throw IllegalArgExc(
                paramExcepted = *arrayOf("resName"),
                detailMsg = "Param `resName` disertai nama package, namun tidak disertai char pemisah ['@', ':']"
            )
            pkg= resName.substring(0, resName.indexOf(foundSep))
        } else {
            for(sep in pkgSeps){
                pkgSepIndex= resName.indexOf(sep)
                if(pkgSepIndex >= 0)
                    break
            }
        }
        if('/' !in resName) throw IllegalArgExc(
            paramExcepted = *arrayOf("resName"),
            detailMsg = "Param `resName` tidak memiliki char pemisah antara type dan entry resource '/'"
        )
        val slashIndex= resName.indexOf('/')
        val entry= resName.substring(slashIndex +1)
        val type= resName.substring(pkgSepIndex +1, slashIndex)

        return getResId(c, type, entry, pkg ?: c.packageName)
    }


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