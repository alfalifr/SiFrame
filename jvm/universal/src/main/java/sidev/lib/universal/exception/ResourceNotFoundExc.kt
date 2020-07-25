package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class ResourceNotFoundExc(
    relatedClass: KClass<*>?= ResourceNotFoundExc::class,
    resourceName: String= "<resource>",
    msg: String= "")
    : Exc(relatedClass, """Resource: "$resourceName" tidak ditemukan.""", msg)