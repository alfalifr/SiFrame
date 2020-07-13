package sidev.lib.implementation.act

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import sidev.lib.android.siframe.lifecycle.activity.BarContentNavAct
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.startAct
import sidev.lib.android.siframe.tool.util.`fun`.startSingleFragAct_config
import sidev.lib.android.siframe.tool.util.asColorIn
import sidev.lib.android.siframe.tool.util.getColorOf
import sidev.lib.android.siframe.tool.util.getFontOf
import sidev.lib.android.siframe.tool.util.isColorIn
import sidev.lib.implementation.R
import sidev.lib.implementation._cob.dumm_module
import sidev.lib.implementation._simulation.edc.frag.TransacFrag
import sidev.lib.implementation._simulation.edc.frag.TransacFrag_Old
import sidev.lib.implementation._simulation.edu_class.frag.ContentFragMain
import sidev.lib.implementation._simulation.edu_class.util.Edu_Class_Const
import sidev.lib.implementation.frag.*

class MainAct : BarContentNavAct() {
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
    fun toVpBnvAct(v: View)= startAct<ViewPagerBnvAct>()
    fun toVpLateinitAct(v: View)= startAct<ViewPagerLateinitAct>()
    fun toDrawerAct(v: View)= startAct<DrawerImplAct>()
    fun toVpDrawerAct(v: View)= startAct<VpDrawerAct>()
    fun toSingleFragAct(v: View)= startSingleFragAct_config<Frag4>(
        _SIF_Constant.DRAWER_END_LAYOUT_ID to R.layout.comp_drawer_start
    ) //startAct<VpDrawerAct>()
    fun toRvPageFrag(v: View)= startSingleFragAct_config<RvFragImpl>()
    fun toVpPageFrag(v: View)= startSingleFragAct_config<VpImplFrag>()
    fun toActBarFrag(v: View)= startSingleFragAct_config<ActBarFrag>()
    fun toVpPageFrag2(v: View)= startSingleFragAct_config<VpImpl2Frag>()
    fun toRvFragDbPage(v: View)= startSingleFragAct_config<RvDbFrag>()
    fun toRadioFrag(v: View)= startSingleFragAct_config<RadioFrag>()
    fun toContentVmFrag(v: View)= startSingleFragAct_config<ContentVmFrag>()
    fun toContentMviFrag(v: View)= startSingleFragAct_config<ContentMviFrag>()
    fun toContentMvpFrag(v: View)= startSingleFragAct_config<ContentMvpFrag>()
    fun toDrawerVpImplFrag(v: View)= startSingleFragAct_config<DrawerVpImplFrag>()
    fun toDrawerImplFrag(v: View)= startSingleFragAct_config<DrawerImplFrag>()

    fun toCobFrag(v: View)= startSingleFragAct_config<CobFrag>()
//    fun toDrawerImpl2Frag(v: View)= startSingleFragAct_config<DrawerImplFrag>()

//    fun toDrawerImpl2Frag(v: View)= startSingleFragAct_config<DrawerImplFrag>()

    fun _simul_edc_toTransaction(v: View)= startSingleFragAct_config<TransacFrag>()
    fun _simul_edc_toTransaction_Old(v: View)= startSingleFragAct_config<TransacFrag_Old>()

    fun _simul_edu_toContentAct(v: View)= startSingleFragAct_config<ContentFragMain>(
        Edu_Class_Const.DATA_MODULE to dumm_module[0]
    )
}
