package sidev.lib.android.siframe.arch.intent_state

import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableLinkBase
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.*
import java.lang.reflect.Field

open class IntentConverter<I: ViewIntent>(var view: ExpirableBase?, var presenter: Presenter?)
    : ExpirableLinkBase{
    override val expirable: ExpirableBase?
        get() = view

    /**
     * Berguna jika [presenter] bkn merupakan tipe data [MviPresenter], terutama saat preState.
     */
    var stateProcessor: StateProcessor<*, I>?= null

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
                getIntentDataPair(intent, field).notNull { map.add(it) }
            }
        }
        return if(map.isNotEmpty()) map
        else null
    }

    open fun getIntentDataPair(intent: I, field: Field): Pair<String, Any>?{
        return try{ Pair(field.name, field.getV(intent)!!) } //Dg anggapan pengambilan nilai dapat dilakukan lewat refleksi.
        catch (e: Exception){ null } //Jika ternyata refleksi dilarang oleh sistem.
    }

    fun postRequest(intent: I /*, vararg data: Pair<String, Any>*/){
        loge("postRequest() MULAI")
        doWhenExpNotExpired {
            loge("postRequest() doWhenExpNotExpired MULAI")
            val sealedName= intent.getSealedClassName(true)!!
            var reqCode= equivReqCodeMap[sealedName]
            if(reqCode == null){
                loge("postRequest() doWhenExpNotExpired reqCode == null")
                reqCode= getEquivReqCode(intent, getDefaultEquivReqCode(intent))
                equivReqCodeMap[sealedName]= reqCode
            }

            loge("postRequest() map MULAI")
            val map= getIntentDataMap(intent)
            loge("postRequest() map SELESAI")

            if(presenter !is MviPresenter<*>){
                loge("postRequest() presenter !is MviPresenter<*> MULAI")
                stateProcessor?.postPreResult(reqCode, map)
                loge("postRequest() presenter !is MviPresenter<*> SELESAI")
            }
/*
        val map= if(data.isEmpty()) null
            else mapOf(*data)
 */
            presenter?.postRequest(reqCode, map)
        }.isNull {
            val clsName= this.classSimpleName()
            val viewName= view?.classSimpleName()
            loge("$clsName.postRequest() view= $viewName.isExpired == TRUE")
            view= null
        }
    }
}