package sidev.lib.implementation._simulation.edu_class.frag

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main._simul_edu_comp_nav_forth_back.view.*
import sidev.kuliah.tekber.edu_class.model.Page
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.TopMiddleBottomBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.lifecycle.fragment.mvp.MvpRvFrag
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.`fun`.toast
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.edu_class.adp.ContentAdp
import sidev.lib.implementation._simulation.edu_class.dialog.ContentWarningDialog
import sidev.lib.implementation._simulation.edu_class.model.Content
import sidev.lib.implementation._simulation.edu_class.pres.PageContentPres
import sidev.lib.implementation._simulation.edu_class.util.Edu_Class_Const
//import sidev.lib.implementation.adp.ContentAdp
import sidev.lib.universal.`fun`.*

class ContentFrag : MvpRvFrag<ContentAdp>(), TopMiddleBottomBase{
    //    lateinit var moduleId: String
    override var bottomContainer: ViewGroup?= null
    override var middleContainer: ViewGroup?= null
    override var topContainer: ViewGroup?= null

    override val bottomLayoutId: Int
        get() = R.layout._simul_edu_comp_nav_forth_back

//    lateinit var pageId: String
    lateinit var pageData: Page
    var isNextPageQuiz= false
    var isNextPageQuizStillValid= true
    var dialogWarning: ContentWarningDialog?= null
//    var canBack= true


    override fun _initMiddleView(middleView: View) {}
    override fun _initTopView(topView: View) {}
    override fun ___initSideBase() {}


    override fun initPresenter(): Presenter? {
        return PageContentPres(this)
    }

    override fun initRvAdp(): ContentAdp {
        return ContentAdp(context!!, null)
    }

    override fun _initBottomView(bottomView: View) {
        loge("_initBottomView() MULAI")
//        val nul= _prop_parentLifecycle == null
//        val type= _prop_parentLifecycle is ViewPagerBase<*>
//        loge("nul= $nul type= $type")
        _prop_parentLifecycle.asNotNull { act: ViewPagerBase<*> ->
            loge("_initBottomView() act: ViewPagerActBase<*>")
            bottomView.ll_back_container.setOnClickListener {
                if(pageData.isQuiz && pageData.isQuizStillValid)
                    toast("Anda masih mengerjakan kuis.\nHarap kumpulkan terlebih dahulu.")
                else
                    act.pageBackward()
            }
            bottomView.ll_forth_container.setOnClickListener {
                if(pageData.isQuiz && pageData.isQuizStillValid){
                    val mainMsg=
                        if(rvAdp.checkAnswer()) "Anda akan mengumpulkan kuis untuk dinilai. Lanjut kumpulkan?"
                        else "Anda beberapa pertanya yg belum terisi. Apakah anda yakin ingin mengumpulkan?"

                    showDialogConfirm(mainMsg, "Kumpulkan") { dialog, isCancelled ->
                        if(!isCancelled){
                            dialog.showPb()
                            sendQuizAnswer()
                        } else
                            dialog.cancel()
                    }
                } else{
                    if(isNextPageQuiz && isNextPageQuizStillValid){
                        showDialogConfirm("Anda akan memulai kuis. Lanjut mengerjakan kuis?",
                        "Lanjut")
                        { dialog, isCancelled ->
                            if(!isCancelled)
                                act.pageForth()
                            dialog.cancel()
                        }
                    } else {
                        act.pageForth()
                    }
                }
            }
            if(isNextPageQuiz){
                bottomView.tv_forth.text= "Lanjut Kuis"
                val tf= bottomView.tv_forth.typeface
                bottomView.tv_forth.setTypeface(tf, Typeface.BOLD)
            } else if(pageData.isQuiz && pageData.isQuizStillValid){
                bottomView.tv_forth.text= "Kumpulkan"
                val tf= bottomView.tv_forth.typeface
                bottomView.tv_forth.setTypeface(tf, Typeface.BOLD)
            } else{
                bottomView.tv_forth.text= "Selanjutnya"
                val tf= bottomView.tv_forth.typeface
                bottomView.tv_forth.setTypeface(tf, Typeface.NORMAL)
            }
        }
    }

    override fun _initView(layoutView: View) {
        __initTopMiddleBottomView(layoutView)
        onRefreshListener= { downloadContent(pageData.id) }
        downloadContent(pageData.id)
///*
        actSimple?.addOnBackBtnListener {
            _prop_parentLifecycle.asNotNullTo { act: ViewPagerBase<Frag> ->
                val fragInd= act.getFragPos(this)
                if(fragInd == act.vp.currentItem){
                    val canNotBack= pageData.isQuiz && pageData.isQuizStillValid
                    if(canNotBack)
                        toast("Anda masih mengerjakan kuis")
                    canNotBack
                } else
                    false
            } ?: false
        }
// */
//        downloadPageList()
    }

    fun downloadContent(pageId: String){
        downloadData(Edu_Class_Const.REQ_GET_CONTENT, Edu_Class_Const.DATA_PAGE_ID to pageId)
        showPb()
    }

    fun sendQuizAnswer(){
        val answerList= rvAdp.getAnswerList()
        downloadData(Edu_Class_Const.REQ_SEND_QUESTION_ANSWER,
            Edu_Class_Const.DATA_QUESTION_ANSWER to answerList,
            Edu_Class_Const.DATA_PAGE_ID to pageData.id
        )
        loge("sendQuizAnswer()")
        var i= -1
        for((questionId, answer) in answerList)
            loge("i= ${++i} questionId= $questionId => answer= ${answer.toSimpleString()}")
    }


    fun showPb(show: Boolean= true){
        layoutView.findViewById<View>(R.id.pb)?.visibility= if(show) View.VISIBLE
        else View.GONE
        layoutView.findViewById<View>(R.id.rv)?.visibility= if(!show) View.VISIBLE
        else View.GONE
        layoutView.findViewById<SwipeRefreshLayout>(R.id.srl)?.isRefreshing= show
        bottomContainer.asNotNull { vg: ViewGroup ->
            vg.getChildAt(0).visibility= if(show) View.GONE
                else View.VISIBLE
        }
    }

    fun showDialogConfirm(mainMsg: String, rightBtnMsg: String,
                          rightBtnTodo: ((ContentWarningDialog, isCancelled: Boolean) -> Unit)?= null){
        val dialog= dialogWarning.orDefault(ContentWarningDialog(context!!))
        dialog.setMainMsg(mainMsg)
        dialog.setRightBtnTxt(rightBtnMsg)
        dialog.onButtonClickListener= { btn, isCancelled ->
            rightBtnTodo?.invoke(dialog, isCancelled)
        }
        dialog.show()
        if(dialogWarning == null)
            dialogWarning= dialog
    }

    override fun onPresenterSucc(
        request: String,
        result: Int,
        data: Map<String, Any>?,
        resCode: Int
    ) {
        showPb(false)
        when(request){
            Edu_Class_Const.REQ_GET_CONTENT -> {
                if(resCode == Edu_Class_Const.RES_OK){
                    val data= data!![Edu_Class_Const.DATA_CONTENT] as ArrayList<Content>
                    rvAdp.dataList= data
                }
            }
            Edu_Class_Const.REQ_SEND_QUESTION_ANSWER -> {
                if(resCode == Edu_Class_Const.RES_OK){
                    dialogWarning?.showPb(false)
                    dialogWarning?.cancel()
                    pageData.isQuizStillValid= false
                    toast("Kuis berhasil dikumpulkan")
                    actSimple.asNotNull { act: ViewPagerBase<ContentFrag> ->
                        act.pageForth()
                        val ind= act.getFragPos(this)
                        loge("act is ViewPagerActBase<ContentFrag> ind= $ind")
                        try{ act.vpFragList[ind-1].isNextPageQuizStillValid= false }
                        catch (e: Exception){}
                    }
                }
            }
        }
    }

    override fun onPresenterFail(
        request: String,
        result: Int?,
        msg: String?,
        e: Exception?,
        resCode: Int
    ) {}
}