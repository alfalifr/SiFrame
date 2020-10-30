package sidev.lib.android.siframe.arch.presenter

import sidev.lib.android.siframe.arch.intent_state.*
import sidev.lib.reflex.annotation.NativeAnnotatedFunctionClassDef
//import sidev.lib.universal.`fun`.*
//import sidev.lib.universal.annotation.AnnotatedFunctionClassImpl
import java.lang.Exception


abstract class MultipleCallbackMviPresenter<I: ViewIntent, R: IntentResult, S: ViewState<*>>(
    callback: StateProcessor<I, R, S>? //PresenterCallback<I>?): Presenter(){
): MviPresenter<I, R, S>(callback), MultipleCallbackArchPresenter<I, R, StateProcessor<I, R, S>>,
    //TODO: <Jumat, 2 Okt 2020> => [NativeAnnotatedFunctionClassDef] Sebaiknya ini dipisahkan saja ke kelas MviPresenter tipe baru.
    //  biar gak berat ukurannya.
    NativeAnnotatedFunctionClassDef { //AnnotatedFunctionClassImpl()
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
    /**
     * Semua instance PresenterCallback harus manggil ini kalo mau request ke presenter
     * yang menerima instance [callback] untuk request.
     */
    final override fun postRequest(
        callback: StateProcessor<I, R, S>,
        request: I,
        data: Map<String, Any>?
    ) {
        this.reqCode= request
        onPreRequest(callback, request, data)
        super<MultipleCallbackArchPresenter>.postRequest(callback, request, data)
    }

    /**
     * Fungsi yang digunakan untuk memproses request yang dipanggil dari fungsi postRequest()
     * yang menerima instance [callback] untuk request.
     *
     * Param [data] merupaka data tambahan karena data rwuest disertakan di dalam [request]
     * yang berupa [ViewIntent].
     */
    abstract override fun processRequest(
        request: PresenterRequest<I, R>,
        data: Map<String, Any>?
    )

    /*
    final override fun postRequest(reqCode: String, data: Map<String, Any>?) {
        onPreRequest(reqCode, data)
        super.postRequest(reqCode, data)
    }
 */

    fun onPreRequest(callback: StateProcessor<I, R, S>, reqCode: I, additionalData: Map<String, Any>?){
        if(callback.isExpired) return

        callback.postPreResult(reqCode, additionalData)
    }


    /**
     * @param request dapat digunakan untuk operasi presenter yg berbarengan sehingga this.reqCode dapat berganti sebelum
     *      dipass ke PresenterCallback
     *
     * Jika pada arsitektur MVI, [result] adalah hasil dari [request] dg tipe data [ViewIntent], dan [resCode] merupakan int kode hasil tersebut.
     * Jika pada arsitektur MVP, [result] dan [resCode] adalah hal yg sama.
     */
    final override fun postSucc(
//        callback: StateProcessor<I, R, S>,
        request: PresenterRequest<I, R>,
        result: R,
        data: Map<String, Any>?,
        resCode: Int
    ) {
        super<MultipleCallbackArchPresenter>.postSucc(request, result, data, resCode)
    }

    /**
     * Jika pada arsitektur MVI, [result] adalah hasil dari [request] dg tipe data [ViewIntent], dan [resCode] merupakan int kode hasil tersebut.
     * Jika pada arsitektur MVP, [result] dan [resCode] adalah hal yg sama.
     */
    final override fun postFail(
//        callback: StateProcessor<I, R, S>,
        request: PresenterRequest<I, R>,
        result: R?,
        msg: String?,
        e: Exception?,
        resCode: Int
    ) {
        super<MultipleCallbackArchPresenter>.postFail(request, result, msg, e, resCode)
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