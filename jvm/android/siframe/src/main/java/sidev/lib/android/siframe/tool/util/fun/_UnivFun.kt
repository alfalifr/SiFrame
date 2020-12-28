package sidev.lib.android.siframe.tool.util.`fun`

import androidx.lifecycle.LifecycleOwner
import sidev.lib.android.siframe.`val`._SIF_Constant
import sidev.lib.android.siframe.tool.manager.StaticManager
import sidev.lib.check.isNull
import sidev.lib.check.notNull

/**
 * @return true jika blok func dijalankan.
 */
fun doOnce(key: String, func: () -> Unit): Boolean {
    var hasDone= false
    getStatic<HashMap<String, Boolean>>(_SIF_Constant.STATIC_BOOLEAN)
        .notNull { boolMap ->
            boolMap[key].notNull { bool ->
                if(!bool){
                    func()
                    boolMap[key]= true
                    hasDone= true
                }
            }.isNull {
                func()
                boolMap[key]= true
                hasDone= true
            }
        }.isNull {
            val boolMap= HashMap<String, Boolean>()
            setStatic(_SIF_Constant.STATIC_BOOLEAN, boolMap)

            func()
            boolMap[key]= true
            hasDone= true
        }
    return hasDone
}

/**
 * Lawan dari doOnce()
 * @return true jika berhasil di undo.
 *      Sebaliknya, false jika memang sebelumnya blum dilakukan doOnce()
 */
fun undo(key: String): Boolean{
    var hasUndone= false
    getStatic<HashMap<String, Boolean>>(_SIF_Constant.STATIC_BOOLEAN)
        .notNull { boolMap ->
            boolMap[key].notNull { bool ->
                if(bool){
                    boolMap[key]= false
                    hasUndone= true
                }
            }
        }
    return hasUndone
}

@JvmOverloads
fun <T: Any> getStatic(key: String, owner: LifecycleOwner?= null, tag: String = ""): T? = StaticManager.getObj(key, owner, tag)

@JvmOverloads
operator fun <T: Any> StaticManager.get(key: String, owner: LifecycleOwner?, tag: String = ""): T? = getObj(key, owner, tag)
@JvmOverloads
operator fun <T: Any> StaticManager.get(key: String, ownerName: String?= null, tag: String = ""): T? = getObj(key, ownerName, tag)

@JvmOverloads
fun setStatic(key: String, obj: Any, owner: LifecycleOwner?= null, tag: String= ""): Any? {
    return if(owner == null) StaticManager.attachObj(key, obj)
    else StaticManager.attachObjToLifecycle(key, obj, owner, tag)
}
//operator fun StaticManager.set(key: String, obj: Any) = setStatic(key, obj)
//operator fun StaticManager.set(key: String, owner: LifecycleOwner, obj: Any) = setStatic(key, obj, owner)
@JvmOverloads
operator fun StaticManager.set(key: String, owner: LifecycleOwner?= null, tag: String = "", obj: Any): Any? = setStatic(key, obj, owner, tag)

fun removeStatic(key: String): Any? = StaticManager.removeObj(key)


