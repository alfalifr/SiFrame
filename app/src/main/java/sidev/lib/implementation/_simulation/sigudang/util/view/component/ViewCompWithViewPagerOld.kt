package com.sigudang.android.utilities.view.component
/*
import android.content.Context
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.sigudang.android.R


open class ViewCompWithViewPagerOld(c: Context, view: View?): ViewComp_old(c, view) {
    override val layoutId= R.layout.component_bar_search
    override var view: View?= null

    protected var vp: ViewPager?= null
    protected var activePageView: View?= null
    protected var activePageInd= 0
    protected var onPageChangeListener: ViewPager.OnPageChangeListener?= null


    override fun initViewCompOnce() {
        super.initViewCompOnce()
        initOnPageChangeListener()
    }

    override fun initViewComp() {}

    protected fun initOnPageChangeListener(){
        onPageChangeListener= object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                activePageView= vp?.getChildAt(position)
                activePageInd= position
            }
        }
    }

    fun setupWithViewPager(vp: ViewPager?){
        this.vp?.removeOnPageChangeListener(onPageChangeListener!!)
        vp?.addOnPageChangeListener(onPageChangeListener!!)
        this.vp= vp
    }
}

 */