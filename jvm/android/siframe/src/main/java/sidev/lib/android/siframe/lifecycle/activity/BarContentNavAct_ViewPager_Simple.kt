package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import sidev.lib.android.siframe.adapter.VpFragAdp
import sidev.lib.android.siframe.lifecycle.fragment.Frag

/**
 * Kelas yg properti abstraknya dapat di-lateinit.
 */
open class BarContentNavAct_ViewPager_Simple
    : BarContentNavAct_ViewPager<Frag>(){
    //<12 Juli 2020> => Walau pun simple tapi programmer ttp dapat menentukan fragmentnya scr langsung
    override var vpFragList: Array<Frag> = arrayOf()
    override var vpFragListStartMark: Array<Int> = arrayOf()

    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {}
    override fun _initView(layoutView: View) {}
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