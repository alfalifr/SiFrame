package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.exception.RuntimeExc
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.classSimpleName

/**
 * Interface yg dapat berkomunikasi dengan presenternya menggunakan fungsi yg ada.
 */
interface MviInteractivePresenterDependent<P: Presenter, I: ViewIntent>
    : InteractivePresenterDependent<P, String, PresenterCallback<String>> {
//    var callbackCtx: Context?
//    val presenter: P?

    val intentConverter: IntentConverter<I>?

    /**
     * Pada framework ini, vararg data dapat diwakilkan ke data
     * di dalam [reqCode] yg berupa <code><I: ViewIntent></code>.
     */
    @CallSuper
    fun downloadData(reqCode: I /*, vararg data: Pair<String, Any>*/) {
        try{ intentConverter!!.postRequest(reqCode) }
        catch (e: KotlinNullPointerException){
            val clsName= this.classSimpleName()
            throw RuntimeExc(commonMsg = "$clsName.downloadData()",
                detailMsg = "intentConverter == NULL. initIntentCoverter() blum dioverride.")
        }
    }
    @CallSuper
    fun uploadData(reqCode: I /*, vararg data: Pair<String, Any>*/) {
        try{ intentConverter!!.postRequest(reqCode) }
        catch (e: KotlinNullPointerException){
            val clsName= this.classSimpleName()
            throw RuntimeExc(commonMsg = "$clsName.uploadData()",
                detailMsg = "intentConverter == NULL. initIntentCoverter() blum dioverride.")
        }
    }
    @CallSuper
    fun sendRequest(reqCode: I /*, vararg data: Pair<String, Any>*/) {
        loge("MviInteractivePresenterDependent.sendRequest() MULAI")
        intentConverter!!.postRequest(reqCode)
        loge("MviInteractivePresenterDependent.sendRequest() SELESAI")
    }
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