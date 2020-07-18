package sidev.lib.android.siframe.arch.intent_state

import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.universal.`fun`.getSealedClassName
import sidev.lib.universal.`fun`.new
import kotlin.reflect.KClass
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

    fun <I: ViewIntent> getEquivReqCode(
        intentClass: KClass<I>,
        defParamValFunc: ((KParameter) -> Any?)?= null
    ): String{
        if(intentObj == null) intentObj = HashMap()

        val key= intentClass.getSealedClassName()!!
        var viewIntentObj= intentObj!![key]
        if(viewIntentObj == null){
            viewIntentObj= new(intentClass, defParamValFunc = defParamValFunc)!!
            intentObj!![key]= viewIntentObj
        }
        return viewIntentObj.equivalentReqCode
    }

    fun <I: ViewIntent> getResultIsTemporary(
        intentClass: KClass<I>,
        defParamValFunc: ((KParameter) -> Any?)?= null
    ): Boolean{
        if(intentObj == null) intentObj = HashMap()

        val key= intentClass.getSealedClassName()!!
        var viewIntentObj= intentObj!![key]
        if(viewIntentObj == null){
            viewIntentObj= new(intentClass, defParamValFunc = defParamValFunc)!!
            intentObj!![key]= viewIntentObj
        }
        return viewIntentObj.isResultTemporary
    }
/*
    @PublishedApi
    internal var `access$intentObj`: HashMap<String, ViewIntent>?
        get() = intentObj
        set(value) {
            intentObj = value
        }
 */
}