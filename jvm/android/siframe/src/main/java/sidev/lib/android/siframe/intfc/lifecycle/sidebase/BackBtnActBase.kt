package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.ComplexLifecycleSideBase
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener

interface BackBtnActBase: ComplexLifecycleSideBase {
//    val actBackBtn: Activity
    var backBtnViewList: ArrayList<View>
    var onBackPressedListenerList: ArrayList<OnBackPressedListener>

    override val layoutId: Int
        get() = _Config.INT_EMPTY

    override fun ___initSideBase() {}

    fun __registerBackBtnView(){
        backBtnViewList= registerBackBtnView()
        for(backBtn in backBtnViewList){
            backBtn.setOnClickListener{
                _sideBase_act.onBackPressed()
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
                _sideBase_act.onBackPressed()
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
    fun isBackPressedHandled(): Boolean{
        var isHandled= false
        for(l in onBackPressedListenerList)
            isHandled= isHandled || l.onBackPressed_()
        return isHandled
    }
}