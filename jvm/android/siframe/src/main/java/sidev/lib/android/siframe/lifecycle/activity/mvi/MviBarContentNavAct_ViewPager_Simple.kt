package sidev.lib.android.siframe.lifecycle.activity.mvi

import sidev.lib.android.siframe.arch.intent_state.*
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct_ViewPager_Simple

/**
 * Kelas yg properti abstraknya dapat di-lateinit.
 */
open class MviBarContentNavAct_ViewPager_Simple
    : BarContentNavAct_ViewPager_Simple(), MviView<ViewIntent, IntentResult, ViewState<*>>{
    override var currentViewState: ViewState<*>?= null
//    override var intentConverter: IntentConverter<ViewIntent>?= null
    override fun initPresenter(): MviPresenter<ViewIntent, IntentResult, ViewState<*>>? = null
    override fun initStateProcessor(): StateProcessor<ViewIntent, IntentResult, ViewState<*>>?= null
    override fun onNoCurrentState() {}
    override fun render(state: ViewState<*>) {}
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