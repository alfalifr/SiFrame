package sidev.lib.android.std.tool.util

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.res.Resources
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toFile
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.`val`._Config

object _EnvUtil {
    const val SUB_DIR_DEVELOPER = "_develop"
    const val SUB_DIR_LOG = "$SUB_DIR_DEVELOPER/_log"
    const val SUB_DIR_TEMP = "_temp"
    val apiLevel: Int
        get()= Build.VERSION.SDK_INT

    fun projectName(c: Context): String=
        try {
            _ResUtil.getString(c, _Config.STRING_APP_NAME_RES)
        } catch (e: Resources.NotFoundException) {
            if(c is Application) c.packageName
            else throw Resources.NotFoundException(
                "Param `c` !is Application dan tidak terdapat StringRes 'R.string.app_name'"
            )
        }

    fun externalDir(c: Context, isAppSpecific: Boolean = true): String? {
        return if(isAppSpecific) c.getExternalFilesDir(null)?.absolutePath
        else {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                @Suppress(SuppressLiteral.DEPRECATION)
                Environment.getExternalStorageDirectory().absolutePath
            else try {
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)?.toFile()?.absolutePath
            } catch (e: IllegalArgumentException){ //Jika ternyata null, maka coba ambil path yang spesifik app, lalu di-substring dan ambil depannya saja.
                externalDir(c, true)?.let {
                    val start= it.indexOf("/Android")
                    if(start > 0) it.substring(0, start)
                    else null
                }
            }
        }
    }

    fun projectDir(c: Context, isAppSpecific: Boolean = true): String? = externalDir(c, isAppSpecific)?.run {
        if(isAppSpecific) this
        else "$this/${projectName(c)}"
    }

    fun projectTempDir(c: Context, isAppSpecific: Boolean = true): String? =
        projectDir(c, isAppSpecific)?.run { "$this/$SUB_DIR_TEMP" }

    fun projectLogDir(c: Context, isAppSpecific: Boolean = true): String? =
        projectDir(c, isAppSpecific)?.run { "$this/$SUB_DIR_LOG" } // /Android

    fun getVersionName(c: Context): String {
        val pInfo: PackageInfo = c.packageManager.getPackageInfo(c.packageName, 0)
        return pInfo.versionName
    }
    fun getVersionCode(c: Context): Long {
        val pInfo: PackageInfo = c.packageManager.getPackageInfo(c.packageName, 0)
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) pInfo.longVersionCode
        else @Suppress(SuppressLiteral.DEPRECATION) pInfo.versionCode.toLong()
    }


/*
    fun checkApiLevel(){
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M
    }
 */
}