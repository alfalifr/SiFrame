package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class ClassCastExc(
    relatedClass: KClass<*>?= ClassCastExc::class,
    fromClass: KClass<*>?= null,
    toClass: KClass<*>?= null,
    msg: String= "")
    : Exc(
        relatedClass,
        "Tipe data \"${fromClass?.qualifiedName ?: fromClass?.java?.name}\" tidak dapat di-cast jadi \"${toClass?.qualifiedName ?: toClass?.java?.name}\"",
        msg
    )