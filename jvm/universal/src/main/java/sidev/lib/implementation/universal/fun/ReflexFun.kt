package sidev.lib.implementation.universal.`fun`

import android.util.Log
//import sidev.lib.android.siframe.model.FK_M
import java.lang.reflect.Field
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


fun <T> Field.getV(obj: Any): T? {
    val acc= this.isAccessible
    this.isAccessible= true
    val v= this.get(obj) as T?
    this.isAccessible= acc
    return v
}

fun Field.setV(obj: Any, value: Any?){
    val acc= this.isAccessible
    this.isAccessible= true
    this.set(obj, value)
    this.isAccessible= acc
}

fun Field.getGenericClass(): Class<*> {
    return ((this.genericType as ParameterizedType)
        .actualTypeArguments.get(0) as Class<*>)
}

fun Field.isGenericSame(name: String): Boolean {
    val genName= this.getGenericClass().name
    return genName == name
}


inline fun <reified T> Any.getField(): Field? {
    val fields= this::class.java.declaredFields
    val supposedType= T::class.java.name

    for(field in fields){
        if(field.type.name == supposedType)
            return field
    }
    return null
}
inline fun <reified F, reified G> Any.getFieldAndGeneric(): Field? {
    val fields= this::class.java.declaredFields
    val genName= G::class.java.name
    for(field in fields)
        if(field is F && field.isGenericSame(genName))
            return field
    return null
}
