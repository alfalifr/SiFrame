package sidev.lib.android.siframe.intfc.`fun`

import sidev.lib.android.siframe.exception.NullPropertyExc
import java.lang.ClassCastException

interface AssertPropNotNullFun {
    fun getProperty(propertyName: String?): Any?
    fun <I, O> assertNotNull(propertyName: String?= null, func: (property: I) -> O): O{
        val prop= getProperty(propertyName)
            ?: throw NullPropertyExc(propertyName = propertyName, ownerName = this::class.java.simpleName)

        return try{ func(prop as I) }
        catch (e: ClassCastException){ throw e }
    }
}