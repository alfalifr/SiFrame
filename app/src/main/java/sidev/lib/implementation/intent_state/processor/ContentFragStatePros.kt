package sidev.lib.implementation.intent_state.processor

import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.intent_state.ContentFragIntent
import sidev.lib.implementation.model.Content
import sidev.lib.implementation.intent_state.ContentFragState
import sidev.lib.implementation.util.Const
import sidev.lib.universal.`fun`.new

class ContentFragStatePros(view: MviView<ContentFragState, ContentFragIntent>)
    : StateProcessor<ContentFragState, ContentFragIntent>(view){
    override fun processState(
        reqCode: String,
        resCode: Int,
        data: Map<String, Any>?,
        isError: Boolean,
        exc: Exception?,
        errorMsg: String?
    ): ContentFragState? {
        loge("processState() isError= $isError")
        return when(reqCode){
            ContentFragIntent.DownloadData.equivalentReqCode -> {
                val state= ContentFragState.DownloadData(isError = isError, errorMsg = errorMsg)
                try{
                    state.rvDataList= data!![Const.DATA_CONTENT] as ArrayList<Content>
                }catch (e:Exception){}
                state
            }
            getEquivReqCode(ContentFragIntent.Login::class) -> {
                val state= ContentFragState.Login(isSucces = resCode == Const.RES_OK,
                    isError = isError, errorMsg = errorMsg ?: "Error login bro")
                if(!isError)
                    state.toastMsg= "Berhasil Login bro!!!"
                state
            }
            else -> null
        }
    }

    override fun processPreState(reqCode: String, data: Map<String, Any>?): ContentFragState? {
        return when(reqCode){
            ContentFragIntent.DownloadData.equivalentReqCode -> {
                ContentFragState.DownloadData(true)
            }
            getEquivReqCode(ContentFragIntent.Login::class) -> {
                ContentFragState.Login(true)
            }
            else -> null
        }
    }
}