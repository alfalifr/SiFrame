package sidev.lib.android.siframe.arch.intent_state

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
    protected var intentObj: HashMap<String, ViewIntent>?= null

//    protected var previousState: S?= null

    final override val isExpired: Boolean
        get()= view.isExpired

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

    inline fun <reified I: ViewIntent> getEquivReqCode(noinline defParamValFunc: ((KParameter) -> Any?)?= null): String{
        if(intentObj == null)
            intentObj= HashMap()
        val key= I::class.getSealedClassName()!!
        var viewIntentObj= intentObj!![key]
        if(viewIntentObj == null){
            viewIntentObj= new<I>(defParamValFunc)!!
            intentObj!![key]= viewIntentObj
        }
        return viewIntentObj.equivalentReqCode
    }

    fun postPreResult(reqCode: String, data: Map<String, Any>?){
        processPreState(reqCode, data)
            .notNull { state ->
                postState(state, true)
            }.isNull {
                loge("StateProcessor.postPreResult() -> state == NULL")
            }
    }
    fun postResult(reqCode: String, resCode: Int, data: Map<String, Any>?,
                   isError: Boolean, exc: Exception?, errorMsg: String?){
        processState(reqCode, resCode, data, isError, exc, errorMsg)
            .notNull { state ->
                postState(state, false)
            }.isNull {
                loge("StateProcessor.processState() -> state == NULL")
            }
    }
    protected fun postState(state: S, isPreState: Boolean){
        try{
            if(!view.isExpired)
                App.ctx.runOnUiThread {
                    view.render(state, isPreState)
                }
        } catch (e: Exception){
            loge("postState() -> Terjadi kesalahan saat render state")
        }
        currentState.value= state
        currentStateIsPreState.value= isPreState
    }
    fun restoreCurrentState(){
        try{
            postState(currentState.value!!, currentStateIsPreState.value!!)
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
}