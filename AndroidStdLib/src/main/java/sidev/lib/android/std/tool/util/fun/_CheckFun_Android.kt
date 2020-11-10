package sidev.lib.android.std.tool.util.`fun`

import java.lang.NullPointerException


//TODO <15 Sep 2020> => Msh ada di modul SiFrame untuk android karena
//  blum bisa dipastikan stacktrace-nya.
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
@JvmOverloads
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