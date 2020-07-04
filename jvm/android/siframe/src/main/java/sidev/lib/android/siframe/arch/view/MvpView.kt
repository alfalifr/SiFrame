package sidev.lib.android.siframe.arch.view

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.arch.presenter.*
import sidev.lib.android.siframe.arch.type.Mvp

interface MvpView: ArchView, Mvp, InteractivePresenterDependentCommon<Presenter>, PresenterCallbackCommon{
    override fun initPresenter(): Presenter?

    override fun sendRequest(reqCode: String, vararg data: Pair<String, Any>) {
        super.sendRequest(reqCode, *data)
        isBusy= true
    }

    fun onPresenterRes(reqCode: String, resCode: Int, data: Map<String, Any>?,
                       isError: Boolean, msg: String?, e: Exception?){
        if(!isError)
            onPresenterSucc(reqCode, resCode, data)
        else
            onPresenterFail(reqCode, resCode, msg, e)
        isBusy= false
    }

    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?)
    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?)
}