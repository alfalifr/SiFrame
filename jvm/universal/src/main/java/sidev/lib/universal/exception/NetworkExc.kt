package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class NetworkExc(relatedClass: KClass<*>?= NetworkExc::class, msg: String= "")
    : Exc(relatedClass, "Terjadi kesalahan jaringan.", msg)