package sidev.lib.android.siframe.lifecycle.activity.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct_ViewPager_Simple

/**
 * Kelas yg properti abstraknya dapat di-lateinit.
 */
open class MvpBarContentNavAct_ViewPager_Simple
    : BarContentNavAct_ViewPager_Simple(), MvpView{
    override fun initPresenter(): Presenter?= null

    override fun onPresenterSucc(request: String, result: Int,
        data: Map<String, Any>?, resCode: Int) {}

    override fun onPresenterFail(request: String, result: Int?,
        msg: String?, e: Exception?, resCode: Int) {}
}


/*
abstract class SimpleAbsBarContentNavAct_ViewPager_Simple
    : SimpleAbsBarContentNavAct(),
    ViewPagerActBase<SimpleAbsFrag> {
    override var onPageFragActiveListener: HashMap<Int, OnPageFragActiveListener>
        = HashMap()
    override val viewPagerActViewView: View
        get() = contentViewContainer
    override val vpFm: FragmentManager
        get() = supportFragmentManager
    override val vpCtx: Context
        get() = this
    override var pageStartInd: Int= 0
    override var pageEndInd: Int
        get() = vpFragList.size
        set(v) {}
    override var vpFragListStartMark: Array<Int>
        get() = arrayOf()
        set(value) {}
    override lateinit var vpFragListMark: Array<Int>

    override fun initView_int(contentView: View) {
        super.initView_int(contentView)
//        initVp()
        _initInheritorBase()
        addOnBackBtnListener {
            pageBackward()
/*
            val isFirstPage= vp.currentItem == 0
            if(!isFirstPage){
                vp.currentItem= vp.currentItem -1
            }
            !isFirstPage
 */
        }
    }
}
 */