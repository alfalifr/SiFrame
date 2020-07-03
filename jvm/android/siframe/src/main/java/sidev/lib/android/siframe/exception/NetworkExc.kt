package sidev.lib.android.siframe.exception

open class NetworkExc(relatedClass: Class<*>?= NetworkExc::class.java, msg: String= "")
    : Exc(relatedClass, "Terjadi kesalahan jaringan.", msg)