package sidev.lib.android.siframe.intfc.lifecycle.rootbase.base

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleBase

/**
 * <8 Juni 2020> => Sementara hanya sbg penanda (marker)
 * <8 Juni 2020> => Udah ada isinya
 *
 * Base Root untuk lifecycle. Inheritor hanya boleh mewarisi maks. 1 interface ini.
 */
interface LifecycleRootBase: LifecycleBase{
    fun ___initRootBase(vararg args: Any)
}