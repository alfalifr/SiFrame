package sidev.lib.android.siframe.arch.view

import android.content.Context
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase
import sidev.lib.android.siframe.intfc.prop.CtxProp

/**
 * Hanya sbg penanda bahwa kelas turunan ini merupakan View dalam arsitektur.
 * Khusus untuk tipe arsitektur ini, interface ini tidak memiliki fungsi khusus.
 */
interface ArchView: InterruptableBase, CtxProp{
    /**
     * Flag apakah turunan interface ini sedang sibuk atau tidak.
     * Flag ini mempengaruhi [InterruptableBase.doWhenNotBusy].
     *
     * Hati-hati dalam memodifikasi nilai flag ini pada turunan [ArchView]
     * karena field ini sering dipakai pada framework ini.
     */
    override var isBusy: Boolean

    /**
     * Sebuah tag untuk menandakan bahwa interface [InterruptableBase] ini
     * sedang sibuk mengerjakan sesuai yg ada pada tag [busyOfWhat].
     *
     * Hati-hati dalam memodifikasi nilai flag ini pada turunan [ArchView]
     * karena field ini sering dipakai pada framework ini.
     */
    override var busyOfWhat: String

    //        get() = false
    override val isInterruptable: Boolean
        get() = true
}