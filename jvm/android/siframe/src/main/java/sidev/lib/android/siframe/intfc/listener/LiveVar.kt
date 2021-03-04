package sidev.lib.android.siframe.intfc.listener

//TODO <5 Mar 2021> -> Rancang LiveVar yg fleksibel.
/*
package sidev.lib.android.siframe.intfc.listener

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import sidev.lib.`val`.Assignment
import sidev.lib.check.notNull
import sidev.lib.structure.data.value.LiveNullableVar


interface LifecycleLiveVar<T>: LiveNullableVar<T>, LifecycleObserver {
    /**
     * Pada interface ini, [o] akan dipanggil jika [hasActiveDefaultObserver]
     * menghasilkan `true`.
     */
    override fun observe(o: ((T?) -> Unit)?)
    override fun observe(o: ((T?, Assignment) -> Unit)?)

    /**
     * [o] hanya dipanggil jika [owner] aktif.
     */
    fun observe(owner: LifecycleOwner, o: ((T?) -> Unit)?)
    fun observe(owner: LifecycleOwner, o: ((T?, Assignment) -> Unit)?)

    fun hasActiveObserver(): Boolean =
        hasActiveDefaultObserver() || hasActivePairedObserver()
    fun hasActiveDefaultObserver(): Boolean
    fun hasActivePairedObserver(): Boolean
}


internal open class LifecycleLiveVarImpl<T>(
    var defaultLifecycleOwner: LifecycleOwner?= null,
    value: T?
): LifecycleLiveVar<T> {
    companion object {
        val DEFAULT= "default"
    }

    //private var msg= DEFAULT
    override var value: T?= value
        set(v){
            field= v
            invokeDefaultObserver(Assignment.ASSIGN)
            invokePairedObserver(Assignment.ASSIGN)
        }

    private var pairedObserver: MutableMap<LifecycleOwner, (T?, Assignment) -> Unit>?= null

    //private var listener: ((value: T?, msg: String) -> Unit)?= null
    private var defaultObserver: ((T?, Assignment) -> Unit)?= null


    override fun invoke() {
        defaultObserver?.invoke(value, Assignment.RE_ASSIGN)
    }
    override fun invoke(v: T?) {
        value= v
    }

    override fun observe(o: ((T?) -> Unit)?) {
        defaultObserver= if(o != null) { `val`, _ ->
            o(`val`)
        } else null
    }

    override fun observe(o: ((T?, Assignment) -> Unit)?) {
        defaultObserver= o
        defaultObserver?.invoke(value, Assignment.INIT)
    }

    /**
     * [o] hanya dipanggil jika [owner] aktif.
     */
    override fun observe(owner: LifecycleOwner, o: ((T?) -> Unit)?) {
        observe(
            owner,
            if(o != null) { `val`: T?, _: Assignment -> o(`val`) }
            else null as ((T?, Assignment) -> Unit)?
        )
    }

    override fun observe(owner: LifecycleOwner, o: ((T?, Assignment) -> Unit)?) {
        if(o != null){
            if(pairedObserver == null)
                pairedObserver= mutableMapOf()
            pairedObserver!![owner]= o
        } else if(pairedObserver != null) {
            pairedObserver!!.remove(owner)
        }
    }

    override fun hasActiveDefaultObserver(): Boolean = defaultLifecycleOwner != null
    override fun hasActivePairedObserver(): Boolean = pairedObserver != null

    fun invokePairedObserver() = invokePairedObserver(Assignment.RE_ASSIGN)
    private fun invokePairedObserver(assignment: Assignment){
        pairedObserver.notNull {
            //val itr= it.iterator()
            for(obs in it.values){
                obs(value, assignment)
            }
        }
    }

    fun invokeDefaultObserver() = invokeDefaultObserver(Assignment.RE_ASSIGN)
    private fun invokeDefaultObserver(assignment: Assignment){
        if(defaultLifecycleOwner != null)
            defaultObserver?.invoke(value, assignment)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun removeObserver(owner: LifecycleOwner){
        pairedObserver?.remove(owner) ?: run {
            defaultLifecycleOwner= null
        }
    }

    /*
    fun setVal(value: T?, msg: String= DEFAULT){
        this.msg= msg
        this.value= value
    }
    fun observe(l: (T?, msg: String) -> Unit){
        listener= l
        invokeListener()
    }
    fun observe(l: (T?) -> Unit){
        listener= {value, msg -> l(value)}
        invokeListener()
    }

    internal fun invokeListener(){
        ctx.runOnUiThread{
            listener?.invoke(value, msg)
//            loge("setVal() v= $value msg= $msg")
            msg= DEFAULT
        }
    }
 */
}
 */