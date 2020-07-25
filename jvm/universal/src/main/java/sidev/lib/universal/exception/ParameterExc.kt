package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class ParameterExc(
    relatedClass: KClass<*>?= ParameterExc::class,
    paramName: String= "<parameter>",
    detMsg: String= "")
    : Exc(relatedClass, "Parameter yg diinputkan: \"$paramName\" salah", detMsg)