package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class UndefinedDeclarationExc(
    relatedClass: KClass<*>?= UndefinedDeclarationExc::class,
    undefinedDeclaration: Any = "<declaration>",
    detailMsg: String= ""
) : Exc(relatedClass, """Terdapat sebuah deklarasi yg tidak jelas, undefined declaration: "$undefinedDeclaration".""", detailMsg)