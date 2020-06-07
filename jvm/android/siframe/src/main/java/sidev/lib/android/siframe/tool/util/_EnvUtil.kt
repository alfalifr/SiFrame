package sidev.lib.android.siframe.tool.util

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Environment
import sidev.lib.android.siframe.customizable._init._ConfigBase


object _EnvUtil{
    val apiLevel: Int
        get()= Build.VERSION.SDK_INT

    fun projectDir(c: Context): String{
        var dir= Environment.getExternalStorageDirectory().absolutePath
        dir += "/" +_ResUtil.getString(c, _ConfigBase.STRING_APP_NAME) //+c.resources.getString(_ConfigBase.STRING_APP_NAME) //R.string.app_name
        return dir
    }
    fun projectTempDir(c: Context): String{
        return projectDir(c) +"/temp"
    }

    fun projectLogDir(c: Context): String{
        return Environment.getExternalStorageDirectory().absolutePath + "/Android/Develop/LogHP/" +
                _ResUtil.getString(c, _ConfigBase.STRING_APP_NAME)
    }

    fun getVersionName(c: Context): String{
        val pInfo: PackageInfo = c.packageManager.getPackageInfo(c.packageName, 0)
        return pInfo.versionName
    }
    fun getVersionCode(c: Context): Long{
        val pInfo: PackageInfo = c.packageManager.getPackageInfo(c.packageName, 0)
        return if(Build.VERSION.SDK_INT >= 28)
            pInfo.longVersionCode
        else
            pInfo.versionCode.toLong()
    }


/*
    fun checkApiLevel(){
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M
    }
 */
}