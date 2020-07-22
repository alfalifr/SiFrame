package sidev.lib.implementation.intent_state.processor

import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.intent_state.*
import sidev.lib.universal.exception.Exc

class ContentFragStatePros(view: MviView<CFIntent, CFRes, CFState<*>>)
    : StateProcessor<CFIntent, CFRes, CFState<*>>(view){
    override fun processPreState(
        intent: CFIntent,
        additionalData: Map<String, Any>?
    ): CFState<*>? {
        return when(intent){
            is ContentFragConf.Intent.DownloadData -> {
                ContentFragConf.State.DownloadData(true)
            }
            is ContentFragConf.Intent.Login -> {
                ContentFragConf.State.Login(true)
            }
        }
    }

    override fun processState(
        intent: CFIntent,
        result: CFRes?,
        additionalData: Map<String, Any>?,
        e: Exc?
    ): CFState<*>? {
        return when(intent){
            is ContentFragConf.Intent.DownloadData -> {
                ContentFragConf.State.DownloadData()
            }
            is ContentFragConf.Intent.Login -> {
                val res= result as? ContentFragConf.Result.Login
                loge("res?.isSuccess = ${res?.isSuccess}")
                val msg= e?.message ?: res?.msg
/*
                    ?: if(res?.isSuccess == true) "Berhasil Login bro!!!"
                    else "Gagal login"
 */
                ContentFragConf.State.Login(toastMsg = msg)
            }
        }
    }
}
/*
class ContentFragStatePros(view: MviView<ContentFragIntent, ContentFragResult, ContentFragState<*>>)
    : StateProcessor<ContentFragState, ContentFragIntent>(view){
    override fun processPreState(
        intent: ContentFragIntent,
        additionalData: Map<String, Any>?
    ): ContentFragState? {
        return when(intent){
            is ContentFragIntent.DownloadData -> {
                ContentFragState.DownloadData(true)
            }
            is ContentFragIntent.Login -> {
                ContentFragState.Login(true)
            }
            else -> null
        }
    }

    override fun processState(
        intent: ContentFragIntent,
        resCode: Int,
        data: Map<String, Any>?,
        isError: Boolean,
        exc: Exception?,
        errorMsg: String?
    ): ContentFragState? {
        return when(intent){
            is ContentFragIntent.DownloadData -> {
                val state= ContentFragState.DownloadData(isError = isError, errorMsg = errorMsg)
                try{
                    state.rvDataList= data!![Const.DATA_CONTENT] as ArrayList<Content>
                }catch (e:Exception){}
                state
            }
            is ContentFragIntent.Login -> {
                val state= ContentFragState.Login(isSucces = resCode == Const.RES_OK,
                    isError = isError, errorMsg = errorMsg ?: "Error login bro")
                if(!isError)
                    state.toastMsg= "Berhasil Login bro!!!"
                state
            }
            else -> null
        }
    }

    /*
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

     */
}
 */