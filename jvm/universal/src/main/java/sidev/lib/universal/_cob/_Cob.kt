package sidev.lib.universal._cob

import sidev.lib.universal.`fun`.*
import sidev.lib.universal.`val`.RoundMethod
import java.lang.Thread.yield
import kotlin.math.log10
import kotlin.reflect.KFunction

fun main(args: Array<String>) {
//    val v = BooleanWrap("a")
    val p= ADE()
    p.a.aoe()
    val decimal= 1.2315f -1.2315f.toInt()
    val decimalRound= decimal *10000
    val decimal2= decimalRound -(decimalRound.toInt())

/*
    fun ok(){}
    fun ada(): KFunction<Unit>{
        fun ya(){}
        return ::ya
    }
    ada().call()
 */

    prin("decimal= $decimal decimalRound= $decimalRound decimal2= $decimal2 1.2315f.toInt()= ${1.2315f.toInt()}")
    val roundClosest= (-1).roundClosest(0 .. Int.MAX_VALUE)
    val intMinVal= Int.MIN_VALUE
    val longMinVal= Long.MIN_VALUE
    prin("longMinVal -1 = ${longMinVal -1} (longMinVal -1) == Long.MAX_VALUE => ${(longMinVal -1) == Long.MAX_VALUE}")
    prin("roundClosest= $roundClosest intMinVal= $intMinVal intMinVal.absoluteValue= ${intMinVal.absoluteValue} Int.MAX_VALUE= ${Int.MAX_VALUE}")
    prin("longMinVal= $longMinVal longMinVal.absoluteValue= ${longMinVal.absoluteValue} Long.MAX_VALUE= ${Long.MAX_VALUE}")
//    val newVal= 1254.6178.round(-2)
    val double1= 12345.678.round(-2)
    val float1= 7654.321f//.round(-2, RoundMethod.CEIL)
    val float2= 7654.321//.round(-2, RoundMethod.CEIL)
    val float3= 7654.321f//.round(-2, RoundMethod.CEIL)
    val res= (((double1 + float1).round(-2) - float2).round(-2) + float3).round(-2)

    prin("double1= $double1 float1= $float1 res= $res")

    prin("log10(123.0).toInt() +1= ${log10(123.0).toInt() +1}")
    prin("1234.5146.getNumberAtDigit(-4)= ${1234.5146.getNumberAtDigit(-4)}")
    prin("1234.getNumberAtDigit(-3)= ${1234.getNumberAtDigit(-3)}")
    prin("1254.6178f.round(-2, RoundMethod.FLOOR)= ${1254.6178f.round(-2, RoundMethod.FLOOR)}")
    prin("1234.round(-3)= ${1234.round(-3)}")
    prin("10.toString(2) = ${10.toString(2)} 10 shl 3 = ${10 shr 3} 10 shl 1 = ${10 shl 1}")
    prin("123.456.getDecimal()= ${123.456.getDecimal()}")
//    prin("(123 shr 3) + (123 shr 1) = ${(123 shr 3) + (123 shr 1)}")

    val a= plus(2L, 3f)
    prin("a= $a type= ${a::class}")
    var pow= 2 pow 0x2
    pow -= 1
    val kur= 1 / 2.5
    pow %= 2.5
    ++pow
    val round= pow roundClosest 1..3
    val absVal= pow.absoluteValue
    val absVal2= (-2.3).absoluteValue
    val minVal= -absVal2
    prin("pow= $pow type= ${pow::class} kur= $kur round= $round absVal= $absVal absVal2= $absVal2 minVal= $minVal")

//    val enumCompare= En.A < En.Z

    prin("En.A < En.Z= ${En.A < En.Z} En.Z > En.E= ${En.Z > En.E}")

    prin("0.0.isZero() => ${0.0.isZero()} 0.00000000.isZero() => ${0.00000000.isZero()} 0.00000001.isZero() => ${0.00000001.isZero()} 0.0000001.isPositive() => ${0.0000001.isPositive()} (-0.0000001).isNegative() => ${(-0.0000001).isNegative()}")
    prin("(-1).isNegative() => ${(-1).isNegative()} (-0.1).isNegative() => ${(-0.1).isNegative()} (-0.00000000001).isPositive() => ${(-0.00000000001).isPositive()}")

    1("aak")

    val b= 1
//    val c= 1 +if(b == 2) b else 3
    prin("1 +if(b == 2) b else 3 => ${1 +if(b == 2) b else 3}")
}
internal fun plus(a: Number, b: Number): Number = a + b
internal fun rem(a: Number, b: Number): Number = a % b

internal class BooleanWrap(private val value: String): Comparable<String> by value

internal class ADE{
    val a: ArrayList<Int> by lazy { ArrayList<Int>() }
}

internal enum class En{A, C, Z, E}

internal fun <T> ArrayList<T>.aoe(){}

internal operator fun Int.invoke(other: Any, other2: Any?= null) = prine("Int.invoke() = this= $this other= $other other2= $other2")