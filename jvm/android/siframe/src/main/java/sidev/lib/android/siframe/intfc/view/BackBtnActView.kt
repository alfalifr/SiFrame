package sidev.lib.android.siframe.intfc.view

import android.app.Activity
import android.view.View
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener

interface BackBtnActView {
    val actBackBtn: Activity
    var backBtnViewList: ArrayList<View>
    var onBackPressedListenerList: ArrayList<OnBackPressedListener>


    fun registerBackBtnView_int(){
        backBtnViewList= registerBackBtnView()
        for(backBtn in backBtnViewList){
            backBtn.setOnClickListener{
                actBackBtn.onBackPressed()
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
                actBackBtn.onBackPressed()
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