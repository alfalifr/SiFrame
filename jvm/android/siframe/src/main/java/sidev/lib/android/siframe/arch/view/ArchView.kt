package sidev.lib.android.siframe.arch.view

import android.content.Context
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase
import sidev.lib.android.siframe.intfc.prop.CtxProp

/**
 * Hanya sbg penanda bahwa kelas turunan ini merupakan View dalam arsitektur.
 * Khusus untuk tipe arsitektur ini, interface ini tidak memiliki fungsi khusus.
 */
interface ArchView: InterruptableBase, CtxProp{
    override var isBusy: Boolean
//        get() = false
    override val isInterruptable: Boolean
        get() = true
}