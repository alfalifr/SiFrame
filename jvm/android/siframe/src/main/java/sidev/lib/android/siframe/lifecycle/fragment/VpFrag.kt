package sidev.lib.android.siframe.lifecycle.fragment

import android.content.Intent
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import org.jetbrains.anko.support.v4.act
import sidev.lib.android.siframe.adapter.VpFragAdp
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.BackBtnBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.MultipleActBarViewPagerBase
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.check.asNotNullTo
import java.lang.Exception

abstract class VpFrag<F: Frag> : Frag(), MultipleActBarViewPagerBase<F>{
    final override val _prop_act: AppCompatActivity
        get() = act as AppCompatActivity
    final override val _prop_view: View
        get() = layoutView
    final override val _prop_intent: Intent
        get() = act.intent
    final override val _prop_fm: FragmentManager
        get() = act.supportFragmentManager

    override val layoutId: Int
        get() = super.layoutId

    /*
    override val _sideBase_ctx: Context
        get() = act
 */

    final override val actBarViewList: SparseArray<View> = SparseArray()
    final override val actBarContainer_vp: ViewGroup?
        get(){
//            val actName= try{actSimple!!::class.java.simpleName} catch (e: Exception){null}
//            loge("actBarContainer_vp actName= $actName")
            return actSimple.asNotNullTo { act: BarContentNavAct ->
//                loge("act.actBarViewContainer != null => ${act.actBarViewContainer != null}")
                act.actBarViewContainer
            }
        }
    final override var defaultActBarView: View?= null
    override var isActBarViewFromFragment: Boolean= false
        set(v) {
            field= v
            if(v) try{ attachActBarView(vp.currentItem) } catch(e: Exception){}
        }
    //<2 Juli 2020> => Programmer gak perlu mendefinisikan scr langsung.
    override var vpFragListStartMark: Array<Int> = arrayOf()
    final override var onPageFragActiveListener: SparseArray<OnPageFragActiveListener> = SparseArray()
    final override lateinit var vpAdp: VpFragAdp
    final override lateinit var vpFragListMark: Array<Int>
    override var pageStartInd: Int= 0
    override var pageEndInd: Int= try{vpFragList.size -1} catch(e: Exception){0}
    override var isVpTitleFragBased: Boolean= false
    override var isVpBackOnBackPressed: Boolean= true

    final override var vpOnPageListenerToNavBar: ViewPager.OnPageChangeListener?= null

//    override lateinit var lateVp: ViewPager
/*
    override fun ___initRootBase(vararg args: Any) {
        super.___initRootBase(*args)
//        ___initSideBase()
    }
 */

    override fun ___initSideBase() {
        super<Frag>.___initSideBase()
        super<MultipleActBarViewPagerBase>.___initSideBase()

        if(_prop_act is BackBtnBase){
            (_prop_act as BackBtnBase).addOnBackBtnListener{
                if(isVpBackOnBackPressed)
                    pageBackward()
                else false
            }
        }
    }
}