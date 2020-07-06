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
    final override val _prop_act: AppCompatActivity?
        get() = activity as AppCompatActivity
    final override val _prop_view: View
        get() = layoutView
    final override val _prop_intent: Intent?
        get() = activity?.intent
    final override val _prop_fm: FragmentManager?
        get() = fragmentManager

    //<6 Juli 2020> => Di-final karena jika diubah di level turunan, maka dapat menyebabkan error.
    final override val layoutId: Int
        get() = super.layoutId
/*
    override val _sideBase_ctx: Context?
        get() = context
 */

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