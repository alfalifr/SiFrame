package sidev.lib.android.siframe.repository

import android.content.Context
import sidev.lib.android.siframe.exception.DataIntegrityExc
import sidev.lib.android.siframe.lifecycle.app.App
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.classSimpleName
import java.lang.Exception

/*
<27 Juni 2020> => Perubahan nama dari Presenter menjadi Repository. Perubahan nama menjadi Repository
                  mengisyaratkan bahwa kelas ini ditujukan untuk tugas pengambil / penyimpan
                  data baik scr lokal maupun remote.
 */
abstract class Repository (private var callback: RepositoryCallback?) {
    enum class Direction{
        IN, OUT
    }
    var reqCode: String= ""
        private set
    val ctx: Context
        get()= App.ctx

//    init{ ctx= callback.callbackCtx }

    /**
     * Fungsi yang digunakan untuk memproses request yang dipanggil dari fungsi postRequest().
     */
    protected abstract fun processRequest(reqCode: String, data: Map<String, Any>?)

    /**
     * Untuk mengecek integritas data yang didapat dari presenter.
     */
    protected abstract fun checkDataIntegrity(reqCode: String, direction: Direction, data: Map<String, Any>?): Boolean


    /**
     * Semua instance PresenterCallback harus manggil ini kalo mau request ke presenter.
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
        if(checkDataIntegrity(sentReqCode, Direction.IN, data)){
            if(callback?.isExpired == false)
                callback!!.onRepoSucc(sentReqCode, resCode, data)
            else{
                loge("callback: \"${callback?.classSimpleName()}\" sudah kadaluwarsa")
                callback= null
            }
        } else
            DataIntegrityExc(this::class.java, "Pengecekan masuk di presenter")
    }
    fun postFail(resCode: Int, msg: String?= null, e: Exception?= null, reqCode: String?= null){
        if(callback?.isExpired == false){
            val sentReqCode= reqCode ?: this.reqCode
            callback!!.onRepoFail(sentReqCode, resCode, msg, e)
        } else{
            loge("callback: \"${callback?.classSimpleName()}\" sudah kadaluwarsa")
            callback= null
        }
    }
}