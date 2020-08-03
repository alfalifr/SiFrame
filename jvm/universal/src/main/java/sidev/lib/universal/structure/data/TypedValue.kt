package sidev.lib.universal.structure.data

import kotlin.reflect.KType

/**
 * Digunakan untuk menyimpan value beserta type-nya.
 */
data class TypedValue<T>(val type: KType, val value: T)

fun <T> T.withType(type: KType): TypedValue<T> = TypedValue(type, this)
//fun TypedValue<*>.component1() = type