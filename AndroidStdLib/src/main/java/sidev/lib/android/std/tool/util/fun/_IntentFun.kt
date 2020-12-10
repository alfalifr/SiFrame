package sidev.lib.android.std.tool.util.`fun`

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import sidev.lib.android.std._external._AnkoInternals
import java.io.Serializable
import java.lang.Exception



//@JvmStatic
fun <T> createIntent(ctx: Context?= null, clazz: Class<out T>?= null, params: Array<out Pair<String, Any?>>): Intent {
    val intent =
        if(ctx != null) Intent(ctx, clazz!!)
        else Intent()
    if (params.isNotEmpty()) _AnkoInternals.fillIntentArguments(
        intent,
        params
    )
    return intent
}

operator fun Intent.set(key: String, value: Any)= fillIntentArg(key, value)

//@JvmStatic
fun Intent.fillIntentArg(key: String, value: Any?) {
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
fun Intent.fillIntentArguments(params: Array<out Pair<String, Any?>>) {
    params.forEach { (key, value) ->
        fillIntentArg(key, value)
    }
}