package sidev.lib.android.siframe.lifecycle.fragment

import android.content.Context
import android.content.Intent
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import org.jetbrains.anko.support.v4.act
import sidev.lib.android.siframe.adapter.VpFragAdp
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.BackBtnActBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.MultipleActBarViewPagerActBase
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNullTo
import java.lang.Exception

abstract class VpFrag<F: SimpleAbsFrag> : SimpleAbsFrag(), MultipleActBarViewPagerActBase<F>{
    override val _sideBase_act: AppCompatActivity
        get() = act as AppCompatActivity
    override val _sideBase_view: View
        get() = layoutView
    override val _sideBase_intent: Intent
        get() = act.intent
    override val _sideBase_ctx: Context
        get() = act
    override val _sideBase_fm: FragmentManager
        get() = act.supportFragmentManager


    override val actBarViewList: SparseArray<View> = SparseArray()
    override val actBarContainer_vp: ViewGroup?
        get(){
            val actName= try{actSimple!!::class.java.simpleName} catch (e: Exception){null}
            loge("actBarContainer_vp actName= $actName")
            return actSimple.asNotNullTo { act: SimpleAbsBarContentNavAct ->
                loge("act.actBarViewContainer != null => ${act.actBarViewContainer != null}")
                act.actBarViewContainer
            }
        }
    override var defaultActBarView: View?= null
    override var isActBarViewFromFragment: Boolean= false
        set(v) {
            field= v
            if(v) attachActBarView(vp.currentItem)
        }


    override var onPageFragActiveListener: SparseArray<OnPageFragActiveListener> = SparseArray()
    override lateinit var vpAdp: VpFragAdp
    override lateinit var vpFragListMark: Array<Int>
    override var pageStartInd: Int= 0
    override var pageEndInd: Int= try{vpFragList.size -1} catch(e: Exception){0}
    override var isVpTitleFragBased: Boolean= false

    override fun ___initRootBase(vararg args: Any) {
        super.___initRootBase(*args)
        ___initSideBase()
        if(_sideBase_act is BackBtnActBase){
            (_sideBase_act as BackBtnActBase).addOnBackBtnListener{
                pageBackward()
            }
        }
    }
}