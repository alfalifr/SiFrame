package sidev.lib.android.siframe.tool

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class ActLifecycleObs(val owner: LifecycleOwner): LifecycleObserver {
    init {
        owner.lifecycle.addObserver(this)
    }

    var detachWhenDestroy= true

    val onCreateFun= HashMap<String, (String, LifecycleOwner) -> Unit>()
    val onStartFun= HashMap<String, (String, LifecycleOwner) -> Unit>()
    val onResumeFun= HashMap<String, (String, LifecycleOwner) -> Unit>()
    val onPauseFun= HashMap<String, (String, LifecycleOwner) -> Unit>()
    val onStopFun= HashMap<String, (String, LifecycleOwner) -> Unit>()
    val onDestroyFun= HashMap<String, (String, LifecycleOwner) -> Unit>()

    private fun iterateFun(funMap: Map<String, (String, LifecycleOwner) -> Unit>){
        for((key, func) in funMap)
            func(key, owner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        iterateFun(onCreateFun)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        iterateFun(onStartFun)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        iterateFun(onResumeFun)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){
        iterateFun(onPauseFun)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        iterateFun(onStopFun)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        iterateFun(onDestroyFun)
        if(detachWhenDestroy)
            owner.lifecycle.removeObserver(this)
    }
}