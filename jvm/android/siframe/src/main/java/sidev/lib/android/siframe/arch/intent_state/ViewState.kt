package sidev.lib.android.siframe.arch.intent_state

import androidx.annotation.Keep
import sidev.lib.android.siframe.arch.type.Mvi

/**
 * <2 Juli 2020> => Sementara hanya sebagai penanda kelas turunan ini sbg State dalam arsitektur MVI.
 *
 * <4 Juli 2020> => Sudah memiliki field dasar, yaitu [isPreState] dan [isError]
 *
 * [isPreState] true jika kelas [ViewState] ini berada pada state sebelum hasil sesungguhnya keluar,
 *      atau dengan kata lain berada pada status buffering atau loading.
 * [isError] scr umum semua state memiliki ini agar lebih intuitif.
 */
//@Keep -> Dari proguard-rules.pro aja biar bisa nambah opsi ,allowoptimization,allowshrinking
abstract class ViewState: Mvi{
    /**
     * true jika kelas [ViewState] ini berada pada state sebelum hasil sesungguhnya keluar,
     *      atau dengan kata lain berada pada status buffering atau loading.
     *
     * Nilai dari field ini hanya dapat diubah di dalam framework ini.
     */
    var isPreState: Boolean= false
        internal set

    /**
     * Knp kok abstrak?
     * Agar programmer dapat menentukan sendiri bagaimana error yg terjadi pada kodenya.
     */
    abstract var isError: Boolean //= false
}