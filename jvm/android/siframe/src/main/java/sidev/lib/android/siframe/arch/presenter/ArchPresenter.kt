package sidev.lib.android.siframe.arch.presenter

import android.content.Context
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.type.Mvp
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableLinkBase
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.check.asntNotNull
import sidev.lib.check.isNull
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
interface ArchPresenter<Req, Res, C: PresenterCallback<Req, Res>>: ExpirableLinkBase {
    enum class Direction{
        IN, OUT
    }
    override val expirable: ExpirableBase?
        get() = callback
    var callback: C?
    val reqCode: Req?
    /**
     * Diassert karena [ctx] digunakan hanya saat callback tidak sama dengan null.
     */
    val ctx: Context
        get()= callback!!._prop_ctx!! //App.ctx -> Gak jadi pake App.ctx karena hal tersebut tidak berlaku jika kelas Application dasar tidak meng-extend App pada framework ini.


    /**
     * Fungsi yang digunakan untuk memproses request yang dipanggil dari fungsi postRequest().
     */
    fun processRequest(request: Req, data: Map<String, Any>?)

    /**
     * Untuk mengecek integritas data yang didapat dari presenter.
     */
    fun checkDataIntegrity(request: Req, direction: Direction, data: Map<String, Any>?): Boolean

    /**
     * Semua instance PresenterCallback harus manggil ini kalo mau request ke presenter.
     */
    fun postRequest(request: Req, data: Map<String, Any>?= null){
//        this.reqCode= reqCode
        doWhenLinkNotExpired {
            if(checkDataIntegrity(request, Direction.OUT, data))
                processRequest(request, data)
            else
                DataIntegrityExc(this::class, "Pengecekan keluar di presenter")
        }.isNull {
            val clsName= javaClass.simpleName //classSimpleName()
            val callbackName= callback?.javaClass?.simpleName //classSimpleName()
            loge("$clsName.postRequest() callback= $callbackName.isExpired == TRUE")
            callback= null
        }
    }

    /**
     * @param request dapat digunakan untuk operasi presenter yg berbarengan sehingga this.reqCode dapat berganti sebelum
     *      dipass ke PresenterCallback
     *
     * Jika pada arsitektur MVI, [result] adalah hasil dari [request] dg tipe data [ViewIntent], dan [resCode] merupakan int kode hasil tersebut.
     * Jika pada arsitektur MVP, [result] dan [resCode] adalah hal yg sama.
     */
    fun postSucc(result: Res, data: Map<String, Any>?= null, resCode: Int= 0, request: Req?= null){
        doWhenLinkNotExpired {
            val sentReqCode= request ?: this.reqCode!!
            if(checkDataIntegrity(sentReqCode, Direction.IN, data))
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
                    callback!!.onPresenterSucc(sentReqCode, result, data, resCode)
                }
            else
                DataIntegrityExc(this::class, "Pengecekan masuk di presenter")
        }.isNull {
            val clsName= javaClass.simpleName //this.classSimpleName()
            val callbackName= callback?.javaClass?.simpleName //classSimpleName()
            loge("$clsName.postSucc() callback= $callbackName.isExpired == TRUE")
            callback= null
        }
    }

    /**
     * Jika pada arsitektur MVI, [result] adalah hasil dari [request] dg tipe data [ViewIntent], dan [resCode] merupakan int kode hasil tersebut.
     * Jika pada arsitektur MVP, [result] dan [resCode] adalah hal yg sama.
     */
    fun postFail(result: Res?= null, msg: String?= null, e: Exception?= null, resCode: Int= 0, request: Req?= null){
        doWhenLinkNotExpired {
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
                callback!!.onPresenterFail(sentReqCode, result, msg, e, resCode)
            }
        }.isNull {
            val clsName= javaClass.simpleName //this.classSimpleName()
            val callbackName= callback?.javaClass?.simpleName //classSimpleName()
            loge("$clsName.postFail() callback= $callbackName.isExpired == TRUE")
            callback= null
        }
    }
/*
    /**
     * Semua instance PresenterCallback harus manggil ini kalo mau request ke presenter.
     */
    fun postRequest(reqCode: T, data: Map<String, Any>?= null)
    /**
     * @param reqCode dapat digunakan untuk operasi presenter yg berbarengan sehingga this.reqCode dapat berganti sebelum
     *      dipass ke PresenterCallback
     */
    fun postSucc(resCode: Int, data: Map<String, Any>?, reqCode: T?= null)
    fun postFail(resCode: Int, msg: String?= null, e: Exception?= null, reqCode: T?= null)
 */
}