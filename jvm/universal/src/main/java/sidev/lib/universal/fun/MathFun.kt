package sidev.lib.universal.`fun`

import kotlin.math.abs

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

fun Int.roundClosest(range: IntRange): Int{
    val diffToFirst= abs(this -range.first)
    val diffToLast= abs(this -range.last)

    return if(diffToFirst < diffToLast) range.first
    else range.last
}