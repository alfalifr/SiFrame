package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.set
import androidx.viewpager.widget.ViewPager
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull

interface MultipleActBarViewPagerActBase<F: SimpleAbsFrag> : ViewPagerActBase<F>, ActBarFromFragBase{
    val actBarViewList: SparseArray<View>
    /**
     * Child pada ViewGroup ini harus 1.
     */
    val actBarContainer_vp: ViewGroup?
    /**
     * View actBar sebelum diganti oleh view yg ada pada actBarViewList.
     */
    var defaultActBarView: View?

    override var isActBarViewFromFragment: Boolean


    override fun ___initSideBase() {
        super.___initSideBase()
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                attachActBarView(position)
            }
        })
    }

    fun attachActBarView(pos: Int){
        if(actBarContainer_vp != null){
            actBarViewList[pos].notNull { actBar ->
                actBarContainer_vp!!.removeAllViews()
                actBarContainer_vp!!.addView(actBar)
            }.isNull {
                if(isActBarViewFromFragment){
                    val frag= vpFragList[pos]
                    if(frag is ActBarFragBase){
                        frag.getActBar().notNull { actBar ->
                            setActBarView(frag, actBar)
                            frag._initActBar(actBar)
                        }
                    }
                }
            }
        }
    }

    /**
     * @return true jika berhasil dan sebaliknya.
     */
    fun setActBarView(frag: F, actBar: View): Boolean{
        if(!isActBarViewFromFragment) return false
        val pos= vpFragList.indexOf(frag)
        return if(pos >= 0){
            if(defaultActBarView == null && actBarContainer_vp != null && actBarContainer_vp!!.childCount > 0){
                defaultActBarView= actBarContainer_vp!!.getChildAt(0)
            }
            actBarViewList[pos] = actBar
            actBarContainer_vp!!.removeAllViews()
            actBarContainer_vp!!.addView(actBar)
            true
        } else false
    }

    fun resetDefaultActBar(){
        if(defaultActBarView != null && actBarContainer_vp != null){
            actBarContainer_vp!!.removeAllViews()
            actBarContainer_vp!!.addView(defaultActBarView)
        }
    }
}