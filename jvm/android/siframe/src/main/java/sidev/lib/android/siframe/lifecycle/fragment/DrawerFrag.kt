package sidev.lib.android.siframe.lifecycle.fragment

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerActBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerFragBase
import sidev.lib.universal.`fun`.asNotNullTo

abstract class DrawerFrag : Frag(), DrawerFragBase {
    override val _prop_act: AppCompatActivity?
        get() = activity as AppCompatActivity
    override val _prop_view: View
        get() = layoutView
    override val _prop_intent: Intent?
        get() = activity?.intent
    override val _prop_fm: FragmentManager?
        get() = fragmentManager
/*
    override val _sideBase_ctx: Context?
        get() = context
 */

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