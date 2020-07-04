package sidev.lib.universal.`fun`

import android.util.Log
//import sidev.lib.android.siframe.model.FK_M
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

const val CLASS_BASE_NAME= "Object"


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
fun Any.className_k(): String? {
    return this::class.qualifiedName
}
fun Any.classSimpleName_k(): String? {
    return this::class.simpleName
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


inline fun <reified T> Any.getField(name: String= ""): Field? {
    val fields= this::class.java.declaredFields
    val supposedType= T::class.java.name

    for(field in fields){
        if(field.type.name == supposedType
            && (name.isBlank() || field.name == name))
            return field
    }
    return null
}

/**
 * Digunakan untuk mengambil semua field yg ada pada sebuah kelas, termasuk super class
 * dan private field. Pengaturan ttg cakupan field yg diambil ada di parameter.
 * Fungsi ini menggunakan Java Reflection, bkn Kotlin.
 * jika [includeInherited] true, maka field super class yg diambil tidak sampai kelas [Object].
 *
 * [includeInherited] true jika termasuk field cuperClass.
 * [justPublic] true jika hanya mengambil yang public.
 *
 * @return null jika field yg didapat 0.
 */
fun Any.getAllFields(includeInherited: Boolean= true, justPublic: Boolean= true)
        : List<Field>? {
    val list= ArrayList<Field>()
    var clazz: Class<*>? = this::class.java
    do{
        Log.e("getAllFields", "getAllFields() ${clazz?.simpleName}")
        val fields= if(justPublic) clazz!!.fields
            else clazz!!.declaredFields
//        val supposedType= T::class.java.name

        for(field in fields){
//            Log.e("getAllFields", "getAllFields() field.name= ${field.name}")
            list.add(field)
        }

        clazz= clazz.superclass //as Class<*>?
    } while(includeInherited
        && clazz != null && clazz.simpleName != CLASS_BASE_NAME)

    return if(list.isNotEmpty()) list
    else null
}

inline fun <reified F, reified G> Any.getFieldAndGeneric(): Field? {
    val fields= this::class.java.declaredFields
    val genName= G::class.java.name
    for(field in fields)
        if(field is F && field.isGenericSame(genName))
            return field
    return null
}
