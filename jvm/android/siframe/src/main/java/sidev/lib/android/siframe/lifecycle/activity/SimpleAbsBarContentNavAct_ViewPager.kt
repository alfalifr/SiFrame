package sidev.lib.android.siframe.lifecycle.activity

import android.content.Context
import android.content.Intent
import android.util.SparseArray
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import sidev.lib.android.siframe.adapter.ViewPagerFragAdp
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerActBase
import java.lang.Exception

abstract class SimpleAbsBarContentNavAct_ViewPager<F: SimpleAbsFrag>
    : SimpleAbsBarContentNavAct(), ViewPagerActBase<F> {
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
        get() = super<ViewPagerActBase>.layoutId
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


    override lateinit var vpAdp: ViewPagerFragAdp
    override lateinit var vpFragListMark: Array<Int>
    override var pageStartInd: Int= 0
    override var pageEndInd: Int= try{vpFragList.size -1} catch(e: Exception){0}
    override var isVpTitleFragBased: Boolean= false

    override fun ___initSideBase() {
        super<ViewPagerActBase>.___initSideBase()
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
/*
    override fun __initView(contentView: View) {
        super.__initView(contentView)
//        initVp()
        ___initSideBase()
//        _initTopMiddleBottomView(contentView)
    }
 */
}