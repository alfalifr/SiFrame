package sidev.lib.android.siframe.intfc.listener

import android.content.Context
import org.jetbrains.anko.runOnUiThread

//import sidev.lib.android.std._external._AnkoInternals.runOnUiThread

open class ContextLiveVar<T>(var ctx: Context){
    companion object{
        val DEFAULT= "default"
    }

    private var msg= DEFAULT
    var value: T?= null
        set(v){
            field= v
            invokeListener()
        }
    private var listener: ((value: T?, msg: String) -> Unit)?= null

    fun setVal(value: T?, msg: String= DEFAULT){
        this.msg= msg
        this.value= value
    }

    fun observe(l: (T?, msg: String) -> Unit){
        listener= l
        invokeListener()
    }
    fun observe(l: (T?) -> Unit){
        listener= {value, msg -> l(value)}
        invokeListener()
    }

    internal fun invokeListener(){
        ctx.runOnUiThread {
            listener?.invoke(value, msg)
//            loge("setVal() v= $value msg= $msg")
            msg= DEFAULT
        }
    }
}