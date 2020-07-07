package sidev.lib.android.siframe.intfc.lifecycle

import sidev.lib.android.siframe._customizable._Config

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
 */
interface LifecycleViewBase: LifecycleBase{
    val layoutId: Int
    val styleId: Int
        get() = _Config.STYLE_APP
}