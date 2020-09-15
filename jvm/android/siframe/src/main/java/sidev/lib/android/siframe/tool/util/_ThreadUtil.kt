package sidev.lib.android.siframe.tool.util

import android.os.Handler

object _ThreadUtil {
    fun delayRun(millis: Long, func: () -> Unit){
        Handler().postDelayed(func, millis)
    }
}