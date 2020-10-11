package sidev.lib.android.siframe.arch.view

import sidev.lib.android.siframe.arch.presenter.*
import sidev.lib.android.siframe.arch.type.Mvp

interface MvpView: ArchView, Mvp, InteractivePresenterDependentCommon, PresenterCallbackCommon{
    override fun initPresenter(): Presenter?

    override fun sendRequest(reqCode: String, vararg data: Pair<String, Any>) {
        super.sendRequest(reqCode, *data)
        isBusy= true
    }

    fun onPresenterRes(request: String, result: Int, data: Map<String, Any>?,
                       isError: Boolean, msg: String?, e: Exception?){
        if(!isError)
            onPresenterSucc(request, result, data, result)
        else
            onPresenterFail(request, result, msg, e, result)
        isBusy= false
    }

    override fun onPresenterSucc(
        request: String,
        result: Int,
        data: Map<String, Any>?,
        resCode: Int
    )

    override fun onPresenterFail(
        request: String,
        result: Int?,
        msg: String?,
        e: Exception?,
        resCode: Int
    )

//    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?)
//    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?)
}