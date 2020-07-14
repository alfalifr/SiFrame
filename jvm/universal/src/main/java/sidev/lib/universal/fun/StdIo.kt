package sidev.lib.universal.`fun`

import sidev.lib.universal.`val`.StringLiteral

fun printe(any: Any) = printCol(StringLiteral.ANSI_RED, any)
fun printCol(color: String, any: Any) = println("$color$any ${StringLiteral.ANSI_RESET}")