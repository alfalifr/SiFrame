package sidev.lib.android.siframe.lifecycle.viewmodel

import androidx.annotation.RestrictTo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.listener.OnFailLifecycleBoundListener
import sidev.lib.android.siframe.tool.util.`fun`.loge

//Nullable karena kemungkinan user pingin ada kondisi saat value == null
open class LifeData<T> : MutableLiveData<T>(), ExpirableBase{
    private val onFailListeners= HashMap<LifecycleOwner, OnFailLifecycleBoundListener>()
    private val onPreLoadListeners= HashMap<LifecycleOwner, () -> Unit>()
    private var lastEnteredLifecycleOwner: LifecycleOwner?= null //Digunakan untuk memasukan onFailListener ke list

    //    private val lifecycleOwners= ArrayList<LifecycleOwner>()
//    var observeWhenValNull= true
    final override val isExpired: Boolean
        get() = !hasObservers()


    fun observe(owner: LifecycleOwner, onPreLoad: (() -> Unit)?= null, onObserve: (T) -> Unit) {
//        loge("observe() LifeData")
        if(onPreLoad != null)
            onPreLoadListeners[owner]= onPreLoad

//        lifecycleOwners.add(owner)
        super.observe(owner, Observer {
//            loge("observe() LifeData it == null => ${it == null}")
            try{ onObserve(it) }
            catch (e: java.lang.IllegalArgumentException){
//                loge("observe() LifeData it == null => TRUE")
                //Jika ternyata value yg dipass ke LifeData == null, maka abaikan oberver()
            }
        })
        lastEnteredLifecycleOwner= owner
/*
        val onFail= object : OnFailLifecycleBoundListener(owner){}
        onFailListeners[owner]= onFail
        return onFail
 */
    }

    /**
     * Untuk memanggil fungsi onPreLoad
     */
    fun onPreLoad(owner: LifecycleOwner){
        onPreLoadListeners[owner]?.invoke()
    }

    fun onFail(func: (resCode: Int, msg: String?, e: Exception?) -> Unit){
        val onFail= object : OnFailLifecycleBoundListener(lastEnteredLifecycleOwner!!){}
        onFail.onFail(func)
        onFailListeners[lastEnteredLifecycleOwner!!]= onFail
    }


    /**
     * Untuk memanggil fungsi onFail
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun postOnFail(owner: LifecycleOwner, resCode: Int, msg: String?, e: Exception?){
        onFailListeners[owner]?.onFail(resCode, msg, e)
    }
/*
    /**
     * Untuk mencari LifecycleOwner yg masih aktif.
     * Fungsi ini harus dipakai saat keadaan benar-benar tidak memungkinkan untuk pass LifecycleOwner.
     */
    private fun lookForActiveOwner(): LifecycleOwner?{
        for(owner in lifecycleOwners)
            if(owner.lifecycle.currentState != Lifecycle.State.DESTROYED)
                return owner
        return null
    }
 */
}