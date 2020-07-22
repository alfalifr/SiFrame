package sidev.lib.android.siframe.arch.presenter

import android.content.Context
import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.prop.CtxProp

/**
 * Interface yg dapat dipanggil oleh ArchPresenter pada framework ini.
 * [Req] merupakan tipe data reqCode, [Res] tipe data resCode.
 */
interface PresenterCallback<Req, Res>: ExpirableBase, CtxProp {
//    var callbackCtx: Context
//    val presenter: P?

    /**
     * @return nilainya ditangkap oleh SifViewModel
     */
    fun onPresenterSucc(request: Req, result: Res, data: Map<String, Any>?, resCode: Int= 0)
    fun onPresenterFail(request: Req, result: Res?, msg: String?= "", e: Exception?= null, resCode: Int= 0)
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