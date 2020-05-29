package sidev.lib.implementation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.act_test.*
import sidev.lib.android.siframe.lifecycle.activity.SimpleAbsBarContentNavAct

class TestAct : SimpleAbsBarContentNavAct() {
    override val contentLayoutId: Int
        get() = R.layout.act_test

    override fun initActBar(actBarView: View) {
        setActBarTitle("TestAct...")
    }
    override fun initNavBar(navBarView: BottomNavigationView) {}
    override fun initView(layoutView: View) {
        tv.text= "Test bro oy!!!"
    }
}
