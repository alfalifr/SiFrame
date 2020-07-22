package sidev.lib.implementation.intent_state

import sidev.lib.android.siframe.arch.intent_state.IntentResult
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.implementation.model.Content
import sidev.lib.implementation.presenter.ContentPresenter

typealias CFIntent= ContentFragConf.Intent
typealias CFRes= ContentFragConf.Result
typealias CFState<T> = ContentFragConf.State<T>

class ContentFragConf{
    sealed class Intent: ViewIntent(){
        object DownloadData: Intent(){
            override val equivalentReqCode: String
                get() = ContentPresenter.REQ_GET_CONTENT
            override val isResultTemporary: Boolean
                get() = false

        }
        data class Login(var uname: String): Intent(){
            override val equivalentReqCode: String
                get() = ContentPresenter.REQ_LOGIN
            override val isResultTemporary: Boolean = true
        }
    }

    sealed class Result: IntentResult(){
        data class DownloadData(val rvDataList: ArrayList<Content>?= null): Result()
        data class Login(val isSuccess: Boolean, val msg: String?= null): Result()
    }

    sealed class State<R: Result>: ViewState<R>(){
        data class DownloadData(
            var isLoading: Boolean= false
//        var rvDataList: ArrayList<Content>?= null,
        ) : State<Result.DownloadData>()

        data class Login(
            var isLoading: Boolean= false,
            var toastMsg: String?= null
//        var isSucces: Boolean= false,
        ) : State<Result.Login>()
    }
}


fun adad(state: ContentFragConf.State<*>){
    when(state){
        is ContentFragConf.State.DownloadData -> {
            state.result?.rvDataList
        }
    }
    state.result
}