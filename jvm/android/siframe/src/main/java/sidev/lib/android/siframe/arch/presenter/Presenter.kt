package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import androidx.annotation.RestrictTo
import sidev.lib.android.siframe.arch.type.Mvp
import sidev.lib.annotation.ChangeLog
import java.lang.Exception

/*
<27 Juni 2020> => Perubahan nama dari Presenter menjadi Repository. Perubahan nama menjadi Repository
                  mengisyaratkan bahwa kelas ini ditujukan untuk tugas pengambil / penyimpan
                  data baik scr lokal maupun remote.

<2 Juli 2020> => -Perubahan nama kembali menjadi Presenter. Perubahan nama tersebut tidak berarti
                   maksud dan tujuan penggunaan kelas ini berubah. Perubahan dilatar-belakangi karena
                   kelas ini memiliki properti dan fungsi yg krg mencukupi untuk dikatakan sbg Repository.
                   Namun, maksud dari kelas ini tetap hanya sebatas manajemen data, bkn logic maupun
                   pengatur view.
                 -callback menjadi public dg alasan agar dapat diubah dari luar kelas ini.
                   Namun, perubahan nilai callback dimaksudkan hanya dilakukan dalam framework ini.

<3 Juli 2020> => -Presenter bkn merupakan kelas root dari semua presenter pada framework ini.
                  Kelas root dari semua presenter yg ada pada framework ini adalah ArchPresenter.
                 -Skema inheritance kembali ke semula. Hal tersebut dikarenakan jika dipisahkan, maka
                  akan menyulitkan manajemen kode karena akan ada banyak kelas berbeda.
                 -Hasil akhir dari:
                    -Intent pada arsitektur MVI dalam framework ini berupa String,
                     bkn turunan kelas ViewIntent.
                    -Untuk mengakomodir kemurnian MVI, maka kelas ViewIntent tetap ada. Kemungkinan
                     akan ada kelas perantara yg menghubungkan antara Presenter biasa dg MviPresenter.
                     Kelas perantara tersebut terletak di dalam MviPresenter dan opsional.

 */
/**
 * Kelas dasar semua presenter yg ada pada framework ini.
 * Kelas [Presenter] tidak meng-extend interface [Mvp] karena kelas ini dapat
 * digunakan pada berbagai arsitektur.
 */
abstract class Presenter(
    callback: PresenterCallback<String, Int>?
): ArchPresenter<String, Int?, PresenterCallback<String, Int?>> {
    @ChangeLog("Jumat, 9 Okt 2020", "private -> protected agar turunan dapat memodif. Anggapannya programmer udah tau konsekuensinya ngedit reqCode di tempat yang salah.")
    final override var reqCode: String= ""
        protected set

    @Suppress("UNCHECKED_CAST")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    final override var callback: PresenterCallback<String, Int?>?
            = if(callback != null) callback as PresenterCallback<String, Int?> else null
/*
        set(v){
            if(v != null){
                if(v.clazz.memberFunctions.find { it.name == "onPresenterSucc" }!!
                        .parameters.find { it.name == "result" }!!
                        .type.isMarkedNullable)
                    throw TypeExc(msg = "Tipe data Presenter(\"${this::class.simpleName}\").PresenterCallback harus PresenterCallback<String, **Int**>, bukan PresenterCallback<String, **Int?**>")
            }
            field= v
        }
 */
/*
    val ctx: Context
        get()= App.ctx
 */

//    init{ ctx= callback.callbackCtx }

    /**
     * Fungsi yang digunakan untuk memproses request yang dipanggil dari fungsi postRequest().
     */
    abstract override fun processRequest(request: String, data: Map<String, Any>?)

    /**
     * Untuk mengecek integritas data yang didapat dari presenter.
     */
    abstract override fun checkDataIntegrity(request: String, direction: ArchPresenter.Direction, data: Map<String, Any>?): Boolean

    @CallSuper
    override fun postRequest(request: String, data: Map<String, Any>?) {
        this.reqCode= request
        super.postRequest(request, data)
    }


    final override fun postSucc(result: Int?, data: Map<String, Any>?, resCode: Int, request: String?) {
        super.postSucc(result, data, resCode, request)
    }

    final override fun postFail(
        result: Int?,
        msg: String?,
        e: Exception?,
        resCode: Int,
        request: String?
    ) {
        super.postFail(result, msg, e, resCode, request)
    }


    /*
    /**
     * Semua instance PresenterCallback harus manggil ini kalo mau request ke presenter.
     */
    final override fun postRequest(reqCode: String, data: Map<String, Any>?/*= null*/){
        this.reqCode= reqCode
        if(checkDataIntegrity(reqCode, ArchPresenter.Direction.OUT, data))
            processRequest(reqCode, data)
        else
            DataIntegrityExc(this::class.java, "Pengecekan keluar di presenter")
    }

    /**
     * @param reqCode dapat digunakan untuk operasi presenter yg berbarengan sehingga this.reqCode dapat berganti sebelum
     *      dipass ke PresenterCallback
     */
    final override fun postSucc(resCode: Int, data: Map<String, Any>?, reqCode: String?/*= null*/){
        val sentReqCode= reqCode ?: this.reqCode
        if(checkDataIntegrity(sentReqCode, Direction.IN, data)){
            if(callback?.isExpired == false)
                callback!!.onPresenterSucc(sentReqCode, resCode, data)
            else{
                loge("callback: \"${callback?.classSimpleName()}\" sudah kadaluwarsa")
                callback= null
            }
        } else
            DataIntegrityExc(this::class.java, "Pengecekan masuk di presenter")
    }
    final override fun postFail(resCode: Int, msg: String?/*= null*/, e: Exception?/*= null*/, reqCode: String?/*= null*/){
        if(callback?.isExpired == false){
            val sentReqCode= reqCode ?: this.reqCode
            callback!!.onPresenterFail(sentReqCode, resCode, msg, e)
        } else{
            loge("callback: \"${callback?.classSimpleName()}\" sudah kadaluwarsa")
            callback= null
        }
    }
 */
}