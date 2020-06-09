package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase

abstract class SingleFragDrawerAct: SingleFragAct(), DrawerActBase{
    override val layoutId: Int
        get()= super<DrawerActBase>.layoutId
    override val contentLayoutId: Int
        get() = super<SingleFragAct>.layoutId

    override lateinit var rootDrawerLayout: DrawerLayout
    override lateinit var contentViewContainer: ViewGroup
    override lateinit var startDrawerContainer: ViewGroup
    override lateinit var endDrawerContainer: ViewGroup

    override fun __initViewFlow(rootView: View) {
        __initDrawer(rootView)
    }

    /**
     * Gak usah karena view kontennya ada di dalam fragment
     */
    override fun _initView(layoutView: View) {}
}