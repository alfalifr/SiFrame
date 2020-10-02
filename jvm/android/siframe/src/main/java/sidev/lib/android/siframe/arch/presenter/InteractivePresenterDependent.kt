package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase

/**
 * Interface yg dapat berkomunikasi dengan presenternya menggunakan fungsi yg ada.
 */
interface InteractivePresenterDependent<Req, P: ArchPresenter<Req, *, *>> //C: PresenterCallback<R>
    : PresenterDependent, InterruptableBase, ExpirableBase {
//    var callbackCtx: Context?
//    val presenter: P?

    @CallSuper
    fun downloadData(reqCode: Req, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? P)?.postRequest(reqCode, map)
        }
    }
    @CallSuper
    fun uploadData(reqCode: Req, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? P)?.postRequest(reqCode, map)
        }
    }
    @CallSuper
    fun sendRequest(reqCode: Req, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? P)?.postRequest(reqCode, map)
        }
    }
/*
    fun initPresenter(): P? {
        return null
    }
 */
}