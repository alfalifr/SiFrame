package sidev.lib.android.std.tool.util.`fun`

import android.util.Log
//import sidev.lib.android.siframe._customizable._Config

enum class LogKind { VERBOSE, DEBUG, INFO, WARN, ERROR }

@JvmOverloads
fun Any.log(
    anyMsg: Any,
    throwable: Throwable?= null,
    header: String?= null,
    footer: String?= null,
    kind: LogKind = LogKind.ERROR
){
    var tag= this::class.java.simpleName
    if(tag.length > 23)
        tag= tag.substring(0, 23)

    var finalMsg= if(anyMsg is String) anyMsg else anyMsg.toString()
    if(header != null && header.isNotBlank())
        finalMsg= "$header \n$finalMsg"
    if(footer != null && footer.isNotBlank())
        finalMsg= "$finalMsg \n$footer"

    if(throwable == null) when(kind){
        LogKind.VERBOSE -> Log.v(tag, finalMsg)
        LogKind.DEBUG -> Log.d(tag, finalMsg)
        LogKind.INFO -> Log.i(tag, finalMsg)
        LogKind.WARN -> Log.w(tag, finalMsg)
        LogKind.ERROR -> Log.e(tag, finalMsg)
//            LogKind.ASSERT -> Log.(tag, finalMsg)
    } else when(kind){
        LogKind.VERBOSE -> Log.v(tag, finalMsg, throwable)
        LogKind.DEBUG -> Log.d(tag, finalMsg, throwable)
        LogKind.INFO -> Log.i(tag, finalMsg, throwable)
        LogKind.WARN -> Log.w(tag, finalMsg, throwable)
        LogKind.ERROR -> Log.e(tag, finalMsg, throwable)
//            LogKind.ASSERT -> Log.(tag, finalMsg)
    }
}

@JvmOverloads
fun Any.logv(
    anyMsg: Any,
    throwable: Throwable?= null,
    header: String?= null,
    footer: String?= null
) = log(anyMsg, throwable, header, footer, LogKind.VERBOSE)

@JvmOverloads
fun Any.logd(
    anyMsg: Any,
    throwable: Throwable?= null,
    header: String?= null,
    footer: String?= null
) = log(anyMsg, throwable, header, footer, LogKind.DEBUG)

@JvmOverloads
fun Any.logi(
    anyMsg: Any,
    throwable: Throwable?= null,
    header: String?= null,
    footer: String?= null
) = log(anyMsg, throwable, header, footer, LogKind.INFO)

@JvmOverloads
fun Any.logw(
    anyMsg: Any,
    throwable: Throwable?= null,
    header: String?= null,
    footer: String?= null
) = log(anyMsg, throwable, header, footer, LogKind.WARN)

@JvmOverloads
fun Any.loge(
    anyMsg: Any,
    throwable: Throwable?= null,
    header: String?= null,
    footer: String?= null
) = log(anyMsg, throwable, header, footer, LogKind.ERROR)

///*
@JvmOverloads
fun Any.logew(anyMsg: Any, throwable: Throwable?= null) =
    //loge("=== \n =================== \n WARNING!!! -> $txt \n =================== \n", throwable)
    loge(
        anyMsg, throwable,
        "================= WARNING =================",
        "=================================="
    )
// */

/*
@JvmOverloads
fun Any.loge(txt: String, throwable: Throwable?= null){
    if(_Config.LOG){
        var tag= this::class.java.simpleName
        if(tag.length > 23)
            tag= tag.substring(0, 23)

        if(throwable == null)
            Log.e(tag, txt)
        else
            Log.e(tag, txt, throwable)

//        val isLoggable= Log.isLoggable(tag, Log.ERROR)
//        Log.e("LOG_UTIL", "isLoggable= $isLoggable")
    }
}
 */
/*
@JvmOverloads
fun Any.loge(any: Any, name: String?= null){
    if(_Config.LOG){
        val name= name ?: any::class.java.name
        var tag= this::class.java.simpleName
        if(tag.length > 23)
            tag= tag.substring(0, 23)
        Log.e(tag, "$name ==> $any")

//        val isLoggable= Log.isLoggable(tag, Log.ERROR)
//        Log.e("LOG_UTIL", "isLoggable= $isLoggable")
    }
}
 */