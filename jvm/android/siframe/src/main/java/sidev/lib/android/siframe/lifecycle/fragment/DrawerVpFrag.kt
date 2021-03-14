package sidev.lib.android.siframe.lifecycle.fragment

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.BackBtnBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerFragBase
import sidev.lib.check.asNotNullTo

abstract class DrawerVpFrag<F: Frag> : VpFrag<F>(), DrawerFragBase {
    final override val layoutId: Int
        get() = super<DrawerFragBase>.layoutId
    final override val contentLayoutId: Int
        get() = super<VpFrag>.layoutId

    final override var rootDrawerLayout: DrawerLayout?= null
    final override var contentViewContainer: ViewGroup?= null
    final override var startDrawerContainer: ViewGroup?= null
    final override var endDrawerContainer: ViewGroup?= null

    final override val _prop_backBtnBase: BackBtnBase?
        get() = activity.asNotNullTo { act: BackBtnBase -> act }
    /*
    final override val onBackBtnListener: OnBackPressedListener
            = object : OnBackPressedListener {
        override fun onBackPressed_(): Boolean {
            return if(rootDrawerLayout != null){
                val startDrawerIsOpen= isDrawerOpen(DrawerBase.Type.DRAWER_START)
                val endDrawerIsOpen= isDrawerOpen(DrawerBase.Type.DRAWER_END)

                if(startDrawerIsOpen)
                    slideDrawer(DrawerBase.Type.DRAWER_START, false)

                if(endDrawerIsOpen)
                    slideDrawer(DrawerBase.Type.DRAWER_END, false)

                startDrawerIsOpen || endDrawerIsOpen
            } else false
        }
    }
 */
/*
    final override var rootDrawerLayout: DrawerLayout?= null
        get() = activity.asNotNullTo { act: DrawerActBase -> act.rootDrawerLayout }
            ?: field
    final override var contentViewContainer: ViewGroup?= null
        get() = activity.asNotNullTo { act: DrawerActBase -> act.contentViewContainer }
            ?: field
    final override var startDrawerContainer: ViewGroup?= null
        get() = activity.asNotNullTo { act: DrawerActBase -> act.startDrawerContainer }
            ?: field
    final override var endDrawerContainer: ViewGroup?= null
        get() = activity.asNotNullTo { act: DrawerActBase -> act.endDrawerContainer }
            ?: field
// */


    override fun __initViewFlow(rootView: View) {
/*
        <10 Juli 2020> => [__initViewFlow] dilakukan dari dalam Fragment semua, tidak scr ekternal
          dari Activity agar alur jadi normal.
        if(_prop_act is DrawerActBase)
            super.__initViewFlow(rootView)
        else
// */
            __initDrawer(rootView)
    }

//    override fun __initDrawer(rootView: View) {}

    /**
     * Kemungkinan dipanggil adalah saat activity-nya berupa SingleFragDrawerAct.
     */
    abstract override fun _initStartDrawerView(startDrawerView: View)
    abstract override fun _initEndDrawerView(endDrawerView: View)
}