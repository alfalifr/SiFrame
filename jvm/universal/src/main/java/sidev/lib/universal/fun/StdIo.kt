package sidev.lib.universal.`fun`

import sidev.lib.universal.`val`.StringLiteral

object IoConfig{
    const val PRINT= true
    const val PRINT_DEBUG= PRINT && false
    const val PRINT_RESULT= PRINT && true
    const val PRINT_WARNING= PRINT && false
    const val PRINT_ERROR= PRINT && true
}

fun prind(any: Any, endWithNewLine: Boolean = true){
    if(IoConfig.PRINT_DEBUG)
        prin(any, StringLiteral.ANSI_CYAN, endWithNewLine)
}
fun prinr(any: Any, endWithNewLine: Boolean = true){
    if(IoConfig.PRINT_RESULT)
        prin(any, StringLiteral.ANSI_GREEN, endWithNewLine)
}
fun prinw(any: Any, endWithNewLine: Boolean = true){
    if(IoConfig.PRINT_WARNING)
        prin(any, StringLiteral.ANSI_YELLOW, endWithNewLine)
}
fun prine(any: Any, endWithNewLine: Boolean = true){
    if(IoConfig.PRINT_ERROR)
        prin(any, StringLiteral.ANSI_RED, endWithNewLine)
}
fun prin(any: Any, color: String= StringLiteral.ANSI_RESET, endWithNewLine: Boolean = true){
    if(IoConfig.PRINT){
        if(endWithNewLine)
            println("$color $any ${StringLiteral.ANSI_RESET}")
        else
            print("$color $any ${StringLiteral.ANSI_RESET}")
    }
}
