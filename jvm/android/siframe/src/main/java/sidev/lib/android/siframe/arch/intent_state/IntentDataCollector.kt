package sidev.lib.android.siframe.arch.intent_state

import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.universal.`fun`.getAllFields
import sidev.lib.universal.`fun`.getSealedClassName
import sidev.lib.universal.`fun`.new
import java.lang.reflect.Field
import kotlin.reflect.KParameter

/**
 * Untuk mengumpulkan instance [ViewIntent] yg berguna agar operasi
 * ke depannya tidak terlalu berat. Arsitektur MVI pada framework ini
 * menggunakan mapping antara [String] dan [ViewIntent] sehingga operasi
 * yg dijalankan dapat berat. Oleh karena itu, nilai sementara disimpan agar
 * tidak membebani operasi.
 */
class IntentDataCollector<I: ViewIntent>(var intentConverter: IntentConverter<I>): Mvi{
    private var intentObj: HashMap<String, ViewIntent>?= null

    /**
     * [String] key pada field ini sama dg key pada [intentObj].
     */
    private var intentObjFieldMap: HashMap<String, List<Field>>?= null


    /**
     * Berfungsi untuk mendapatkan [ViewIntent.equivalentReqCode]
     * tanpa membutuhkan instance [ViewIntent].
     */
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

    /**
     * Berfungsi untuk mengambil key dari intent data pair.
     * Berguna jika programmer meng-override fungsi [IntentConverter.getIntentDataPair]
     * dan ingin mengambil key dari intent data pair.
     */
    inline fun <reified I2: I> getIntentDataPairKey(
        fieldName: String, intentObj: I2?= null, noinline defObjValInit: ((KParameter) -> Any?)?= null
    ): String?{
        if(`access$intentObj` == null){
            `access$intentObj`= HashMap()
            `access$intentObjFieldMap`= HashMap()
        }
        val key= I2::class.getSealedClassName()!!
        var viewIntentObj= `access$intentObj`!![key]
        var viewIntentField= `access$intentObjFieldMap`!![key]
        if(viewIntentObj == null){
            viewIntentObj= new<I2>(defObjValInit)!!
            viewIntentField= viewIntentObj.getAllFields(justPublic = false)
            `access$intentObj`!![key]= viewIntentObj
            if(viewIntentField != null)
                `access$intentObjFieldMap`!![key]= viewIntentField
        }
        var field: Field?= null
        for(f in viewIntentField!!){
            if(f.name == fieldName)
                field= f
        }
        val pair= intentConverter.getIntentDataPair(intentObj!!, field!!)
        return pair?.first
    }


    @PublishedApi
    internal var `access$intentObj`: HashMap<String, ViewIntent>?
        get() = intentObj
        set(value) {
            intentObj = value
        }
    @PublishedApi
    internal var `access$intentObjFieldMap`: HashMap<String, List<Field>>?
        get() = intentObjFieldMap
        set(value) {
            intentObjFieldMap = value
        }
}