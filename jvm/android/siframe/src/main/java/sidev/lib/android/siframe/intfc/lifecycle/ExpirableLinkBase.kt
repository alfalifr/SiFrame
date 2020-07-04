package sidev.lib.android.siframe.intfc.lifecycle

import androidx.lifecycle.LifecycleObserver
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase

interface ExpirableLinkBase : LifecycleBase, LifecycleObserver{
    val expirable: ExpirableBase?
    fun <T> doWhenLinkNotExpired(func: () -> T): T?{
        return if(expirable?.isExpired?.not() == true) func()
        else null
    }
}