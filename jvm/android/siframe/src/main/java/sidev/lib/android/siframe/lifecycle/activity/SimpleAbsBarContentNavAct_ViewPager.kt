package sidev.lib.android.siframe.lifecycle.activity

import android.content.Context
import android.content.Intent
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import sidev.lib.android.siframe.adapter.VpFragAdp
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.MultipleActBarViewPagerActBase
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import java.lang.Exception

abstract class SimpleAbsBarContentNavAct_ViewPager<F: SimpleAbsFrag>
    : SimpleAbsBarContentNavAct(), MultipleActBarViewPagerActBase<F> { //ViewPagerActBase<F>
    override val _sideBase_act: AppCompatActivity
        get() = this
    override val _sideBase_view: View
        get() = contentViewContainer
    override val _sideBase_intent: Intent
        get() = intent
    override val _sideBase_ctx: Context
        get() = this
    override val _sideBase_fm: FragmentManager
        get() = supportFragmentManager

    override var onPageFragActiveListener: SparseArray<OnPageFragActiveListener>
        = SparseArray()

    override val layoutId: Int
        get() = super<SimpleAbsBarContentNavAct>.layoutId
    override val contentLayoutId: Int
        get() = super<MultipleActBarViewPagerActBase>.layoutId
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
            if(v) attachActBarView(vp.currentItem)
        }
    override val actBarContainer_vp: ViewGroup
        get() = actBarViewContainer
    override var defaultActBarView: View?= null


    override lateinit var vpAdp: VpFragAdp
    override lateinit var vpFragListMark: Array<Int>
    override var pageStartInd: Int= 0
    override var pageEndInd: Int= try{vpFragList.size -1} catch(e: Exception){0}
    override var isVpTitleFragBased: Boolean= false
    override var vpBackOnBackPressed: Boolean= true

    override var vpOnPageListenerToNavBar: ViewPager.OnPageChangeListener?= null


    override fun ___initSideBase() {
        super<MultipleActBarViewPagerActBase>.___initSideBase()
        addOnBackBtnListener {
            if(vpBackOnBackPressed)
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