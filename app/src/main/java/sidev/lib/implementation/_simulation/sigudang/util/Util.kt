package sidev.lib.implementation._simulation.sigudang.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sigudang.android.utilities.constant.Constants
import sidev.lib.universal.tool.util.ReflexUtil
import sidev.lib.universal.tool.util.ThreadUtil
import java.io.File

object Util{
    const val MAIN_REF = "main_ref" // ini merupakan tag untuk menyimpan di share preference

    fun setSharedPref(c: Context, value: String?, key: String) {
        val editor = c.getSharedPreferences(MAIN_REF, MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.commit()
    }

    //untuk mendapatkan status negara pengguna
    fun getSharedPref(c: Context, key: String): String? {
        val prefs = c.getSharedPreferences(MAIN_REF, MODE_PRIVATE)
        return prefs.getString(key, null)
    }

    //untuk mengubah dari double menjadi formatted nomimal dalam bentuk string
    fun convertToFormattedValue(value : Long) : String {
        val valTemp = value.toString()
        var finalVal = ""

        for(i in valTemp.length - 1 downTo 0){
            finalVal = if((i - valTemp.length) % 3 == 0 && i != 0)
                "." + valTemp[i] + finalVal
            else
                valTemp[i] + finalVal
        }

        return "$finalVal,-"
    }

    // format ke nomor telepon

    // untuk search string
    fun searchString(toBeSearch: String, vararg keywords: String) : Boolean {

        for(j in keywords.indices) {
            for(i in 0 .. toBeSearch.length - keywords[j].length)
                if(toBeSearch.substring(i, i + keywords[j].length).equals(keywords[j], ignoreCase = true))
                    return true
        }

        return false
    }

    // agar key maupun url dari server biar apan maka setidaknya di encode
    fun encodeBase64(str: String): String {
        val encodedBytes = Base64.encode(str.toByteArray(), Base64.DEFAULT)
        return String(encodedBytes)
    }

    fun decodeBase64(str: String): String {
        val decodedBytes = Base64.decode(str.toByteArray(), Base64.DEFAULT)
        return String(decodedBytes)
    }

    fun isStoragePermissionGranted(context: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            return if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Log.containerView(TAG,"Permission is granted");
                true
            } else {

                //Log.containerView(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Log.containerView(TAG,"Permission is granted");
            return true
        }
    }

    fun convertStringToDouble(str: String?): Double{
        if(str == null)
            return -1.0
        if(str.equals(""))
            return -1.0
        return str.toDouble()
    }

    fun isFineLocationPermissionGranted(context: Activity): Boolean {
        return if(Build.VERSION.SDK_INT >= 23) {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                true
            else {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                false
            }
        } else
            true
    }

    fun isGoogleMapsInstalled(ctx: Activity): Boolean {
        return try {
            val info = ctx.packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }

    fun makeFixDigitStr(numDigit: Int, currentNumber: Any) : String {
        if(currentNumber is Long || currentNumber is Int){
            var returnVal: String
            val currentNumberStr = currentNumber.toString()
            val len = currentNumberStr.length

            if(len < numDigit) {
                returnVal = currentNumberStr

                for(i in 0 until numDigit - len)
                    returnVal = "0$returnVal"
            } else
                returnVal = currentNumberStr.substring(0, numDigit)

            return returnVal
        }

        return ""
    }

    fun regexCheck(patternStr: String, toBeChecked: String) : Boolean {
        if(Regex(patternStr).matches(toBeChecked))
            return true
        return false
    }

    fun isNumberOnly(str : String) : Boolean {
        val pattern = "^[0-9]*\$"
        return (regexCheck(pattern, str))
    }
    /**
     * Msh dummy
     */
    fun getRoleType(c: Context): Int{
//        return Constants.STATE_ROLE_LESSEE
        return getSharedPref(c, Constants.KEY_PREF_USER_ROLE_TYPE)?.toInt() ?: Constants.USER_ROLE_LESSEE
    }

    fun setRoleType(c: Context, role: Int){
        setSharedPref(c, role.toString(), Constants.KEY_PREF_USER_ROLE_TYPE)
    }

    fun isDirLocal(dir: String): Boolean {
        return if(dir.isNotBlank()){
            val file= File(dir).absoluteFile
            val exist= file.exists()
            val caller= ThreadUtil.getCurrentCallerFunName(1)
            Log.e("UTIL", "caller= $caller dir= $dir file_exist= $exist")
            exist
        } else false
    }
}