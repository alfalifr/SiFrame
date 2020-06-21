package sidev.lib.universal.`fun`

import java.lang.Exception

fun <T> T.iff(func: (T) -> Boolean): Boolean{
    return func(this)
}

/**
 * Mirip dg notNull() namun terdapat cast untuk input lambda.
 */
inline fun <T1, reified T2> T1?.asNotNull(f: (T2) -> Unit): T1? {
    if(this is T2)
        f(this)
    return this
}

/**
 * Mirip dg notNull() namun terdapat cast untuk input lambda.
 * Bkn merupakan fungsi chaining.
 */
inline fun <T1, reified T2, O> T1?.asNotNullTo(f: (T2) -> O): O? {
    return if(this is T2) f(this)
    else null
}

fun <T> T?.notNull(f: (T) -> Unit): T? {
    if(this != null)
        f(this)
    return this
}

/**
 * Bkn merupakan fungsi chaining.
 */
fun <I, O> I?.notNullTo(f: (I) -> O): O? {
    return if(this != null) f(this)
    else null
}

fun <T> T?.isNull(f: () -> Unit): T? {
    if(this == null)
        f()
    return this
}

/**
 * Bkn merupakan fungsi chaining.
 */
fun <I, O> I?.isNullTo(f: () -> O): O? {
    return if(this == null) f()
    else null
}

fun <T> ifNullElse(any: T?, default: T?): T?{
    return any ?: default
}

fun <T> ifNullDefault(any: T?, default: T): T{
    return any ?: default
}

/**
 * Jika this null, maka akan mengembalikan nilai def.
 * Memaksa def tidak boleh null.
 */
fun <T> T?.orDefault(def: T): T {
    return this ?: def
}

/**
 * Jika this null, maka akan mengembalikan nilai def.
 * def boleh null sehingga nilai return dapat null.
 */
fun <T> T?.orElse(def: T?): T? {
    return this ?: def
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