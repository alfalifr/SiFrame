package sidev.lib.android.siframe.lifecycle.fragment

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerFragBase
import sidev.lib.universal.`fun`.asNotNullTo

abstract class DrawerVpFrag<F: Frag> : VpFrag<F>(), DrawerFragBase{
    override val layoutId: Int
        get() = super<DrawerFragBase>.layoutId
    override val contentLayoutId: Int
        get() = super<VpFrag>.layoutId

    override var rootDrawerLayout: DrawerLayout?= null
        get() = activity.asNotNullTo { act: DrawerActBase -> act.rootDrawerLayout }
            ?: field
    override var contentViewContainer: ViewGroup?= null
        get() = activity.asNotNullTo { act: DrawerActBase -> act.contentViewContainer }
            ?: field
    override var startDrawerContainer: ViewGroup?= null
        get() = activity.asNotNullTo { act: DrawerActBase -> act.startDrawerContainer }
            ?: field
    override var endDrawerContainer: ViewGroup?= null
        get() = activity.asNotNullTo { act: DrawerActBase -> act.endDrawerContainer }
            ?: field


    override fun __initViewFlow(rootView: View) {
        if(_prop_act is DrawerActBase)
            super.__initViewFlow(rootView)
        else
            __initDrawer(rootView)
    }

//    override fun __initDrawer(rootView: View) {}

    /**
     * Kemungkinan dipanggil adalah saat activity-nya berupa SingleFragDrawerAct.
     */
    abstract override fun _initStartDrawerView(startDrawerView: View)
    abstract override fun _initEndDrawerView(endDrawerView: View)
}