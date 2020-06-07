package sidev.lib.android.siframe.lifecycle.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
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

    override var onPageFragActiveListener: HashMap<Int, OnPageFragActiveListener>
        = HashMap()

    override val layoutId: Int
        get() = super<SimpleAbsBarContentNavAct>.layoutId
    override val contentLayoutId: Int
        get() = super<ViewPagerActBase>.layoutId


    /*
    override val viewPagerActViewView: View
        get() = contentViewContainer
 */


    override var pageStartInd: Int= 0
    override var pageEndInd: Int= try{vpFragList.size -1} catch(e: Exception){0}
    override lateinit var vpFragListMark: Array<Int>


    override fun _initInheritorBase() {
        super<ViewPagerActBase>._initInheritorBase()
    }

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