package sidev.lib.universal._cob

fun main(args: Array<String>) {
//    val v = BooleanWrap("a")
    val p= ADE()
    p.a.aoe()
}

class BooleanWrap(private val value: String): Comparable<String> by value

class ADE{
    val a: ArrayList<Int> by lazy { ArrayList<Int>() }
}

fun <T> ArrayList<T>.aoe(){}