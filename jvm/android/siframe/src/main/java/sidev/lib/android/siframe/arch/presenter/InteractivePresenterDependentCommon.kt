package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase

/**
 * INterface yg dapat berkomunikasi dengan presenternya menggunakan fungsi yg ada.
 */
interface InteractivePresenterDependentCommon<P: Presenter>
    : InteractivePresenterDependent<P, String, PresenterCallback<String>> {
//    var callbackCtx: Context?
//    val presenter: P?
/*
    @CallSuper
    fun downloadData(reqCode: R, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        presenter?.postRequest(reqCode, map)
    }
    @CallSuper
    fun uploadData(reqCode: R, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        presenter?.postRequest(reqCode, map)
    }
    @CallSuper
    fun sendRequest(reqCode: R, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        presenter?.postRequest(reqCode, map)
    }
/*
    fun initPresenter(): P? {
        return null
    }
 */

 */
}