package sidev.lib.implementation.act

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.act_drawer.view.*
import org.jetbrains.anko.textColorResource
import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct_Simple
import sidev.lib.android.siframe.lifecycle.activity.SingleFragDrawerAct
import sidev.lib.android.siframe.lifecycle.activity.SingleFragDrawerAct_BarContentNav
import sidev.lib.android.siframe.tool.util.`fun`.detachFromParent
import sidev.lib.implementation.R

/*
class SingleFragImplAct : SingleFragDrawerAct(){
    override lateinit var fragment: Fragment
    override val startDrawerLayoutId: Int
        get() = R.layout.comp_drawer_start
    override val endDrawerLayoutId: Int
        get() = _Config.INT_EMPTY

    override fun _initStartDrawerView(startDrawerView: View) {
        startDrawerView.tv.text= "Ini teks sebenarnya dari StartDrawer SingleFragImplAct SingleFragDrawerAct"
        startDrawerView.tv.textColorResource= _ColorRes.TEXT_LIGHT
    }
    override fun _initEndDrawerView(endDrawerView: View) {}
}
 */

class SingleFragImplAct : SingleFragDrawerAct_BarContentNav(){
    override lateinit var fragment: Fragment
    override val startDrawerLayoutId: Int
        get() = R.layout.comp_drawer_start
    override val endDrawerLayoutId: Int
        get() = _Config.INT_EMPTY


    override fun _initActBar(actBarView: View) {
        waitForFrag { frag ->
            val fragName= fragment::class.java.simpleName
            setActBarTitle("SingleFragAct: $fragName")
            Log.e("SingleFragImplAct", "_initActBar")
        }
    }
    override fun _initNavBar(navBarView: BottomNavigationView) {}

    override fun _initStartDrawerView(startDrawerView: View) {
        startDrawerView.tv.text= "Ini teks sebenarnya dari StartDrawer SingleFragImplAct"
        startDrawerView.tv.textColorResource= _ColorRes.TEXT_LIGHT
    }
    override fun _initEndDrawerView(endDrawerView: View) {}

    override fun __initViewFlow(rootView: View) {
        super.__initViewFlow(rootView)
        rootView.findViewById<View>(fragContainerId)!!//.detachFromParent()
    }
}