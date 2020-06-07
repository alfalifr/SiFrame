package sidev.lib.android.siframe.tool.util.log;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.annotation.StringRes;


public class ToastApp {
    //untuk menampilkan teks sesuai mode debug
    public static void debugTeks(Context context, CharSequence text, int duration){
        if(LogApp.log) Toast.makeText(context, text, duration).show();
    }
    public static void debugTeks(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        if(LogApp.log) Toast.makeText(context, context.getResources().getText(resId), duration).show();
    }

    //untuk menampilkan teks sesuai alur app, bkn sesuai mode debug
    public static void tampilkanTeks(Context context, CharSequence text, int duration){
        Toast.makeText(context, text, duration).show();
    }
    public static void tampilkanTeks(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        Toast.makeText(context, context.getResources().getText(resId), duration).show();
    }
/*
    public static Toast makeText(Context context, CharSequence text, int duration) {
        if(LogApp.log) return Toast.makeText(context, text, duration);
        else return null;
    }

    public static Toast makeText(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        if(LogApp.log) return Toast.makeText(context, context.getResources().getText(resId), duration);
        else return null;
    }
*/
}
