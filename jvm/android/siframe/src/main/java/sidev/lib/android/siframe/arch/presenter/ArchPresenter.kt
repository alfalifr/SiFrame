package sidev.lib.android.siframe.arch.presenter

import android.content.Context
import sidev.lib.android.siframe.arch.type.Mvp
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.exception.DataIntegrityExc
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableLinkBase
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.asntNotNull
import sidev.lib.universal.`fun`.classSimpleName
import sidev.lib.universal.`fun`.isNull
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
 */
interface ArchPresenter<T, C: PresenterCallback<T>>: ExpirableLinkBase {
    enum class Direction{
        IN, OUT
    }
    override val expirable: ExpirableBase?
        get() = callback
    var callback: C?
    val reqCode: T?
    /**
     * Diassert karena [ctx] digunakan hanya saat callback tidak sama dengan null.
     */
    val ctx: Context
        get()= callback!!._prop_ctx!! //App.ctx -> Gak jadi pake App.ctx karena hal tersebut tidak berlaku jika kelas Application dasar tidak meng-extend App pada framework ini.


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
        doWhenLinkNotExpired {
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
        doWhenLinkNotExpired {
            val sentReqCode= reqCode ?: this.reqCode!!
            if(checkDataIntegrity(sentReqCode, Direction.IN, data))
                callback.asNotNull { c: MvpView ->
                    val innerSentReqCode= try{ sentReqCode as String }
                        catch (e: ClassCastException){
                            loge("reqCode bkn String, memanggil reqCode.toString()= $sentReqCode")
                            sentReqCode.toString()
                        }
                    c.onPresenterRes(innerSentReqCode, resCode, data,
                        false, null, null)
                }.asntNotNull<PresenterCallback<T>, MvpView> {
                    callback!!.onPresenterSucc(sentReqCode, resCode, data)
                }
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
        doWhenLinkNotExpired {
            val sentReqCode= reqCode ?: this.reqCode!!
            callback.asNotNull { c: MvpView ->
                val innerSentReqCode= try{ sentReqCode as String }
                    catch (e: ClassCastException){
                        loge("reqCode bkn String, memanggil reqCode.toString()= $sentReqCode")
                        sentReqCode.toString()
                    }
                c.onPresenterRes(innerSentReqCode, resCode, null,
                    true, msg, e)
            }.asntNotNull<PresenterCallback<T>, MvpView> {
                callback!!.onPresenterFail(sentReqCode, resCode, msg, e)
            }
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