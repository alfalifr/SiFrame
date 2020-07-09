package sidev.lib.android.siframe.exception

open class ResourceNotFoundExc(
    relatedClass: Class<*>?= ResourceNotFoundExc::class.java,
    resourceName: String= "<resource>",
    msg: String= "")
    : Exc(relatedClass, """Resource: "$resourceName" tidak ditemukan.""", msg)