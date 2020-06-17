package sidev.lib.implementation.act

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util._AppUtil
import sidev.lib.android.siframe.tool.util.`fun`.startAct
import sidev.lib.android.siframe.tool.util.`fun`.startSingleFragAct_config
import sidev.lib.implementation.R
import sidev.lib.implementation.frag.*
import sidev.lib.universal.tool.util.ThreadUtil

class MainAct : SimpleAbsBarContentNavAct() {
    override val contentLayoutId: Int
        get() = R.layout.act_main

    override fun _initActBar(actBarView: View) {
        setActBarTitle("TestAct...")
    }
    override fun _initNavBar(navBarView: BottomNavigationView) {}
    override fun _initView(layoutView: View) {
//        tv.text= "Test bro oy!!!"
/*
        ThreadUtil.delayRun(3000){
            startAct<Test2Act>()
        }
        ThreadUtil.delayRun(5000){
            _AppUtil.blockApp(this)
        }
        ThreadUtil.delayRun(9000){
            _AppUtil.openAppBlock(this)
        }
 */
    }

    fun toVpAct(v: View)= startAct<ViewPagerAct>()
    fun toDrawerAct(v: View)= startAct<DrawerImplAct>()
    fun toVpDrawerAct(v: View)= startAct<VpDrawerAct>()
    fun toSingleFragAct(v: View)= startSingleFragAct_config<Frag4>(
        _SIF_Constant.DRAWER_END_LAYOUT_ID to R.layout.comp_drawer_start
    ) //startAct<VpDrawerAct>()
    fun toRvPageFrag(v: View)= startSingleFragAct_config<RvFrag>()
    fun toVpPageFrag(v: View)= startSingleFragAct_config<VpImplFrag>()
    fun toActBarFrag(v: View)= startSingleFragAct_config<ActBarFrag>()
    fun toVpPageFrag2(v: View)= startSingleFragAct_config<VpImpl2Frag>()
    fun toRvFragDbPage(v: View)= startSingleFragAct_config<RvDbFrag>()
    fun toRadioFrag(v: View)= startSingleFragAct_config<RadioFrag>()
}
