package sidev.lib.android.siframe.exception

open class ParameterExc(
    relatedClass: Class<*>?= ParameterExc::class.java,
    paramName: String= "<parameter>",
    detMsg: String= "")
    : Exc(relatedClass, "Parameter yg diinputkan: \"$paramName\" salah", detMsg)