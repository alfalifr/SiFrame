package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase

/**
 * INterface yg dapat berkomunikasi dengan presenternya menggunakan fungsi yg ada.
 */
interface InteractivePresenterDependent<P: ArchPresenter<R, *>, R> //C: PresenterCallback<R>
    : PresenterDependent, InterruptableBase, ExpirableBase {
//    var callbackCtx: Context?
//    val presenter: P?

    @CallSuper
    fun downloadData(reqCode: R, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? P)?.postRequest(reqCode, map)
        }
    }
    @CallSuper
    fun uploadData(reqCode: R, vararg data: Pair<String, Any>) {
        doWhenNotBusy {
            val map= if(data.isEmpty()) null
            else mapOf(*data)
            (presenter as? P)?.postRequest(reqCode, map)
        }
    }
    @CallSuper
    fun sendRequest(reqCode: R, vararg data: Pair<String, Any>) {
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