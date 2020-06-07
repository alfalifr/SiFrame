package sidev.lib.android.siframe.exception

class DataIntegrityExc(relatedClass: Class<*>?= DataIntegrityExc::class.java, msg: String= "")
    : Exc(relatedClass, "Integritas data bermasalah.", msg)