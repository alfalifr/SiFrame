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
}
fun plus(a: Number, b: Number): Number = a + b
fun rem(a: Number, b: Number): Number = a % b

class BooleanWrap(private val value: String): Comparable<String> by value

class ADE{
    val a: ArrayList<Int> by lazy { ArrayList<Int>() }
}

fun <T> ArrayList<T>.aoe(){}