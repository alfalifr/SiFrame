package sidev.lib.android.siframe.intfc.listener

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

abstract class OnFailLifecycleBoundListener(val lifecycleOwner: LifecycleOwner): OnFailListener {
    override var tag: Any?= null
    private var onFail: ((resCode: Int, msg: String?, e: Exception?) -> Unit)? = null
///*
    override fun onFail(resCode: Int, msg: String?, e: Exception?) {
        if(lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) return
        onFail?.invoke(resCode, msg, e)
    }
// */
///*
    fun onFail(func: (resCode: Int, msg: String?, e: Exception?) -> Unit){
        onFail= func
    }
// */
}