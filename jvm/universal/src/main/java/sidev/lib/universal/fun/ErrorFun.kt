package sidev.lib.universal.`fun`

import sidev.lib.universal.exception.Exc
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf


fun createErrorSimple(msg: String?= null, cause: Throwable?= null, code: Int= 1): Exc?{
    return if(msg == null && cause == null && code == 0) null
    else Exc(msg, cause, code)
}

inline fun <reified T: Throwable> createError(msg: String?= null, cause: Throwable?= null): T?
    = createError(T::class, msg, cause)

/**
 * Fungsi yg digunakan untuk membuat error dg tipe [errorClass] dari [msg] dan [cause] yg diberikan.
 * @return [T] throwable, null jika [msg] dan [cause] sama dg null.
 */
fun <T: Throwable> createError(errorClass: KClass<T>, msg: String?= null, cause: Throwable?= null): T?{
    return if(msg != null || cause != null) {
        new(errorClass) {
            if(it.type.classifier == String::class) msg ?: ""
            else if(cause != null && (it.type.classifier as? KClass<*>)?.isSuperclassOf(cause::class) == true) cause
            else null
        }
    } else null
}
