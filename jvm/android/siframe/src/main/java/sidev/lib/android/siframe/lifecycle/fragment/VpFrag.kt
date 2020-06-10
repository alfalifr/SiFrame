package sidev.lib.android.siframe.lifecycle.fragment

import android.content.Context
import android.content.Intent
import android.util.SparseArray
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import org.jetbrains.anko.support.v4.act
import sidev.lib.android.siframe.adapter.ViewPagerFragAdp
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ViewPagerActBase
import sidev.lib.android.siframe.intfc.listener.OnPageFragActiveListener
import java.lang.Exception

abstract class VpFrag<F: SimpleAbsFrag> : SimpleAbsFrag(), ViewPagerActBase<F>{
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

    override var onPageFragActiveListener: SparseArray<OnPageFragActiveListener> = SparseArray()
    override lateinit var vpAdp: ViewPagerFragAdp
    override lateinit var vpFragListMark: Array<Int>
    override var pageStartInd: Int= 0
    override var pageEndInd: Int= try{vpFragList.size -1} catch(e: Exception){0}
    override var isVpTitleFragBased: Boolean= false

    override fun ___initRootBase(vararg args: Any) {
        super.___initRootBase(*args)
        ___initSideBase()
    }
}