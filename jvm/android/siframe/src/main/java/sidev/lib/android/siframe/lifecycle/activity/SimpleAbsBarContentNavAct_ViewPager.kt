package sidev.lib.android.siframe.lifecycle.activity

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import sidev.lib.android.siframe.intfc.view.ViewPagerActView
import java.lang.Exception

abstract class SimpleAbsBarContentNavAct_ViewPager<F: SimpleAbsFrag> : SimpleAbsBarContentNavAct(),
    ViewPagerActView<F> {
    override var onPageFragActiveListener: HashMap<Int, OnPageFragActiveListener>
        = HashMap()
    override val viewPagerActViewView: View
        get() = contentViewContainer
    override val fm: FragmentManager
        get() = supportFragmentManager
    override val ctx: Context
        get() = this

    override var pageStartInd: Int= 0
    override var pageEndInd: Int= try{vpFragList.size -1} catch(e: Exception){0}
    override lateinit var vpFragListMark_int: Array<Int>

    override fun initView_int(contentView: View) {
        super.initView_int(contentView)
        initVp()
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