package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase

/**
 * Interface yg dapat berkomunikasi dengan presenternya menggunakan fungsi yg ada.
 */
interface InteractivePresenterDependent<Req> //P: ArchPresenter<Req, *, *>, C: PresenterCallback<R>
    : PresenterDependent, PresenterRequester<Req>, InterruptableBase, ExpirableBase {
//    var callbackCtx: Context?
//    val presenter: P?

    @CallSuper
    override fun downloadData(reqCode: Req, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? ArchPresenter<Req, *, *>)?.postRequest(reqCode, map)
        }
    }
    @CallSuper
    override fun uploadData(reqCode: Req, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? ArchPresenter<Req, *, *>)?.postRequest(reqCode, map)
        }
    }
    @CallSuper
    override fun sendRequest(reqCode: Req, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? ArchPresenter<Req, *, *>)?.postRequest(reqCode, map)
        }
    }
/*
    fun initPresenter(): P? {
        return null
    }
 */
}