package sidev.lib.universal.exception

import kotlin.reflect.KClass

open class PropertyAccessExc(
    relatedClass: KClass<*>?= PropertyAccessExc::class,
    kind: Kind= Kind.Null,
    ownerName: String?= "<owner>",
    propertyName: String?= "<properti>")
    : Exc(relatedClass, "Terdapat bbrp properti ${kind.msg}",
    """Properti yg bermasalah: "$propertyName" pada "$ownerName". Harap setting dulu."""){

    enum class Kind(val msg: String){
        Null("nullable yg msh null"),
        Uninitialized("lateinit yg blum diinit")
    }
}