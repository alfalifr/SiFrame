package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.asNotNullTo

/**
 * Interface yg dapat berkomunikasi dengan presenternya menggunakan fungsi yg ada.
 */
interface MviInteractivePresenterDependent<I: ViewIntent> //P: MviPresenter<I, *, *>
    : InteractivePresenterDependent<I> { // StateProcessor<ViewState, I>
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
                loge("${javaClass.simpleName}.downloadData() -> intentConverter di-assign dg default obj.")
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
                loge("${javaClass.simpleName}.uploadData() -> intentConverter di-assign dg default obj.")
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
                loge("${javaClass.simpleName}.sendRequest() -> intentConverter di-assign dg default obj.")
            }
            intentConverter!!.postRequest(reqCode)
        }
    }

    private fun initIntentConverter(){
        loge("IntentConverter == NULL, meng-init IntentConverter")
        //Cek dulu apakah callback presenternya berupa StateProcessor.
        // Jika iya, maka ambil intentConverter-nya.
        presenter?.callback.asNotNullTo { sp: StateProcessor<I, *, *> ->
            intentConverter= sp.intentConverter
            intentConverter
        }
        //Jika tidak, maka init saja intentConverter-nya.
        // Jika callback merupakan StateProcessor tapi tidak punya intentConverter,
        // maka init saja intentConverter-nya.
/*
        .isNull {
            intentConverter= IntentConverter(this, presenter)
            presenter?.callback.asNotNull { sp: StateProcessor<*, I> ->
                intentConverter!!.stateProcessor= sp
            }
        }
 */
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