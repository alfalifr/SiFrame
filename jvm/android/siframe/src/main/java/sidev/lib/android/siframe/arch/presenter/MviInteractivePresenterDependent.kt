package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.view.MviView
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

    //<4 Juli 2020> => val berubah jadi var karena [intentConverter] dapat diassign dari interface ini.
    var intentConverter: IntentConverter<I>?

    /**
     * Pada framework ini, vararg data dapat diwakilkan ke data
     * di dalam [reqCode] yg berupa <code><I: ViewIntent></code>.
     */
    @CallSuper
    fun downloadData(reqCode: I /*, vararg data: Pair<String, Any>*/) {
        doWhenNotBusy {
            //<4 Juli 2020> => Definisi baru.
            if(intentConverter == null){
                initIntentConverter()
                loge("${this.classSimpleName()}.downloadData() -> intentConverter di-assign dg default obj.")
            }
            intentConverter!!.postRequest(reqCode)
        }
/*
        <4 Juli 2020> => Definisi lama.
        try{ intentConverter!!.postRequest(reqCode) }
        catch (e: KotlinNullPointerException){
            val clsName= this.classSimpleName()
            throw RuntimeExc(commonMsg = "$clsName.downloadData()",
                detailMsg = "intentConverter == NULL. initIntentCoverter() blum dioverride.")
        }
 */
    }

    @CallSuper
    fun uploadData(reqCode: I /*, vararg data: Pair<String, Any>*/) {
        doWhenNotBusy {
            //<4 Juli 2020> => Definisi baru.
            if(intentConverter == null){
                initIntentConverter()
                loge("${this.classSimpleName()}.uploadData() -> intentConverter di-assign dg default obj.")
            }
            intentConverter!!.postRequest(reqCode)
        }
/*
        <4 Juli 2020> => Definisi lama.
        try{ intentConverter!!.postRequest(reqCode) }
        catch (e: KotlinNullPointerException){
            val clsName= this.classSimpleName()
            throw RuntimeExc(commonMsg = "$clsName.uploadData()",
                detailMsg = "intentConverter == NULL. initIntentCoverter() blum dioverride.")
        }
 */
    }

    @CallSuper
    fun sendRequest(reqCode: I /*, vararg data: Pair<String, Any>*/) {
        doWhenNotBusy {
            //<4 Juli 2020> => Definisi baru.
            if(intentConverter == null){
                initIntentConverter()
                loge("${this.classSimpleName()}.sendRequest() -> intentConverter di-assign dg default obj.")
            }
            intentConverter!!.postRequest(reqCode)
        }
    }

    private fun initIntentConverter(){
        intentConverter= IntentConverter(this, presenter)
        if(this is MviView<*, I>)
            intentConverter!!.stateProcessor= initStateProcessor()
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