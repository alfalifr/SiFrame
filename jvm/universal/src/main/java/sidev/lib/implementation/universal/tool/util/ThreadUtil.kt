package sidev.lib.implementation.universal.tool.util

import android.os.Handler

object ThreadUtil{
    fun delayRun(millis: Long, func: () -> Unit){
        Handler().postDelayed(func, millis)
    }

    fun getStackFunName(ind: Int): String {
        val stackTrace= Thread.currentThread().stackTrace
        val stack= stackTrace[ind]
        val funName= stack.methodName
        val className= stack.className
        return "$className.$funName()"
    }
    fun getCurrentCallerFunName(backInd: Int= 0): String {
        return getStackFunName(5 +backInd)
    }
    fun getCurrentFunName(): String {
        return getStackFunName(4)
    }
}