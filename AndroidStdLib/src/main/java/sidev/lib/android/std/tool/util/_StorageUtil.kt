package sidev.lib.android.std.tool.util

import android.content.Context
import sidev.lib.android.std._val._Config
import sidev.lib.jvm.tool.util.TimeUtil

object _StorageUtil{

    object SharedPref{

        /**
         * @param expDuration in sec
         */
        fun set(c: Context, key: String, value: String?, expDuration: Long= 0): Boolean {
            return if(value != null){
                if(expDuration >= 1)
                    setExpDuration(c, key, expDuration)

                c.getSharedPreferences(_Config.MAIN_REF, Context.MODE_PRIVATE)
                    .edit().putString(key, value)
                    .commit()
            } else
                remove(c, key)
        }

        //untuk mendapatkan status negara pengguna
        fun get(c: Context, key: String): String? {
            val prefs = c.getSharedPreferences(_Config.MAIN_REF, Context.MODE_PRIVATE)
            val expTime= getExpTime(c, key)
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
            val expKey= key + _Config._KEY_PREF_EXP_TIME
            return c.getSharedPreferences(_Config.MAIN_REF, Context.MODE_PRIVATE)
                .edit().putString(expKey, timestamp)
                .commit()
        }

        /**
         * @param duration in sec
         */
        fun setExpDuration(c: Context, key: String, duration: Long): Boolean {
            val expKey= key + _Config._KEY_PREF_EXP_TIME
            val timestampA= TimeUtil.timestamp()
            val timestamp= if(duration > 0)
                TimeUtil.timestamp(diff = duration * 1000)
            else null

            return c.getSharedPreferences(_Config.MAIN_REF, Context.MODE_PRIVATE)
                .edit().putString(expKey, timestamp)
                .commit()
        }

        fun getExpTime(c: Context, key: String): String? {
            val expKey= key + _Config._KEY_PREF_EXP_TIME
            return c.getSharedPreferences(_Config.MAIN_REF, Context.MODE_PRIVATE)
                .getString(expKey, null)
        }

        fun removeExpTime(c: Context, key: String): Boolean{
            val expKey= key + _Config._KEY_PREF_EXP_TIME
            return c.getSharedPreferences(_Config.MAIN_REF, Context.MODE_PRIVATE)
                .edit().remove(expKey)
                .commit()
        }

        fun remove(c: Context, key: String): Boolean{
            removeExpTime(c, key)

            return c.getSharedPreferences(_Config.MAIN_REF, Context.MODE_PRIVATE).edit()
                .remove(key)
                .commit()
            //val expTime= getExpTime(c, key)
        }

        fun clear(c: Context): Boolean{
            return c.getSharedPreferences(_Config.MAIN_REF, Context.MODE_PRIVATE).edit()
                .clear()
                .commit()
        }
    }
}