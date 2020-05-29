package sidev.lib.android.siframe.util.`fun`

import android.util.Log
import java.lang.reflect.ParameterizedType


inline fun <reified T> Any.getGenericType(order: Int= 0){
    val stringListField = this::class.java.getDeclaredField("stringList");
    val stringListType =  this::class.java.typeParameters[order] as ParameterizedType
    val stringListClass = stringListType.actualTypeArguments[0] as Class<*>
    Log.e("Any.getGenericType", "stringListClass = $stringListClass") // class java.lang.String.
}

fun Any.className(): String {
    return this::class.java.name
}
fun Any.classSimpleName(): String {
    return this::class.java.simpleName
}