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
        fun set(c: Context, key: String, value: String?, expDuration: Long= 0): Boolean {
//        val callerFun= ReflexUtil.getCurrentCallerFunName()
//        Log.e("UTIL", "setSharedPref() callerFun= $callerFun")
            Log.e("UTIL", "setSharedPref() key= $key value= $value expDuration= $expDuration")
            return if(value != null){
                if(expDuration >= 1)
                    setExpDuration(c, key, expDuration)

                c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE)
                    .edit().putString(key, value)
                    .commit()
            } else
                remove(c, key)
        }

        //untuk mendapatkan status negara pengguna
        fun get(c: Context, key: String): String? {
//        Log.e("UTIL", "getSharedPref() key= $key")
            val prefs = c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE)
            val expTime= getExpTime(c, key)
            Log.e("UTIL", "getSharedPref() key= $key expTime= $expTime")
            return if(expTime != null){
                val timeNow= TimeUtil.timestamp()
                val diff= TimeUtil.getTimeDiff(timeNow, expTime)
                if(diff > 0)
                    prefs.getString(key, null)
                else {
                    remove(c, key)
                    /* set(c, key, null)
                    removeExpTime(c, key) */
                    null
                }
            } else prefs.getString(key, null)
//        return prefs.getString(key, null)
        }

        fun setExpTime(c: Context, key: String, timestamp: String?): Boolean {
            val expKey= key + _SIF_Constant._KEY_PREF_EXP_TIME
            return c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE)
                .edit().putString(expKey, timestamp)
                .commit()
        }

        /**
         * @param duration in sec
         */
        fun setExpDuration(c: Context, key: String, duration: Long): Boolean {
            val expKey= key + _SIF_Constant._KEY_PREF_EXP_TIME
            val timestampA= TimeUtil.timestamp()
            val timestamp= if(duration > 0)
                TimeUtil.timestamp(diff= duration *1000)
            else null
            Log.e("UTIL", "timestampA= $timestampA timestamp= $timestamp")

            return c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE)
                .edit().putString(expKey, timestamp)
                .commit()
        }

        fun getExpTime(c: Context, key: String): String? {
            val expKey= key + _SIF_Constant._KEY_PREF_EXP_TIME
            Log.e("UTIL", "getSharedPrefExp() expKey= $expKey")
            return c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE)
                .getString(expKey, null)
        }

        fun removeExpTime(c: Context, key: String): Boolean{
            Log.e("UTIL", "removeSharedPrefExp()")
            val expKey= key + _SIF_Constant._KEY_PREF_EXP_TIME
            return c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE)
                .edit().remove(expKey)
                .commit()
        }

        fun remove(c: Context, key: String): Boolean{
            removeExpTime(c, key)

            return c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE).edit()
                .remove(key)
                .commit()
            //val expTime= getExpTime(c, key)
        }

        fun clear(c: Context): Boolean{
            return c.getSharedPreferences(_SIF_Constant.MAIN_REF, Context.MODE_PRIVATE).edit()
                .clear()
                .commit()
        }
    }
}