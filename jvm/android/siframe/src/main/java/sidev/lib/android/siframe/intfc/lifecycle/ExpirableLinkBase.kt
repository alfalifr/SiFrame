package sidev.lib.android.siframe.intfc.lifecycle

import androidx.lifecycle.LifecycleObserver
import sidev.lib.android.std.tool.util.`fun`.loge

interface ExpirableLinkBase : LifecycleBase, LifecycleObserver{
    val expirable: ExpirableBase?
    fun <T> doWhenLinkNotExpired(func: () -> T): T?{
        loge("expirable?.isExpired?.not()= ${expirable?.isExpired?.not()}")
        return if(expirable?.isExpired?.not() == true) func()
        else null
    }
}