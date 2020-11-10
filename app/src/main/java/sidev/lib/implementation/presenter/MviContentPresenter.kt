package sidev.lib.implementation.presenter

import sidev.lib.android.siframe.arch.annotation.ViewIntentFunction
import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.presenter.ArchPresenter
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.std.tool.util._ThreadUtil
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.collection.copyGrowTimely
import sidev.lib.collection.toArrayList
import sidev.lib.implementation._cob.contentList
import sidev.lib.implementation.intent_state.*

class MviContentPresenter(c: StateProcessor<CFIntent, CFRes, CFState<*>>?)
    : MviPresenter<CFIntent, CFRes, CFState<*>>(c){
    companion object{
        val REQ_GET_CONTENT= "get_content"
        val REQ_LOGIN= "login"
        val DATA_UNAME= "uname"
    }

    override fun checkDataIntegrity(
        request: CFIntent,
        direction: ArchPresenter.Direction,
        data: Map<String, Any>?
    ): Boolean {
        return when(request){
            else -> true
        }
    }

    override fun processRequest(request: CFIntent, additionalData: Map<String, Any>?) {
        callAnnotatedViewIntentFun(request)
/*
        when(reqCode){
            is ContentFragIntent.DownloadData /*REQ_GET_CONTENT*/ -> getContent()
            is ContentFragIntent.Login -> {
                val uname= reqCode.uname //try{ data!![DATA_UNAME] as String } catch (e: Exception){ "sriname" }
                login(uname)
            }
        }
 */
    }

/*
    override fun checkDataIntegrity(
        reqCode: String,
        direction: ArchPresenter.Direction,
        data: Map<String, Any>?
    ): Boolean {
        return when(reqCode){
            else -> true
        }
    }
    override fun processRequest(reqCode: String, data: Map<String, Any>?) {
        when(reqCode){
            REQ_GET_CONTENT -> getContent()
            getEquivReqCode(ContentFragIntent.Login::class) -> {
                val uname= try{ data!![DATA_UNAME] as String } catch (e: Exception){ "sriname" }
                login(uname)
            }
        }
    }
 */
    @ViewIntentFunction(ContentFragConf.Intent.Login::class)
    fun login(uname: String){
        _ThreadUtil.delayRun(3000){
            if(uname == "uname")
                postSucc(ContentFragConf.Result.Login(true), null)
            else
                postSucc(ContentFragConf.Result.Login(false, "Uname != uname :( uname= $uname")) //"Uname != uname :( uname= $uname"
        }
        _ThreadUtil.delayRun(5000){
            postFail(msg= "Error login bro dari presenter")
        }
    }

    @ViewIntentFunction(ContentFragConf.Intent.DownloadData::class)
    fun getContent(){
        loge("getContent()")
        _ThreadUtil.delayRun(3000){
            loge("getContent() ThreadUtil.delayRun(3000)")
            postSucc(ContentFragConf.Result.DownloadData(contentList.toArrayList().copyGrowTimely(10) as ArrayList))
        }
/*
        ThreadUtil.delayRun(5000){
            loge("getContent() ThreadUtil.delayRun(3000)")
            postSucc(Const.RES_OK, null)
        }
 */
/*
        ThreadUtil.delayRun(5000){
            loge("getContent() ThreadUtil.delayRun(5000)")
            val list= contentList.toArrayList()
            list.add(Content("9", "Ini judul baru ke 9", "Desc ke 9"))
            list.add(Content("10", "Ini judul baru ke 10", "Desc ke 10"))
            postSucc(Const.RES_OK, mapOf(Const.DATA_CONTENT to list))
        }
        ThreadUtil.delayRun(7000){
            postFail(0)
        }
        ThreadUtil.delayRun(9000){
            val list= ArrayList<Content>()
            postSucc(Const.RES_OK, mapOf(Const.DATA_CONTENT to list))
        }
 */
    }
}