package sidev.lib.universal.exception

open class IllegalAccessExc(relatedClass: Class<*>?= IllegalAccessExc::class.java, msg: String= "")
    : Exc(relatedClass, "Terjadi kesalahan saat mengakses fungsi atau properti.", msg)