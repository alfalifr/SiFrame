package sidev.lib.android.siframe.arch.intent_state

import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.universal.`fun`.getSealedClassName
import sidev.lib.universal.`fun`.new
import kotlin.reflect.KParameter

/**
 * Sesuai namanya, kelas ini berfungsi untuk mendapatkan [ViewIntent.equivalentReqCode]
 * tanpa membutuhkan instance [ViewIntent].
 *
 * Kelas ini merupakan versi ringan dari [IntentDataCollector] karena tidak menyimpan
 * field dari kelas [ViewIntent].
 */
class IntentPropGetter: Mvi {
    private var intentObj: HashMap<String, ViewIntent>?= null

    inline fun <reified I: ViewIntent> getEquivReqCode(
        noinline defParamValFunc: ((KParameter) -> Any?)?= null
    ): String{
        if(`access$intentObj` == null)
            `access$intentObj` = HashMap()
        val key= I::class.getSealedClassName()!!
        var viewIntentObj= `access$intentObj`!![key]
        if(viewIntentObj == null){
            viewIntentObj= new(I::class, defParamValFunc = defParamValFunc)!!
            `access$intentObj`!![key]= viewIntentObj
        }
        return viewIntentObj.equivalentReqCode
    }

    inline fun <reified I: ViewIntent> getResultIsTemporary(
        noinline defParamValFunc: ((KParameter) -> Any?)?= null
    ): Boolean{
        if(`access$intentObj` == null)
            `access$intentObj` = HashMap()
        val key= I::class.getSealedClassName()!!
        var viewIntentObj= `access$intentObj`!![key]
        if(viewIntentObj == null){
            viewIntentObj= new(I::class, defParamValFunc = defParamValFunc)!!
            `access$intentObj`!![key]= viewIntentObj
        }
        return viewIntentObj.isResultTemporary
    }

    @PublishedApi
    internal var `access$intentObj`: HashMap<String, ViewIntent>?
        get() = intentObj
        set(value) {
            intentObj = value
        }
}