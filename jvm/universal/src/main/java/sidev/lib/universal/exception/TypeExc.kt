package sidev.lib.universal.exception

open class TypeExc(relatedClass: Class<*>?= TypeExc::class.java, msg: String= "")
    : Exc(relatedClass, "Tipe data yang di-pass salah", msg)