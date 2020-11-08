package sidev.lib.implementation._simulation.edu_class.frag

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main._simul_edu_comp_nav_modul_item_container.view.*
//import kotlinx.android.synthetic.main.comp_nav_modul_item_container.view.*
//import sidev.kuliah.tekber.edu_class.R
//import sidev.kuliah.tekber.edu_class.adp.PageNavAdp
//import sidev.kuliah.tekber.edu_class.frag.ContentFrag
import sidev.kuliah.tekber.edu_class.model.Module
import sidev.kuliah.tekber.edu_class.model.Page
//import sidev.kuliah.tekber.edu_class.presenter.PageContentPres
//import sidev.kuliah.tekber.edu_class.util.Const
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.BarContentNavBase
//import sidev.lib.android.siframe.customizable._init._Config
//import sidev.lib.android.siframe.customizable.view.ModVp
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerBase
import sidev.lib.android.siframe.lifecycle.fragment.mvp.MvpDrawerVpFrag
//import sidev.lib.android.siframe.presenter.Presenter
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util._ViewUtil.setBgColorTintRes
import sidev.lib.android.siframe.tool.util._ViewUtil.setColorTintRes
import sidev.lib.android.siframe.tool.util.`fun`.getExtra
import sidev.lib.android.siframe.tool.util.`fun`.getRootView
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.`fun`.toast
import sidev.lib.android.siframe.view.ModVp
import sidev.lib.check.asNotNull
import sidev.lib.check.notNull
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.edu_class.adp.PageNavAdp
import sidev.lib.implementation._simulation.edu_class.pres.PageContentPres
import sidev.lib.implementation._simulation.edu_class.util.Edu_Class_Const

class ContentFragMain : MvpDrawerVpFrag<ContentFrag>(){ //, PresenterCallbackCommon{
    override val startDrawerLayoutId: Int
        get() = R.layout._simul_edu_comp_nav_modul_item_container
    override val endDrawerLayoutId: Int
        get() = _Config.INT_EMPTY
/*
    override val bottomLayoutId: Int
        get() = R.layout.comp_nav_forth_back
 */

    override var vpFragList: Array<ContentFrag> = arrayOf()
    override var vpFragListStartMark: Array<Int> = arrayOf()
    override var isVpBackOnBackPressed: Boolean= false
    /*
    override var bottomContainer: View?= null
    override var middleContainer: View?= null
    override var topContainer: View?= null
 */

    lateinit var module: Module
    lateinit var selectedPageId: String

    lateinit var sideNavAdp: PageNavAdp


//    override fun _initNavBar(navBarView: BottomNavigationView) {}
    override fun _initEndDrawerView(endDrawerView: View) {}
//    override fun _initMiddleView(middleView: View) {}
//    override fun _initTopView(topView: View) {}

    override fun _initDataFromIntent(intent: Intent) {
        super._initDataFromIntent(intent)
        module= intent.getExtra(Edu_Class_Const.DATA_MODULE)!!
    }

    override fun initPresenter(): Presenter? {
        loge("initPresenter()")
        return PageContentPres(this)
    }
/*
    override fun _initActBar(actBarView: View) {
        setActBarTitle(module.name)
        actBarView.findViewById<ImageView>(_Config.ID_IV_BACK).notNull { iv ->
            iv.setImageResource(R.drawable.ic_hamburger)
            setColor(iv, R.color.putih)
            iv.rotation= 0f
            iv.setOnClickListener { slideDrawer(DrawerBase.Type.DRAWER_START) }
        }
    }
 */

/*
    override fun _initBottomView(bottomView: View) {
        bottomView.ll_back_container.setOnClickListener { pageBackward() }
        bottomView.ll_forth_container.setOnClickListener { pageForth() }
        loge("_initBottomView()")
    }
 */

    override fun _initStartDrawerView(startDrawerView: View) {
        loge("_initStartDrawerView()")
        setBgColorTintRes(startDrawerView, R.color.putih)
        startDrawerView.ll_reload_container.setOnClickListener { downloadPageList() }

        sideNavAdp= PageNavAdp(context!!, null)
        sideNavAdp.setOnItemClickListener { v, pos, data ->
            val isNextPageQuiz= data.isQuiz
            val isNextPageQuizStilValid= data.isQuizStillValid
//            loge("pos= $pos isNextPageQuiz= $isNextPageQuiz isNextPageQuizStilValid= $isNextPageQuizStilValid")
            if(isNextPageQuiz && isNextPageQuizStilValid){
                vpFragList[vp.currentItem].
                showDialogConfirm("Halaman yang Anda tujuh adalah halaman kuis.\nApakah Anda ingin lanjut mengerjakan kuis?",
                    "Lanjut")
                { dialog, isCancelled ->
                    if(!isCancelled)
                        toPage(pos)
                    dialog.cancel()
                }
            } else
                toPage(pos)
        }

        _ViewUtil.Comp.getTv?.invoke(startDrawerView).notNull { tv -> tv.text= module.name }
        _ViewUtil.Comp.getRv?.invoke(startDrawerView).notNull { rv ->
            sideNavAdp.rv= rv
            loge("sideNavAdp.rv udah diisi")
        }
        downloadPageList()
    }

    override fun _initView(layoutView: View) {
        activity.asNotNull { act: BarContentNavBase ->
            act.setActBarTitle(module.name)
            act.actBarViewContainer.findViewById<ImageView>(_Config.ID_IV_BACK).notNull { iv ->
                iv.setImageResource(R.drawable.ic_hamburger)
                setColorTintRes(iv, R.color.putih)
                iv.rotation= 0f
                iv.setOnClickListener { slideDrawer(DrawerBase.Type.DRAWER_START) }
            }
        }
        vp.asNotNull { modVp: ModVp ->
            modVp.isTouchable= false
            modVp.isTouchInterceptable= false
            loge("vp is ModableVp")
        }
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                sideNavAdp.selectedPageInd= position
            }
        })
    }

/*
    fun initSideNavView(){
//        __initTopMiddleBottomView(actSimple!!.layoutView)
/*
        actSimple.asNotNull { act: DrawerActBase ->
            context!!.inflate(R.layout.comp_nav_modul_item_container, act.startDrawerContainer).notNull { v ->
                _ViewUtil.Comp.getTv?.invoke(v).notNull { tv -> tv.text= module.name }
                _ViewUtil.Comp.getRv?.invoke(v).notNull { rv -> sideNavAdp.rv= rv }

                act.setDrawerView(DrawerActBase.Type.DRAWER_START, v)
            }
        }
 */
    }
 */


    fun downloadPageList(){
        loge("downloadPageList() module.id= ${module.id}")
        downloadData(Edu_Class_Const.REQ_GET_PAGE, Edu_Class_Const.DATA_MODULE_ID to module.id)
        showSideNavPb()
        showVpPb()
        showSideNavNoData(false)
    }


    fun showVpPb(show: Boolean= true){
        getRootView()?.findViewById<ProgressBar>(R.id.pb).notNull { pb ->
            pb.visibility= if(show) View.VISIBLE
            else View.GONE
            vp.visibility= if(!show) View.VISIBLE
            else View.GONE
        }
    }
    fun showSideNavPb(show: Boolean= true){
//        loge("showSideNavPb() startDrawerContainer.findViewById<View>(R.id.pb)= ${startDrawerContainer?.findViewById<View>(R.id.pb)}")
        startDrawerContainer?.findViewById<View>(R.id.pb)?.visibility= if(show) View.VISIBLE
        else View.GONE
        startDrawerContainer?.findViewById<View>(R.id.rv)?.visibility= if(!show) View.VISIBLE
        else View.GONE
//        startDrawerContainer.findViewById<SwipeRefreshLayout>(R.id.srl)?.isRefreshing= show
    }
    fun showSideNavNoData(show: Boolean= true, msg: String?= null){
        startDrawerContainer?.findViewById<View>(R.id.ll_reload_container).notNull { v ->
            v.visibility= if(show) View.VISIBLE
            else View.GONE
            v.findViewById<TextView>(R.id.tv_no_data)?.text= msg
        }
        startDrawerContainer?.findViewById<View>(R.id.rv)?.visibility= if(!show) View.VISIBLE
        else View.GONE
    }

    fun toPage(pos: Int){
        selectedPageId= sideNavAdp.dataList!![pos].id
        vp.currentItem= pos
        sideNavAdp.selectedPageInd= pos
    }

    override fun onPresenterSucc(
        request: String,
        result: Int,
        data: Map<String, Any>?,
        resCode: Int
    ) {
        showVpPb(false)
        showSideNavPb(false)
        when(request){
            Edu_Class_Const.REQ_GET_PAGE -> {
                loge("onPresenterSucc() resCode == Const.RES_NO_PAGE => ${resCode == Edu_Class_Const.RES_NO_PAGE}")
                if(resCode == Edu_Class_Const.RES_NO_PAGE)
                    showSideNavNoData(msg= Edu_Class_Const.MSG_NO_DATA)
                else {
                    val pageList= data!![Edu_Class_Const.DATA_PAGE] as ArrayList<Page>
                    if(pageList.isEmpty())
                        showSideNavNoData(msg= Edu_Class_Const.MSG_NO_DATA)
                    else {
//                        val pageListSize= pageList.size
                        showSideNavNoData(false)
                        val contentFragList= Array(pageList.size){
                            val frag= ContentFrag()
//                            frag.pageId= pageList[it].id
                            frag.pageData= pageList[it]
                            frag.isNextPageQuiz=
                                try{ pageList[it+1].isQuiz }
                                catch (e: IndexOutOfBoundsException){ false }
                            frag.isNextPageQuizStillValid=
                                try{ pageList[it+1].isQuizStillValid }
                                catch (e: IndexOutOfBoundsException){ true }
                            frag
                        }
                        sideNavAdp.dataList= pageList
                        setFragList(contentFragList)
                        loge("onPresenterSucc() Const.REQ_GET_PAGE pageList.isNotEmpty() pageList.size= ${pageList.size}")
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
    ) {
        showVpPb(false)
        showSideNavPb(false)
        when(request){
            Edu_Class_Const.REQ_GET_PAGE -> {
                toast("Terjadi kesalahan download data page.\nHarap ulangi.")
                showSideNavNoData(msg= Edu_Class_Const.MSG_LOAD_ERROR_PAGE)
            }
            else -> toast("Terjadi kesalahan.\nHarap ulangi halaman ini.")
        }
    }

}