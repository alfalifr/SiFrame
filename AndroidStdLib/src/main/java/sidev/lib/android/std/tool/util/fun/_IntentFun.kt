package sidev.lib.android.std.tool.util.`fun`

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.`val`._Constant
import sidev.lib.annotation.Warning
import sidev.lib.collection.asList
import sidev.lib.exception.IllegalArgExc
import sidev.lib.structure.data.value.Var
import sidev.lib.structure.data.value.varOf
import java.io.Serializable
import java.lang.Exception



//@JvmStatic
fun <T> createIntent(ctx: Context?= null, clazz: Class<out T>?= null, params: Array<out Pair<String, Any?>>): Intent {
    val intent = if(ctx != null) {
        if(clazz == null) throw IllegalArgExc(
            paramExcepted = *arrayOf("clazz"),
            detailMsg = "`ctx` != null, namun `clazz` == null"
        )
        Intent(ctx, clazz)
    } else Intent()
    if (params.isNotEmpty()) intent.fillArgs(params)
    return intent
}

operator fun Intent.set(key: String, value: Any?)= fillArg(key, value)
@Deprecated("Menyebabkan impact yg cukup besar ke performa.")
@Warning("NOT_PERFORMANCE, karena harus meng-copy dulu ke object `Bundle` yg baru")
operator fun <T> Intent.get(
    key: String, default: T?= null,
    isSearchNested: Boolean= false
): T? = getExtra(key, default, isSearchNested)

operator fun Bundle.set(key: String, value: Any?)= fillArg(key, value)
operator fun <T> Bundle.get(
    key: String, default: T?= null,
    isSearchNested: Boolean= false
): T? = getExtra(key, default, isSearchNested)

operator fun Bundle.plus(other: Bundle): Bundle= Bundle(this).apply { putAll(other) }

/**
 * Menambahkan [bundle] langsung ke `this` `Intent` secara linier, bkn nested `Bundle`.
 */
fun Intent.putExtraToLinear(bundle: Bundle, replaceExisting: Boolean = true): Boolean {
    val arr= bundle.toPairedArray()
    return if(arr.isEmpty()) false
    else fillArgs(arr, replaceExisting)
}

/**
 * Menambahkan [bundle] langsung ke `this` `Bundle` secara linier, bkn nested `Bundle`.
 */
fun Bundle.putExtraToLinear(bundle: Bundle, replaceExisting: Boolean = true): Boolean {
    val arr= bundle.toPairedArray()
    return if(arr.isEmpty()) false
    else fillArgs(arr, replaceExisting)
}

/**
 * Menambahkan [bundles] ke `this.extension` `Bundle` secara nested.
 * Mengembalikan nilai key yang baru dalam bentuk list.
 */
fun Intent.putExtraToNested(vararg bundles: Bundle): List<String> {
    val ext= extras
    if(ext?.isEmpty != false)
        return emptyList()
    val newKeys= ext.getNewKeys(bundles.size)
    for((i, key) in newKeys.withIndex()){
        putExtra(key, bundles[i])
    }
    return newKeys
}
/**
 * Menambahkan [bundles] ke `this.extension` `Bundle` secara nested.
 * Mengembalikan nilai key yang baru dalam bentuk list.
 */
fun Bundle.putExtraToNested(vararg bundles: Bundle): List<String> {
    val newKeys= getNewKeys(bundles.size)
    for((i, key) in newKeys.withIndex()){
        putBundle(key, bundles[i])
    }
    return newKeys
}


fun Bundle.toMap(): Map<String, Any?> {
    val keys= keySet()
    val res= HashMap<String, Any?>(keys.size)
    for(k in keys){
        res[k]= this[k]
    }
    return res
}
fun Bundle.toPairedArray(): Array<out Pair<String, Any?>> {
    val keys= keySet()
    val res= arrayOfNulls<Pair<String, Any?>>(keys.size)
    for((i, k) in keys.withIndex()){
        res[i]= k to this[k]
    }
    @Suppress(SuppressLiteral.UNCHECKED_CAST)
    return res as Array<out Pair<String, Any?>>
}

@Deprecated("Menyebabkan impact yg cukup besar ke performa.")
@Warning("NOT_PERFORMANCE, karena harus meng-copy dulu ke object `Bundle` yg baru")
fun <T> Intent.getExtra(
    key: String, default: T? = null,
    isSearchNested: Boolean = false
): T? = extras?.getExtra(key, default, isSearchNested) ?: default

//@Deprecated("Menyebabkan impact yg cukup besar ke performa.")
//@Warning("NOT_PERFORMANCE, karena harus meng-copy dulu ke object `Bundle` yg baru")
fun <T> Bundle.getExtra(
    key: String, default: T? = null,
    isSearchNested: Boolean = false
): T? {
    return if(!isSearchNested){
        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        try { (this[key] as T)!! }
        catch (e: Exception) { default }
    } else {
        getExtraNested(key, default)
    }
}
private fun <T> Bundle.getExtraNested(
    key: String, default: T? = null,
    isFound: Var<Boolean> = varOf(false)
): T? {
    val keys= keySet()
    if(key in keys){
        isFound.value= true
        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        return try { this[key] as T }
        catch (e: Exception){ default }
    }
    for(k in keys){
        val vals= this[k]
        if(vals is Bundle){
            isFound.value= false
            val nestedVal= vals.getExtraNested(key, default, isFound)
            if(isFound.value)
                return nestedVal
        }
    }
    return default
}

fun Bundle.getNewKey(prefix: String = "_"): String {
    val keys= keySet()

    if(prefix !in keys)
        return prefix

    var i= -1
    var newKey: String
    do{
        newKey= _Constant.EXTRA_DATA +"_${++i}"
    } while(newKey in keys)
    return newKey
}

internal fun Bundle.getNewKeys(count: Int, prefix: String = "_"): List<String> {
    val keys= keySet()
    val res= mutableListOf<String>()

    var i= -1
    var limit= count

    if(prefix !in keys){
        res += prefix
        limit--
    }

    var newKey: String
    for(u in 0 until limit){
        do{
            newKey= _Constant.EXTRA_DATA +"$prefix${++i}"
        } while(newKey in keys)
        res += newKey
    }
    return res
}

//@JvmStatic
fun Intent.fillArg(key: String, value: Any?) {
    when (value) {
        null -> putExtra(key, null as Serializable?)
        is Int -> putExtra(key, value)
        is Long -> putExtra(key, value)
        is CharSequence -> putExtra(key, value)
        is String -> putExtra(key, value)
        is Float -> putExtra(key, value)
        is Double -> putExtra(key, value)
        is Char -> putExtra(key, value)
        is Short -> putExtra(key, value)
        is Boolean -> putExtra(key, value)
        is Serializable -> putExtra(key, value)
        is Bundle -> putExtra(key, value)
        is Parcelable -> putExtra(key, value)
        is Array<*> -> when {
            value.isArrayOf<CharSequence>() -> putExtra(key, value)
            value.isArrayOf<String>() -> putExtra(key, value)
            value.isArrayOf<Parcelable>() -> putExtra(key, value)
            else -> throw Exception("Intent extra ${key} has wrong type ${value.javaClass.name}")
        }
        is IntArray -> putExtra(key, value)
        is LongArray -> putExtra(key, value)
        is FloatArray -> putExtra(key, value)
        is DoubleArray -> putExtra(key, value)
        is CharArray -> putExtra(key, value)
        is ShortArray -> putExtra(key, value)
        is BooleanArray -> putExtra(key, value)
        else -> throw Exception("Intent extra $key has wrong type ${value.javaClass.name}")
    }
}

//@JvmStatic
fun Intent.fillArgs(params: Array<out Pair<String, Any?>>, replaceExisting: Boolean = true): Boolean {
    return if(replaceExisting){
        params.forEach { (key, value) ->
            fillArg(key, value)
        }
        true
    } else {
        var res= false
        for((key, value) in params){
            if(hasExtra(key)) continue
            fillArg(key, value)
            res= true
        }
        res
    }
}

//@JvmStatic
fun Bundle.fillArg(key: String, value: Any?) {
    when (value) {
        null -> putSerializable(key, null as Serializable?)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is CharSequence -> putCharSequence(key, value)
        is String -> putString(key, value)
        is Float -> putFloat(key, value)
        is Double -> putDouble(key, value)
        is Char -> putChar(key, value)
        is Short -> putShort(key, value)
        is Boolean -> putBoolean(key, value)
        is Serializable -> putSerializable(key, value)
        is Bundle -> putBundle(key, value)
        is Parcelable -> putParcelable(key, value)
        is Array<*> -> @Suppress(SuppressLiteral.UNCHECKED_CAST) when {
            value.isArrayOf<CharSequence>() -> putCharSequenceArray(key, value as Array<CharSequence>)
            value.isArrayOf<String>() -> putStringArray(key, value as Array<String>)
            value.isArrayOf<Parcelable>() -> putParcelableArray(key, value as Array<Parcelable>)
            else -> throw Exception("Intent extra $key has wrong type ${value.javaClass.name}")
        }
        is IntArray -> putIntArray(key, value)
        is LongArray -> putLongArray(key, value)
        is FloatArray -> putFloatArray(key, value)
        is DoubleArray -> putDoubleArray(key, value)
        is CharArray -> putCharArray(key, value)
        is ShortArray -> putShortArray(key, value)
        is BooleanArray -> putBooleanArray(key, value)
        else -> throw Exception("Intent extra $key has wrong type ${value.javaClass.name}")
    }
}

//@JvmStatic
fun Bundle.fillArgs(params: Array<out Pair<String, Any?>>, replaceExisting: Boolean = true): Boolean {
    return if(replaceExisting){
        params.forEach { (key, value) ->
            fillArg(key, value)
        }
        true
    } else {
        var res= false
        for((key, value) in params){
            if(containsKey(key)) continue
            fillArg(key, value)
            res= true
        }
        res
    }
}