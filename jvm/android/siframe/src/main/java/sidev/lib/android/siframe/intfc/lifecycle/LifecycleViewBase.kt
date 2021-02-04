package sidev.lib.android.siframe.intfc.lifecycle

import androidx.annotation.StyleRes
import sidev.lib.android.siframe.`val`._SIF_Config
import sidev.lib.android.siframe.arch.view.ArchView

/**
 * <8 Juni 2020> => Sementara hanya sbg penanda (marker)
 *
 * Catatan pola pengembangan:
 *      - Fun yg berawalan dg _ menandakan init.
 *          TIDAK boleh dipanggil di luar framework
 *          Implementasi dapat dirubah di luar framework.
 *      - Fun yg berawalan dg __ menandakan internal.
 *          Dapat berupa init maupun tidak.
 *          TIDAK boleh dipanggil di luar framework
 *          Implementasi TIDAK dapat dirubah di luar framework.
 *      - Fun yg berawalan dg ___ menandakan init internal dan harus dipanggil.
 *          Pemanggilan TIDAK boleh di luar framework.
 *          Implementasi TIDAK dapat dirubah di luar framework.
 *
 *      - Properti atau fun yg berakhir dg _ berarti namanya udah ada yg ngembari pada framework asli Android.
 *
 * <10 Juli 2020> => [LifecycleViewBase] adalah turunan [ArchView].
 */
interface LifecycleViewBase: LifecycleBase, ArchView {
    val layoutId: Int
    @get:StyleRes
    val styleId: Int
        get() = _SIF_Config.STYLE_APP
}