package sidev.lib.implementation.presenter

import sidev.lib.android.siframe.arch.presenter.*
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation._cob.contentList
import sidev.lib.implementation.util.Const
import sidev.lib.universal.`fun`.copyGrowTimely
import sidev.lib.universal.`fun`.toArrayList
import sidev.lib.universal.tool.util.ThreadUtil

class ContentPresenter(c: PresenterCallback<String, Int>?) : Presenter(c){
    companion object{
        val REQ_GET_CONTENT= "get_content"
        val REQ_LOGIN= "login"
        val DATA_UNAME= "data_uname"
    }

    override fun checkDataIntegrity(
        request: String,
        direction: ArchPresenter.Direction,
        data: Map<String, Any>?
    ): Boolean {
        return when(request){
            else -> true
        }
    }
    override fun processRequest(request: String, data: Map<String, Any>?) {
        when(request){
            REQ_GET_CONTENT -> getContent()
            REQ_LOGIN -> {
                val uname= try{ data!![DATA_UNAME] as String } catch (e: Exception){ "sriname" }
                login(uname)
            }
        }
//        postSucc("1", null)
    }

    fun login(uname: String){
        ThreadUtil.delayRun(3000){
            if(uname == "uname")
                postSucc(Const.RES_OK, null)
            else
                postFail(0, msg= "Uname != uname :( uname= $uname")
        }
        ThreadUtil.delayRun(5000){
            postFail(0, "Error login bro dari presenter")
        }
    }

    fun getContent(){
        loge("getContent()")
        ThreadUtil.delayRun(3000){
            loge("getContent() ThreadUtil.delayRun(3000)")
            postSucc(Const.RES_OK, mapOf(Const.DATA_CONTENT to contentList.toArrayList().copyGrowTimely(8)))
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