package sidev.lib.android.siframe.arch.presenter

import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.type.Mvp
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.check.asntNotNull
import sidev.lib.exception.DataIntegrityExc
import java.lang.Exception

/**
 * Sbg penanda bahwa kelas turunan ini merupakan Presenter dalam arsitektur.
 * Parameter <T> adalah tipe data untuk reqCode.
 * Parameter <C> adalah tipe data untuk callback.
 *
 * Interface ini tidak meng-extend [Mvp] karena dapat digunakan pada berbagai macam arsitektur.
 *
 * <3 Juli 2020> => Sementara, parameter T dan C belum memiliki pengaruh signifikan pada fitur.
 *                  Namun, parameter tersebut memungkinkan programmer untuk memiliki tipe data
 *                  yg digunakan sbg requestCode.
 *
 * [Req] adalah tipe data yg digunakan untuk mengirim request,
 * [Res] adalah tipe data yg digunakan sebagai hasil respon dari [Req].
 */
interface MultipleCallbackArchPresenter<Req, Res, C: PresenterCallback<Req, Res>>
    : ArchPresenter<Req, Res, C> {
    /**
     * Fungsi yang digunakan untuk memproses request yang dipanggil dari fungsi postRequest().
     * Implementasi default pada interface ini yaitu memanggil [processRequest] dg callback
     * adalah [callback] utama interface ini.
     */
    override fun processRequest(request: Req, data: Map<String, Any>?){
        callback?.let { processRequest(it, request, data) }
    }
    /**
     * Fungsi yang digunakan untuk memproses request yang dipanggil dari fungsi postRequest()
     * yang menerima instance [callback] untuk request.
     */
    fun processRequest(callback: C, request: Req, data: Map<String, Any>?)

    /**
     * Semua instance PresenterCallback harus manggil ini kalo mau request ke presenter
     * yang menerima instance [callback] untuk request.
     */
    fun postRequest(callback: C, request: Req, data: Map<String, Any>?= null) {
        if(callback.isExpired) return

        if(checkDataIntegrity(request, ArchPresenter.Direction.OUT, data))
            processRequest(callback, request, data)
        else
            throw DataIntegrityExc(this::class, "Pengecekan keluar di presenter")
    }

    /**
     * @param request dapat digunakan untuk operasi presenter yg berbarengan sehingga this.reqCode dapat berganti sebelum
     *      dipass ke PresenterCallback
     *
     * Jika pada arsitektur MVI, [result] adalah hasil dari [request] dg tipe data [ViewIntent], dan [resCode] merupakan int kode hasil tersebut.
     * Jika pada arsitektur MVP, [result] dan [resCode] adalah hal yg sama.
     */
    @Suppress(SuppressLiteral.IMPLICIT_CAST_TO_ANY)
    fun postSucc(callback: C, result: Res, data: Map<String, Any>?= null, resCode: Int= 0, request: Req?= null) {
        if(callback.isExpired) return

        val sentReqCode= request ?: this.reqCode!!
        if(checkDataIntegrity(sentReqCode, ArchPresenter.Direction.IN, data))
            callback.asNotNull { c: MvpView ->
                val innerSentReqCode= try{ sentReqCode as String }
                catch (e: ClassCastException){
                    loge("reqCode bkn String, memanggil reqCode.toString()= $sentReqCode")
                    sentReqCode.toString()
                }
//                    loge("sentReqCode= $sentReqCode result= $result")
                c.onPresenterRes(innerSentReqCode, result as? Int ?: resCode, data,
                    false, null, null)
            }.asntNotNull<PresenterCallback<Req, Res>, MvpView> {
                callback.onPresenterSucc(sentReqCode, result, data, resCode)
            }
        else
            throw DataIntegrityExc(this::class, "Pengecekan masuk di presenter")
    }

    /**
     * Jika pada arsitektur MVI, [result] adalah hasil dari [request] dg tipe data [ViewIntent], dan [resCode] merupakan int kode hasil tersebut.
     * Jika pada arsitektur MVP, [result] dan [resCode] adalah hal yg sama.
     */
    fun postFail(callback: C, result: Res?= null, msg: String?= null, e: Exception?= null, resCode: Int= 0, request: Req?= null) {
        if(callback.isExpired) return

        val sentReqCode= request ?: this.reqCode!!
        callback.asNotNull { c: MvpView ->
            val innerSentReqCode= try{ sentReqCode as String }
            catch (e: ClassCastException){
                loge("reqCode bkn String, memanggil reqCode.toString()= $sentReqCode")
                sentReqCode.toString()
            }
            c.onPresenterRes(innerSentReqCode, result as? Int ?: resCode, null,
                true, msg, e)
        }.asntNotNull<PresenterCallback<Req, Res>, MvpView> {
            callback.onPresenterFail(sentReqCode, result, msg, e, resCode)
        }
    }
}