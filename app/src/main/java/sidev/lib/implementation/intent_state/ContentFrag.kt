package sidev.lib.implementation.intent_state

import sidev.lib.android.siframe.arch.intent_state.IntentResult
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.implementation.model.Content
import sidev.lib.implementation.presenter.ContentPresenter

sealed class ContentFragState: ViewState(){
    abstract override var isError: Boolean
    abstract val errorMsg: String?

    data class DownloadData(
        var isLoading: Boolean= false,
        var rvDataList: ArrayList<Content>?= null,
        override var isError: Boolean= false,
        override var errorMsg: String?= null
    ) : ContentFragState()

    data class Login(
        var isLoading: Boolean= false,
        var toastMsg: String?= null,
        var isSucces: Boolean= false,
        override var isError: Boolean= false,
        override var errorMsg: String?= null
    ) : ContentFragState()
}

sealed class ContentFragIntent: ViewIntent(){
    object DownloadData: ContentFragIntent(){
        override val equivalentReqCode: String
            get() = ContentPresenter.REQ_GET_CONTENT
        override val isResultTemporary: Boolean
            get() = false


        sealed class Result: IntentResult(){
            data class Succes(val rvDataList: ArrayList<Content>?= null)
            object Error
        }
    }
    data class Login(var uname: String): ContentFragIntent(){
        override val equivalentReqCode: String
            get() = ContentPresenter.REQ_LOGIN
        override val isResultTemporary: Boolean = true
    }
}