package sidev.lib.android.siframe.intfc.lifecycle.sidebase

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.base.ComplexLifecycleSideBase
import sidev.lib.android.siframe.intfc.listener.OnBackPressedListener
import sidev.lib.android.std.`val`._Config
import sidev.lib.collection.isNotNullAndEmpty

interface BackBtnBase: ComplexLifecycleSideBase {
    //    val actBackBtn: Activity
    override val _prop_ctx: AppCompatActivity
    var backBtnViewList: ArrayList<View>
    var onBackPressedListenerList: ArrayList<OnBackPressedListener>
    /**
     * Wadah menampung [OnBackPressedListener] yg akan dicopot pada saat
     * interface ini memproses listener yg ada ([isHandlingBackBtn] == true)
     * pada fungsi [isBackPressedHandled] agar tidak terjadi [ConcurrentModificationException].
     */
    var removedOnBackPressedListenerList: ArrayList<OnBackPressedListener>
    var isHandlingBackBtn: Boolean

    /**
     * Jika true, maka [onBackPressedListenerList] akan di-iterasi scr bertahap,
     * artinya, jika ada satu listener yg @return true, maka listener di urutan selanjutnya
     * akan diabaikan dan iterasi selesai.
     */
    val isBackBtnHandledGradually: Boolean
        get()= true

    override val layoutId: Int
        get() = _Config.INT_EMPTY


    override fun ___initSideBase() {}

    fun __registerBackBtnView(){
        backBtnViewList= registerBackBtnView()
        for(backBtn in backBtnViewList){
            backBtn.setOnClickListener{
                _prop_ctx.onBackPressed()
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
                _prop_ctx.onBackPressed()
            }
        }
    }

    fun addOnBackBtnListener(l: OnBackPressedListener,
                             position: Int= onBackPressedListenerList.size){
        onBackPressedListenerList.add(position, l)
    }
    fun addOnBackBtnListener(tag: String= this@BackBtnBase::class.java.simpleName,
                             position: Int= onBackPressedListenerList.size,
                             func: () -> Boolean){
        addOnBackBtnListener(object: OnBackPressedListener{
            override val tag: String?
                get() = tag

            override fun onBackPressed_(): Boolean {
                return func()
            }
        }, position)
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

        for(l in onBackPressedListenerList){
            if(isBackBtnHandledGradually && isHandled) break
            isHandled= isHandled || l.onBackPressed_()
        }

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