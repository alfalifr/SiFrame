package sidev.lib.universal.`fun`

import android.util.Log
import sidev.lib.universal.structure.NestedIterator
import sidev.lib.universal.structure.NestedIteratorImpl
import sidev.lib.universal.structure.NestedSequence
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


val Class<*>.superclassesTree: Sequence<Class<*>>
    get()= object : Sequence<Class<*>> {
        override fun iterator(): Iterator<Class<*>>
            = object: Iterator<Class<*>>{
            private var now: Class<*>?= superclass
            override fun hasNext(): Boolean = now != null
            override fun next(): Class<*>{
                val next= now
                now= now?.superclass
                return next!!
            }
        }
    }

val Class<*>.superInterfacesTree: NestedSequence<Class<*>>
    get()= object : NestedSequence<Class<*>>{
        override fun iterator(): NestedIterator<Class<*>>
            = object: NestedIteratorImpl<Class<*>>(this@superInterfacesTree){
            override fun getIterator(now: Class<*>): Iterator<Class<*>>? {
                val int= now.interfaces
                return if(int.isNotEmpty()) int.iterator()
                else null
            }
        }
    }


/**
 * Digunakan untuk mengambil semua field yg ada pada sebuah kelas, termasuk super class
 * dan private field. Pengaturan ttg cakupan field yg diambil ada di parameter.
 * Fungsi ini menggunakan Java Reflection, bkn Kotlin.
 * jika [includeInherited] true, maka field super class yg diambil tidak sampai kelas [Object].
 *
 * @param includeInherited true jika termasuk field cuperClass.
 * @param justPublic true jika hanya mengambil yang public.
 * @param justOverriden true jika field yg diambil hanya field paling bawah milik child-class.
 *   false jika field yg diambil adalah delkarasi dasar milik superclass.
 *
 *
 * @return null jika field yg didapat 0.
 */
fun Any.getAllFields(includeInherited: Boolean= true, justPublic: Boolean= true, justOverriden: Boolean= true)
        : List<Field>? {
    val list= ArrayList<Field>()
    var clazz: Class<*>? = this::class.java
    val fieldNameSet= LinkedHashSet<String>()
    do{
        Log.e("getAllFields", "getAllFields() ${clazz?.simpleName}")
        val fields= if(justPublic) clazz!!.fields
            else clazz!!.declaredFields
//        val supposedType= T::class.java.name

        for(field in fields){
            if(justOverriden){
                if(!fieldNameSet.contains(field.name)){
                    fieldNameSet.add(field.name)
                    list.add(field)
                }
            } else{
                list.add(field)
                Log.e("getAllFields", "getAllFields() added field.name= ${field.name}")
            }
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
