package sidev.lib.android.siframe.intfc.listener

import android.view.View


interface OnClickListListener {
    companion object{
        val DEFAULT_ID= 0
    }
    val onClickListenerList: HashMap<Int, (v: View?) -> Unit>
    var registeredView: ArrayList<View>?

    fun initRegisteredViewList(){
        if(registeredView == null)
            registeredView= ArrayList()
    }
    fun addOnClickListener(v: View, l: (v: View?) -> Unit){
        initRegisteredViewList()
        val vInd= registeredView?.indexOf(v) ?: -1
        if(vInd < 0){
            v.setOnClickListener { v ->
                iterateOnClickListener(v)
            }
            registeredView!!.add(v)
        }
        onClickListenerList[v.id]= l
    }
    fun iterateOnClickListener(v: View?){
        if(v != null){
            for(l in onClickListenerList)
                if(l.key == v.id || l.key == DEFAULT_ID)
                    l.value(v)
        }
    }
}