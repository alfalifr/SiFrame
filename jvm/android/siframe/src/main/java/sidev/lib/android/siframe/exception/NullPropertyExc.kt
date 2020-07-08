package sidev.lib.android.siframe.exception

open class NullPropertyExc(
    relatedClass: Class<*>?= NullPropertyExc::class.java,
    ownerName: String?= "<owner>",
    propertyName: String?= "<properti>")
    : Exc(relatedClass, "Terdapat bbrp properti nullable yg masih null",
    """Properti: "$propertyName" pada "$ownerName" memiliki nilai NULL. Harap setting dulu.""")