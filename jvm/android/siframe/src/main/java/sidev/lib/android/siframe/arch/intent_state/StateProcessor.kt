package sidev.lib.android.siframe.arch.intent_state

import android.content.Context
import org.jetbrains.anko.runOnUiThread
import sidev.lib.android.siframe.arch.presenter.PresenterCallback
import sidev.lib.android.siframe.arch.presenter.PresenterCallbackCommon
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.arch.viewmodel.LifeData
import sidev.lib.android.siframe.exception.RuntimeExc
import sidev.lib.android.siframe.lifecycle.app.App
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.*
import kotlin.reflect.KParameter

abstract class StateProcessor<S: ViewState, I: ViewIntent>(var view: MviView<S, I>):
//    PresenterDependent<MviPresenter<S>>,
    PresenterCallbackCommon {
    internal var currentState= LifeData<S>() //?= null
    internal var currentStateIsPreState= LifeData<Boolean>() // false
    internal var intentConverter: IntentConverter<I>?= null //Untuk dapat mendapatkan pemetaan
                //antara ViewIntent dan reqCode.
    protected var intentEquivReqCodeGetter: IntentEquivReqCodeGetter?= null
//        internal set
//    protected var intentObj: HashMap<String, ViewIntent>?= null

//    protected var previousState: S?= null

    final override val isExpired: Boolean
        get()= view.isExpired
    override val _prop_ctx: Context?
        get() = view._prop_ctx

    abstract fun processPreState(reqCode: String, data: Map<String, Any>?): S?
    abstract fun processState(reqCode: String, resCode: Int, data: Map<String, Any>?,
                              isError: Boolean, exc: Exception?, errorMsg: String?): S?


    /**
     * @return null jika nama gak ketemu dalam instance [intentConverter.equivReqCodeMap]
     */
    fun getEquivViewIntentName(reqCode: String): String?{
        return intentConverter.notNullTo {
            var className: String?= null
            for(pair in it.equivReqCodeMap){
                if(pair.value == reqCode){
                    className= pair.key
                    break
                }
            }
            className
        }
    }

    inline fun <reified I: ViewIntent> getEquivReqCode(
        noinline defParamValFunc: ((KParameter) -> Any?)?= null
    ): String{
        if(`access$intentEquivReqCodeGetter` == null)
            `access$intentEquivReqCodeGetter`= IntentEquivReqCodeGetter()
        return `access$intentEquivReqCodeGetter`!!.getEquivReqCode<I>(defParamValFunc)
    }


    fun postPreResult(reqCode: String, data: Map<String, Any>?){
        processPreState(reqCode, data)
            .notNull { state ->
                state.isPreState= true
                postState(state)
            }.isNull {
                loge("StateProcessor.postPreResult() -> state == NULL")
            }
    }
    fun postResult(reqCode: String, resCode: Int, data: Map<String, Any>?,
                   isError: Boolean, exc: Exception?, errorMsg: String?){
        processState(reqCode, resCode, data, isError, exc, errorMsg)
            .notNull { state ->
                state.isPreState= false
                postState(state)
            }.isNull {
                loge("StateProcessor.processState() -> state == NULL")
            }
    }

    /**
     * <4 Juli 2020> => parameter isPreState dihilangkan. Sbg gantinya, isPreState disimpan
     *   di dalam [state]. Tujuannya adalah agar lebih intuitif.
     */
    protected fun postState(state: S/*, isPreState: Boolean*/){
        try{
            if(!view.isExpired)
                /*App.ctx*/
                //Dicoba dulu apakah ctx tidak sama dg null.
                view._prop_ctx.notNull { ctx ->
                    ctx.runOnUiThread {
                        view.__render(state)
                    }
                }.isNull { //Jika null, maka dipaksa untuk dijalankan di Thread yg aktif.
                    view.__render(state)
                }
        } catch (e: Exception){
            loge("postState() -> Terjadi kesalahan saat render state \n Error= ${e::class.java.simpleName} \n Msg= ${e.message} Cause= ${e.cause}")
        }
        currentState.value= state
        currentStateIsPreState.value= state.isPreState
    }
    fun restoreCurrentState(){
        try{
            currentState.value!!.isPreState= currentStateIsPreState.value!!
            postState(currentState.value!!)
        } catch (e: KotlinNullPointerException){
            throw RuntimeExc(commonMsg = "StateProcessor.restoreCurrentState()",
                detailMsg = "Tidak bisa mengembalikan state ke semula karena currentState == NULL")
        }
        val viewName= view::class.simpleName
        loge("$viewName berhasil di-revert ke state sebelumnya.")
        loge("previousState= $currentState")
    }
/*
    @Deprecated("Gak dipake pada konteks MVI di kelas ini")
    override val presenter: MviPresenter<S>?= null
 */

    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?) {
        postResult(reqCode, resCode, data, false, null, null)
    }

    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?) {
        postResult(reqCode, resCode, null, true, e, msg)
    }

    @PublishedApi
    internal var `access$intentEquivReqCodeGetter`: IntentEquivReqCodeGetter?
        get() = intentEquivReqCodeGetter
        set(v){
            intentEquivReqCodeGetter= v
        }
}