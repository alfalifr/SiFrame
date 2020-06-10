package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.SingleDrawerFragActBase

abstract class SingleFragDrawerAct_BarContentNav: SingleFragAct_BarContentNav(), SingleDrawerFragActBase{ //SingleFragAct(), DrawerActBase{
    override val isViewInitFirst: Boolean
        get() = false
    override val isContentLayoutInflatedFirst: Boolean
        get() = false

    override val layoutId: Int
        get() = super<SingleDrawerFragActBase>.layoutId
    override val contentLayoutId: Int
        get() = super<SingleDrawerFragActBase>.contentLayoutId

    override lateinit var contentViewContainer: ViewGroup
    override lateinit var rootDrawerLayout: DrawerLayout
    override lateinit var startDrawerContainer: ViewGroup
    override lateinit var endDrawerContainer: ViewGroup

    override fun __initViewFlow(rootView: View) {
        super.__initViewFlow(rootView)
        __initDrawer(rootView)
    }

    /**
     * Gak usah karena view kontennya ada di dalam fragment
     */
    final override fun _initView(layoutView: View) {}

    final override fun _reinitStartDrawerView(func: (startDrawerView: View) -> Unit)
            = super._reinitStartDrawerView(func)
    final override fun _reinitEndDrawerView(func: (endDrawerView: View) -> Unit)
            = super._reinitEndDrawerView(func)
}