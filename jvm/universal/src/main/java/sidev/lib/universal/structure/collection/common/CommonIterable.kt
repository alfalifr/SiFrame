package sidev.lib.universal.structure.collection.common

import sidev.lib.universal.`val`.SuppressLiteral

/**
 * Struktur data yg menunjukan semua jenis tipe yg dapat di-iterasi dalam Kotlin, kecuali [Array].
 * Untuk kasus [Array], interface ini mengakomodasi dg penggunaan [ArrayIterable].
 */
interface CommonIterable<out T>: Iterable<T>, Sequence<T>, ArrayIterable<T> {
    override fun iterator(): Iterator<T>
}

internal class CommonIterableImpl_Iterable<out T>(private val iterable: Iterable<T>): CommonIterable<T>{
    override fun iterator(): Iterator<T> = iterable.iterator()
}
internal class CommonIterableImpl_Sequence<out T>(private val sequence: Sequence<T>): CommonIterable<T>{
    override fun iterator(): Iterator<T> = sequence.iterator()
}

fun <T> commonIterableOf(vararg element: T): CommonIterable<T>
    = object : CommonIterable<T>{
        override fun iterator(): Iterator<T> = element.iterator()
    }

fun <T> Sequence<T>.asCommonIterable(): CommonIterable<T>
    = object :
    CommonIterable<T> {
    override fun iterator(): Iterator<T> = this@asCommonIterable.iterator()
}

fun <T> Iterable<T>.asCommonIterable(): CommonIterable<T>
    = object : CommonIterable<T> {
    override fun iterator(): Iterator<T> = this@asCommonIterable.iterator()
}

@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <T> Any.asCommonIterable(): CommonIterable<T>?{
    return when(this){
        is Iterable<*> -> CommonIterableImpl_Iterable(this as Iterable<T>)
        is Sequence<*> -> CommonIterableImpl_Sequence(this as Sequence<T>)
        else -> null
    }
}