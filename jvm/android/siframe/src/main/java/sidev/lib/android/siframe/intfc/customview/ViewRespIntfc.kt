package sidev.lib.android.siframe.intfc.customview

import android.view.View
//import sidev.lib.android.siframe.customizable.view.intfc.CustomView

interface ViewRespIntfc: CustomView {
    val onClickListenerList: List<View.OnClickListener>
    fun addOnClickListener(l: View.OnClickListener){
        (onClickListenerList as ArrayList<View.OnClickListener>)
            .add(l)
    }
    fun iterateOnClickListener(v: View?){
        for(l in onClickListenerList)
            l.onClick(v)
    }
}