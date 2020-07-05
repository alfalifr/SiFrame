package sidev.lib.android.siframe.arch.presenter

import sidev.lib.android.siframe.arch.intent_state.*
import sidev.lib.android.siframe.arch.intent_state.INTENT_IS_RESULT_TEMPORARY
import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.classSimpleName
import sidev.lib.universal.`fun`.isNull
import kotlin.reflect.KParameter

abstract class MviPresenter<S: ViewState>(
    callback: StateProcessor<S, *>? //PresenterCallback<I>?): Presenter(){
): Presenter(callback), Mvi {

    protected var intentPropGetter: IntentPropGetter?= null
        private set


    inline fun <reified I: ViewIntent> getEquivReqCode(
        noinline defParamValFunc: ((KParameter) -> Any?)?= null
    ): String{
        if(`access$intentPropGetter` == null)
            `access$intentPropGetter`= IntentPropGetter()
        return `access$intentPropGetter`!!.getEquivReqCode<I>(defParamValFunc)
    }

    inline fun <reified I: ViewIntent> getResultIsTemporary(
        noinline defParamValFunc: ((KParameter) -> Any?)?= null
    ): Boolean{
        if(`access$intentPropGetter` == null)
            `access$intentPropGetter`= IntentPropGetter()
        return `access$intentPropGetter`!!.getResultIsTemporary<I>(defParamValFunc)
    }


    final override fun postRequest(reqCode: String, data: Map<String, Any>?) {
//        loge("MviPresenter.postRequest() MULAI")
        onPreRequest(reqCode, data)
//        loge("MviPresenter.postRequest() onPreRequest SELESAI")
        super.postRequest(reqCode, data)
//        loge("MviPresenter.postRequest() SELESAI")
    }

    fun onPreRequest(reqCode: String, data: Map<String, Any>?){
        doWhenLinkNotExpired {
            callback.asNotNull { sp: StateProcessor<S, *> ->
                //Dg anggapan setiap request pada arsitektur MVI dalam framework ini
                // selalu memberikan info ttg apakah state yg dihasilkan dari request
                // bersifat sementara atau tidak.
                val isStateTemporary=
                    try{ data!![INTENT_IS_RESULT_TEMPORARY] as Boolean }
                    catch (e: Exception){
                        loge("reqCode= $reqCode isStateTemporary tidak ditemukan!")
                        false
                    }
                sp.postPreResult(reqCode, data, isStateTemporary)
            }
            //(callback as? StateProcessor<S, *>)?.postPreResult(reqCode, data, )
        }.isNull {
            val clsName= this.classSimpleName()
            val callbackName= callback?.classSimpleName()
            loge("$clsName.onPreRequest() callback= $callbackName.isExpired == TRUE")
            callback= null
        }
    }

    @PublishedApi
    internal var `access$intentPropGetter`: IntentPropGetter?
        get() = intentPropGetter
        set(v){
            intentPropGetter= v
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