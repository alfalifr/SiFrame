package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase

/**
 * Interface yg dapat dipanggil oleh ArchPresenter pada framework ini.
 * Parameter <T> merupakan tipe data reqCode.
 */
interface PresenterCallback<T>: ExpirableBase {
//    var callbackCtx: Context?
//    val presenter: P?

    /**
     * @return nilainya ditangkap oleh SifViewModel
     */
    fun onPresenterSucc(reqCode: T, resCode: Int, data: Map<String, Any>?)
    fun onPresenterFail(reqCode: T, resCode: Int, msg: String?= "", e: Exception?= null)
/*
    @CallSuper
    fun downloadData(reqCode: String, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        presenter?.postRequest(reqCode, map)
    }
    @CallSuper
    fun uploadData(reqCode: String, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        presenter?.postRequest(reqCode, map)
    }
    @CallSuper
    fun sendRequest(reqCode: String, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        presenter?.postRequest(reqCode, map)
    }

    fun initPresenter(): P? {
        return null
    }
 */
}