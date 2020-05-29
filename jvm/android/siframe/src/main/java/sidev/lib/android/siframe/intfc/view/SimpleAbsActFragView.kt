package sidev.lib.android.siframe.intfc.view

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper

interface SimpleAbsActFragView{
    val layoutId: Int
    val styleId: Int
    var layoutView: View
    fun initView(){
        Log.e("SimpleAbsActFragView", "Act/Frag ini (${this::class.java.simpleName}) initView() di panggil")
        if(layoutView != null)
            initView(layoutView)
        else
            Log.e("SimpleAbsActFragView", "Act/Frag ini (${this::class.java.simpleName} belum diinit)")
    }
    fun initView(layoutView: View)
    @CallSuper
    fun initView_int(layoutView: View){}
    fun setStyle(act: Activity){
        act.setTheme(styleId)
    }
    fun <D> getIntentData(key: String, i: Intent?= null, default: D?= null): D{
        return i?.extras?.get(key) as D? ?: default as D
    }
}