package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.BackBtnBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase

abstract class DrawerBarContentNavAct : BarContentNavAct(), DrawerActBase {
    override val isViewInitFirst: Boolean
        get() = false
    override val isContentLayoutInflatedFirst: Boolean
        get() = false

//    override lateinit var contentViewContainer: ViewGroup
    final override lateinit var rootDrawerLayout: DrawerLayout
    final override lateinit var startDrawerContainer: ViewGroup
    final override lateinit var endDrawerContainer: ViewGroup

    override val layoutId: Int
        get() = super<DrawerActBase>.layoutId

    final override val _prop_backBtnBase: BackBtnBase?
        get() = this

    override fun __initViewFlow(rootView: View) {
        super.__initViewFlow(rootView)
        __initDrawer(rootView)
//        val contentType= contentViewContainer::class.java.simpleName
//        contentViewContainer.removeViewAt(0)
//        Log.e("DrawerBarContentNavAct", "contentViewContainer != null = ${contentViewContainer != null} contentViewContainer.childCount= ${contentViewContainer.childCount} contentType= $contentType")
    }
}