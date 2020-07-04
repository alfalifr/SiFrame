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
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.MultipleActBarViewPagerBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import sidev.lib.universal.`fun`.classSimpleName
import java.lang.Exception

abstract class BarContentNavAct_ViewPager<F: Frag>
    : BarContentNavAct(), MultipleActBarViewPagerBase<F> { //ViewPagerActBase<F>
    override val _prop_act: AppCompatActivity
        get() = this
    override val _prop_view: View
        get() = contentViewContainer
    override val _prop_intent: Intent
        get() = intent
    override val _prop_fm: FragmentManager
        get() = supportFragmentManager

/*
    override val _prop_ctx: Context
        get() = this
 */

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

    //<2 Juli 2020> => Programmer gak perlu mendefinisikan scr langsung.
    override var vpFragListStartMark: Array<Int> = arrayOf()
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