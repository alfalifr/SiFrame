package sidev.lib.android.siframe.tool.util.log;

import android.util.Log;

import sidev.lib.android.std._val._Config;

public class LogApp {
    public static boolean log= _Config.INSTANCE.getLOG(); //true;
    public static final int GAGAL= -2;

    public static int v(String tag, String msg) {
        if(log) return Log.v(tag, msg);
        else return GAGAL;
    }
    public static int v(String tag, String msg, Throwable tr) {
        if(log) return Log.v(tag, msg, tr);
        else return GAGAL;
    }

    public static int d(String tag, String msg) {
        if(log) return Log.d(tag, msg);
        else return GAGAL;
    }
    public static int d(String tag, String msg, Throwable tr) {
        if(log) return Log.d(tag, msg, tr);
        else return GAGAL;
    }

    public static int i(String tag, String msg) {
        if(log) return Log.i(tag, msg);
        else return GAGAL;
    }
    public static int i(String tag, String msg, Throwable tr) {
        if(log) return Log.i(tag, msg, tr);
        else return GAGAL;
    }

    public static int w(String tag, String msg) {
        if(log) return Log.w(tag, msg);
        else return GAGAL;
    }
    public static int w(String tag, String msg, Throwable tr) {
        if(log) return Log.w(tag, msg, tr);
        else return GAGAL;
    }
    public static int w(String tag, Throwable tr) {
        if(log) return Log.w(tag, tr);
        else return GAGAL;
    }

    public static int e(String tag, String msg) {
        if(log) return Log.e(tag, msg);
        else return GAGAL;
    }
    public static int e(String tag, String msg, Throwable tr) {
        if(log) return Log.e(tag, msg, tr);
        else return GAGAL;
    }
}
