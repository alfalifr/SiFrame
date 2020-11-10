package sidev.lib.implementation.act

import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import sidev.lib.android.siframe.lifecycle.activity.*
import sidev.lib.android.std.tool.util.`fun`.loge

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

class SingleFragImplAct : SingleFragDrawerAct_BarContentNav_Simple(){
//    override var startDrawerLayoutId: Int= R.layout.comp_drawer_start
//    override var endDrawerLayoutId: Int= _Config.INT_EMPTY


    override fun _initActBar(actBarView: View) {
//        isActBarViewFromFragment= true
        loge("SingleFragImplAct._initActBar()")
        waitForFrag { frag ->
            val fragName= frag::class.java.simpleName
            setActBarTitle("SingleFragAct: $fragName")
            Log.e("SingleFragImplAct", "_initActBar")
        }
    }
    override fun _initNavBar(navBarView: BottomNavigationView) {}
/*
    override fun _initStartDrawerView(startDrawerView: View) {
        startDrawerView.tv.text= "Ini teks sebenarnya dari StartDrawer SingleFragImplAct"
        startDrawerView.tv.textColorResource= _ColorRes.TEXT_LIGHT
    }
    override fun _initEndDrawerView(endDrawerView: View) {
        endDrawerView.tv.text= "Ini teks sebenarnya dari EndDrawer SingleFragImplAct"
        endDrawerView.tv.textColorResource= _ColorRes.TEXT_LIGHT
    }
 */
}