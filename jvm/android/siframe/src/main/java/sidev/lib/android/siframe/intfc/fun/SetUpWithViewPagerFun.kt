package sidev.lib.android.siframe.intfc.`fun`

import android.view.View
import androidx.annotation.CallSuper
import androidx.viewpager.widget.ViewPager

interface SetUpWithViewPagerFun {
    var vp: ViewPager?
    var onPageChangeListener: ViewPager.OnPageChangeListener?
    var activePageView: View?
    @CallSuper
    fun setUpWithViewPager(vp: ViewPager){
        if(onPageChangeListener != null)
            this.vp?.removeOnPageChangeListener(onPageChangeListener!!)
        onPageChangeListener= object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                activePageView= vp.getChildAt(position)
            }
        }
        vp.addOnPageChangeListener(onPageChangeListener!!)
        this.vp= vp
    }
}