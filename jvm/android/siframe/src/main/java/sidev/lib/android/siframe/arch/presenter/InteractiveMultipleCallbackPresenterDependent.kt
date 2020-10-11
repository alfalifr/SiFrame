package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase

/**
 * Interface yg dapat berkomunikasi dengan presenternya menggunakan fungsi yg ada.
 */
interface InteractiveMultipleCallbackPresenterDependent<
        Req, Res,
        P: MultipleCallbackArchPresenter<Req, Res, PresenterCallback<Req, Res>>
        > //C: PresenterCallback<R>
    : InteractivePresenterDependent<Req>, MultipleCallbackPresenterRequester<Req, Res> {
    //    var callbackCtx: Context?
//    val presenter: P?

    @CallSuper
    override fun downloadData(callback: PresenterCallback<Req, Res>, reqCode: Req, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? P)?.postRequest(callback, reqCode, map)
        }
    }
    @CallSuper
    override fun uploadData(callback: PresenterCallback<Req, Res>, reqCode: Req, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? P)?.postRequest(callback, reqCode, map)
        }
    }
    @CallSuper
    override fun sendRequest(callback: PresenterCallback<Req, Res>, reqCode: Req, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? P)?.postRequest(callback, reqCode, map)
        }
    }
/*
    fun initPresenter(): P? {
        return null
    }
 */
}