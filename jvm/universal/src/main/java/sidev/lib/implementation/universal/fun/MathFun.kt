package sidev.lib.implementation.universal.`fun`

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