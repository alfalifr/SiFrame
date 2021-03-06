package sidev.lib.universal.`fun`

import android.util.Log
import sidev.lib.universal.structure.collection.iterator.NestedIterator
import sidev.lib.universal.structure.collection.iterator.NestedIteratorImpl
import sidev.lib.universal.structure.collection.iterator.NestedIteratorSimple
import sidev.lib.universal.structure.collection.iterator.NestedIteratorSimpleImpl
import sidev.lib.universal.structure.collection.sequence.NestedSequence
//import sidev.lib.android.siframe.model.FK_M
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

const val CLASS_BASE_NAME= "Object"
const val METHOD_GETTER_NAME_PREFIX= "get"
const val METHOD_SETTER_NAME_PREFIX= "set"


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
val Class<*>.classesTree: Sequence<Class<*>>
    get()= object : Sequence<Class<*>> {
        override fun iterator(): Iterator<Class<*>>
                = object: Iterator<Class<*>>{
            private var now: Class<*>?= this@classesTree
            override fun hasNext(): Boolean = now != null
            override fun next(): Class<*>{
                val next= now
                now= now?.superclass
                return next!!
            }
        }
    }

val Class<*>.inheritanceTree: NestedSequence<Class<*>>
    get()= object : NestedSequence<Class<*>>{
        override fun iterator(): NestedIteratorSimple<Class<*>>
            = object : NestedIteratorSimpleImpl<Class<*>>(this@inheritanceTree){
            override fun getOutputIterator(nowInput: Class<*>): Iterator<Class<*>>?
                = nowInput.supertypes.iterator()
        }
    }

/** List yg berisi [superclass] dan [interfaces]. */
val Class<*>.supertypes: List<Class<*>>
    get(){
        val res= interfaces.toMutableList()
        if(superclass != null)
            res.add(0, superclass)
        return res
    }

val Class<*>.superInterfacesTree: NestedSequence<Class<*>>
    get()= object :
        NestedSequence<Class<*>> {
        override fun iterator(): NestedIteratorSimple<Class<*>>
            = object: NestedIteratorSimpleImpl<Class<*>>(this@superInterfacesTree){
            override fun getOutputIterator(nowInput: Class<*>): Iterator<Class<*>>? {
                val int= nowInput.interfaces
                return if(int.isNotEmpty()) int.iterator()
                else null
            }
        }
    }

/** Mengambil semua [Class.getDeclaredFields] dari `this.extension` [Class] dan superclass. */
val Class<*>.fieldsTree: NestedSequence<Field>
    get()= object: NestedSequence<Field>{
        override fun iterator(): NestedIterator<*, Field>
            = object: NestedIteratorImpl<Class<*>, Field>(this@fieldsTree.classesTree.iterator()){
            override fun getOutputIterator(nowInput: Class<*>): Iterator<Field>? = nowInput.declaredFields.iterator()
            override fun getInputIterator(nowOutput: Field): Iterator<Class<*>>? = null
        }
    }

/** Mengambil semua [Class.getDeclaredFields] dari `this.extension` [Class] dan superclass. */
val Class<*>.methodsTree: NestedSequence<Method>
    get()= object: NestedSequence<Method>{
        override fun iterator(): NestedIterator<*, Method>
            = object: NestedIteratorImpl<Class<*>, Method>(this@methodsTree.inheritanceTree.iterator()){
            override fun getOutputIterator(nowInput: Class<*>): Iterator<Method>? = nowInput.declaredMethods.iterator()
            override fun getInputIterator(nowOutput: Method): Iterator<Class<*>>? = null
        }
    }

val Class<*>.getterMethodsTree: Sequence<Method>
    get()= methodsTree.filter {
        it.name.startsWith(METHOD_GETTER_NAME_PREFIX)
                && it.parameterCount == 0
                && it.returnType != Void.TYPE
    }
val Class<*>.getterMethods: Sequence<Method>
    get()= methods.asSequence().filter {
        it.name.startsWith(METHOD_GETTER_NAME_PREFIX)
                && it.parameterCount == 0
                && it.returnType != Void.TYPE
    }

val Class<*>.setterMethodsTree: Sequence<Method>
    get()= methodsTree.filter {
        it.name.startsWith(METHOD_SETTER_NAME_PREFIX)
                && it.parameterCount == 1
                && it.returnType == Void.TYPE
    }
val Class<*>.setterMethods: Sequence<Method>
    get()= methods.asSequence().filter {
        it.name.startsWith(METHOD_SETTER_NAME_PREFIX)
                && it.parameterCount == 1
                && it.returnType == Void.TYPE
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
