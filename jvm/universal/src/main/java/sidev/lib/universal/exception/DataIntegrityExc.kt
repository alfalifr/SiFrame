package sidev.lib.universal.exception

open class DataIntegrityExc(relatedClass: Class<*>?= DataIntegrityExc::class.java, msg: String= "")
    : Exc(relatedClass, "Integritas data bermasalah.", msg)