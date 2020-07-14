package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.comp_drawer_start_iv.view.*
import kotlinx.android.synthetic.main.comp_drawer_start_iv.view.tv
import kotlinx.android.synthetic.main.comp_rb.view.*
import kotlinx.android.synthetic.main.page_rg.view.*
import org.jetbrains.anko.imageResource
import sidev.lib.android.siframe._customizable._ColorRes
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.lifecycle.fragment.DrawerFrag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R

class DrawerImplFrag : DrawerFrag(){
    override val contentLayoutId: Int
        get() = R.layout.page_rg
    override val startDrawerLayoutId: Int
        get() = R.layout.comp_drawer_start_iv
    override val endDrawerLayoutId: Int
        get() = _Config.INT_EMPTY


    override fun _initStartDrawerView(startDrawerView: View) {
        loge("Tes masuk _initStartDrawerView() DrawerImplFrag")
        startDrawerView.iv.imageResource= R.drawable.ic_check_circle
        startDrawerView.tv.text= "Teks starDrawer DrawerImplFrag"
        _ViewUtil.setColorRes(startDrawerView.iv, R.color.ijo)
        _ViewUtil.setBgColorRes(startDrawerView, _ColorRes.COLOR_LIGHT)
    }

    override fun _initEndDrawerView(endDrawerView: View) {
        loge("Tes masuk _initEndDrawerView() DrawerImplFrag")
    }

    override fun _initView(layoutView: View) {
        loge("_initView() DrawerImplFrag")
        layoutView.comp_3.rb.text = "Ini dia teks nya " +layoutView.comp_3.rb.text
    }
}