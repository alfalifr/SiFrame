package sidev.lib.android.siframe.util.`fun`

import android.util.Log


fun Any.loge(txt: String){
    Log.e(this::class.java.simpleName, txt)
}

fun Any.loge(any: Any, name: String?= null){
    val name= name ?: any::class.java
    Log.e(this::class.java.simpleName, "$name ==> $any")
}