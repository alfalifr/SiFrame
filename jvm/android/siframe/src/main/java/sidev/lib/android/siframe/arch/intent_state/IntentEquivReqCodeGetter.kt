package sidev.lib.android.siframe.arch.intent_state

import sidev.lib.universal.`fun`.getSealedClassName
import sidev.lib.universal.`fun`.new
import kotlin.reflect.KParameter

class IntentEquivReqCodeGetter {
    private var intentObj: HashMap<String, ViewIntent>?= null

    inline fun <reified I: ViewIntent> getEquivReqCode(
        noinline defParamValFunc: ((KParameter) -> Any?)?= null
    ): String{
        if(`access$intentObj` == null)
            `access$intentObj` = HashMap()
        val key= I::class.getSealedClassName()!!
        var viewIntentObj= `access$intentObj`!![key]
        if(viewIntentObj == null){
            viewIntentObj= new<I>(defParamValFunc)!!
            `access$intentObj`!![key]= viewIntentObj
        }
        return viewIntentObj.equivalentReqCode
    }

    @PublishedApi
    internal var `access$intentObj`: HashMap<String, ViewIntent>?
        get() = intentObj
        set(value) {
            intentObj = value
        }
}