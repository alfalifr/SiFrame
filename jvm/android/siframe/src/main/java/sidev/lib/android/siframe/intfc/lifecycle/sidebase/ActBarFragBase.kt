package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import androidx.fragment.app.Fragment
import sidev.lib.android.siframe.intfc.`fun`.InitActBarFun
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.intfc.prop.ActProp
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.`fun`.notNullTo

interface ActBarFragBase: LifecycleSideBase, InitActBarFun, ActBarFromFragBase, ActProp {
    override val actBarId: Int
    var actBarView: View?

    override fun ___initSideBase() {}

    /**
     * @return true jika actBar berhasil diganti dan sebaliknya.
     */
    fun setActBar(actBar: View): Boolean{
        return when(_sideBase_act){
            is SimpleAbsBarContentNavAct -> {
                (_sideBase_act as SimpleAbsBarContentNavAct).setActBarView(actBar)
                true
            }
            is MultipleActBarViewPagerActBase<*> ->
                (_sideBase_act as MultipleActBarViewPagerActBase<in SimpleAbsFrag>).setActBarView(this as SimpleAbsFrag, actBar)
            else -> false
        }
    }

    /**
     * Fungsi dibiarkan tidak dipanggil secara internal karena akan terjadi pemborosan resource
     * jika actBar diinflate tapi tidak dipakai. Fungsi ini hanya dipanggil oleh class MultipleActBarViewPagerActBase
     * sebagai wadah dari fragment ini.
     */
    fun getActBar(): View? {
        return actBarView
            ?: _sideBase_act.inflate(actBarId).notNullTo {
                actBarView= it
                it
            }
    }
}