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

fun <T> Sequence<T>.toCommonIterable(): CommonIterable<T>
    = object :
    CommonIterable<T> {
    override fun iterator(): Iterator<T> = this@toCommonIterable.iterator()
}

fun <T> Iterable<T>.toCommonIterable(): CommonIterable<T>
    = object : CommonIterable<T> {
    override fun iterator(): Iterator<T> = this@toCommonIterable.iterator()
}

@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <T> Any.toCommonIterable(): CommonIterable<T>?{
    return when(this){
        is Iterable<*> -> CommonIterableImpl_Iterable(this as Iterable<T>)
        is Sequence<*> -> CommonIterableImpl_Sequence(this as Sequence<T>)
        else -> null
    }
}


/**
 * Merubah [CommonIterable] menjadi [CommonIndexedList].
 * Knp kok gak bisa jadi [CommonList]? Karena [CommonIterable] sudah pasti key-nya Int.
 */
fun <V> CommonIterable<V>.toCommonIndexedList(): CommonIndexedList<V> = when(this){
    is CommonIndexedList<*> -> this as CommonIndexedList<V>
    else -> (this as Iterable<V>).toList().toCommonIndexedList()!!
}
/**
 * Merubah [CommonIterable] menjadi [CommonIndexedMutableList].
 * Knp kok gak bisa jadi [CommonMutableList]? Karena [CommonIterable] sudah pasti key-nya Int.
 */
fun <V> CommonIterable<V>.toCommonIndexedMutableList(): CommonIndexedMutableList<V> = when(this){
    is CommonIndexedMutableList<*> -> this as CommonIndexedMutableList<V>
    else -> (this as Iterable<V>).toMutableList().toCommonIndexedMutableList()!!
}


operator fun <V> CommonIterable<V>.plus(other: CommonIterable<V>): CommonIterable<V>
        = ((this as Iterable<V>) + (other as Iterable<V>)).toCommonIterable()

fun <V> CommonIterable<V>.asSequence(): Sequence<V> = this