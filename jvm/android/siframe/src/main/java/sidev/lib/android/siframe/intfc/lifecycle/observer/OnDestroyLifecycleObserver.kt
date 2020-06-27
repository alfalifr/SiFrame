package sidev.lib.android.siframe.intfc.lifecycle.observer

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import sidev.lib.android.siframe.tool.manager.StaticManager

interface OnDestroyLifecycleObserver: LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy(callingOwner: LifecycleOwner){
        Log.e("StaticManager", "Lifecycle.Event.ON_DESTROY callingOwner::class.java.simpleName = ${callingOwner::class.java.simpleName}")
    }
}