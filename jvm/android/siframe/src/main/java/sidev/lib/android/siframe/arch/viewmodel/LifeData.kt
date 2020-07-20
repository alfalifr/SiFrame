package sidev.lib.android.siframe.arch.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase
import sidev.lib.android.siframe.intfc.listener.OnFailLifecycleBoundListener
import sidev.lib.universal.structure.prop.TagProp
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.runWithParamTypeSafety

//Nullable karena kemungkinan user pingin ada kondisi saat value == null
open class LifeData<T> : MutableLiveData<T>(), ExpirableBase,
    TagProp<String> {
    final override val isExpired: Boolean
        get() = !hasObservers()

    /**
     * Dalam konteks [LifeData] ini, [tag] berfungsi untuk menandai
     * request khusus yg diberikan oleh [FiewModel].
     */
    final override var tag: String?= null


    private val onFailListeners= HashMap<LifecycleOwner, OnFailLifecycleBoundListener>()
    private val onPreLoadListeners= HashMap<LifecycleOwner, () -> Unit>()
    /**
     * Digunakan untuk chaining pemasukan onFailListener ke list.
     */
    private var lastEnteredLifecycleOwner: LifecycleOwner?= null
    /**
     * Jika true, maka observer yg terdaftar tidak akan di-notify terhadap perubahan nilai [mData].
     */
    protected var isObserverNotified= true
        private set


    fun setValue(value: T?, notifyObserver: Boolean){
        val initVal= isObserverNotified
        isObserverNotified= notifyObserver
        super.setValue(value)
        isObserverNotified= initVal
    }

    fun observe(owner: LifecycleOwner, onPreLoad: (() -> Unit)?= null, onObserve: (T) -> Unit) {
        if(onPreLoad != null)
            onPreLoadListeners[owner]= onPreLoad

        super.observe(owner, Observer {
//            loge("observe() LifeData it == null => ${it == null} isObserverNotified= $isObserverNotified it= $it")
//            loge("observe() LifeData owner !is InterruptableBase => ${owner !is InterruptableBase} !owner.isBusy => ${(owner as? InterruptableBase)?.isBusy?.not()} \n owner.busyOfWhat => ${(owner as? InterruptableBase)?.busyOfWhat} tag= $tag")
            //Jika [owner] sedang sibuk, maka cek sibuk atas hal apa [owner] tersebut.
            //  Jika ternyat sibuk akan hal lain, maka jangan tampilkan layar buffer (onPreLoad).
            if(isObserverNotified){
                if(owner !is InterruptableBase
                    || !owner.isBusy
                    || owner.busyOfWhat != tag){
                    runWithParamTypeSafety(this::class.java, onObserve, it){
                        loge("Data di ${this::class.simpleName} dg tag= \"$tag\" == NULL dan tidak dapat di-pass ke onObserve", it)
                    }
/*
                    //[onObserve] tidak diletakkan di dalam try-catch karena pngecekan hanya
                    //  sebatas apakah tipe [it] sesuai dengan kriteria tipe paramater [onObsrve].
                    //Jika [onObsrve] diletakkan di dalam try-catch, kemungkinan error di dalam
                    //  [onObsrve] akan diabaikan. Hal tersebut tidaklah diinginkan.
                    val checkFun= {it: T -> loge("${this::class.simpleName} checFun it= ${it.toString()}") }
                    val safeToPass=
                        try{ checkFun(it); true }
                        catch (e: IllegalArgumentException){ false }
                        catch (e: NullPointerException){ false }
loge("safeToPass= $safeToPass")
                    //Jika ternyata value yg dipass ke LifeData == null, maka abaikan oberver.
                    if(safeToPass) onObserve(it) //Ternyata Kotlin 1.3.72 msh blum bisa melakukan
                            //cek nullability bagi var dg tipe yg tidak dapat disimpulkan lewat refleksi (bkn reified).
 */
                }
                else
                    onPreLoad?.invoke()
            }
        })
        lastEnteredLifecycleOwner= owner
    }

    /**
     * Agar [isObserverNotified] memiliki efek di fungsi ini.
     */
    @CallSuper
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            if(isObserverNotified)
                observer.onChanged(it)
        })
    }
    /**
     * Agar [isObserverNotified] memiliki efek di fungsi ini.
     */
    @CallSuper
    override fun observeForever(observer: Observer<in T>) {
        super.observeForever{
            if(isObserverNotified)
                observer.onChanged(it)
        }
    }


    /**
     * Untuk memanggil fungsi onPreLoad
     */
    fun onPreLoad(owner: LifecycleOwner){
        if(owner.lifecycle.currentState != Lifecycle.State.DESTROYED)
            onPreLoadListeners[owner]?.invoke()
        else
            onPreLoadListeners.remove(owner)
    }

    fun onFail(func: (resCode: Int, msg: String?, e: Exception?) -> Unit){
        val onFail= object : OnFailLifecycleBoundListener(lastEnteredLifecycleOwner!!){}
        onFail.onFail(func)
        onFailListeners[lastEnteredLifecycleOwner!!]= onFail
    }


    /**
     * Untuk memanggil fungsi onFail
     */
//    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun postOnFail(owner: LifecycleOwner, resCode: Int, msg: String?, e: Exception?){
        if(owner.lifecycle.currentState != Lifecycle.State.DESTROYED)
            onFailListeners[owner]?.onFail(resCode, msg, e)
        else
            onFailListeners.remove(owner)
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