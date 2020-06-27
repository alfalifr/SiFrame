package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerBase
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag

abstract class DrawerBarContentNavAct_ViewPager<F: SimpleAbsFrag>
    : SimpleAbsBarContentNavAct_ViewPager<F>(), DrawerActBase {
    override val isViewInitFirst: Boolean
        get() = false
    override val isContentLayoutInflatedFirst: Boolean
        get() = false

    override lateinit var contentViewContainer: ViewGroup
    override lateinit var rootDrawerLayout: DrawerLayout
    override lateinit var startDrawerContainer: ViewGroup
    override lateinit var endDrawerContainer: ViewGroup

    override val layoutId: Int
        get() = super<DrawerActBase>.layoutId

    override fun __initViewFlow(rootView: View) {
        super.__initViewFlow(rootView)
        __initDrawer(rootView)
    }
    /*
    override val contentLayoutId: Int
        get() = super<SimpleAbsBarContentNavAct_ViewPager>.contentLayoutId

 */
}

/*
abstract class DrawerBarContentNavAct_ViewPager<F: SimpleAbsFrag>
    : SimpleAbsBarContentNavAct_ViewPager<F>(), DrawerActBase{
    override val isViewInitFirst: Boolean
        get() = false
    override val isContentLayoutInflatedFirst: Boolean
        get() = false

    override lateinit var contentViewContainer: ViewGroup
    override lateinit var rootDrawerLayout: DrawerLayout
    override lateinit var startDrawerContainer: ViewGroup
    override lateinit var endDrawerContainer: ViewGroup

    override val layoutId: Int
        get() = super<DrawerActBase>.layoutId

    override fun __initViewFlow(rootView: View) {
        super.__initViewFlow(rootView)
        __initDrawer(rootView)
//        val contentType= contentViewContainer::class.java.simpleName
//        contentViewContainer.removeViewAt(0)
//        Log.e("DrawerBarContentNavAct", "contentViewContainer != null = ${contentViewContainer != null} contentViewContainer.childCount= ${contentViewContainer.childCount} contentType= $contentType")
    }
}
 */