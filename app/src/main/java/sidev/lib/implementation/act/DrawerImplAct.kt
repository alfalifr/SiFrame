package sidev.lib.implementation.act

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.act_drawer.view.*
import org.jetbrains.anko.textColorResource
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.lifecycle.activity.DrawerBarContentNavAct
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R

class DrawerImplAct : DrawerBarContentNavAct(){
    override val contentLayoutId: Int
        get() = R.layout.act_drawer
    override val startDrawerLayoutId: Int
        get() = R.layout.comp_drawer_start
    override val endDrawerLayoutId: Int
        get() = _Config.INT_EMPTY


    override fun _initActBar(actBarView: View) {}
    override fun _initNavBar(navBarView: BottomNavigationView) {}

    override fun _initView(layoutView: View) {
        layoutView.tv!!.text= "Ini teks sebenarnya dari DrawerAct 1"
        layoutView.iv.setOnClickListener {
            rootDrawerLayout.openDrawer(startDrawerContainer)
        }
        loge("_initView() ${this::class.java.simpleName}")
//        layoutView.findViewById<TextView>(R.id.tv).text= "Ini teks sebenarnya dari DrawerAct 2"
//        Log.e("DrawerImplAct", "_initView")
    }

    override fun _initStartDrawerView(startDrawerView: View) {
        startDrawerView.tv.text= "Ini teks sebenarnya dari startDrawer"
        startDrawerView.tv.textColorResource= R.color.putih
    }
    override fun _initEndDrawerView(endDrawerView: View) {}
}
