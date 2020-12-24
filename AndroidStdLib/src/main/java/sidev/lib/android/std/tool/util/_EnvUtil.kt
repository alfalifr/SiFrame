package sidev.lib.android.std.tool.util

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toFile
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.`val`._Config
import java.io.File
import java.lang.NullPointerException

object _EnvUtil {
    const val SUB_DIR_LOG = "_develop/_log"
    val apiLevel: Int
        get()= Build.VERSION.SDK_INT

    fun projectName(c: Context): String= _ResUtil.getString(c, _Config.STRING_APP_NAME_RES)

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

    fun projectDir(c: Context, isAppSpecific: Boolean = true): String? = externalDir(c, isAppSpecific)?.run { "$this/${projectName(c)}" }

    fun projectTempDir(c: Context): String? = projectDir(c, true)?.run { "$this/_temp" }

    fun projectLogDir(c: Context): String? = externalDir(c, true)?.run { "$this/$SUB_DIR_LOG/${projectName(c)}" } // /Android

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