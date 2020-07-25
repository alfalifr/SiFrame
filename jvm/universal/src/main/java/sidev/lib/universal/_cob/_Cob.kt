package sidev.lib.universal._cob

import sidev.lib.universal.`fun`.*

fun main(args: Array<String>) {
//    val v = BooleanWrap("a")
    val p= ADE()
    p.a.aoe()

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
}
fun plus(a: Number, b: Number): Number = a + b
fun rem(a: Number, b: Number): Number = a % b

class BooleanWrap(private val value: String): Comparable<String> by value

class ADE{
    val a: ArrayList<Int> by lazy { ArrayList<Int>() }
}

enum class En{A, C, Z, E}

fun <T> ArrayList<T>.aoe(){}