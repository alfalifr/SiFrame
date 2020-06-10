package sidev.lib.android.siframe.lifecycle.activity

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import sidev.lib.android.siframe.adapter.ViewPagerFragAdp
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerActBase

/**
 * Kelas yg properti abstraknya dapat di-lateinit
 */
open class SimpleAbsBarContentNavAct_ViewPager_Simple
    : SimpleAbsBarContentNavAct_ViewPager<SimpleAbsFrag>(){

    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {}
    override fun _initView(layoutView: View) {}

    override var vpFragList: Array<SimpleAbsFrag> = arrayOf()
    override var vpFragListStartMark: Array<Int> = arrayOf()
    override lateinit var vpAdp: ViewPagerFragAdp
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