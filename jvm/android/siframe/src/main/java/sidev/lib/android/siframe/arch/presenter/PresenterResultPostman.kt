package sidev.lib.android.siframe.arch.presenter

import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import java.lang.Exception

/**
 * Interface akar yang dapat melakukan posting terhadap hasil dari [ArchPresenter], baik itu berhasil maupun gagal.
 */
interface PresenterResultPostman<Req, Res> {

    /**
     * @param request dapat digunakan untuk operasi presenter yg berbarengan sehingga this.reqCode dapat berganti sebelum
     *      dipass ke PresenterCallback
     *
     * Jika pada arsitektur MVI, [result] adalah hasil dari [request] dg tipe data [ViewIntent], dan [resCode] merupakan int kode hasil tersebut.
     * Jika pada arsitektur MVP, [result] dan [resCode] adalah hal yg sama.
     */
    @Suppress(SuppressLiteral.IMPLICIT_CAST_TO_ANY)
    fun postSucc(result: Res, data: Map<String, Any>?= null, resCode: Int= 0, request: Req?= null)

    /**
     * Jika pada arsitektur MVI, [result] adalah hasil dari [request] dg tipe data [ViewIntent], dan [resCode] merupakan int kode hasil tersebut.
     * Jika pada arsitektur MVP, [result] dan [resCode] adalah hal yg sama.
     */
    fun postFail(result: Res?= null, msg: String?= null, e: Exception?= null, resCode: Int= 0, request: Req?= null)
}