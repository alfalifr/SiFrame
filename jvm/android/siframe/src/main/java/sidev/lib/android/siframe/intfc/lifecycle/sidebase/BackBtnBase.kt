package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.ComplexLifecycleSideBase
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.isNotNullAndEmpty

interface BackBtnBase: ComplexLifecycleSideBase {
    //    val actBackBtn: Activity
    override val _prop_act: AppCompatActivity
    var backBtnViewList: ArrayList<View>
    var onBackPressedListenerList: ArrayList<OnBackPressedListener>
    /**
     * Wadah menampung [OnBackPressedListener] yg akan dicopot pada saat
     * interface ini memproses listener yg ada ([isHandlingBackBtn] == true)
     * pada fungsi [isBackPressedHandled] agar tidak terjadi [ConcurrentModificationException].
     */
    var removedOnBackPressedListenerList: ArrayList<OnBackPressedListener>
    var isHandlingBackBtn: Boolean

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
    fun addOnBackBtnListener(tag: String= this@BackBtnBase::class.java.simpleName,
                             func: () -> Boolean){
        addOnBackBtnListener(object: OnBackPressedListener{
            override val tag: String?
                get() = tag

            override fun onBackPressed_(): Boolean {
                return func()
            }
        })
    }

    fun removeOnBackBtnListener(l: OnBackPressedListener){
        if(!isHandlingBackBtn)
            onBackPressedListenerList.remove(l)
        else
            removedOnBackPressedListenerList.add(l)
    }
    /**
     * @return true jika listener dg [tag] berhasil dihilangkan dari [onBackPressedListenerList].
     */
    fun removeOnBackBtnListenerByTag(tag: String): Boolean{
        var removedListener: OnBackPressedListener?= null
        for(l in onBackPressedListenerList){
            if(l.tag == tag)
                removedListener= l
        }
        return if(removedListener != null){
            removeOnBackBtnListener(removedListener)
            true
        } else false
    }

    fun isBackPressedHandled(): Boolean{
        isHandlingBackBtn= true
        var isHandled= false

        for(l in onBackPressedListenerList)
            isHandled= isHandled || l.onBackPressed_()

        isHandlingBackBtn= false
        resolveRemovedListener()

        return isHandled
    }

    /**
     * Memproses pencopotan [OnBackPressedListener] yg tertunda
     * karena pemrosesn listener pada fungsi [isBackPressedHandled].
     */
    private fun resolveRemovedListener(){
        if(!isHandlingBackBtn && removedOnBackPressedListenerList.isNotNullAndEmpty()){
            for(l in removedOnBackPressedListenerList)
                onBackPressedListenerList.remove(l)
            removedOnBackPressedListenerList.clear()
        }
    }
}