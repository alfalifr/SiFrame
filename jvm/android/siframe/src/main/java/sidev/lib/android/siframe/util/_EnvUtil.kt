package sidev.lib.android.siframe.util

import android.content.Context
import android.os.Environment
import sidev.lib.android.siframe.customizable._init._ConfigBase

object _EnvUtil{
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
}