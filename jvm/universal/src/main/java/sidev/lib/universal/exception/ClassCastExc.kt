package sidev.lib.universal.exception

open class ClassCastExc(
    relatedClass: Class<*>?= ClassCastExc::class.java,
    fromClass: Class<*>?= null,
    toClass: Class<*>?= null,
    msg: String= "")
    : Exc(
        relatedClass,
        "Tipe data \"${fromClass?.name}\" tidak dapat di-cast jadi \"${toClass?.name}\"",
        msg
    )