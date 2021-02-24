package sidev.lib.android.siframe.tool.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.siframe.tool.util.`fun`.name
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.notNull
import sidev.lib.check.notNullTo
import sidev.lib.collection.removeLastIf
import sidev.lib.exception.IllegalStateExc
import sidev.lib.structure.data.value.TaggedVal
import sidev.lib.structure.data.value.component1
import sidev.lib.structure.data.value.component2
import sidev.lib.structure.data.value.withTag
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object StaticManager: LifecycleObserver {
    private var taggedLifecycleOwners: ArrayList<TaggedVal<String, LifecycleOwner>>?= null

//    private var lifecycleOwner: ArrayList<LifecycleOwner>?= null

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
    @JvmOverloads
    fun <T> getObj(key: String, ownerName: String?= null, tag: String = ""): T? {
        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        return staticObjList.notNullTo {
            it[
                if(ownerName == null) key
                else getObjKeyForOwner(ownerName, tag, key)
            ] as T?
        }
//            .also { loge("StaticManager.getObj() key= $key ownerName= $ownerName tag= $tag it= $it") }
/*
        return try{
        } catch (e: Exception){
            when(e){
                is KotlinNullPointerException, is NullPointerException -> null
                else -> throw e
            }
        }
 */
    }
    @JvmOverloads
    fun <T> getObj(key: String, owner: LifecycleOwner?, tag: String = ""): T? = getObj(key, owner?.name, tag)

    private fun getObjKeyForOwner(owner: LifecycleOwner, tag: String = "", key: String?= null): String = getObjKeyForOwner(owner.name, tag, key)
    private fun getObjKeyForOwner(ownerName: String, tag: String = "", key: String?= null): String = "$ownerName${if(tag.isBlank()) "" else "@$tag"}.${key ?: ""}"

    private fun registerLifecycleOwner(owner: LifecycleOwner, tag: String= ""): TaggedVal<String, LifecycleOwner>{
        loge("owner: ${owner::class.java.simpleName} is registered!!!")
        if(taggedLifecycleOwners == null)
            taggedLifecycleOwners = ArrayList()
        val existingOne= taggedLifecycleOwners!!.find { (tag2, lc) ->
            tag2 == tag && lc.name == owner.name
        }
        return if(existingOne == null){
            owner.lifecycle.addObserver(this)
            (owner withTag tag).also {
                taggedLifecycleOwners!! += it
            }
        } else existingOne
    }

    //private var removedOwner: TaggedVal<String, LifecycleOwner>?= null
    //@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun unregisterLifecycleOwner(callingOwner: LifecycleOwner){
        loge("Lifecycle.Event.ON_DESTROY callingOwner::class.java.simpleName = ${callingOwner::class.java.simpleName}")
        var removedLc: TaggedVal<String, LifecycleOwner>?= null
        if(!taggedLifecycleOwners!!.removeLastIf {
            removedLc= it
            it.value.name == callingOwner.name
        })
            removedLc= null
        //removedOwner= removedLc
        callingOwner.lifecycle.removeObserver(this)
        if(taggedLifecycleOwners!!.isEmpty())
            taggedLifecycleOwners = null
    }

    /**
     * Hanya mereset staticObjList karena obj lifecycleOwner attachedStaticObjList semuanya lifecycle-aware
     * Return `true` jika [staticObjList] != `null` dan `isNotEmpty`.
     */
    fun reset(): Boolean {
        val bool= staticObjList?.isNotEmpty() == true
        staticObjList?.clear()
        staticObjList = null
        return bool
    }

    fun attachObj(key: String, obj: Any): Any? {
        if(staticObjList == null)
            staticObjList = HashMap()
        val before= staticObjList!!.put(key, obj)
        loge("object attached obj::class.java.simpleName= ${obj::class.java.simpleName}")
        return before
    }
    @JvmOverloads
    fun attachObjToLifecycle(
        key: String, obj: Any, lifecycleOwner: LifecycleOwner, tag: String = ""
    ): Any? {
        if(staticObjList == null)
            staticObjList = HashMap()
        val (tag2, owner)= registerLifecycleOwner(lifecycleOwner, tag)
        val mapKey= getObjKeyForOwner(owner, tag2!!, key)
        val before= staticObjList!!.put(mapKey, obj)
        loge("object attached obj= ${obj::class.java.simpleName} to lifecycle= ${lifecycleOwner::class.java.simpleName} key= $mapKey")
        return before
    }

    fun removeObj(key: String): Any? {
        var before: Any? = null
        if(staticObjList != null){
            before= staticObjList!!.remove(key)
            if(staticObjList!!.isEmpty())
                staticObjList = null
        }
        loge("object removed key= $key removed= $before")
        return before
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun removeObjAttachedToLifecyle(callingOwner: LifecycleOwner): Boolean {
        val taggedRemovedOwner= //removedOwner ?:
            taggedLifecycleOwners?.findLast { it.value.name == callingOwner.name }
            ?: run {
                loge("objects detached from lifecycle= ${callingOwner::class.java.simpleName} ==FALSE==")
                return false
            }
        /*
        if(removedOwner == null)
            throw IllegalStateExc(
                stateOwner = this::class,
                currentState = "this.removedOwner == null",
                expectedState = "this.removedOwner != null",
                detMsg = "Terjadi kesalahan alur eksekusi"
            )
         */
        if(staticObjList != null){
            val keys= staticObjList!!.keys
            val (removedTag, removedOwner)= taggedRemovedOwner //!!
/*
            if(removedOwner.name != callingOwner.name) // Harusnya sama, jadi gak perlu dicek lagi.
                throw IllegalStateExc(
                    stateOwner = this::class,
                    currentState = "removedOwner.name != callingOwner.name",
                    expectedState = "removedOwner.name == callingOwner.name",
                    detMsg = "Terjadi kesalahan alur eksekusi"
                )
 */
            val ownerKey= getObjKeyForOwner(removedOwner, removedTag!!) //callingOwner.name //.className()
            val removedObjKeyList= ArrayList<String>()
            for(key in keys){
                if(key.startsWith(ownerKey)){
                    val obj= staticObjList!![key] //staticObjList!!.remove(key)
                    if(obj != null)
                        removedObjKeyList += key
                    loge("obj= ${obj?.javaClass?.simpleName} key= $key")
                }
            }
            for(key in removedObjKeyList){
                val before= staticObjList!!.remove(key)
                loge("object removed Lifecycle.Event.ON_DESTROY key= $key removed= $before")
            }
            if(staticObjList!!.isEmpty()){
                staticObjList = null
                loge("staticObjList= null")
            }
            unregisterLifecycleOwner(removedOwner)
        }
        //removedOwner= null
        return true
    }
}