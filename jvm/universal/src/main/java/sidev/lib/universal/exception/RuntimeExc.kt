package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class RuntimeExc(relatedClass: KClass<*>?= RuntimeExc::class,
                      commonMsg: String= "Terjadi kesalahan saat runtime.",
                      detailMsg: String= "")
    : Exc(relatedClass, commonMsg, detailMsg)