package sidev.lib.universal.exception

import sidev.lib.universal.`fun`.clazz

//import java.lang.Exception

open class Exc(relatedClass: Class<*>?, commonMsg: String= "", detMsg: String= "",
               override var cause: Throwable?= null, var code: Int= 1)
    : Exception("\n" +
        "======================================================= \n" +
        "Related Class  : ${(relatedClass ?: cause?.clazz?.java ?: Exc::class.java).name} \n" +
        "Message        : ${if(commonMsg.isNotBlank()) commonMsg else "<empty>"} \n" +
        "Detail Message : ${if(detMsg.isNotBlank()) detMsg else "<empty>"} \n" +
        "Code           : $code \n" +
        "======================================================= "
    ){
//    override var cause: Throwable?= cause
    override var message: String?= null
        get()= field ?: super.message

    open val relatedClass: Class<*>? =
        relatedClass ?: this::class.java

    constructor(msg: String?= null, cause: Throwable?= null, code: Int= 1)
            : this(null, msg ?: "", cause = cause, code = code)
}