package sidev.lib.universal.exception

open class PropertyAccessExc(
    relatedClass: Class<*>?= PropertyAccessExc::class.java,
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