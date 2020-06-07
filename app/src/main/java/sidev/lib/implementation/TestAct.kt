package sidev.lib.implementation

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.act_test.*
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct
import sidev.lib.android.siframe.tool.util._AppUtil
import sidev.lib.android.siframe.tool.util.`fun`.startAct
import sidev.lib.implementation.universal.tool.util.ThreadUtil

class TestAct : SimpleAbsBarContentNavAct() {
    override val contentLayoutId: Int
        get() = R.layout.act_test

    override fun initActBar(actBarView: View) {
        setActBarTitle("TestAct...")
    }
    override fun initNavBar(navBarView: BottomNavigationView) {}
    override fun initView(layoutView: View) {
        tv.text= "Test bro oy!!!"

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
}
