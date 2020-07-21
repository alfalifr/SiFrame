package sidev.lib.android.siframe.arch.intent_state

import sidev.lib.android.siframe.arch.presenter.MviInteractivePresenterDependent
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.type.Mvi
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableLinkBase
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.*
import java.lang.reflect.Field

/**
 * Kelas yg digunakan untuk mengkoversi object [ViewIntent] yg digunakan sbg parameter saat
 * memanggil fungsi [downloadData], [uploadData], dan [sendRequest] yg ada pada
 * interface [MviInteractivePresenterDependent] menjadi bentuk String.
 * Hal tersebut dikarenakan [MviPresenter] pada dasarnya adalah Presenter yg [reqCode]-nya
 * memakai String.
 *
 * Kelas ini opsional pada arsitektur MVI pada framework ini. Jika programmer memutuskan untuk
 * menggunakan [ViewIntent] sbg parameter dan belum meng-override fungsi [MviView.initIntentCoverter],
 * maka scr default object [IntentConverter] yg ada pada [MviView] akan di-instantiate
 * menggunakan definisi default kelas ini.
 */
open class IntentConverter<I: ViewIntent>(var expirableView: ExpirableBase?, var presenter: Presenter?)
    : Mvi, ExpirableLinkBase {
    override val expirable: ExpirableBase?
        get() = expirableView

    /**
     * Berguna jika [presenter] bkn merupakan tipe data [MviPresenter], terutama saat preState.
     */
    var stateProcessor: StateProcessor<*, I>?= null
        set(v){
            if(field != null)
                field!!.intentConverter= null
            field= v
            v?.intentConverter= this
        }

    /**
     * Key-nya adalah nama dari kelas <code><I: ViewIntent></code>.
     *   Key-nya berupa qualified name, yaitu nama yg dimulai dari nama sealed super class hingga
     *   nama kelas dari <code><I: ViewIntent></code> yg dipidahkan oleh titik (.).
     * Value-nya adalah equivalentReqCode.
     *
     * Tujuan dari pemetaan yg disimpan di sini adalah agar pemetaan dapat dicari baik jika
     * Key-nya yg diketahui atau Value-nya yg diketahui.
     */
    internal var equivReqCodeMap= HashMap<String, String>()

    fun getDefaultEquivReqCode(intent: I): String{
        return intent.equivalentReqCode
    }
    open fun getEquivReqCode(intent: I, suggestedReqCode: String): String{
        return suggestedReqCode
    }

    /**
     * Pada framework ini, ViewIntent dapat berupa data class di mana data yg ada di dalamnya
     * merupakan argumen data jika pada gaya reqCode dg tipe data String. Untuk mengubah ke gaya lama,
     * intent yg diberikan oleh programmer akan di-pass ke fungsi ini untuk kemudian diubah ke bentuk map
     * seperti gaya lama.
     *
     * @return null jika tidak ada data yg dikembalikan oleh <code>getIntentDataPair()</code>.
     */
    fun getIntentDataMap(intent: I): Map<String, Any>?{
        val map= HashMap<String, Any>()
        intent.getAllFields(justPublic = false).notNull { fields ->
            for(field in fields){
                //Field yg gak boleh dimodifikasi definisi pair datanya adalah
                // dua field yg namanya ada di dalam konstanta di bawah.
                if(field.name != INTENT_EQUIVALENT_REQ_CODE
                    && field.name != INTENT_IS_RESULT_TEMPORARY)
                    getIntentDataPair(intent, field).notNull { map.add(it) }
                else
                    getDefaultIntentDataPair(intent, field).notNull { map.add(it) }
            }
        }
        return if(map.isNotEmpty()) map
        else null
    }

    fun getDefaultIntentDataPair(intent: I, field: Field): Pair<String, Any>?{
        return try{ Pair(field.name, field.getV(intent)!!) } //Dg anggapan pengambilan nilai dapat dilakukan lewat refleksi.
        catch (e: Exception){ null } //Jika ternyata refleksi dilarang oleh sistem.
    }

    /**
     * Programmer dapat mengubah definisi pair data pada sebuah [ViewIntent].
     * Namun, ada bbrp definisi yg sudah pakem dan programmer gak boleh mengubah definisi
     * default yg terdapat pada fungsi [getDefaultIntentDataPair].
     * Definisi pair data yg sudah pakem tidak di-pass ke sini oleh framework ini.
     */
    open fun getIntentDataPair(intent: I, field: Field): Pair<String, Any>?{
        return getDefaultIntentDataPair(intent, field)
    }

    fun postRequest(intent: I /*, vararg data: Pair<String, Any>*/){
        doWhenLinkNotExpired {
            val sealedName= intent::class.getSealedClassName(true)!!
            var reqCode= equivReqCodeMap[sealedName]
            if(reqCode == null){
                reqCode= getEquivReqCode(intent, getDefaultEquivReqCode(intent))
                equivReqCodeMap[sealedName]= reqCode
            }

            val map= getIntentDataMap(intent)
/*
            if(presenter !is MviPresenter<*, *>){
                stateProcessor?.postPreResult(reqCode, map, intent.isResultTemporary)
                loge("postRequest() presenter !is MviPresenter<*> \n reqCode= $reqCode isResultTemproray= ${intent.isResultTemporary}")
            }
 */
            stateProcessor?.postPreResult(intent, map)
            presenter?.postRequest(reqCode, map)
        }.isNull {
            val clsName= this.classSimpleName()
            val viewName= expirableView?.classSimpleName()
            loge("$clsName.postRequest() view= $viewName.isExpired == TRUE")
            expirableView= null
        }
    }
}