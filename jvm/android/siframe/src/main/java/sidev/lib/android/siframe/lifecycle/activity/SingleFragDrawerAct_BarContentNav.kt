package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.SingleFragDrawerActBase

abstract class SingleFragDrawerAct_BarContentNav: SingleFragAct_BarContentNav(), SingleFragDrawerActBase{ //SingleFragAct(), DrawerActBase{
    override val isViewInitFirst: Boolean
        get() = false
    override val isContentLayoutInflatedFirst: Boolean
        get() = false

    override val layoutId: Int
        get() = super<SingleFragDrawerActBase>.layoutId
    override val contentLayoutId: Int
        get() = super<SingleFragDrawerActBase>.contentLayoutId

    override lateinit var contentViewContainer: ViewGroup
    override lateinit var rootDrawerLayout: DrawerLayout
    override lateinit var startDrawerContainer: ViewGroup
    override lateinit var endDrawerContainer: ViewGroup

    //<27 Juni 2020> => __initDrawer(rootView) gak jadi di __initViewFlow(). Error dapat terjadi
    //                      karena saat __initDrawer(rootView) pada kelas SingleFragDrawerAct_BarContentNav_Simple
    //                      terdapat pemanggilan fragment yg blum diinit karena __initFrag() dilakukan di ___initSideBase()
    override fun __initViewFlow(rootView: View) {
        super.__initViewFlow(rootView)
        __initDrawer(rootView)
    }
/*
    override fun ___initSideBase() {
        super<SingleFragAct_BarContentNav>.___initSideBase()
//        super<SingleFragDrawerActBase>.___initSideBase()
        __initDrawer(layoutView.rootView)
    }
 */

    /**
     * Gak usah karena view kontennya ada di dalam fragment
     */
    final override fun _initView(layoutView: View) {}
}