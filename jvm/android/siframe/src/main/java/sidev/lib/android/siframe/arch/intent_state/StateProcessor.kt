package sidev.lib.android.siframe.arch.intent_state

import android.content.Context
import org.jetbrains.anko.runOnUiThread
import sidev.lib.android.siframe.arch.presenter.PresenterCallbackCommon
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.exception.RuntimeExc
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.*
import kotlin.reflect.KParameter

abstract class StateProcessor<S: ViewState, I: ViewIntent>(view: MviView<S, I>):
//    PresenterDependent<MviPresenter<S>>,
    PresenterCallbackCommon {
    var view: MviView<S, I> = view
        internal set(v){
            field= v
            intentConverter?.expirableView= v
        }
    internal var currentState: HashMap<String, S>?= null
    internal var currentStateOrder: LinkedHashSet<String>?= null
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

    inline fun <reified I: ViewIntent> getEquivReqCode(
        noinline defParamValFunc: ((KParameter) -> Any?)?= null
    ): String{
        if(`access$intentPropGetter` == null)
            `access$intentPropGetter`= IntentPropGetter()
        return `access$intentPropGetter`!!.getEquivReqCode<I>(defParamValFunc)
    }


    /**
     * Fungsi dipanggil segera setelah sebuah [ViewIntent] dg kode [reqCode] dikirim
     * namun hasil dari request belum didapat, sehingga dapat disebut fungsi ini berfungsi
     * sbg buffer selagi menunggu hasil request datang.
     *
     * @param isStateTemporary true jika state yg akan datang baik dari fungsi [postPreResult]
     *   atau [postResult] dg kode [reqCode] tidak akan disimpan pada [currentState].
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
        }
//        currentStateIsPreState= state.isPreState
    }

    protected fun saveCurrentState(reqCode: String, state: S){
        //Jika temporary, gak usah disimpan.
        // Kecuali jika state merupakan preState, maka tetap disimpan agar saat restore
        // saat screen rotation, layar buffer tetap muncul.
        if(state.isPreState
            || !currentStateIsTemporary!!
                    [reqCode]!!){
            if(currentState == null){
                currentState= HashMap()
                currentStateOrder= LinkedHashSet()
            }
            if(currentStateOrder!!.contains(reqCode)){
                currentStateOrder!!.remove(reqCode)
            }
            currentStateOrder!!.add(reqCode)
            currentState!![reqCode]= state
        }
        //Cabang ini berfungsi untuk menghilangkan preState yg [ViewState]-nya
        // bersifat sementara agar saat di-restore layar buffer tidak muncul
        // padahal hasil request udah didapatkan.
        else if(!state.isPreState
            && currentStateIsTemporary!!
                    [reqCode]!!){
            currentState?.remove(reqCode)
            currentStateOrder?.remove(reqCode)
        }
    }

    fun restoreCurrentState(){
        try{
            for(reqCode in currentStateOrder!!){
                val state= currentState!![reqCode]!!
                postState(reqCode, state, true)
            }
//            currentState!!.isPreState= currentStateIsPreState
        } catch (e: KotlinNullPointerException){
            throw RuntimeExc(commonMsg = "StateProcessor.restoreCurrentState()",
                detailMsg = "Tidak bisa mengembalikan state ke semula karena currentState == NULL")
        }
        val viewName= view::class.simpleName
        loge("$viewName berhasil di-restore ke state sebelumnya.")
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
    internal var `access$intentPropGetter`: IntentPropGetter?
        get() = intentPropGetter
        set(v){
            intentPropGetter= v
        }
}