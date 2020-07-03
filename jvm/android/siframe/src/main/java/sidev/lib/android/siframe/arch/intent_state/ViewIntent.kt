package sidev.lib.android.siframe.arch.intent_state

import sidev.lib.universal.`fun`.classSimpleName
import sidev.lib.universal.`fun`.getSealedClassName
import sidev.lib.universal.tool.util.StringUtil

/**
 * <2 Juli 2020> => Sementara hanya sebagai penanda kelas turunan ini sbg Intent dalam arsitektur MVI.
 */
open class ViewIntent{
    open val equivalentReqCode: String
        = StringUtil.toSnakeCase(this.classSimpleName(), true) //this.getSealedClassName(true)!!
    fun getReqCode()= equivalentReqCode
}