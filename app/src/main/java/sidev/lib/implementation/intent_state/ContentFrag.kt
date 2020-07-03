package sidev.lib.implementation.intent_state

import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.implementation.model.Content
import sidev.lib.implementation.presenter.ContentPresenter

sealed class ContentFragState: ViewState(){
    abstract val isError: Boolean
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
        override var isError: Boolean= false,
        override var errorMsg: String?= null
    ) : ContentFragState()
}

sealed class ContentFragIntent: ViewIntent(){
    object DownloadData: ContentFragIntent(){
        override val equivalentReqCode: String
            get() = ContentPresenter.REQ_GET_CONTENT
    }
    data class Login(var uname: String): ContentFragIntent(){
        override val equivalentReqCode: String
            get() = ContentPresenter.REQ_LOGIN
    }
}