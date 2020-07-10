package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.BackBtnBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.SingleFragDrawerActBase
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener

abstract class SingleFragDrawerAct_BarContentNav: SingleFragAct_BarContentNav(), SingleFragDrawerActBase{ //SingleFragAct(), DrawerActBase{
    final override val isViewInitFirst: Boolean
        get() = false
    final override val isContentLayoutInflatedFirst: Boolean
        get() = false

    final override val layoutId: Int
        get() = super<SingleFragDrawerActBase>.layoutId
    final override val contentLayoutId: Int
        get() = super<SingleFragDrawerActBase>.contentLayoutId

//    override lateinit var contentViewContainer: ViewGroup
    final override lateinit var rootDrawerLayout: DrawerLayout
    final override lateinit var startDrawerContainer: ViewGroup
    final override lateinit var endDrawerContainer: ViewGroup

    final override val _prop_backBtnBase: BackBtnBase?
        get() = this
/*
    //<27 Juni 2020> => __initDrawer(rootView) gak jadi di __initViewFlow(). Error dapat terjadi
    //                      karena saat __initDrawer(rootView) pada kelas SingleFragDrawerAct_BarContentNav_Simple
    //                      terdapat pemanggilan fragment yg blum diinit karena __initFrag() dilakukan di ___initSideBase()
    override fun __initViewFlow(rootView: View) {
        super.__initViewFlow(rootView)
        __initDrawer(rootView)
    }
 */

    override fun ___initSideBase() {
        super<SingleFragAct_BarContentNav>.___initSideBase()
        //super<SingleFragDrawerActBase>.___initSideBase()
        // -> <6 Juli 2020> => dikomen karena SingleFragDrawerActBase
        //        gak punya implementasi ___initSideBase()
        __initDrawer(layoutView.rootView)
    }

    /**
     * Gak usah karena view kontennya ada di dalam fragment
     */
    final override fun _initView(layoutView: View) {}
}