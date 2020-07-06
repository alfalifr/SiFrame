package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import sidev.lib.android.siframe.exception.RuntimeExc

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
     * Fungsi ini akan error jika [SingleFragActBase] ini tidak memiliki
     * view di dalam [startDrawerContainer].
     */
    fun _reinitStartDrawerView(func: (drawer: DrawerLayout, startDrawerView: View) -> Unit){
        try{
            val startDrawer= startDrawerContainer.getChildAt(0)!!
            func(rootDrawerLayout, startDrawer)
        } catch (e: KotlinNullPointerException){
            //Jika ternyata Activity induk tidak punya startDrawer, maka throw RuntimeExc.
            throw RuntimeExc(commonMsg = "_reinitStartDrawerView() oleh Fragment: \"${fragment::class.java.simpleName}\"",
                detailMsg = "Activity: \"${this::class.java.simpleName}\" tidak punya startDrawer")
        }
    }
    fun _reinitEndDrawerView(func: (drawer: DrawerLayout, endDrawerView: View) -> Unit){
        try{
            val endDrawer= endDrawerContainer.getChildAt(0)!!
            func(rootDrawerLayout, endDrawer)
        } catch (e: KotlinNullPointerException){
            //Jika ternyata Activity induk tidak punya startDrawer, maka throw RuntimeExc.
            throw RuntimeExc(commonMsg = "_reinitStartDrawerView() oleh Fragment: \"${fragment::class.java.simpleName}\"",
                detailMsg = "Activity: \"${this::class.java.simpleName}\" tidak punya endDrawer")
        }
    }
}