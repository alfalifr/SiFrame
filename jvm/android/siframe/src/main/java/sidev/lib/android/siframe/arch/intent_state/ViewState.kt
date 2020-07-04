package sidev.lib.android.siframe.arch.intent_state

/**
 * <2 Juli 2020> => Sementara hanya sebagai penanda kelas turunan ini sbg State dalam arsitektur MVI.
 *
 * <4 Juli 2020> => Sudah memiliki field dasar, yaitu [isPreState] dan [isError]
 *
 * [isPreState] true jika kelas [ViewState] ini berada pada state sebelum hasil sesungguhnya keluar,
 *      atau dengan kata lain berada pada status buffering atau loading.
 * [isError] scr umum semua state memiliki ini agar lebih intuitif.
 */
abstract class ViewState{
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