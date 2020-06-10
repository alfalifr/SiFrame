package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct
import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.util.`fun`.getExtra

/**
 * Interface yg digunakan untuk menggabungkan fungsionalitas SingleFragActBase dan DrawerActBase.
 * Terdapat beberapa fungsi yg muncul pada gabungan fungsional ini.
 */
interface SingleFragDrawerActBase : SingleFragActBase, DrawerActBase{
    override val layoutId: Int
        get() = super<DrawerActBase>.layoutId
    override val contentLayoutId: Int
        get() = super<SingleFragActBase>.layoutId

/*
    override fun __initDrawer(rootView: View) {
        try{
            this.startDrawerLayoutId= _sideBase_intent.getExtra(_SIF_Constant.DRAWER_START_LAYOUT_ID, startDrawerLayoutId)!!
            this.endDrawerLayoutId= _sideBase_intent.getExtra(_SIF_Constant.DRAWER_END_LAYOUT_ID, endDrawerLayoutId)!!
        } catch (e: Exception){}
        super.__initDrawer(rootView)
    }
 */

    /**
     * Dapat dipanggil oleh fragment yg dilakukan tidak di awal.
     */
    fun _reinitStartDrawerView(func: (drawer: DrawerLayout, startDrawerView: View) -> Unit){
        func(rootDrawerLayout, startDrawerContainer.getChildAt(0))
    }
    fun _reinitEndDrawerView(func: (drawer: DrawerLayout, endDrawerView: View) -> Unit){
        func(rootDrawerLayout, endDrawerContainer.getChildAt(0))
    }
}