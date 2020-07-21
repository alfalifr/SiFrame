package sidev.lib.android.siframe.arch.intent_state

import android.view.View
import sidev.lib.android.siframe.arch.presenter.ArchPresenter
import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.android.siframe.arch.view.AutoRestoreViewClient
import sidev.lib.android.siframe.arch.view.AutoRestoreViewOwner
import sidev.lib.universal.`fun`.classSimpleName
import sidev.lib.universal.tool.util.StringUtil

/**
 * <2 Juli 2020> => Sementara hanya sebagai penanda kelas turunan ini sbg Intent dalam arsitektur MVI.
 *
 *
 */
//@Keep -> Dari proguard-rules.pro aja biar bisa nambah opsi ,allowoptimization,allowshrinking

/**
 * <21 Juli 2020> => Untuk pengemebangan ke depannya. Digunakan untuk [ArchPresenter.postSucc] dan [ArchPresenter.postFail].
 */
open class IntentResult: Mvi

open class ViewIntent: Mvi{
    open val equivalentReqCode: String
        = StringUtil.toSnakeCase(this.classSimpleName(), true) //this.getSealedClassName(true)!!

    /**
     * Flag apakah [ViewState] yg dihasilkan oleh [ViewIntent] ini bersifat sementara atau tidak.
     * Jika bersifat sementara, maka [ViewState] yg dihasilkan tidak akan disimpan pada [StateProcessor.currentViewState].
     * sehingga saat fungsi [StateProcessor.restoreCurrentState] dipanggil [ViewState] hasil
     * [ViewIntent] ini tidak ditampilkan pada layar.
     */
    open val isResultTemporary: Boolean = false
//    fun getReqCode()= equivalentReqCode

    final override fun equals(other: Any?): Boolean
        = if(other is ViewIntent) equivalentReqCode == other.equivalentReqCode
        else super.equals(other)

    final override fun hashCode(): Int = equivalentReqCode.hashCode()

    override fun toString(): String
        = "${this::class.simpleName ?: "ViewIntent"} equivalentReqCode= $equivalentReqCode, isResultTemporary= $isResultTemporary"
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


/**
 * Kelas ViewIntent yg digunakan scr internal dalam framework ini untuk memberikan
 * sinyal berupa [Register] untuk melakukan [AutoRestoreViewClient.registerAutoRestoreView]
 * dan [ExtractAll] untuk melakukan [AutoRestoreViewOwner.extractAllViewContent].
 */
sealed class AutoRestoreIntent{
    data class Register(val id: String, val view: View): AutoRestoreIntent()
    object ExtractAll: AutoRestoreIntent()
}