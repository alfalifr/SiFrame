package sidev.lib.android.siframe.exception

import java.lang.Exception

open class Exc(relatedClass: Class<*>?, commonMsg: String= "", detMsg: String= "")
    : Exception("\n" +
        "Related Class: ${(relatedClass ?: Exc::class.java).name} \n" +
        "Msg= ${if(commonMsg.isNotBlank()) commonMsg else "<empty>"} \n" +
        "Detail Msg= ${if(commonMsg.isNotBlank()) detMsg else "<empty>"}"
    ){
    open val relatedClass: Class<*>? =
        relatedClass ?: this::class.java

    constructor(msg: String= ""): this(null, msg)
}