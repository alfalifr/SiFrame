package sidev.lib.android.siframe.exception

import java.lang.Exception

open class Exc(relatedClass: Class<*>?, msg: String= "")
    : Exception(
        "Related Class: ${(relatedClass ?: Exc::class.java).name} \n" +
        "Msg= ${if(msg.isNotEmpty()) msg else "<empty>"}"
    ){
    open val relatedClass: Class<*>? =
        relatedClass ?: this::class.java

    constructor(msg: String= ""): this(null, msg)
}