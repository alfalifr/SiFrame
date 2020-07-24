package sidev.lib.universal.`fun`

import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.pow

/*
val lessThan: (Int, Int) -> Boolean=
    {i1, i2 -> i1 < i2}

val lessThanEqual: (Int, Int) -> Boolean=
    {i1, i2 -> i1 <= i2}

val moreThan: (Int, Int) -> Boolean=
    {i1, i2 -> i1 > i2}

val moreThanEqual: (Int, Int) -> Boolean=
    {i1, i2 -> i1 >= i2}
 */

fun <T: Comparable<T>> lessThan():(T, T) -> Boolean= {i1, i2 -> i1 < i2}
fun <T: Comparable<T>> lessThanEqual():(T, T) -> Boolean= {i1, i2 -> i1 <= i2}
fun <T: Comparable<T>> moreThan():(T, T) -> Boolean= {i1, i2 -> i1 > i2}
fun <T: Comparable<T>> moreThanEqual():(T, T) -> Boolean= {i1, i2 -> i1 >= i2}


infix fun Int.roundClosest(range: IntRange): Int{
    val diffToFirst= abs(this -range.first)
    val diffToLast= abs(this -range.last)

    return if(diffToFirst < diffToLast) range.first
    else range.last
}
infix fun Number.roundClosest(range: IntRange): Number{
    val diffToFirst= (this -range.first).absoluteValue
    val diffToLast= (this -range.last).absoluteValue

    return if(diffToFirst < diffToLast) range.first
    else range.last
}


/** Fungsi yg mengubah [String] menjadi [Number] apapun. */
fun String.toNumber(): Number{
    return try{ toInt() }
    catch (e: NumberFormatException){
        try{ toFloat() }
        catch (e: NumberFormatException){
            try{ toLong() }
            catch (e: NumberFormatException){ toDouble() }
        }
    }
}

val Number.absoluteValue: Number
    get()= when(this){
        is Int -> absoluteValue
        is Long -> absoluteValue
        is Float -> absoluteValue
        is Double -> absoluteValue
        else -> this
    }

/*
========================
Operator Overriding
========================
 */
/**
 * Bentuk generic dari plus() yg inputnya berupa [Number].
 * @return angka hasil operasi,
 *   dan angka `this.extension` ini sendiri jika tipe pasti [other] tidak diketahui.
 */
operator fun Number.plus(other: Number): Number{
    return when(this){
        is Int -> this + other
        is Long -> this + other
        is Float -> this + other
        is Double -> this + other
        is Byte -> this + other
        is Short -> this + other
        else -> this
    }
}
operator fun Number.minus(other: Number): Number{
    return when(this){
        is Int -> this - other
        is Long -> this - other
        is Float -> this - other
        is Double -> this - other
        is Byte -> this - other
        is Short -> this - other
        else -> this
    }
}
operator fun Number.times(other: Number): Number{
    return when(this){
        is Int -> this * other
        is Long -> this * other
        is Float -> this * other
        is Double -> this * other
        is Byte -> this * other
        is Short -> this * other
        else -> this
    }
}
operator fun Number.div(other: Number): Number{
    return when(this){
        is Int -> this / other
        is Long -> this / other
        is Float -> this / other
        is Double -> this / other
        is Byte -> this / other
        is Short -> this / other
        else -> this
    }
}
operator fun Number.rem(other: Number): Number{
    return when(this){
        is Int -> this % other
        is Long -> this % other
        is Float -> this % other
        is Double -> this % other
        is Byte -> this % other
        is Short -> this % other
        else -> this
    }
}
operator fun Number.compareTo(other: Number): Int{
    return when(this){
        is Int -> this.compareTo(other)
        is Long -> this.compareTo(other)
        is Float -> this.compareTo(other)
        is Double -> this.compareTo(other)
        is Byte -> this.compareTo(other)
        is Short -> this.compareTo(other)
        else -> 0
    }
}
operator fun Number.inc(): Number= this +1
operator fun Number.dec(): Number= this -1
operator fun Number.unaryPlus(): Number= this
operator fun Number.unaryMinus(): Number= this * -1


/** Fungsi ini msh bergantung pada Java. */
infix fun Number.pow(other: Number): Number{
    val initVal= try{ toFloat() }
    catch (e: NumberFormatException){
        try{ toDouble() }
        catch (e: NumberFormatException){ return this }
    }
    val usedOther= try{ other.toFloat() }
    catch (e: NumberFormatException){
        try{ other.toDouble() }
        catch (e: NumberFormatException){ return this }
    }
    return when(initVal){
        is Float -> when(usedOther){
            is Float -> initVal.pow(usedOther)
            is Double -> initVal.toDouble().pow(usedOther)
            else -> this
        }
        is Double -> when(usedOther){
            is Float -> initVal.pow(usedOther.toDouble())
            is Double -> initVal.pow(usedOther)
            else -> this
        }
        else -> this
    }
}



//=========compareTo============
operator fun Int.compareTo(other: Number): Int{
    return when(other){
        is Int -> this.compareTo(other)
        is Long -> this.compareTo(other)
        is Float -> this.compareTo(other)
        is Double -> this.compareTo(other)
        is Byte -> this.compareTo(other)
        is Short -> this.compareTo(other)
        else -> 0
    }
}
operator fun Long.compareTo(other: Number): Int{
    return when(other){
        is Int -> this.compareTo(other)
        is Long -> this.compareTo(other)
        is Float -> this.compareTo(other)
        is Double -> this.compareTo(other)
        is Byte -> this.compareTo(other)
        is Short -> this.compareTo(other)
        else -> 0
    }
}
operator fun Float.compareTo(other: Number): Int{
    return when(other){
        is Int -> this.compareTo(other)
        is Long -> this.compareTo(other)
        is Float -> this.compareTo(other)
        is Double -> this.compareTo(other)
        is Byte -> this.compareTo(other)
        is Short -> this.compareTo(other)
        else -> 0
    }
}
operator fun Double.compareTo(other: Number): Int{
    return when(other){
        is Int -> this.compareTo(other)
        is Long -> this.compareTo(other)
        is Float -> this.compareTo(other)
        is Double -> this.compareTo(other)
        is Byte -> this.compareTo(other)
        is Short -> this.compareTo(other)
        else -> 0
    }
}
operator fun Byte.compareTo(other: Number): Int{
    return when(other){
        is Int -> this.compareTo(other)
        is Long -> this.compareTo(other)
        is Float -> this.compareTo(other)
        is Double -> this.compareTo(other)
        is Byte -> this.compareTo(other)
        is Short -> this.compareTo(other)
        else -> 0
    }
}
operator fun Short.compareTo(other: Number): Int{
    return when(other){
        is Int -> this.compareTo(other)
        is Long -> this.compareTo(other)
        is Float -> this.compareTo(other)
        is Double -> this.compareTo(other)
        is Byte -> this.compareTo(other)
        is Short -> this.compareTo(other)
        else -> 0
    }
}



//=========Plus============
operator fun Int.plus(other: Number): Number{
//    val res= this + other
    return when(other){
        is Int -> this + other
        is Long -> this + other
        is Float -> this + other
        is Double -> this + other
        is Byte -> this + other
        is Short -> this + other
        else -> this
    }
}
operator fun Long.plus(other: Number): Number{
    return when(other){
        is Int -> this + other
        is Long -> this + other
        is Float -> this + other
        is Double -> this + other
        is Byte -> this + other
        is Short -> this + other
        else -> this
    }
}
operator fun Float.plus(other: Number): Number{
    return when(other){
        is Int -> this + other
        is Long -> this + other
        is Float -> this + other
        is Double -> this + other
        is Byte -> this + other
        is Short -> this + other
        else -> this
    }
}
operator fun Double.plus(other: Number): Number{
    return when(other){
        is Int -> this + other
        is Long -> this + other
        is Float -> this + other
        is Double -> this + other
        is Byte -> this + other
        is Short -> this + other
        else -> this
    }
}
operator fun Byte.plus(other: Number): Number{
    return when(other){
        is Int -> this + other
        is Long -> this + other
        is Float -> this + other
        is Double -> this + other
        is Byte -> this + other
        is Short -> this + other
        else -> this
    }
}
operator fun Short.plus(other: Number): Number{
    return when(other){
        is Int -> this + other
        is Long -> this + other
        is Float -> this + other
        is Double -> this + other
        is Byte -> this + other
        is Short -> this + other
        else -> this
    }
}


//===========Minus=============
operator fun Int.minus(other: Number): Number{
    return when(other){
        is Int -> this - other
        is Long -> this - other
        is Float -> this - other
        is Double -> this - other
        is Byte -> this - other
        is Short -> this - other
        else -> this
    }
}
operator fun Long.minus(other: Number): Number{
    return when(other){
        is Int -> this - other
        is Long -> this - other
        is Float -> this - other
        is Double -> this - other
        is Byte -> this - other
        is Short -> this - other
        else -> this
    }
}
operator fun Float.minus(other: Number): Number{
    return when(other){
        is Int -> this - other
        is Long -> this - other
        is Float -> this - other
        is Double -> this - other
        is Byte -> this - other
        is Short -> this - other
        else -> this
    }
}
operator fun Double.minus(other: Number): Number{
    return when(other){
        is Int -> this - other
        is Long -> this - other
        is Float -> this - other
        is Double -> this - other
        is Byte -> this - other
        is Short -> this - other
        else -> this
    }
}
operator fun Byte.minus(other: Number): Number{
    return when(other){
        is Int -> this - other
        is Long -> this - other
        is Float -> this - other
        is Double -> this - other
        is Byte -> this - other
        is Short -> this - other
        else -> this
    }
}
operator fun Short.minus(other: Number): Number{
    return when(other){
        is Int -> this - other
        is Long -> this - other
        is Float -> this - other
        is Double -> this - other
        is Byte -> this - other
        is Short -> this - other
        else -> this
    }
}


//===========Times=============
operator fun Int.times(other: Number): Number{
    return when(other){
        is Int -> this * other
        is Long -> this * other
        is Float -> this * other
        is Double -> this * other
        is Byte -> this * other
        is Short -> this * other
        else -> this
    }
}
operator fun Long.times(other: Number): Number{
    return when(other){
        is Int -> this * other
        is Long -> this * other
        is Float -> this * other
        is Double -> this * other
        is Byte -> this * other
        is Short -> this * other
        else -> this
    }
}
operator fun Float.times(other: Number): Number{
    return when(other){
        is Int -> this * other
        is Long -> this * other
        is Float -> this * other
        is Double -> this * other
        is Byte -> this * other
        is Short -> this * other
        else -> this
    }
}
operator fun Double.times(other: Number): Number{
    return when(other){
        is Int -> this * other
        is Long -> this * other
        is Float -> this * other
        is Double -> this * other
        is Byte -> this * other
        is Short -> this * other
        else -> this
    }
}
operator fun Byte.times(other: Number): Number{
    return when(other){
        is Int -> this * other
        is Long -> this * other
        is Float -> this * other
        is Double -> this * other
        is Byte -> this * other
        is Short -> this * other
        else -> this
    }
}
operator fun Short.times(other: Number): Number{
    return when(other){
        is Int -> this * other
        is Long -> this * other
        is Float -> this * other
        is Double -> this * other
        is Byte -> this * other
        is Short -> this * other
        else -> this
    }
}



//===========Divide=============
operator fun Int.div(other: Number): Number{
//    val res= this + other
    return when(other){
        is Int -> this / other
        is Long -> this / other
        is Float -> this / other
        is Double -> this / other
        is Byte -> this / other
        is Short -> this / other
        else -> this
    }
}
operator fun Long.div(other: Number): Number{
    return when(other){
        is Int -> this / other
        is Long -> this / other
        is Float -> this / other
        is Double -> this / other
        is Byte -> this / other
        is Short -> this / other
        else -> this
    }
}
operator fun Float.div(other: Number): Number{
    return when(other){
        is Int -> this / other
        is Long -> this / other
        is Float -> this / other
        is Double -> this / other
        is Byte -> this / other
        is Short -> this / other
        else -> this
    }
}
operator fun Double.div(other: Number): Number{
    return when(other){
        is Int -> this / other
        is Long -> this / other
        is Float -> this / other
        is Double -> this / other
        is Byte -> this / other
        is Short -> this / other
        else -> this
    }
}
operator fun Byte.div(other: Number): Number{
    return when(other){
        is Int -> this / other
        is Long -> this / other
        is Float -> this / other
        is Double -> this / other
        is Byte -> this / other
        is Short -> this / other
        else -> this
    }
}
operator fun Short.div(other: Number): Number{
    return when(other){
        is Int -> this / other
        is Long -> this / other
        is Float -> this / other
        is Double -> this / other
        is Byte -> this / other
        is Short -> this / other
        else -> this
    }
}



//===========Remain=============
operator fun Int.rem(other: Number): Number{
//    val res= this + other
    return when(other){
        is Int -> this % other
        is Long -> this % other
        is Float -> this % other
        is Double -> this % other
        is Byte -> this % other
        is Short -> this % other
        else -> this
    }
}
operator fun Long.rem(other: Number): Number{
    return when(other){
        is Int -> this % other
        is Long -> this % other
        is Float -> this % other
        is Double -> this % other
        is Byte -> this % other
        is Short -> this % other
        else -> this
    }
}
operator fun Float.rem(other: Number): Number{
    return when(other){
        is Int -> this % other
        is Long -> this % other
        is Float -> this % other
        is Double -> this % other
        is Byte -> this % other
        is Short -> this % other
        else -> this
    }
}
operator fun Double.rem(other: Number): Number{
    return when(other){
        is Int -> this % other
        is Long -> this % other
        is Float -> this % other
        is Double -> this % other
        is Byte -> this % other
        is Short -> this % other
        else -> this
    }
}
operator fun Byte.rem(other: Number): Number{
    return when(other){
        is Int -> this % other
        is Long -> this % other
        is Float -> this % other
        is Double -> this % other
        is Byte -> this % other
        is Short -> this % other
        else -> this
    }
}
operator fun Short.rem(other: Number): Number{
    return when(other){
        is Int -> this % other
        is Long -> this % other
        is Float -> this % other
        is Double -> this % other
        is Byte -> this % other
        is Short -> this % other
        else -> this
    }
}