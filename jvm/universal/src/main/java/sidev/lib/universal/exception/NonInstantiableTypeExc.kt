package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class NonInstantiableTypeExc(
    relatedClass: KClass<*>?= NonInstantiableTypeExc::class,
    typeClass: KClass<*>?= null,
    msg: String= "")
    : Exc(
    relatedClass,
    "Tipe data \"${typeClass?.qualifiedName ?: typeClass?.java?.name}\" tidak dapat di-instantiate karena merupakan interface, abstract, anonymous class, atau null.",
    msg
)