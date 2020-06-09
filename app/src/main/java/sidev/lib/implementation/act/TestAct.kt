package sidev.lib.implementation.act

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.act_test.*
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.customizable._init._Constant
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.tool.util._AppUtil
import sidev.lib.android.siframe.tool.util.`fun`.startAct
import sidev.lib.implementation.R
import sidev.lib.universal.tool.util.ThreadUtil

class TestAct : SimpleAbsBarContentNavAct() {
    override val contentLayoutId: Int
        get() = R.layout.act_test

    override fun _initActBar(actBarView: View) {
        setActBarTitle("TestAct...")
    }
    override fun _initNavBar(navBarView: BottomNavigationView) {}
    override fun _initView(layoutView: View) {
//        tv.text= "Test bro oy!!!"

        ThreadUtil.delayRun(3000){
            startAct<Test2Act>()
        }
        ThreadUtil.delayRun(5000){
            _AppUtil.blockApp(this)
        }
        ThreadUtil.delayRun(9000){
            _AppUtil.openAppBlock(this)
        }
    }

    fun toVpAct(v: View)= startAct<ViewPagerAct>()
    fun toDrawerAct(v: View)= startAct<DrawerImplAct>()
    fun toVpDrawerAct(v: View)= startAct<VpDrawerAct>()
}
