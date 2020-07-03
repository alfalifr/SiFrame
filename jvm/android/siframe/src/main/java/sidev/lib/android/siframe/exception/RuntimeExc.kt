package sidev.lib.android.siframe.exception

open class RuntimeExc(relatedClass: Class<*>?= RuntimeExc::class.java,
                      commonMsg: String= "Terjadi kesalahan saat runtime.",
                      detailMsg: String= "")
    : Exc(relatedClass, commonMsg, detailMsg)