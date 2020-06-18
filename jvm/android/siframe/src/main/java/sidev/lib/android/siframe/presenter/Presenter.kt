package sidev.lib.android.siframe.presenter

import android.content.Context
import sidev.lib.android.siframe.exception.DataIntegrityExc
import java.lang.Exception

/*
<Alif -> Amir, 2 Mei 2020> <Selesai:0> <baca> => Semua presenter pada pengembangan tahap 2 ini
disarankan memakai kelas ini sebagai superclass agar standar.
 */
abstract class Presenter (private val callback: PresenterCallback) {
    enum class Direction{
        IN, OUT
    }
    protected var reqCode: String= ""
        private set
    protected var ctx: Context?= null
        private set

    init{ ctx= callback.callbackCtx }

    /**
     * Fungsi yang digunakan untuk memproses request yang dipanggil dari fungsi postRequest().
     */
    protected abstract fun processRequest(reqCode: String, data: Map<String, Any>?)

    /**
     * Untuk mengecek integritas data yang didapat dari presenter.
     */
    protected abstract fun checkDataIntegrity(reqCode: String, direction: Direction, data: Map<String, Any>?): Boolean


    /**
     * Semua instance PresenterCallableFragAct harus manggil ini kalo mau request ke presenter.
     */
    fun postRequest(reqCode: String, data: Map<String, Any>?= null){
        this.reqCode= reqCode
        if(checkDataIntegrity(reqCode, Direction.OUT, data))
            processRequest(reqCode, data)
        else
            DataIntegrityExc(this::class.java, "Pengecekan keluar di presenter")
    }

    /**
     * @param reqCode dapat digunakan untuk operasi presenter yg berbarengan sehingga this.reqCode dapat berganti sebelum
     *      dipass ke PresenterCallback
     */
    fun postSucc(resCode: Int, data: Map<String, Any>?, reqCode: String?= null){
        val sentReqCode= reqCode ?: this.reqCode
        if(checkDataIntegrity(sentReqCode, Direction.IN, data))
            callback.onPresenterSucc(sentReqCode, resCode, data)
        else
            DataIntegrityExc(this::class.java, "Pengecekan masuk di presenter")
    }
    fun postFail(resCode: Int, msg: String?= null, e: Exception?= null, reqCode: String?= null){
        val sentReqCode= reqCode ?: this.reqCode
        callback.onPresenterFail(sentReqCode, resCode, msg, e)
    }
}