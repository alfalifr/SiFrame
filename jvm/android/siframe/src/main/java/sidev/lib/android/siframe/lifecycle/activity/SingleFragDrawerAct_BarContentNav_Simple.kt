package sidev.lib.android.siframe.lifecycle.activity

import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerFragBase
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.getExtra
import sidev.lib.universal.`fun`.asNotNull

/**
 * Kelas yg properti abstraknya dapat di-lateinit.
 */
open class SingleFragDrawerAct_BarContentNav_Simple: SingleFragDrawerAct_BarContentNav(){
    final override lateinit var fragment: Fragment
    final override var startDrawerLayoutId: Int= _Config.INT_EMPTY
    final override var endDrawerLayoutId: Int= _Config.INT_EMPTY

    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {}
//    final override fun _initView(layoutView: View) {}


    //<27 Juni 2020> => __initDrawer(rootView) gak jadi di __initViewFlow(). Error dapat terjadi
    //  karena saat __initDrawer(rootView) pada kelas SingleFragDrawerAct_BarContentNav_Simple
    //  terdapat pemanggilan fragment yg blum diinit karena __initFrag() dilakukan di ___initSideBase()
    //<6 Juli 2020> => Definisi baru. Kemabali diletakkan di sini karena pengambilan id layout drawer
    //  lebih cocok dilakukan sebelum [__initDrawer].
    //  Jika ternyata fragment merupakan [DrawerFragBase], maka ambil [DrawerFragBase.startDrawerLayoutId]
    //  atau [DrawerFragBase.endDrawerLayoutId]-nya lalu init untuk kedua-kalinya.
    @CallSuper
    override fun __initDrawer(rootView: View) {
        startDrawerLayoutId= _prop_intent.getExtra(_SIF_Constant.DRAWER_START_LAYOUT_ID, startDrawerLayoutId)!!
        endDrawerLayoutId= _prop_intent.getExtra(_SIF_Constant.DRAWER_END_LAYOUT_ID, endDrawerLayoutId)!!
        super.__initDrawer(rootView)
    }

    @CallSuper
    override fun ___initSideBase() {
        super.___initSideBase()
///*
        fragment.asNotNull { frag: DrawerFragBase ->
            startDrawerLayoutId= frag.startDrawerLayoutId
            endDrawerLayoutId= frag.endDrawerLayoutId
            super.__initDrawer(layoutView.rootView) //Ini init kedua setelah sblumnya suda diinit namun fragment msh blum diinit.
                    //<6 Juli 2020> => Knp harus manggil super?
                    //   Jika manggil langsung [__initDrawer], maka id layout drawer dari intent akan diambil lagi
                    //   sehingga id layout drawer dari fragment akan kalah.
        }
/*
        .asntNotNull<Fragment, DrawerFragBase> {
//            <6 Juli 2020> => Definisi lama.
            startDrawerLayoutId= _prop_intent.getExtra(_SIF_Constant.DRAWER_START_LAYOUT_ID, startDrawerLayoutId)!!
            endDrawerLayoutId= _prop_intent.getExtra(_SIF_Constant.DRAWER_END_LAYOUT_ID, endDrawerLayoutId)!!
            __initDrawer(layoutView.rootView) //Ini init kedua setelah sblumnya suda diinit namun fragment msh blum diinit.
            loge("___initSideBase() fragment as NOT DrawerFragBase")
        }
// */
    }

    @CallSuper
    override fun _initStartDrawerView(startDrawerView: View) {
        try{ //Karena kemungkinan Drawer sudah diinit namun fragment msh blum.
            fragment.asNotNull { frag: DrawerFragBase ->
                frag._initStartDrawerView(startDrawerView)
            }
        } catch (e: Exception){}
    }
    @CallSuper
    override fun _initEndDrawerView(endDrawerView: View) {
        try{ //Karena kemungkinan Drawer sudah diinit namun fragment msh blum.
            fragment.asNotNull { frag: DrawerFragBase ->
                frag._initEndDrawerView(endDrawerView)
            }
        } catch (e: Exception){}
    }
}

/*
class SingleFragDrawerAct_BarContentNav_Simple: SingleFragDrawerAct_BarContentNav(){
    override lateinit var fragment: Fragment
    override var startDrawerLayoutId: Int= _Config.INT_EMPTY
    override var endDrawerLayoutId: Int= _Config.INT_EMPTY

    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {}
    override fun _initView(layoutView: View) {}

    override fun _initStartDrawerView(startDrawerView: View) {}
    override fun _initEndDrawerView(endDrawerView: View) {}
}
 */