package sidev.lib.android.siframe.intfc.lifecycle

import sidev.lib.android.siframe.tool.util.`fun`.loge

/**
 * Interface ini digunakan untuk menahan interaksi dari user.
 * Sehingga user harus menunggu agar proses yg berjalan hingga selesai.
 * Interface ini tidak dapat digunakan untuk manajemen antrian Thread.
 */
interface InterruptableBase {
    /**
     * Sbg flag jika interface ini sedang sibuk atau tidak.
     */
    val isBusy: Boolean

    /**
     * Apakah interface ini dapat di-interrupt atau tidak saat sibuk.
     */
    val isInterruptable: Boolean

    fun doWhenNotBusy(func: () -> Unit){
        if(isInterruptable || !isBusy)
            func()
        else
            onInterruptedWhenBusy()
    }
    fun onInterruptedWhenBusy(){
        loge("InterruptableBase.onInterruptedWhenBusy() -> proses ditahan karena masih sibuk")
    }
}