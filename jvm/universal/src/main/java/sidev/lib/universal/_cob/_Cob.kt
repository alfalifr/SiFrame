package sidev.lib.universal._cob

fun main(args: Array<String>) {
    val v = BooleanWrap("a")
}

class BooleanWrap(private val value: String): Comparable<String> by value