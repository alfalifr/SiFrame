package sidev.lib.android.siframe.arch.intent_state

import sidev.lib.universal.`fun`.classSimpleName
import sidev.lib.universal.`fun`.getSealedClassName
import sidev.lib.universal.tool.util.StringUtil

/**
 * <2 Juli 2020> => Sementara hanya sebagai penanda kelas turunan ini sbg Intent dalam arsitektur MVI.
 */
abstract class ViewIntent{
    open val equivalentReqCode: String
        = StringUtil.toSnakeCase(this.classSimpleName(), true) //this.getSealedClassName(true)!!

    /**
     * Flag apakah [ViewState] yg dihasilkan oleh [ViewIntent] ini bersifat sementara atau tidak.
     * Jika bersifat sementara, maka [ViewState] yg dihasilkan tidak akan disimpan pada [StateProcessor.currentState].
     * sehingga saat fungsi [StateProcessor.restoreCurrentState] dipanggil [ViewState] hasil
     * [ViewIntent] ini tidak ditampilkan pada layar.
     */
    open val isResultTemporary: Boolean
        = false
//    fun getReqCode()= equivalentReqCode
}

/*
Knp kok gak pake refleksi aja?
Karena operasinya akan membebani processor HP.
 */
/**
 * Nilai dari field ini harus sama dg nama field [ViewIntent.equivalentReqCode].
 * Jika tidak sama, dpat menyebabkan malfungsi pada framework ini.
 */
internal const val INTENT_EQUIVALENT_REQ_CODE= "equivalentReqCode"

/**
 * Nilai dari field ini harus sama dg nama field [ViewIntent.isResultTemporary].
 * Jika tidak sama, dpat menyebabkan malfungsi pada framework ini.
 */
internal const val INTENT_IS_RESULT_TEMPORARY= "isResultTemporary"