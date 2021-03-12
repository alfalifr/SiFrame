package sidev.lib.android.siframe.arch.intent_state

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import org.jetbrains.anko.runOnUiThread
//import sidev.lib.android.std._external._AnkoInternals.runOnUiThread
import sidev.lib.android.siframe.arch.presenter.PresenterCallback
import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.android.siframe.arch.view.AutoRestoreViewOwner
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.tool.ViewContentExtractor
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.isNull
import sidev.lib.check.notNull
import sidev.lib.check.notNullTo
import sidev.lib.exception.Exc
import sidev.lib.exception.RuntimeExc
import sidev.lib.exception.createErrorSimple
import sidev.lib.reflex.jvm.forceCall
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties


abstract class StateProcessor<I: ViewIntent, R: IntentResult, S: ViewState<*>>(view: MviView<I, R, S>):
    Mvi, AutoRestoreViewOwner,
//    PresenterDependent<MviPresenter<S>>,
    PresenterCallback<I, R> {
    var view: MviView<I, R, S> = view
        internal set(v){
            field= v
            intentConverter?.expirableView= v
        }
    internal var currentViewState: HashMap<I, S>?= null
    internal var currentViewStateOrder: LinkedHashSet<I>?= null
//    private var currentStateIsTemporary: HashMap<I, Boolean>?= null

//    internal var currentStateIsPreState= false
    internal var intentConverter: IntentConverter<I>?= null //Untuk dapat mendapatkan pemetaan
/*
        set(v){
            field= v
            v?.expirableView= view
        }
 */
                //antara ViewIntent dan reqCode.
    protected var intentPropGetter: IntentPropGetter?= null
    override val viewContentExtractor: ViewContentExtractor by lazy { ViewContentExtractor() }

//        internal set
//    protected var intentObj: HashMap<String, ViewIntent>?= null

//    protected var previousState: S?= null

    final override val isExpired: Boolean
        get()= view.isExpired
    override val _prop_ctx: Context?
        get() = view._prop_ctx

    /**
     * Fungsi yg memetakan dari hasil request menjadi (pre)state.
     * Fungsi ini dapat mengembalikan null jika programmer ingin mengabaikan hasil
     * request yg didapat, walaupun hal tersebut sangat tidak disarankan pada
     * arsitektur MVI demi reaktivitas app.
     */
    abstract fun processPreState(intent: I, additionalData: Map<String, Any>?): S?
    abstract fun processState(intent: I, result: R?, additionalData: Map<String, Any>?, e: Exc?): S?


    /**
     * @return null jika nama gak ketemu dalam instance [IntentConverter.equivReqCodeMap]
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

    fun <I: ViewIntent> getEquivReqCode(
        intentClass: KClass<I>,
        defParamValFunc: ((KParameter) -> Any?)?= null
    ): String{
        if(intentPropGetter == null)
            intentPropGetter= IntentPropGetter()
        return intentPropGetter!!.getEquivReqCode(intentClass, defParamValFunc)
    }


    /**
     * Fungsi dipanggil segera setelah sebuah [ViewIntent] dg kode [intent] dikirim
     * namun hasil dari request belum didapat, sehingga dapat disebut fungsi ini berfungsi
     * sbg buffer selagi menunggu hasil request datang.
     *
     * @param isStateTemporary true jika state yg akan datang baik dari fungsi [postPreResult]
     *   atau [postResult] dg kode [intent] tidak akan disimpan pada [currentViewState].
     */
    fun postPreResult(intent: I, additonalData: Map<String, Any>?){ //, isStateTemporary: Boolean
/*
        if(currentStateIsTemporary == null)
            currentStateIsTemporary= HashMap()
        currentStateIsTemporary!![reqCode]= reqCode.isResultTemporary
 */

        processPreState(intent, additonalData)
            .notNull { state ->
                state.isPreState= true
//                currentStateIsPreState= true
                postState(intent, state)
            }.isNull {
                loge("StateProcessor.postPreResult() -> state == NULL")
            }
    }
    fun postResult(intent: I, result: R?, additionalData: Map<String, Any>?, e: Exc?){
        processState(intent, result, additionalData, e)
            .notNull { state ->
                state.isPreState= false
                if(result != null){
                    (state::class.memberProperties.find{
                        val propFound= it is KMutableProperty<*>
                                && it.name == STATE_RESULT
                        if(propFound){
                            if(it.returnType.classifier != result::class){
                                loge("StateProcessor.processState() -> Tipe data dari resCode: \"${result::class.qualifiedName}\" tidak sama dg state.result: \"${state::class.qualifiedName}\", resCode diabaikan!")
                                false
                            } else true
                        } else false
                    } as? KMutableProperty<*>)?.setter?.forceCall(state, result)
                }
                state.error= e
//                currentStateIsPreState= false
                postState(intent, state)
            }.isNull {
                loge("StateProcessor.processState() -> state == NULL")
            }
    }

    /**
     * <4 Juli 2020> => parameter isPreState dihilangkan. Sbg gantinya, isPreState disimpan
     *   di dalam [state]. Tujuannya adalah agar lebih intuitif.
     */
    protected fun postState(intent: I, state: S, isRestoreMode: Boolean= false){
        if(!isRestoreMode)
            saveCurrentState(intent, state)
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
            throw e //<6 Juli 2020> => Jika terjadi kesalahan saat render, maka app akan crash.
                //Hal tersebut bertujuan agar programmer tidak bingung saat terjadi keanehan saat render
                // padahal error terjadi pada framework.
        }
//        currentStateIsPreState= state.isPreState
    }

    protected fun saveCurrentState(intent: I, state: S){
//        loge("saveCurrentState() reqCode= $reqCode state.isPreState= ${state.isPreState} !currentStateIsTemporary!![reqCode]!!= ${!currentStateIsTemporary!![reqCode]!!} \n state.isError= ${state.isError}")

        //Jika temporary, gak usah disimpan.
        // Kecuali jika state merupakan preState, maka tetap disimpan agar saat restore
        // saat screen rotation, layar buffer tetap muncul.
        if(state.isPreState || !intent.isResultTemporary){ //!currentStateIsTemporary!![reqCode.equivalentReqCode]!!){
            if(currentViewState == null){
                currentViewState= HashMap()
                currentViewStateOrder= LinkedHashSet()
            }
            if(currentViewStateOrder!!.contains(intent)){
                currentViewStateOrder!!.remove(intent)
            }
            currentViewStateOrder!!.add(intent)
            currentViewState!![intent]= state
        }
        //Cabang ini berfungsi untuk menghilangkan preState yg [ViewState]-nya
        // bersifat sementara agar saat di-restore layar buffer tidak muncul
        // padahal hasil request udah didapatkan.
        else if(!state.isPreState && intent.isResultTemporary) {//currentStateIsTemporary!![reqCode.equivalentReqCode]!!){
            currentViewState?.remove(intent)
            currentViewStateOrder?.remove(intent)
        }
    }

    fun restoreCurrentState(){
        try{
            for(intent in currentViewStateOrder!!){
                val state= currentViewState!![intent]!!
                postState(intent, state, true)
            }
//            currentState!!.isPreState= currentStateIsPreState
        } catch (e: KotlinNullPointerException){
            throw RuntimeExc(commonMsg = "StateProcessor.restoreCurrentState()",
                detailMsg = "Tidak bisa mengembalikan state ke semula karena currentState == NULL")
        }
        val viewName= view::class.simpleName
        loge("$viewName berhasil di-restore ke state sebelumnya.")
        loge("previousState= $currentViewState")
    }

    @CallSuper
    override fun registerAutoRestoreView(id: String, v: View) {
        viewContentExtractor.registerView(id, v)
    }

    /**
     * Digunakan untuk mengesktrak semua view yg telah disimpan di dalam [viewContentExtractor].
     * Fungsi ini harus dipanggil sesaat sebelum activity dihancurkan karena fungsi ini
     * akan memanggil [ViewContentExtractor.clearAllSavedViews].
     */
    @CallSuper
    override fun extractAllViewContent() {
        viewContentExtractor.extractAllViewContent()
        viewContentExtractor.clearAllSavedViews()
    }

    /*
    @Deprecated("Gak dipake pada konteks MVI di kelas ini")
    override val presenter: MviPresenter<S>?= null
 */

    final override fun onPresenterSucc(request: I, result: R, data: Map<String, Any>?, resCode: Int) {
        postResult(request, result, data, null)
    }

    final override fun onPresenterFail(
        request: I,
        result: R?,
        msg: String?,
        e: Exception?,
        resCode: Int
    ) {
        val error= createErrorSimple(msg, e, resCode)
        postResult(request, result, null, error)
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
abstract class StateProcessor<S: ViewState, I: ViewIntent>(view: MviView<S, I>):
    Mvi, AutoRestoreViewOwner,
//    PresenterDependent<MviPresenter<S>>,
    PresenterCallbackCommon {
    var view: MviView<S, I> = view
        internal set(v){
            field= v
            intentConverter?.expirableView= v
        }
    internal var currentViewState: HashMap<String, S>?= null
    internal var currentViewStateOrder: LinkedHashSet<String>?= null
    private var currentStateIsTemporary: HashMap<String, Boolean>?= null

//    internal var currentStateIsPreState= false
    internal var intentConverter: IntentConverter<I>?= null //Untuk dapat mendapatkan pemetaan
/*
        set(v){
            field= v
            v?.expirableView= view
        }
 */
                //antara ViewIntent dan reqCode.
    protected var intentPropGetter: IntentPropGetter?= null
    override val viewContentExtractor: ViewContentExtractor by lazy { ViewContentExtractor() }

//        internal set
//    protected var intentObj: HashMap<String, ViewIntent>?= null

//    protected var previousState: S?= null

    final override val isExpired: Boolean
        get()= view.isExpired
    override val _prop_ctx: Context?
        get() = view._prop_ctx

    /**
     * Fungsi yg memetakan dari hasil request menjadi (pre)state.
     * Fungsi ini dapat mengembalikan null jika programmer ingin mengabaikan hasil
     * request yg didapat, walaupun hal tersebut sangat tidak disarankan pada
     * arsitektur MVI demi reaktivitas app.
     */
    abstract fun processPreState(reqCode: String, data: Map<String, Any>?): S?
    abstract fun processState(reqCode: String, resCode: Int, data: Map<String, Any>?,
                              isError: Boolean, exc: Exception?, errorMsg: String?): S?


    /**
     * @return null jika nama gak ketemu dalam instance [IntentConverter.equivReqCodeMap]
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

    fun <I: ViewIntent> getEquivReqCode(
        intentClass: KClass<I>,
        defParamValFunc: ((KParameter) -> Any?)?= null
    ): String{
        if(intentPropGetter == null)
            intentPropGetter= IntentPropGetter()
        return intentPropGetter!!.getEquivReqCode(intentClass, defParamValFunc)
    }


    /**
     * Fungsi dipanggil segera setelah sebuah [ViewIntent] dg kode [reqCode] dikirim
     * namun hasil dari request belum didapat, sehingga dapat disebut fungsi ini berfungsi
     * sbg buffer selagi menunggu hasil request datang.
     *
     * @param isStateTemporary true jika state yg akan datang baik dari fungsi [postPreResult]
     *   atau [postResult] dg kode [reqCode] tidak akan disimpan pada [currentViewState].
     */
    fun postPreResult(reqCode: String, data: Map<String, Any>?, isStateTemporary: Boolean){
        if(currentStateIsTemporary == null)
            currentStateIsTemporary= HashMap()
        currentStateIsTemporary!![reqCode]= isStateTemporary

        processPreState(reqCode, data)
            .notNull { state ->
                state.isPreState= true
//                currentStateIsPreState= true
                postState(reqCode, state)
            }.isNull {
                loge("StateProcessor.postPreResult() -> state == NULL")
            }
    }
    fun postResult(reqCode: String, resCode: Int, data: Map<String, Any>?,
                   isError: Boolean, exc: Exception?, errorMsg: String?){
        processState(reqCode, resCode, data, isError, exc, errorMsg)
            .notNull { state ->
                state.isPreState= false
//                currentStateIsPreState= false
                postState(reqCode, state)
            }.isNull {
                loge("StateProcessor.processState() -> state == NULL")
            }
    }

    /**
     * <4 Juli 2020> => parameter isPreState dihilangkan. Sbg gantinya, isPreState disimpan
     *   di dalam [state]. Tujuannya adalah agar lebih intuitif.
     */
    protected fun postState(reqCode: String, state: S, isRestoreMode: Boolean= false){
        if(!isRestoreMode)
            saveCurrentState(reqCode, state)
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
            throw e //<6 Juli 2020> => Jika terjadi kesalahan saat render, maka app akan crash.
                //Hal tersebut bertujuan agar programmer tidak bingung saat terjadi keanehan saat render
                // padahal error terjadi pada framework.
        }
//        currentStateIsPreState= state.isPreState
    }

    protected fun saveCurrentState(reqCode: String, state: S){
//        loge("saveCurrentState() reqCode= $reqCode state.isPreState= ${state.isPreState} !currentStateIsTemporary!![reqCode]!!= ${!currentStateIsTemporary!![reqCode]!!} \n state.isError= ${state.isError}")

        //Jika temporary, gak usah disimpan.
        // Kecuali jika state merupakan preState, maka tetap disimpan agar saat restore
        // saat screen rotation, layar buffer tetap muncul.
        if(state.isPreState
            || !currentStateIsTemporary!!
                    [reqCode]!!){
            if(currentViewState == null){
                currentViewState= HashMap()
                currentViewStateOrder= LinkedHashSet()
            }
            if(currentViewStateOrder!!.contains(reqCode)){
                currentViewStateOrder!!.remove(reqCode)
            }
            currentViewStateOrder!!.add(reqCode)
            currentViewState!![reqCode]= state
        }
        //Cabang ini berfungsi untuk menghilangkan preState yg [ViewState]-nya
        // bersifat sementara agar saat di-restore layar buffer tidak muncul
        // padahal hasil request udah didapatkan.
        else if(!state.isPreState
            && currentStateIsTemporary!!
                    [reqCode]!!){
            currentViewState?.remove(reqCode)
            currentViewStateOrder?.remove(reqCode)
        }
    }

    fun restoreCurrentState(){
        try{
            for(reqCode in currentViewStateOrder!!){
                val state= currentViewState!![reqCode]!!
                postState(reqCode, state, true)
            }
//            currentState!!.isPreState= currentStateIsPreState
        } catch (e: KotlinNullPointerException){
            throw RuntimeExc(commonMsg = "StateProcessor.restoreCurrentState()",
                detailMsg = "Tidak bisa mengembalikan state ke semula karena currentState == NULL")
        }
        val viewName= view::class.simpleName
        loge("$viewName berhasil di-restore ke state sebelumnya.")
        loge("previousState= $currentViewState")
    }

    @CallSuper
    override fun registerAutoRestoreView(id: String, v: View) {
        viewContentExtractor.registerView(id, v)
    }

    /**
     * Digunakan untuk mengesktrak semua view yg telah disimpan di dalam [viewContentExtractor].
     * Fungsi ini harus dipanggil sesaat sebelum activity dihancurkan karena fungsi ini
     * akan memanggil [ViewContentExtractor.clearAllSavedViews].
     */
    @CallSuper
    override fun extractAllViewContent() {
        viewContentExtractor.extractAllViewContent()
        viewContentExtractor.clearAllSavedViews()
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