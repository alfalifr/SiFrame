package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.app.Activity
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.set
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.asNotNullTo
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.tool.util.ThreadUtil
import java.lang.Exception

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
        val thisName= this::class.java.simpleName
        loge("thisName= $thisName ___initSideBase()")
//        super.___initSideBase()
        if(defaultActBarView == null && actBarContainer_vp != null && actBarContainer_vp!!.childCount > 0){
            defaultActBarView= actBarContainer_vp!!.getChildAt(0)
        }
//        loge("___initSideBase() defaultActBarView == null = ${defaultActBarView == null}")

        setFragList(vpFragList)
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                attachActBarView(position)
                attachActBarTitle(position)
                vpFragList[position].onActive(_sideBase_view, this@MultipleActBarViewPagerActBase, position)
                onPageFragActiveListener[position]?.onPageFragActive(_sideBase_view, position) //
            }
        })
        if(vpFragList.isNotEmpty()){
            attachActBarView(0)
            attachActBarTitle(0)
        }
    }

    fun attachActBarView(pos: Int){
        val isActContentNavAct= this is SimpleAbsBarContentNavAct
        val caller= ThreadUtil.getCurrentCallerFunName()
        val caller1= ThreadUtil.getCurrentCallerFunName(1)
        val caller2= ThreadUtil.getCurrentCallerFunName(2)
        val caller3= ThreadUtil.getCurrentCallerFunName(3)
        loge("attachActBarView() ||| pos= $pos this::class.java.simpleName= ${this::class.java.simpleName} \n caller= $caller \n caller1= $caller1 \n caller2= $caller2 \n caller3= $caller3 \n isActContentNavAct= $isActContentNavAct")
        if(actBarContainer_vp != null){
            loge("MASUK ||||")
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
                        }.isNull { resetDefaultActBar() }
                    } else
                        resetDefaultActBar()
                }
            }
        }
    }

    override fun attachActBarTitle(pos: Int){
        val isActContentNavAct= this is SimpleAbsBarContentNavAct
        val caller= ThreadUtil.getCurrentCallerFunName()
        val caller1= ThreadUtil.getCurrentCallerFunName(1)
        val caller2= ThreadUtil.getCurrentCallerFunName(2)
        val caller3= ThreadUtil.getCurrentCallerFunName(3)
        loge("attachActBarTitle() ||| pos= $pos this::class.java.simpleName= ${this::class.java.simpleName} \n caller= $caller \n caller1= $caller1 \n caller2= $caller2 \n caller3= $caller3 \n isActContentNavAct= $isActContentNavAct")
        if(isVpTitleFragBased){
            loge("MASUK ||||")
            when(this){
                is Activity -> this
                is Fragment -> this.activity
                else -> null
            }.asNotNull { act: SimpleAbsBarContentNavAct ->
                val title= vpFragList[pos].fragTitle
                try{ act.setActBarTitle(title) }
                catch (e: Exception){
                    (actBarViewList[pos] ?: defaultActBarView).notNull { actBar ->
                        _ViewUtil.Comp.setTvTitleTxt(actBar, title)
                    }
                    /* Karena act.setActBarTitle() dapat dioverride.
                     */
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
/*
            if(defaultActBarView == null && actBarContainer_vp != null && actBarContainer_vp!!.childCount > 0){
                defaultActBarView= actBarContainer_vp!!.getChildAt(0)
            }
 */
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