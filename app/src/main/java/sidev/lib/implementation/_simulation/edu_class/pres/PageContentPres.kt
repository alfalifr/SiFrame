package sidev.lib.implementation._simulation.edu_class.pres

//import sidev.kuliah.tekber.edu_class._cob.dumm_module
//import sidev.kuliah.tekber.edu_class._cob.dumm_page
//import sidev.kuliah.tekber.edu_class.util.Const
import sidev.lib.android.siframe.arch.presenter.ArchPresenter
import sidev.lib.android.siframe.arch.presenter.Presenter
//import sidev.lib.android.siframe.arch.presenter.PresenterCallback
import sidev.lib.android.siframe.arch.presenter.PresenterCallbackCommon
import sidev.lib.android.siframe.arch.presenter.PresenterCallbackCommon_
//import sidev.lib.android.siframe.presenter.Presenter
//import sidev.lib.android.siframe.presenter.PresenterCallback
import sidev.lib.android.siframe.tool.util._StorageUtil
import sidev.lib.android.siframe.tool.util.`fun`.toObjList
import sidev.lib.implementation._cob.dumm_module
import sidev.lib.implementation._cob.dumm_page
import sidev.lib.implementation._simulation.edu_class.util.Edu_Class_Const
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.`fun`.search
import sidev.lib.universal.tool.util.ThreadUtil

class PageContentPres(c: PresenterCallbackCommon) : Presenter(c){
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
            Edu_Class_Const.REQ_GET_PAGE -> {
                val moduleId= data!![Edu_Class_Const.DATA_MODULE_ID] as String
                getPage(moduleId)
            }
            Edu_Class_Const.REQ_GET_CONTENT -> {
                val pageId= data!![Edu_Class_Const.DATA_PAGE_ID] as String
                getContent(pageId)
            }
            Edu_Class_Const.REQ_SEND_QUESTION_ANSWER -> {
                val answerList= data!![Edu_Class_Const.DATA_QUESTION_ANSWER] as Map<String, List<String>>
                val pageId= data[Edu_Class_Const.DATA_PAGE_ID] as String
                val uname= _StorageUtil.SharedPref.get(ctx!!, Edu_Class_Const.KEY_UNAME)!!
                sendQuestionAnswer(uname, pageId, answerList)
            }
        }
    }

    fun getPage(moduleId: String){
        ThreadUtil.delayRun(3000){
            dumm_module.search { module -> module.id == moduleId }.notNull { module ->
                val page= module.pageList.toObjList()!!
                postSucc(Edu_Class_Const.RES_OK, mapOf(Edu_Class_Const.DATA_PAGE to page))
            }
        }
    }

    fun getContent(pageId: String){
        ThreadUtil.delayRun(3000){
            dumm_page.search { page -> page.id == pageId }.notNull { page ->
                val content= page.contentList.toObjList()!!
                postSucc(Edu_Class_Const.RES_OK, mapOf(Edu_Class_Const.DATA_CONTENT to content))
            }
        }
    }

    fun sendQuestionAnswer(uname: String, pageId: String, answerList: Map<String, List<String>>){
        ThreadUtil.delayRun(3000){
            postSucc(Edu_Class_Const.RES_OK, null)
        }
    }
}