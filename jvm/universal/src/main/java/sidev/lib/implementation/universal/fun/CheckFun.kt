package sidev.lib.implementation.universal.`fun`

import java.lang.Exception


fun <T> T?.notNull(f: (T) -> Unit): T? {
    if(this != null)
        f(this)
    return this
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