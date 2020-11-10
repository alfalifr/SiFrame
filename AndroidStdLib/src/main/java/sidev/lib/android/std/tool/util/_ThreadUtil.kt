package sidev.lib.android.std.tool.util

import android.os.Handler

object _ThreadUtil {
    fun delayRun(millis: Long, func: () -> Unit){
        Handler().postDelayed(func, millis)
    }
}