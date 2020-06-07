package sidev.lib.android.siframe.presenter

import android.content.Context
import androidx.annotation.CallSuper

interface PresenterCallback {
    var callbackCtx: Context?
    var presenter: Presenter?

    fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?)
    fun onPresenterFail(reqCode: String, resCode: Int, msg: String?= "", e: Exception?= null)
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

    fun initPresenter(): Presenter? {
        return null
    }
}