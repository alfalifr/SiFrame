package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class TypeExc(
    relatedClass: KClass<*>?= TypeExc::class,
    expectedType: KClass<*>?= null,
    actualType: KClass<*>?= null,
    msg: String= "")
    : Exc(relatedClass, "Tipe data tidak sesuai, seharusnya: \"${expectedType?.qualifiedName ?: expectedType?.java?.name}\" tapi yg ada: \"${actualType?.qualifiedName ?: actualType?.java?.name}\".", msg)