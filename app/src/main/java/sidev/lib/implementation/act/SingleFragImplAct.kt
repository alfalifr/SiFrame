package sidev.lib.implementation.act

import android.content.Context
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loge("SingleFragImplAct ======== onCreate")
    }

    override fun onStart() {
        super.onStart()
        loge("SingleFragImplAct ======== onStart")
    }

    override fun onResume() {
        super.onResume()
        loge("SingleFragImplAct ======== onResume")
    }

    override fun onPause() {
        super.onPause()
        loge("SingleFragImplAct ======== onPause")
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to [Activity.onStop] of the containing
     * Activity's lifecycle.
     */
    override fun onStop() {
        super.onStop()
        loge("SingleFragImplAct ======== onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        loge("SingleFragImplAct ======== onDestroy")
    }
/*
    override fun onAttach(context: Context) {
        super.onAttach(context)
        loge("SingleFragImplAct ======== onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        loge("SingleFragImplAct ======== onDetach")
    }
 */
}