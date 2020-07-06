package sidev.lib.android.siframe.tool.util.`fun`

import android.util.Log
import sidev.lib.android.siframe.customizable._init._Config
import java.lang.Exception

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