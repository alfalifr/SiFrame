package sidev.lib.universal.tool.util

import android.os.Handler

object ThreadUtil{
    fun delayRun(millis: Long, func: () -> Unit){
        Handler().postDelayed(func, millis)
    }

    fun getStackFunName(ind: Int): String {
        val stackTrace= Thread.currentThread().stackTrace
/*
        for((i, stack) in stackTrace.withIndex()){
            val funName= stack.methodName
            val className= stack.className
            Log.e("ThreadUtil", "i= $i $className.$funName()")
        }
 */
        val stack= stackTrace[ind]
        val funName= stack.methodName
        val className= stack.className
        return "$className.$funName()"
    }
    fun getCurrentCallerFunName(backInd: Int= 0): String {
        val tempRes= getStackFunName(6 +backInd)
        val thisFunName= "getCurrentCallerFunName"
//        Log.e("ThreadUtil", "getCurrentCallerFunName tempRes= $tempRes")
        if(tempRes.endsWith("$thisFunName\$default()")){
//            Log.e("ThreadUtil", "getCurrentCallerFunName = endsWith \$default()")
            return getStackFunName(6 +backInd +1)
        }
/*
        else{
            Log.e("ThreadUtil", "getCurrentCallerFunName = NOT!!! endsWith \$default()")
        }
 */
        return tempRes
    }
    fun getCurrentFunName(): String {
        return getStackFunName(4)
    }
}