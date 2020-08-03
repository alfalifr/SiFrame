package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class ImplementationExc(
    relatedClass: KClass<*>?= ImplementationExc::class,
    implementedClass: KClass<*>?= null,
    msg: String= "")
    : Exc(relatedClass, "Terjadi kesalahan dalam implementasi kelas: \"$implementedClass\"", msg)