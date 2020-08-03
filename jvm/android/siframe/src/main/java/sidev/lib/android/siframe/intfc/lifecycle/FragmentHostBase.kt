package sidev.lib.android.siframe.intfc.lifecycle

import androidx.fragment.app.FragmentManager
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.intfc.prop.FragmentManagerProp

/**
 * [_prop_fm] merupakan FragmentManager yg digunakan untuk me-manage fragment yg menempel di dalam [FragmentHostBase] ini.
 * [_prop_fm] bkn merupakan FragmentManger yg digunakan oleh parent.
 */
interface FragmentHostBase : LifecycleBase, FragmentManagerProp