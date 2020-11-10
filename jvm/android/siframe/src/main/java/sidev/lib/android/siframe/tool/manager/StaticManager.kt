package sidev.lib.android.siframe.tool.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import sidev.lib.android.std.tool.util.`fun`.loge

object StaticManager: LifecycleObserver {
    private var lifecycleOwner: ArrayList<LifecycleOwner>?= null

    /**
     * Sebaiknya data yang disimpan di sini berukuran minimal.
     * Semisal object yang disimpan adalah List, sebaiknya diubah menjadi Array<T> agar lebih
     * kecil alokasi memorinya.
     */
    private var staticObjList: HashMap<String, Any>?= null
//    private var attachedStaticObjRefList: HashMap<String, String>?= null

    /**
     * @param ownerName merupakan nama lengkap dari class, bkn simpleName
     */
    fun <T: Any> getObj(key: String, ownerName: String?= null): T? {
        return try{
            staticObjList!![
                if(ownerName == null) key
                else getObjKeyForOwner(key, ownerName)
            ] as T?
        } catch (e: Exception){
            when(e){
                is KotlinNullPointerException, is NullPointerException -> null
                else -> throw e
            }
        }
    }

    private fun getObjKeyForOwner(key: String, ownerName: String): String{
        return "$ownerName.$key"
    }

    private fun registerLifecycleOwner(owner: LifecycleOwner){
        loge("owner: ${owner::class.java.simpleName} is registered!!!")
        if(lifecycleOwner == null)
            lifecycleOwner = ArrayList()
        if(!lifecycleOwner!!.contains(owner)){
            owner.lifecycle.addObserver(this)
            lifecycleOwner!!.add(owner)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun unregisterLifecycleOwner(callingOwner: LifecycleOwner){
        loge("Lifecycle.Event.ON_DESTROY callingOwner::class.java.simpleName = ${callingOwner::class.java.simpleName}")
        lifecycleOwner!!.remove(callingOwner)
        callingOwner.lifecycle.removeObserver(this)
        if(lifecycleOwner!!.isEmpty())
            lifecycleOwner = null
    }

    /**
     * Hanya mereset staticObjList karena obj lifecycleOwner attachedStaticObjList semuanya lifecycle-aware
     */
    fun reset(){
        staticObjList = null
    }

    fun attachObj(key: String, obj: Any){
        if(staticObjList == null)
            staticObjList = HashMap()
        staticObjList!![key]= obj
        loge("object attached obj::class.java.simpleName= ${obj::class.java.simpleName}")
    }
    fun attachObjToLifecycle(key: String, obj: Any, lifecycleOwner: LifecycleOwner){
        if(staticObjList == null)
            staticObjList = HashMap()
        staticObjList!![getObjKeyForOwner(
            key,
            lifecycleOwner.javaClass.name //.className()
        )]= obj
        loge("object attached obj= ${obj::class.java.simpleName} to lifecycle= ${lifecycleOwner::class.java.simpleName}")
        registerLifecycleOwner(
            lifecycleOwner
        )
    }

    fun removeObj(key: String){
        if(staticObjList != null){
            staticObjList!!.remove(key)
            if(staticObjList!!.isEmpty())
                staticObjList = null
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun removeObjAttachedToLifecyle(callingOwner: LifecycleOwner){
        if(staticObjList != null){
            val keys= staticObjList!!.keys
            val ownerName= callingOwner.javaClass.name //.className()
            val removedObjList= ArrayList<Any>()
            for(key in keys){
                if(key.startsWith(ownerName)){
                    val obj= staticObjList!![key] //staticObjList!!.remove(key)
                    if(obj != null)
                        removedObjList.add(obj)
                    loge("obj= ${obj?.javaClass?.simpleName} key= $key")
                }
            }
            for(obj in removedObjList)
                staticObjList!!.remove(obj)
            if(staticObjList!!.isEmpty()){
                staticObjList = null
                loge("staticObjList= null")
            }

        }
    }
}