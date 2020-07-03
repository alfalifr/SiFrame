package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.RestrictTo
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.classSimpleName
import sidev.lib.universal.`fun`.isNull
import java.lang.Exception
abstract class MviPresenter<S: ViewState>(
    callback: StateProcessor<S, *>? //PresenterCallback<I>?): Presenter(){
): Presenter(callback){

    final override fun postRequest(reqCode: String, data: Map<String, Any>?) {
        loge("MviPresenter.postRequest() MULAI")
        onPreRequest(reqCode, data)
        loge("MviPresenter.postRequest() onPreRequest SELESAI")
        super.postRequest(reqCode, data)
        loge("MviPresenter.postRequest() SELESAI")
    }

    fun onPreRequest(reqCode: String, data: Map<String, Any>?){
        doWhenExpNotExpired {
            (callback as? StateProcessor<S, *>)?.postPreResult(reqCode, data)
        }.isNull {
            val clsName= this.classSimpleName()
            val callbackName= callback?.classSimpleName()
            loge("$clsName.onPreRequest() callback= $callbackName.isExpired == TRUE")
            callback= null
        }
    }
}

/*
abstract class MviPresenter<S: ViewState, I: ViewIntent>(
    @RestrictTo(RestrictTo.Scope.LIBRARY) override var callback: StateProcessor<S, I>? //PresenterCallback<I>?
) : ArchPresenter<I, StateProcessor<S, I>>, Mvi {

    final override var reqCode: I?= null
        private set
/*
    val ctx: Context
        get()= App.ctx
 */

//    init{ ctx= callback.callbackCtx }

    /**
     * Fungsi yang digunakan untuk memproses request yang dipanggil dari fungsi postRequest().
     */
    abstract override fun processRequest(reqCode: I, data: Map<String, Any>?)

    /**
     * Untuk mengecek integritas data yang didapat dari presenter.
     */
    abstract override fun checkDataIntegrity(reqCode: I, direction: ArchPresenter.Direction, data: Map<String, Any>?): Boolean


    final override fun postRequest(reqCode: I, data: Map<String, Any>?) {
        this.reqCode= reqCode
        onPreRequest(reqCode, data)
        super.postRequest(reqCode, data)
    }

    final override fun postSucc(resCode: Int, data: Map<String, Any>?, reqCode: I?) {
        super.postSucc(resCode, data, reqCode)
    }

    final override fun postFail(resCode: Int, msg: String?, e: Exception?, reqCode: I?) {
        super.postFail(resCode, msg, e, reqCode)
    }

    fun onPreRequest(reqCode: I, data: Map<String, Any>?){
        callback?.postPreResult(reqCode, data)
    }
}
 */