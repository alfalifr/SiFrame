package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.RestrictTo
import sidev.lib.android.siframe.arch.annotation.ViewIntentFunction
import sidev.lib.android.siframe.arch.intent_state.*
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.*
import sidev.lib.universal.annotation.AnnotatedFunctionClassImpl
import java.lang.Exception


abstract class MviPresenter<I: ViewIntent, R: IntentResult, S: ViewState<*>>(
    @RestrictTo(RestrictTo.Scope.LIBRARY) override var callback: StateProcessor<I, R, S>? //PresenterCallback<I>?): Presenter(){
): AnnotatedFunctionClassImpl(), ArchPresenter<I, R, StateProcessor<I, R, S>> {
    final override lateinit var reqCode: I
        private set
/*
    private var intentPropGetter: IntentPropGetter?= null

    fun <I: ViewIntent> getEquivReqCode(
        intentClass: KClass<I>,
        defParamValFunc: ((KParameter) -> Any?)?= null
    ): String{
        if(intentPropGetter == null) intentPropGetter= IntentPropGetter()

        return intentPropGetter!!.getEquivReqCode(intentClass, defParamValFunc)
    }

    fun <I: ViewIntent> getResultIsTemporary(
        intentClass: KClass<I>,
        defParamValFunc: ((KParameter) -> Any?)?= null
    ): Boolean{
        if(intentPropGetter == null) intentPropGetter= IntentPropGetter()

        return intentPropGetter!!.getResultIsTemporary(intentClass, defParamValFunc)
    }
 */

    final override fun postRequest(request: I, data: Map<String, Any>?) {
        this.reqCode= request
        onPreRequest(request, data)
        super.postRequest(request, data)
    }

    /**
     * Sesuai namanya, param data diubah namanya jadi [additionalData] karena data request
     * dapat disertakan di dalam [request] yg berupa [ViewIntent].
     */
    abstract override fun processRequest(request: I, additionalData: Map<String, Any>?)

    /*
    final override fun postRequest(reqCode: String, data: Map<String, Any>?) {
        onPreRequest(reqCode, data)
        super.postRequest(reqCode, data)
    }
 */

    fun onPreRequest(reqCode: I, additionalData: Map<String, Any>?){
        doWhenLinkNotExpired {
            callback?.postPreResult(reqCode, additionalData)
/*
            callback.asNotNull { sp: StateProcessor<S, I> ->
                //Dg anggapan setiap request pada arsitektur MVI dalam framework ini
                // selalu memberikan info ttg apakah state yg dihasilkan dari request
                // bersifat sementara atau tidak.
/*
                val isStateTemporary=
                    try{ data!![INTENT_IS_RESULT_TEMPORARY] as Boolean }
                    catch (e: Exception){ false }
 */
                sp.postPreResult(reqCode, additionalData)
            }
 */
            //(callback as? StateProcessor<S, *>)?.postPreResult(reqCode, data, )
        }.isNull {
            val clsName= this.classSimpleName()
            val callbackName= callback?.classSimpleName()
            loge("$clsName.onPreRequest() callback= $callbackName.isExpired == TRUE")
            callback= null
        }
    }

    /** @return `true` jika operasi fungsi yg di-anotasi ditemukan dan berhasil dipanggil, `false` sebaliknya. */
    protected fun callAnnotatedViewIntentFun(intent: I): Boolean
        = callAnnotatedFunctionWithParamContainer(ViewIntentFunction::class, intent) { it.clazz == intent::class } != null


    final override fun postSucc(result: R, data: Map<String, Any>?, resCode: Int, request: I?) {
        super.postSucc(result, data, resCode, request)
    }

    final override fun postFail(result: R?, msg: String?, e: Exception?, resCode: Int, request: I?) {
        super.postFail(result, msg, e, resCode, request)
    }

    /*
    @PublishedApi
    internal var `access$intentPropGetter`: IntentPropGetter?
        get() = intentPropGetter
        set(v){
            intentPropGetter= v
        }
 */
}


/*
abstract class MviPresenter<S: ViewState>(
    callback: StateProcessor<S, *>? //PresenterCallback<I>?): Presenter(){
): Presenter(callback), Mvi {

    protected var intentPropGetter: IntentPropGetter?= null
        private set


    fun <I: ViewIntent> getEquivReqCode(
        intentClass: KClass<I>,
        defParamValFunc: ((KParameter) -> Any?)?= null
    ): String{
        if(intentPropGetter == null) intentPropGetter= IntentPropGetter()

        return intentPropGetter!!.getEquivReqCode(intentClass, defParamValFunc)
    }

    fun <I: ViewIntent> getResultIsTemporary(
        intentClass: KClass<I>,
        defParamValFunc: ((KParameter) -> Any?)?= null
    ): Boolean{
        if(intentPropGetter == null) intentPropGetter= IntentPropGetter()

        return intentPropGetter!!.getResultIsTemporary(intentClass, defParamValFunc)
    }


    final override fun postRequest(reqCode: String, data: Map<String, Any>?) {
        onPreRequest(reqCode, data)
        super.postRequest(reqCode, data)
    }

    fun onPreRequest(reqCode: String, data: Map<String, Any>?){
        doWhenLinkNotExpired {
            callback.asNotNull { sp: StateProcessor<S, *> ->
                //Dg anggapan setiap request pada arsitektur MVI dalam framework ini
                // selalu memberikan info ttg apakah state yg dihasilkan dari request
                // bersifat sementara atau tidak.
                val isStateTemporary=
                    try{ data!![INTENT_IS_RESULT_TEMPORARY] as Boolean }
                    catch (e: Exception){ false }
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
/*
    @PublishedApi
    internal var `access$intentPropGetter`: IntentPropGetter?
        get() = intentPropGetter
        set(v){
            intentPropGetter= v
        }
 */
}

 */

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