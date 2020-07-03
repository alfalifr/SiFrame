package sidev.lib.android.siframe.exception

open class IllegalAccessExc(relatedClass: Class<*>?= IllegalAccessExc::class.java, msg: String= "")
    : Exc(relatedClass, "Terjadi kesalahan saat mengakses fungsi atau properti.", msg)