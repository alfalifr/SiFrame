package sidev.lib.android.siframe.tool.util

import android.content.Context
import android.util.Log
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.universal.tool.util.TimeUtil

object _StorageUtil{

    object SharedPref{

        /**
         * @param expDuration in sec
         */
        fun setSharedPref(c: Context, key: String, value: String?, expDuration: Long= 0) {
//        val callerFun= ReflexUtil.getCurrentCallerFunName()
//        Log.e("UTIL", "setSharedPref() callerFun= $callerFun")
            Log.e("UTIL", "setSharedPref() key= $key value= $value expDuration= $expDuration")
            val editor = c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE).edit()
            editor.putString(key, value)
            if(expDuration >= 1)
                setSharedPrefExpDuration(c, key, expDuration)
            if(value == null)
                removeSharedPrefExp(c, key)
            editor.commit()
        }

        //untuk mendapatkan status negara pengguna
        fun getSharedPref(c: Context, key: String): String? {
//        Log.e("UTIL", "getSharedPref() key= $key")
            val prefs = c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE)
            val expTime= getSharedPrefExp(c, key)
            Log.e("UTIL", "getSharedPref() key= $key expTime= $expTime")
            return if(expTime != null){
                val timeNow= TimeUtil.timestamp()
                val diff= TimeUtil.getTimeDiff(timeNow, expTime)
                if(diff > 0)
                    prefs.getString(key, null)
                else {
                    setSharedPref(c, key, null)
                    removeSharedPrefExp(c, key)
                    null
                }
            } else prefs.getString(key, null)
//        return prefs.getString(key, null)
        }

        fun setSharedPrefExpTime(c: Context, key: String, timestamp: String?) {
            val expKey= key + _SIF_Constant._KEY_PREF_EXP_TIME
            val editor = c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE).edit()
            editor.putString(expKey, timestamp)
            editor.commit()
        }

        /**
         * @param duration in sec
         */
        fun setSharedPrefExpDuration(c: Context, key: String, duration: Long) {
            val expKey= key + _SIF_Constant._KEY_PREF_EXP_TIME
            val editor = c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE).edit()
            val timestampA= TimeUtil.timestamp()
            val timestamp= if(duration > 0)
                TimeUtil.timestamp(diff= duration *1000)
            else null
            Log.e("UTIL", "timestampA= $timestampA timestamp= $timestamp")

            editor.putString(expKey, timestamp)
            editor.commit()
        }

        fun getSharedPrefExp(c: Context, key: String): String? {
            val expKey= key + _SIF_Constant._KEY_PREF_EXP_TIME
            Log.e("UTIL", "getSharedPrefExp() expKey= $expKey")
            val prefs = c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE)
            return prefs.getString(expKey, null)
        }

        fun removeSharedPrefExp(c: Context, key: String){
            Log.e("UTIL", "removeSharedPrefExp()")
            val expKey= key + _SIF_Constant._KEY_PREF_EXP_TIME
            val editor = c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE).edit()
            editor.putString(expKey, null)
            editor.commit()
        }
    }
}