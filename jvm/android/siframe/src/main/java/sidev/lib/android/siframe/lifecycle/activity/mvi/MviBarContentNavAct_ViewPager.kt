package sidev.lib.android.siframe.lifecycle.activity.mvi

import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct_ViewPager

abstract class MviBarContentNavAct_ViewPager<F: Frag, S: ViewState, I: ViewIntent>
    : BarContentNavAct_ViewPager<F>(), MviView<S, I> { //ViewPagerActBase<F>
    override var currentViewState: S?= null
//    override var intentConverter: IntentConverter<I>?= null
    abstract override fun initPresenter(): MviPresenter<S, I>?
}
/*
abstract class MviBarContentNavAct_ViewPager<F: Frag>
    : BarContentNavAct(), MultipleActBarViewPagerBase<F> { //ViewPagerActBase<F>
    override val _prop_act: AppCompatActivity
        get() = this
    override val _sideBase_view: View
        get() = contentViewContainer
    override val _sideBase_intent: Intent
        get() = intent
    override val _sideBase_ctx: Context
        get() = this
    override val _sideBase_fm: FragmentManager
        get() = supportFragmentManager

//    override lateinit var lateVp: ViewPager

    override var onPageFragActiveListener: SparseArray<OnPageFragActiveListener>
        = SparseArray()

    override val layoutId: Int
        get() = super<BarContentNavAct>.layoutId
    override val contentLayoutId: Int
        get() = super<MultipleActBarViewPagerBase>.layoutId
/*
    /**
     * top-middle-bottom container terletak pada SimpleAbsBarContentNavAct.contentViewContainer
     */
    override var topContainer: View?= null
    override var middleContainer: View?= null
    override var bottomContainer: View?= null
 */

    /*
    override val viewPagerActViewView: View
        get() = contentViewContainer
 */
    override val actBarViewList: SparseArray<View> = SparseArray()
    override var isActBarViewFromFragment: Boolean= false
        set(v){
            field= v
            if(v) try{ attachActBarView(vp.currentItem) } catch(e: Exception){}
        }
    override val actBarContainer_vp: ViewGroup
        get() = actBarViewContainer
    override var defaultActBarView: View?= null


    override lateinit var vpAdp: VpFragAdp
    override lateinit var vpFragListMark: Array<Int>
    override var pageStartInd: Int= 0
    override var pageEndInd: Int= try{vpFragList.size -1} catch(e: Exception){0}
    override var isVpTitleFragBased: Boolean= false
        set(v){
            field= v
            if(!v)
                try{ setActBarTitle(this.classSimpleName()) }
                catch (e: Exception){}
        }
    override var isVpBackOnBackPressed: Boolean= true

    override var vpOnPageListenerToNavBar: ViewPager.OnPageChangeListener?= null


    override fun ___initSideBase() {
        super<MultipleActBarViewPagerBase>.___initSideBase()
        addOnBackBtnListener {
            if(isVpBackOnBackPressed)
                pageBackward()
            else false
        }
    }

    /*
    override fun __initView(contentView: View) {
        super.__initView(contentView)
//        initVp()
        ___initSideBase()
//        _initTopMiddleBottomView(contentView)
    }
 */
}
 */