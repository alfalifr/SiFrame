package sidev.lib.universal.`fun`

import sidev.lib.universal.`val`.StringLiteral

object IoConfig{
    const val PRINT= true
    const val PRINT_DEBUG= false
    const val PRINT_RESULT= false
    const val PRINT_WARNING= false
}

fun prine(any: Any) = prin(any, StringLiteral.ANSI_RED)
fun prinw(any: Any){
    if(IoConfig.PRINT_WARNING)
        prin(any, StringLiteral.ANSI_CYAN)
}
fun prinr(any: Any){
    if(IoConfig.PRINT_RESULT)
        prin(any, StringLiteral.ANSI_GREEN)
}
fun prind(any: Any){
    if(IoConfig.PRINT_DEBUG)
        prin(any, StringLiteral.ANSI_YELLOW)
}
fun prin(any: Any, color: String= StringLiteral.ANSI_RESET){
    if(IoConfig.PRINT)
        println("$color$any ${StringLiteral.ANSI_RESET}")
}
