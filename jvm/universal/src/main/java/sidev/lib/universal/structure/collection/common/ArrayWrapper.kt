package sidev.lib.universal.structure.collection.common

import kotlin.reflect.KProperty

/** Digunakan sbg interface common yg menunjukan sifat iterable dari [Array]. */
interface ArrayIterable<out T>: Iterable<T>

/**
 * Struktur data pembungkus [Array]. Tujuan dari interface ini adalah untuk memudahkan akses
 * elemen pada tipe data yg scr umum dapat berisi bbrp elemen.
 * Interface ini bersifat immutable agar sesuai dg konsep pada [CommonList].
 */
interface ArrayWrapper<T>: CommonIterable<T> {
/*
    /** Array yg dibungkus dalam interface ini. */
    val array: Array<T>
 */
/*
    /**
     * Creates a new array with the specified [size], where each element is calculated by calling the specified
     * [init] function.
     *
     * The function [init] is called for each array element sequentially starting from the first one.
     * It should return the value for an array element given its index.
     */
    public inline constructor(size: Int, init: (Int) -> T)
 */

    /**
     * Returns the array element at the specified [index]. This method can be called using the
     * index operator.
     * ```
     * value = arr[index]
     * ```
     *
     * If the [index] is out of bounds of this array, throws an [IndexOutOfBoundsException] except in Kotlin/JS
     * where the behavior is unspecified.
     */
    operator fun get(index: Int): T

    /**
     * Returns the number of elements in the array.
     */
    val size: Int

    /**
     * Creates an iterator for iterating over the elements of the array.
     */
    override operator fun iterator(): Iterator<T>
}

interface MutableArrayWrapper<T>:
    ArrayWrapper<T> {
    /**
     * Sets the array element at the specified [index] to the specified [element]. This method can
     * be called using the index operator.
     * ```
     * arr[index] = value
     * ```
     *
     * If the [index] is out of bounds of this array, throws an [IndexOutOfBoundsException] except in Kotlin/JS
     * where the behavior is unspecified.
     *
     * @return element sebelumnya pada [index]
     *   Tujuan dari pengembalian elemen adalah agar kompatibel dg [MutableList].
     */
    operator fun set(index: Int, element: T): T
}

fun <T> Array<T>.asWrapped(): MutableArrayWrapper<T> =
    MutableArrayWrapperImpl(this)
fun IntArray.asWrapped(): MutableArrayWrapper<Int> =
    MutableArrayWrapperImpl_Int(
        this
    )
fun LongArray.asWrapped(): MutableArrayWrapper<Long> =
    MutableArrayWrapperImpl_Long(
        this
    )
fun ShortArray.asWrapped(): MutableArrayWrapper<Short> =
    MutableArrayWrapperImpl_Short(
        this
    )
fun FloatArray.asWrapped(): MutableArrayWrapper<Float> =
    MutableArrayWrapperImpl_Float(
        this
    )
fun DoubleArray.asWrapped(): MutableArrayWrapper<Double> =
    MutableArrayWrapperImpl_Double(
        this
    )
fun ByteArray.asWrapped(): MutableArrayWrapper<Byte> =
    MutableArrayWrapperImpl_Byte(
        this
    )
fun BooleanArray.asWrapped(): MutableArrayWrapper<Boolean> =
    MutableArrayWrapperImpl_Boolean(
        this
    )
fun CharArray.asWrapped(): MutableArrayWrapper<Char> =
    MutableArrayWrapperImpl_Char(
        this
    )


fun <T> arrayWrapperOf(vararg element: T): ArrayWrapper<T>
    = object : ArrayWrapper<T> {
    private val array= element as Array<T>

    override fun get(index: Int): T = array[index]
    override val size: Int get() = array.size
    override fun iterator(): Iterator<T> = array.iterator()
//    override fun getValue(owner: Any?, prop: KProperty<*>): Array<T> = array
}

fun <T> mutableArrayWrapperOf(vararg element: T): MutableArrayWrapper<T>
    = object : MutableArrayWrapper<T> {
    private val array= element as Array<T>

    override fun get(index: Int): T = array[index]
    override fun set(index: Int, element: T): T {
        val prevVal= array[index]
        array[index]= element
        return prevVal
    }
    override val size: Int get() = array.size
    override fun iterator(): Iterator<T> = array.iterator()
//    override fun getValue(owner: Any?, prop: KProperty<*>): Array<T> = array
}


internal open class ArrayWrapperImpl<T>(val array: Array<T>): ArrayWrapper<T> {
    //    constructor(size: Int, init: (Int) -> T): this(Array<T>(size, init))
    override fun get(index: Int): T = array[index]
    override val size: Int get() = array.size
    override fun iterator(): Iterator<T> = array.iterator()
//    override fun getValue(owner: Any?, prop: KProperty<*>): Array<T> = array
}

internal class MutableArrayWrapperImpl<T>(array: Array<T>): ArrayWrapperImpl<T>(array), MutableArrayWrapper<T> {
    override fun set(index: Int, element: T): T {
        val prevVal= array[index]
        array[index]= element
        return prevVal
    }
}


/*
==============
IntArray
==============
 */
internal open class ArrayWrapperImpl_Int(primitiveArray: IntArray): ArrayWrapperImpl<Int>(primitiveArray.toTypedArray())
internal class MutableArrayWrapperImpl_Int(array: IntArray): ArrayWrapperImpl_Int(array), MutableArrayWrapper<Int> {
    override fun set(index: Int, element: Int): Int {
        val prevVal= array[index]
        array[index]= element
        return prevVal
    }
}

/*
==============
LongArray
==============
 */
internal open class ArrayWrapperImpl_Long(primitiveArray: LongArray): ArrayWrapperImpl<Long>(primitiveArray.toTypedArray())
internal class MutableArrayWrapperImpl_Long(array: LongArray): ArrayWrapperImpl_Long(array),
    MutableArrayWrapper<Long> {
    override fun set(index: Int, element: Long): Long {
        val prevVal= array[index]
        array[index]= element
        return prevVal
    }
}

/*
==============
ShortArray
==============
 */
internal open class ArrayWrapperImpl_Short(primitiveArray: ShortArray): ArrayWrapperImpl<Short>(primitiveArray.toTypedArray())
internal class MutableArrayWrapperImpl_Short(array: ShortArray): ArrayWrapperImpl_Short(array),
    MutableArrayWrapper<Short> {
    override fun set(index: Int, element: Short): Short {
        val prevVal= array[index]
        array[index]= element
        return prevVal
    }
}

/*
==============
FloatArray
==============
 */
internal open class ArrayWrapperImpl_Float(primitiveArray: FloatArray): ArrayWrapperImpl<Float>(primitiveArray.toTypedArray())
internal class MutableArrayWrapperImpl_Float(array: FloatArray): ArrayWrapperImpl_Float(array),
    MutableArrayWrapper<Float> {
    override fun set(index: Int, element: Float): Float {
        val prevVal= array[index]
        array[index]= element
        return prevVal
    }
}

/*
==============
DoubleArray
==============
 */
internal open class ArrayWrapperImpl_Double(primitiveArray: DoubleArray): ArrayWrapperImpl<Double>(primitiveArray.toTypedArray())
internal class MutableArrayWrapperImpl_Double(array: DoubleArray): ArrayWrapperImpl_Double(array),
    MutableArrayWrapper<Double> {
    override fun set(index: Int, element: Double): Double {
        val prevVal= array[index]
        array[index]= element
        return prevVal
    }
}

/*
==============
ByteArray
==============
 */
internal open class ArrayWrapperImpl_Byte(primitiveArray: ByteArray): ArrayWrapperImpl<Byte>(primitiveArray.toTypedArray())
internal class MutableArrayWrapperImpl_Byte(array: ByteArray): ArrayWrapperImpl_Byte(array),
    MutableArrayWrapper<Byte> {
    override fun set(index: Int, element: Byte): Byte {
        val prevVal= array[index]
        array[index]= element
        return prevVal
    }
}

/*
==============
BooleanArray
==============
 */
internal open class ArrayWrapperImpl_Boolean(primitiveArray: BooleanArray): ArrayWrapperImpl<Boolean>(primitiveArray.toTypedArray())
internal class MutableArrayWrapperImpl_Boolean(array: BooleanArray): ArrayWrapperImpl_Boolean(array),
    MutableArrayWrapper<Boolean> {
    override fun set(index: Int, element: Boolean): Boolean {
        val prevVal= array[index]
        array[index]= element
        return prevVal
    }
}

/*
==============
CharArray
==============
 */
internal open class ArrayWrapperImpl_Char(primitiveArray: CharArray): ArrayWrapperImpl<Char>(primitiveArray.toTypedArray())
internal class MutableArrayWrapperImpl_Char(array: CharArray): ArrayWrapperImpl_Char(array),
    MutableArrayWrapper<Char> {
    override fun set(index: Int, element: Char): Char {
        val prevVal= array[index]
        array[index]= element
        return prevVal
    }
}