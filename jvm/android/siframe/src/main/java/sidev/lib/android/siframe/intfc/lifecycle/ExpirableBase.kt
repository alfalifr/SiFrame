package sidev.lib.android.siframe.intfc.lifecycle

import androidx.lifecycle.LifecycleObserver
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase

interface ExpirableBase : LifecycleBase, LifecycleObserver{
    val isExpired: Boolean
}