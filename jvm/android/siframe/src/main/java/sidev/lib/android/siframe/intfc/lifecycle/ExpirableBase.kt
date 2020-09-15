package sidev.lib.android.siframe.intfc.lifecycle

import androidx.lifecycle.LifecycleObserver
import sidev.lib.android.siframe.tool.util.`fun`.loge

interface ExpirableBase : LifecycleBase, LifecycleObserver{
    val isExpired: Boolean
    fun <T> doWhenNotExpired(func: () -> T): T?{
        return if(!isExpired) func()
        else {
            loge("ExpirableBase: ${javaClass.simpleName} isExpired == TRUE")
            null
        }
    }
}