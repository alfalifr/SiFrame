package sidev.lib.universal.`fun`

import sidev.lib.universal.`val`.RoundMethod
import sidev.lib.universal.`val`.SuppressLiteral
import sidev.lib.universal.exception.UndefinedDeclarationExc
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

fun <T: Comparable<T>> lessThan_():(T, T) -> Boolean= { i1, i2 -> i1 < i2 }
fun <T: Comparable<T>> lessThanEqual_():(T, T) -> Boolean= { i1, i2 -> i1 <= i2 }
fun <T: Comparable<T>> moreThan_():(T, T) -> Boolean= { i1, i2 -> i1 > i2 }
fun <T: Comparable<T>> moreThanEqual_():(T, T) -> Boolean= { i1, i2 -> i1 >= i2 }

fun <T: Comparable<T>> lessThan(i1: T, i2: T): Boolean = i1 < i2
fun <T: Comparable<T>> lessThanEqual(i1: T, i2: T): Boolean = i1 <= i2
fun <T: Comparable<T>> moreThan(i1: T, i2: T): Boolean = i1 > i2
fun <T: Comparable<T>> moreThanEqual(i1: T, i2: T): Boolean = i1 >= i2

fun <T: Comparable<T>> asc(i1: T, i2: T): Boolean = lessThan(i1,  i2)
fun <T: Comparable<T>> desc(i1: T, i2: T): Boolean = moreThan(i1,  i2)

/*
========== Universal Comparison ==============
Digunakan untuk komparasi antar tipe data. Tidak direkomendasikan digunakan di luar library ini.
==============================================
*/
fun <T: Comparable<T>> univLessThan(i1: T, i2: T): Boolean
        = try{ i1 < i2 } catch (e: ClassCastException){ (i1 as Comparable<*>) < (i2 as Comparable<*>) }
fun <T: Comparable<T>> univLessThanEqual(i1: T, i2: T): Boolean
        = try{ i1 <= i2 } catch (e: ClassCastException){ (i1 as Comparable<*>) <= (i2 as Comparable<*>) }
fun <T: Comparable<T>> univMoreThan(i1: T, i2: T): Boolean
        = try{ i1 > i2 } catch (e: ClassCastException){ (i1 as Comparable<*>) > (i2 as Comparable<*>) }
fun <T: Comparable<T>> univMoreThanEqual(i1: T, i2: T): Boolean
        = try{ i1 >= i2 } catch (e: ClassCastException){ (i1 as Comparable<*>) >= (i2 as Comparable<*>) }

fun <T: Comparable<T>> univAsc(i1: T, i2: T): Boolean = univLessThan(i1,  i2)
fun <T: Comparable<T>> univDesc(i1: T, i2: T): Boolean = univMoreThan(i1,  i2)


infix fun Int.roundClosest(range: IntRange): Int{
    val diffToFirst= (this -range.first).asNumber().absoluteValue //Dijadikan number agar Int.MIN_VALUE dan Long.MIN_VLAUE dapat diubah jadi absolute value.
    val diffToLast= (this -range.last).asNumber().absoluteValue

    return if(diffToFirst < diffToLast) range.first
    else range.last
}
infix fun Number.roundClosest(range: IntRange): Int{
    val diffToFirst= (this -range.first).absoluteValue
    val diffToLast= (this -range.last).absoluteValue

    return if(diffToFirst < diffToLast) range.first
    else range.last
}

fun Number.isZero(): Boolean = this.compareTo(0) == 0
fun Number.isNegative(): Boolean = this < 0
fun Number.isPositive(): Boolean = this > 0

fun Number.isNotZero(): Boolean = !isZero()
fun Number.isNotNegative(): Boolean = !isNegative()
fun Number.isNotPositive(): Boolean = !isPositive()

/** @return true jika `this.extension` merupakan angka dg tipe data yg memiliki angka di belakang koma. */
fun Number.isDecimalType(): Boolean = this is Double || this is Float

/**
 * Mengambil angka pada digit [digitPlace]. Fungsi ini tidak mengambil angka di belakang koma.
 * [digitPlace] dihitung dari belakang, bkn dari depan. [digitPlace] dimulai dari 0.
 * Jika [digitPlace] negatif, brarti angka yg diambil berada di belakang koma.
 */
fun Number.getNumberAtDigit(digitPlace: Int): Int{
//    if(digitPlace.isNegative()) throw ParameterExc(paramName = "digitPlace", detMsg = "Tidak boleh negatif.")
    if(digitPlace.isNegative()){
        if(!this.isDecimalType()) return 0 //Jika ternyata angka yg diambil adalah di belakang koma,
                // sedangkan tipe data angka kelas ini tidak memiliki koma, maka return 0.
        val newThis= this * (10 pow -digitPlace).toInt()
        return newThis.getNumberAtDigit(0)
    }
    val digitPlaceDividerFactor= (digitPlace).notNegativeOr(0)
    val digitPlaceModderFactor= (digitPlace+1).notNegativeOr(0)

    val digitPlaceDivider= (10 pow digitPlaceDividerFactor).toInt()
    val digitPlaceModder= (10 pow digitPlaceModderFactor).toInt()

    return ((this % digitPlaceModder) / digitPlaceDivider).toInt() //as T
}

/**
 * Membulatkan angka pada [digitPlace] -1 dg menjadikan angka pada [digitPlace]-1 jadi 0
 * dan angka pada [digitPlace] ditambah 1 jika [digitPlace]-1 >= 5.
 */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <T: Number> T.round(digitPlace: Int= 0, method: RoundMethod= RoundMethod.ROUND): T{
    if(digitPlace.isNegative()){
        if(!this.isDecimalType()) return this//Jika ternyata angka yg diambil adalah di belakang koma,
                // sedangkan tipe data angka kelas ini tidak memiliki koma, maka return angka ini.
        val digitTimer= (10 pow -digitPlace).toInt().toDouble() //Agar hasil koma bisa kelihatan dg pas.
        val newThis= this * digitTimer
        return (newThis.round(0, method) / digitTimer) as T
    }
    val numberInDigit= getNumberAtDigit(digitPlace-1)

    val digitPlaceDividerFactor= (digitPlace).notNegativeOr(0)
    val digitPlaceDivider= (10 pow digitPlaceDividerFactor).toInt()

    val increment= when(method){
        RoundMethod.ROUND -> if(numberInDigit < 5) 0 else 1
        RoundMethod.CEIL -> 1
        RoundMethod.FLOOR -> 0
    }
    return (((this / digitPlaceDivider).toInt() + increment) * digitPlaceDivider) as T
}

/**
 * Mengambil angka desimal saja. Kemungkinan @return 0 jika `this.extension` adalah angka bulat.
 * Fungsi ini tidak menjamin angka desimal yg diambil bulat dan sesuai input.
 */
fun Number.getDecimal(): Number = this -(this.toInt())



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

fun Number.asNumber(): Number = this
/**
 * Mengambil nilai absolut dari `this.extension` [Number] apapun formatnya.
 * Special Case:
 *   - `Int.MIN_VALUE` dan `Long.MIN_VALUE` akan menghasilkan MIN_VALUE +1 agar bisa jadi positif.
 */
val <T: Number> T.absoluteValue: T
    get(){
        val res= when(this){
            is Int -> absoluteValue
            is Long -> absoluteValue
            is Float -> absoluteValue
            is Double -> absoluteValue
            else -> if(!isNegative()) this else -this
        }
        return if(res.isNotNegative()) res as T
        else (res +1).absoluteValue as T
    }

/*
========================
Operator Overriding
========================
 */
//operator fun <T: Number> T.plus(other: T): T = this + other
/**
 * Bentuk generic dari plus() yg inputnya berupa [Number].
 * @return angka hasil operasi,
 *   dan angka `this.extension` ini sendiri jika tipe pasti [other] tidak diketahui.
 */
infix operator fun Number.plus(other: Number): Number{
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
infix operator fun Number.minus(other: Number): Number{
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
infix operator fun Number.times(other: Number): Number{
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
infix operator fun Number.div(other: Number): Number{
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
infix operator fun Number.rem(other: Number): Number{
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
infix operator fun Number.compareTo(other: Number): Int{
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



//=========compareTo interType============
infix operator fun Number.compareTo(other: String): Int = this.toString().compareTo(other)
infix operator fun String.compareTo(other: Number): Int = this.compareTo(other.toString())
infix operator fun Number.compareTo(other: Char): Int = this.toChar().compareTo(other)
infix operator fun Char.compareTo(other: Number): Int = this.compareTo(other.toChar())
infix operator fun String.compareTo(other: Char): Int = this.compareTo(other.toString())
infix operator fun Char.compareTo(other: String): Int = this.toString().compareTo(other)

infix operator fun <T1, T2> Comparable<T1>.compareTo(other: T2): Int
        = when(this){
            is Number -> when(other){
                is Number -> compareTo(other)
                is String -> compareTo(other)
                is Char -> compareTo(other)
                else -> throw UndefinedDeclarationExc(undefinedDeclaration = "${this::class}.compareTo(${other?.clazz})")
            }
            is String -> when(other){
                is Number -> compareTo(other)
                is String -> compareTo(other)
                is Char -> compareTo(other)
                else -> throw UndefinedDeclarationExc(undefinedDeclaration = "${this::class}.compareTo(${other?.clazz})")
            }
            is Char -> when(other){
                is Number -> compareTo(other)
                is String -> compareTo(other)
                is Char -> compareTo(other)
                else -> throw UndefinedDeclarationExc(undefinedDeclaration = "${this::class}.compareTo(${other?.clazz})")
            }
            else -> throw UndefinedDeclarationExc(undefinedDeclaration = "${this::class}.compareTo(${other?.clazz})")
        }


//=========compareTo============
infix operator fun Int.compareTo(other: Number): Int{
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
infix operator fun Long.compareTo(other: Number): Int{
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
infix operator fun Float.compareTo(other: Number): Int{
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
infix operator fun Double.compareTo(other: Number): Int{
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
infix operator fun Byte.compareTo(other: Number): Int{
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
infix operator fun Short.compareTo(other: Number): Int{
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
infix operator fun Int.plus(other: Number): Number{
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
infix operator fun Long.plus(other: Number): Number{
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
infix operator fun Float.plus(other: Number): Number{
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
infix operator fun Double.plus(other: Number): Number{
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
infix operator fun Byte.plus(other: Number): Number{
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
infix operator fun Short.plus(other: Number): Number{
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
infix operator fun Int.minus(other: Number): Number{
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
infix operator fun Long.minus(other: Number): Number{
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
infix operator fun Float.minus(other: Number): Number{
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
infix operator fun Double.minus(other: Number): Number{
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
infix operator fun Byte.minus(other: Number): Number{
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
infix operator fun Short.minus(other: Number): Number{
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
infix operator fun Int.times(other: Number): Number{
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
infix operator fun Long.times(other: Number): Number{
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
infix operator fun Float.times(other: Number): Number{
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
infix operator fun Double.times(other: Number): Number{
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
infix operator fun Byte.times(other: Number): Number{
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
infix operator fun Short.times(other: Number): Number{
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
infix operator fun Int.div(other: Number): Number{
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
infix operator fun Long.div(other: Number): Number{
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
infix operator fun Float.div(other: Number): Number{
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
infix operator fun Double.div(other: Number): Number{
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
infix operator fun Byte.div(other: Number): Number{
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
infix operator fun Short.div(other: Number): Number{
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
infix operator fun Int.rem(other: Number): Number{
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
infix operator fun Long.rem(other: Number): Number{
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
infix operator fun Float.rem(other: Number): Number{
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
infix operator fun Double.rem(other: Number): Number{
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
infix operator fun Byte.rem(other: Number): Number{
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
infix operator fun Short.rem(other: Number): Number{
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