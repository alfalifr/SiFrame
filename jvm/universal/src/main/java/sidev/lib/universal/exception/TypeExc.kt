package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class TypeExc(relatedClass: KClass<*>?= TypeExc::class, msg: String= "")
    : Exc(relatedClass, "Tipe data yang di-pass salah", msg)