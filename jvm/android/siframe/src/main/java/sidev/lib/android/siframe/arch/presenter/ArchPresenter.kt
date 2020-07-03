package sidev.lib.android.siframe.arch.presenter

import android.content.Context
import sidev.lib.android.siframe.exception.DataIntegrityExc
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableLinkBase
import sidev.lib.android.siframe.lifecycle.app.App
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.classSimpleName
import sidev.lib.universal.`fun`.isNull
import java.lang.Exception

/**
 * Sbg penanda bahwa kelas turunan ini merupakan Presenter dalam arsitektur.
 * Parameter <T> adalah tipe data untuk reqCode.
 * Parameter <C> adalah tipe data untuk callback.
 *
 * <3 Juli 2020> => Sementara, parameter T dan C belum memiliki pengaruh signifikan pada fitur.
 *                  Namun, parameter tersebut memungkinkan programmer untuk memiliki tipe data
 *                  yg digunakan sbg requestCode.
 */
interface ArchPresenter<T, C: PresenterCallback<T>>: ExpirableLinkBase {
    enum class Direction{
        IN, OUT
    }
    override val expirable: ExpirableBase?
        get() = callback
    var callback: C?
    val reqCode: T?
    val ctx: Context
        get()= App.ctx

    /**
     * Fungsi yang digunakan untuk memproses request yang dipanggil dari fungsi postRequest().
     */
    fun processRequest(reqCode: T, data: Map<String, Any>?)

    /**
     * Untuk mengecek integritas data yang didapat dari presenter.
     */
    fun checkDataIntegrity(reqCode: T, direction: Direction, data: Map<String, Any>?): Boolean

    /**
     * Semua instance PresenterCallback harus manggil ini kalo mau request ke presenter.
     */
    fun postRequest(reqCode: T, data: Map<String, Any>?= null){
//        this.reqCode= reqCode
        doWhenExpNotExpired {
            if(checkDataIntegrity(reqCode, Direction.OUT, data))
                processRequest(reqCode, data)
            else
                DataIntegrityExc(this::class.java, "Pengecekan keluar di presenter")
        }.isNull {
            val clsName= this.classSimpleName()
            val callbackName= callback?.classSimpleName()
            loge("$clsName.postRequest() callback= $callbackName.isExpired == TRUE")
            callback= null
        }
    }

    /**
     * @param reqCode dapat digunakan untuk operasi presenter yg berbarengan sehingga this.reqCode dapat berganti sebelum
     *      dipass ke PresenterCallback
     */
    fun postSucc(resCode: Int, data: Map<String, Any>?, reqCode: T?= null){
        doWhenExpNotExpired {
            val sentReqCode= reqCode ?: this.reqCode!!
            if(checkDataIntegrity(sentReqCode, Direction.IN, data))
                callback!!.onPresenterSucc(sentReqCode, resCode, data)
            else
                DataIntegrityExc(this::class.java, "Pengecekan masuk di presenter")
        }.isNull {
            val clsName= this.classSimpleName()
            val callbackName= callback?.classSimpleName()
            loge("$clsName.postSucc() callback= $callbackName.isExpired == TRUE")
            callback= null
        }
    }
    fun postFail(resCode: Int, msg: String?= null, e: Exception?= null, reqCode: T?= null){
        doWhenExpNotExpired {
            val sentReqCode= reqCode ?: this.reqCode!!
            callback!!.onPresenterFail(sentReqCode, resCode, msg, e)
        }.isNull {
            val clsName= this.classSimpleName()
            val callbackName= callback?.classSimpleName()
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