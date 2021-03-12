package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import sidev.lib.android.siframe.intfc.`fun`.InitActBarFun
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.LifecycleSideBase
import sidev.lib.android.siframe.intfc.prop.AppCompatActProp
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.inflate
import sidev.lib.check.notNullTo

interface ActBarFragBase: LifecycleSideBase, InitActBarFun, ActBarFromFragBase, AppCompatActProp {
    override val actBarId: Int
    var actBarView: View?
    val isActBarInit: Boolean
    val isMultipleActBar: Boolean

    override fun ___initSideBase() {}

    /**
     * @return true jika actBar berhasil diganti dan sebaliknya.
     */
    fun setActBar(actBar: View): Boolean{
        return when(_prop_ctx){
            is BarContentNavAct -> {
                (_prop_ctx as BarContentNavAct).setActBarView(actBar)
                true
            }
            is MultipleActBarViewPagerBase<*> ->
                (_prop_ctx as MultipleActBarViewPagerBase<in Frag>).setActBarView(this as Frag, actBar)
            else -> false
        }
    }

    /**
     * Fungsi dibiarkan tidak dipanggil secara internal karena akan terjadi pemborosan resource
     * jika actBar diinflate tapi tidak dipakai. Fungsi ini hanya dipanggil oleh class MultipleActBarViewPagerActBase
     * sebagai wadah dari fragment ini.
     */
    fun getActBar(): View? {
        return actBarView ?: initActBar()
    }

    fun initActBar(): View? {
        return _prop_ctx?.inflate(actBarId).notNullTo {
            actBarView= it
            it
        }
    }
}