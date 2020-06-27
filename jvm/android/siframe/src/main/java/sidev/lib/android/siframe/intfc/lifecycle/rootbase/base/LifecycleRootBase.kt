package sidev.lib.android.siframe.intfc.lifecycle.rootbase.base

import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase

/**
 * <8 Juni 2020> => Sementara hanya sbg penanda (marker)
 * <8 Juni 2020> => Udah ada isinya
 *
 * Base Root untuk lifecycle. Inheritor hanya boleh mewarisi maks. 1 interface ini.
 */
interface LifecycleRootBase: LifecycleViewBase{
    fun ___initRootBase(vararg args: Any)
}