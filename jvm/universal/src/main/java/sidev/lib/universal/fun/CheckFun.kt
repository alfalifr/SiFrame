package sidev.lib.universal.`fun`

import java.lang.Exception


fun <T> T?.notNull(f: (T) -> Unit): T? {
    if(this != null)
        f(this)
    return this
}
fun <T> T?.isNull(f: () -> Unit): T? {
    if(this == null)
        f()
    return this
}

fun <T> ifNullElse(any: T?, default: T?): T?{
    return any ?: default
}

fun <T> ifNullDefault(any: T?, default: T): T{
    return any ?: default
}

/*
=============================
Assert Function
=============================
 */
fun assertNotNull(obj: Any?, msg: String= ""): Nothing? {
    if(obj == null)
        throw Exception(msg)
    return null
}