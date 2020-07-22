package sidev.lib.android.siframe.arch.intent_state

import androidx.annotation.Keep
import sidev.lib.android.siframe.arch.presenter.ArchPresenter
import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.universal.exception.Exc

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
abstract class ViewState<R: IntentResult>: Mvi{
    /**
     * Hasil dari request oleh [ViewIntent], null jika terjadi error.
     * Jika nilai property ini tidak null, maka [error] harus null agar tidak terjadi kontradiksi.
     * Namun, ada satu kondisi saat [result] dan [error] null bersamaan, yaitu saat [isPreState] == true.
     */
    var result: R?= null
    /**
     * Jika request oleh [ViewIntent] terjadi kesalahan, null jika request berjalan sprti semestinya.
     * Jika nilai property ini tidak null, maka [result] harus null agar tidak terjadi kontradiksi.
     */
    var error: Exc?= null
        internal set
    /**
     * true jika kelas [ViewState] ini berada pada state sebelum hasil sesungguhnya keluar,
     *      atau dengan kata lain berada pada status buffering atau loading.
     *
     * Nilai dari field ini hanya dapat diubah di dalam framework ini.
     */
    var isPreState: Boolean= false
        internal set
/*
    <22 Juli 2020> => Informasi ttg error ada di IntentResult.
    /**
     * Knp kok abstrak?
     * Agar programmer dapat menentukan sendiri bagaimana error yg terjadi pada kodenya.
     */
    abstract var isError: Boolean //= false
 */
}

/**
 * Nilai dari field ini harus sama dg nama field [ViewState.result].
 * Jika tidak sama, dpat menyebabkan malfungsi pada framework ini.
 */
internal const val STATE_RESULT= "result"