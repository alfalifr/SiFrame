package sidev.lib.universal.`fun`

import android.util.Log
import java.lang.NullPointerException
import kotlin.Exception

/**
 * Cara aman untuk mendapatkan  nilai dari `lateinit var` dari `this.extension` [I].
 * Perlu dicatat bahwa properti `lateinit var` yg diambil adalah properti di dalam `this.extension` [I],
 * sehingga pemanggilan fungsi ini dilakukan bkn setelah akses properti `lateinit var`, namun sebelum itu.
 *
 * @return nilai apapun yg dikembalikan oleh [func],
 *   null jika ternyata terdapat akses `lateinit var` yg belum diinit.
 */
inline fun <I, O> I.getLateinit(func: I.() -> O): O?{
    return try{ func() }
    catch (e: UninitializedPropertyAccessException){ null }
}


inline fun <T> T.iff(func: (T) -> Boolean): Boolean{
    return func(this)
}

/**
 * Mirip dg [notNull] namun terdapat cast untuk input lambda.
 */
inline fun <T1, reified T2> T1?.asNotNull(f: (T2) -> Unit): T1? {
    if(this is T2) f(this)
    return this
}
/**
 * Lawan dari [asNotNull], yaitu fungsi dijalankan saat [T1] bkn merupakan [T2].
 * [f] tidak dijalankan jika [T1] == null.
 */
inline fun <T1, reified T2> T1?.asntNotNull(f: () -> Unit): T1? {
    if(this !is T2 && this != null) f()
    return this
}

/**
 * Memiliki fungsi yg sama dg [asntNotNull], namun [f] dijalankan juga jika [T1] == null.
 */
inline fun <T1, reified T2> T1?.asnt(f: () -> Unit): T1? {
    if(this !is T2) f()
    return this
}

/**
 * Mirip dg [notNull] namun terdapat cast untuk input lambda.
 * Bkn merupakan fungsi chaining.
 */
inline fun <T1, reified T2, O> T1?.asNotNullTo(f: (T2) -> O): O? {
    return if(this is T2) f(this)
    else null
}
/**
 * Lawan dari [asNotNullTo], yaitu fungsi dijalankan saat [T1] bkn merupakan [T2].
 */
inline fun <T1, reified T2, O> T1?.asntNotNullTo(f: () -> O): O? {
    return if(this !is T2 && this != null) f()
    else null
}

inline fun <T> T?.notNull(f: (T) -> Unit): T? {
    if(this != null) f(this)
    return this
}

/**
 * Bkn merupakan fungsi chaining.
 */
inline fun <I, O> I?.notNullTo(f: (I) -> O): O? {
    return if(this != null) f(this)
    else null
}

inline fun <T> T?.isNull(f: () -> Unit): T? {
    if(this == null) f()
    return this
}

/**
 * Bkn merupakan fungsi chaining.
 */
inline fun <I, O> I?.isNullTo(f: () -> O): O? {
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
 * Jika this null, maka akan mengembalikan nilai [def].
 * Memaksa [def] tidak boleh null.
 */
fun <T> T?.orDefault(def: T): T {
    return this ?: def
}

/**
 * Jika this null, maka akan mengembalikan nilai [def].
 * [def] boleh null sehingga nilai return dapat null.
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

/*
=============================
Ignoring Function
=============================
 */
/**
 * Fungsi sederhana untuk menjalankan sebuah blok [trya] dan mengabaikan Exception yang terjadi.
 * Jika tidak ingin mengabaikan Exception, maka programmer dapat mengubah blok [catcha].
 * [ignoreError] true jika semua bentuk exception, baik itu [Exception] maupun [Error].
 *
 * @return true jika blok pada [trya] berhasil dieksekusi dan sebaliknya.
 */
fun trya(
    ignoreError: Boolean= true,
    catcha: (Throwable) -> Unit = { Log.e("TRY_CATCH", "Throwable: ${it::class.java.simpleName} diabaikan!!!", it) },
    trya:() -> Unit
): Boolean{
    return try{ trya()
        true
    } catch (e: Throwable){
        if(ignoreError || e is Exception) catcha(e)
        else throw e
        false
    }
}
/**
 * Fungsi sederhana untuk menjalankan sebuah blok [trya] dan mengabaikan Exception yang terjadi.
 * Jika tidak ingin mengabaikan Exception, maka programmer dapat mengubah blok [catcha].
 * [ignoreError] true jika semua bentuk exception, baik itu [Exception] maupun [Error].
 *
 * Bentuk lebih [trya] dg typed-throwable
 *
 * @return true jika blok pada [trya] berhasil dieksekusi dan sebaliknya.
 */
inline fun <reified T: Throwable> tryCatch(
    ignoreError: Boolean= true,
    catcha: (T) -> Unit = { Log.e("TRY_CATCH", "Throwable: ${it::class.java.simpleName} diabaikan!!!", it) },
    trya:() -> Unit
): Boolean{
    return try{ trya()
        true
    } catch (e: Throwable){
        if((ignoreError || e is Exception) && e is T) catcha(e)
        else throw e
        false
    }
}


/*
=============================
Error Soruce Checking
=============================
 */
/**
 * Digunakan untuk menjalankan fungsi lambda [func] di mana [paramInput] dg tipe [T]
 * tidak dapat diketahui secara pasti menggunakan refleksi sehingga dapat
 * menyebabkan NullPointerException.
 *
 * NullPointerException dapat terjadi pada Kotlin 1.3.72 yg biasanya melibatkan
 * input parameter dg tipe [T!] (ada tanda serunya, artinya Kotlin tidak tau pasti apakah
 * T bisa null atau tidak).
 *
 * Fungsi ini mengecek apakah error berasal dari line di mana [paramInput] pertama kali diinputkan
 * atau error berasal dari dalam implementasi [func]. Jika error berasal dari pertama kali saat
 * [paramInput] diinputkan ke [func], maka error dapat diabaikan atau tidak tergantung [ignoreException].
 * Apabila error berasal dari dalam implementasi [func], maka error selalu di-throw.
 *
 * Tujuan dari fungsi ini hanya sbg pengecek type-safety [paramInput] saat pertama kali diinput
 * ke [func].
 */
inline fun <T, O> runWithParamTypeSafety(
    runningCls: Class<*>,
    func: (T) -> O,
    paramInput: T,
    ignoreException: Boolean= true,
    noinline onExceptionIgnored: ((e: NullPointerException) -> Unit)?= null
) {
    try{ func(paramInput) }
    catch (e: NullPointerException){
//        Log.e("runWithParamTypeSafety", "e.stackTrace[1].className= ${e.stackTrace[1].className} ignoreException= ${!ignoreException}")
        //e.stackTrace[1] merupakan operasi input [paramInput] pertama kali ke [func].
        //e.stackTrace[0] merupakan operasi get value dari boxed variable. Di sini lah, error NullPointerException-nya.
        if(e.stackTrace[1].className.startsWith(runningCls.name).not()
                || !ignoreException)
            throw e
        else
            onExceptionIgnored?.invoke(e)
    }
}