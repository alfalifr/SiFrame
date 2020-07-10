package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.ComplexLifecycleSideBase
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener

interface BackBtnBase: ComplexLifecycleSideBase {
    //    val actBackBtn: Activity
    override val _prop_act: AppCompatActivity
    var backBtnViewList: ArrayList<View>
    var onBackPressedListenerList: ArrayList<OnBackPressedListener>

    override val layoutId: Int
        get() = _Config.INT_EMPTY

    override fun ___initSideBase() {}

    fun __registerBackBtnView(){
        backBtnViewList= registerBackBtnView()
        for(backBtn in backBtnViewList){
            backBtn.setOnClickListener{
                _prop_act.onBackPressed()
            }
        }
    }

    fun registerBackBtnView(): ArrayList<View>{
        return ArrayList()
    }
    fun registerBackBtnView(v: View){
        if(!backBtnViewList.contains(v)){
            backBtnViewList.add(v)
            v.setOnClickListener {
                _prop_act.onBackPressed()
            }
        }
    }


    fun addOnBackBtnListener(l: OnBackPressedListener){
        onBackPressedListenerList.add(l)
    }
    fun addOnBackBtnListener(func: () -> Boolean){
        addOnBackBtnListener(object: OnBackPressedListener{
            override fun onBackPressed_(): Boolean {
                return func()
            }
        })
    }
    fun removeOnBackBtnListener(l: OnBackPressedListener){
        onBackPressedListenerList.remove(l)
    }
    fun removeOnBackBtnListener(func: () -> Boolean){
        addOnBackBtnListener(object: OnBackPressedListener{
            override fun onBackPressed_(): Boolean {
                return func()
            }
        })
    }
    fun isBackPressedHandled(): Boolean{
        var isHandled= false
        for(l in onBackPressedListenerList)
            isHandled= isHandled || l.onBackPressed_()
        return isHandled
    }
}