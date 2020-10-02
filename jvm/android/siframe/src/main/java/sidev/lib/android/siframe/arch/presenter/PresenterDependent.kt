package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase

/**
 * Interface yg bergantung pada sebuah [ArchPresenter].
 */
interface PresenterDependent: ExpirableBase { //P: ArchPresenterRoot, R, C: PresenterCallback<R>
//    var callbackCtx: Context?
    val presenter: ArchPresenter<*, *, *>?
/*
    @CallSuper
    fun downloadData(reqCode: String, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        presenter?.postRequest(reqCode, map)
    }
    @CallSuper
    fun uploadData(reqCode: String, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        presenter?.postRequest(reqCode, map)
    }
    @CallSuper
    fun sendRequest(reqCode: String, vararg data: Pair<String, Any>) {
        val map= if(data.isEmpty()) null
            else mapOf(*data)
        presenter?.postRequest(reqCode, map)
    }
 */

    fun initPresenter(): ArchPresenter<*, *, *>? {
        return null
    }
}