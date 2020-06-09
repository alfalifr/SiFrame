package sidev.lib.android.siframe.tool.util.`fun`

import sidev.lib.android.siframe.tool.`var`._SIF_Constant
import sidev.lib.android.siframe.tool.manager.StaticManager
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull

/**
 * @return true jika blok func dijalankan.
 */
fun doOnce(key: String, func: () -> Unit): Boolean{
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

fun <T: Any> getStatic(key: String, ownerName: String?= null): T?{
    return StaticManager.getObj(key, ownerName)
}

fun setStatic(key: String, obj: Any){
    StaticManager.attachObj(key, obj)
}

fun removeStatic(key: String){
    StaticManager.removeObj(key)
}

