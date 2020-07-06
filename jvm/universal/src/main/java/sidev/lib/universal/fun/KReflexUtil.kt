package sidev.lib.universal.`fun`

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import java.io.Serializable
import java.lang.Exception
import kotlin.reflect.KClass
import kotlin.reflect.KDeclarationContainer
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.createType
import kotlin.reflect.full.defaultType
import kotlin.reflect.full.starProjectedType

const val K_CLASS_BASE_NAME= "KClassImpl"
/*
fun Any.findSealedSubclass(func: ()): Class{
    this::class.isInstance()
}
 */

/**
 * [onlyExtendedClass] true jika iterasi hanya dilakukan pada super class dg jenis Class, bkn Interface.
 */
fun Any.iterateSuperClass_k(onlyExtendedClass: Boolean= true, iterator: (KClass<*>) -> Unit){
/*
    for(supertype in this::class.supertypes){
        Log.e("iterateSuperClass_k", "!!!onlyExtendedClass iterateSuperClass_k() supertype.classifier= ${(supertype.classifier as KClass<*>).simpleName}")
    }
    val isFirstDeclar= this::class.supertypes.first().classifier is KDeclarationContainer
    Log.e("iterateSuperClass_k", "this::class.simpleName= ${this::class.simpleName}")
    Log.e("iterateSuperClass_k", "isFirstDeclar= $isFirstDeclar")
 */
    if(this::class.supertypes.isEmpty()
        || this::class.simpleName == K_CLASS_BASE_NAME
    ) return
//    Log.e("iterateSuperClass_k", "iterateSuperClass_k() LOLOS BRO")

    if(onlyExtendedClass){
        val supertype= this::class.supertypes.first() //super yg berupa class pasti ada di awal deklarasi.
//        Log.e("iterateSuperClass_k", "iterateSuperClass_k() supertype.classifier= ${(supertype.classifier as KClass<*>).simpleName}")
//        throw Exception()
        iterator(supertype.classifier as KClass<*>)
        supertype.classifier!!.iterateSuperClass_k(onlyExtendedClass, iterator)
    } else{
        for(supertype in this::class.supertypes){
//            Log.e("iterateSuperClass_k", "!!!onlyExtendedClass iterateSuperClass_k() supertype.classifier= ${(supertype.classifier as KClass<*>).simpleName}")
            iterator(supertype.classifier as KClass<*>)
            supertype.classifier!!.iterateSuperClass_k(onlyExtendedClass, iterator)
        }
    }
}

fun Any.iterateSealedClass(iterator: (Class<*>) -> Unit){
    for(clazz in this::class.sealedSubclasses){
        iterator(clazz.java)
    }
}
fun Any.findSealedSubclass(iterator: (Class<*>) -> Boolean): Class<*>?{
    for(clazz in this::class.sealedSubclasses){
        if(iterator(clazz.java))
            return clazz.java
    }
    return null
}

/**
 * [isQualifiedName] -true jika nama yg diambil adalah nama lengkap dimulai dari sealed super class
 *                    hingga kelas ini yg dipidahkan oleh titik (.).
 *                   -false jika nama yg diambil hanyalah nama kelas ini.
 * @return -null jika kelas ini gak punya [KClass.simpleName]
 *         -[KClass.simpleName] jika ternyata kelas ini gak punya sealed super class.
 */
fun Any.getSealedClassName(isQualifiedName: Boolean= true): String?{
    Log.e("getSealedClassName", ".getSealedClassName() MAULAI")
    val thisName= if(this is KClass<*>) this.simpleName
        else this::class.simpleName
    return thisName.notNullTo { thisName ->
        var thisNameRes= thisName
        if(isQualifiedName){
//            Log.e("getSealedClassName", ".getSealedClassName() QUALIFIED MAULAI")
            var superName= ""
            var isSealedSuperFound= false
            this.iterateSuperClass_k { clazz ->
                try{
                    superName= clazz.simpleName!! +"." +superName
                    if(clazz.isSealed){
                        isSealedSuperFound= true
                        return@iterateSuperClass_k
                    }
                }
                catch (e: KotlinNullPointerException){ return@iterateSuperClass_k }
            }
//            Log.e("getSealedClassName", ".getSealedClassName() QUALIFIED SELESAI")
            if(isSealedSuperFound)
                thisNameRes= superName +thisNameRes
        }
        Log.e("getSealedClassName", "thisNameRes= $thisNameRes")
        thisNameRes
    }
}

/**
 * Digunakan untuk meng-instatiate instance baru menggunakan Kotlin Reflection.
 *
 * [defParamValFunc] dipanggil jika nilai default dari [defParamValFunc.param] dg tipe [KParameter]
 * tidak dapat diperoleh.
 *
 * Nilai default suatu [KParameter] tidak diperoleh dari fungsi di dalam
 * framework ini disebabkan karena [KParameter] bkn merupakan tipe primitif atau
 * tidak terdefinisi pada fungsi [defaultPrimitiveValue].
 */
inline fun <reified T> new(noinline defParamValFunc: ((param: KParameter) -> Any?)?= null): T?{
    //1. Cari constructor dg parameter tersedikit.
    val constrs= T::class.constructors
    var constr: KFunction<T>? = null //constrs.first()
    var minParams= Int.MAX_VALUE
    for(cons in constrs){
        if(minParams > cons.parameters.size){
            constr= cons
            minParams= cons.parameters.size
        }
    }

    val params=  constr!!.parameters //: List<KParameter>?= null
    val defParamVal= ArrayList<Any?>()

    for(param in params){
        val type= param.type //.classifier!!//as KClass<*>
        val typeClass= type.classifier as KClass<*>
        val paramVal=
            try{ defaultPrimitiveValue(typeClass) }
            catch (e: Exception) {
                try{ defParamValFunc!!(param) }
                catch (e: Exception){
                    Log.e("newInstance()", "paramName= ${param.name} nilai param tidak terdefinisi!")
                    null
                }
            }
//        Log.e("newInstance()", "paramName= ${param.name} paramVal= $paramVal")
        if(paramVal != null){
            defParamVal.add(paramVal)
        } else{
            if(type.isMarkedNullable)
                defParamVal.add(null)
            else
                return null //Karena class udah gak bisa di-instantiate.
        }
    }
/*
    if(defParamValFunc == null){
        /* implementasi lama di atas */
    }else{
        for(param in params){
            val paramVal= defParamValFunc(param)
            if(paramVal != null){
                defParamVal.add(paramVal)
            } else{
                if(param.type.isMarkedNullable)
                    defParamVal.add(null)
                else
                    return throw Exception("parameter: ${param.name} tipe: ${(param.type.classifier as KClass<*>).simpleName} gak boleh null.")
            }
        }
    }
 */
    return constr.call(*defParamVal.toTypedArray())
}

/**
 * Digunakan untuk mendapatkan nilai default dari suatu tipe data yg ada pada [clazz].
 * Nilai default dapat diperoleh jika tipe data pada [clazz] merupakan tipe primitif
 * sesuai definisi yg ada.
 */
fun <T: Any> defaultPrimitiveValue(clazz: KClass<T>): T?{
//    Log.e("defaultPrimitiveValue()", "clazz.simpleName= ${clazz.simpleName} String.classSimpleName_k()= ${String::class.classSimpleName()}")
    val res= when (clazz.simpleName) {
//        null -> intent.putExtra(it.first, null as Serializable?)
//        CharSequence.classSimpleName() -> ""
        Int::class.simpleName -> 0
        Long::class.simpleName -> 0
        String::class.simpleName -> ""
        Float::class.simpleName -> 0f
        Double::class.simpleName -> 0.0
        Char::class.simpleName -> '_'
        Short::class.simpleName -> 0.toShort()
        Boolean::class.simpleName -> true
        Byte::class.simpleName -> 0.toByte()

        Serializable::class.simpleName -> 0

        IntArray::class.simpleName -> IntArray(0)
        LongArray::class.simpleName -> LongArray(0)
        FloatArray::class.simpleName -> FloatArray(0)
        DoubleArray::class.simpleName -> DoubleArray(0)
        CharArray::class.simpleName -> CharArray(0)
        ShortArray::class.simpleName -> ShortArray(0)
        BooleanArray::class.simpleName -> BooleanArray(0)
        ByteArray::class.simpleName -> ByteArray(0)

//        is Serializable -> intent.putExtra(it.first, value)
//        is Bundle -> intent.putExtra(it.first, value)
//        is Parcelable -> intent.putExtra(it.first, value)
//        Array(0){it as T}.classSimpleName_k() -> Array(0){ it as T }

        else -> {
            Log.e("defaultPrimitiveValue()", "Kelas: ${clazz.simpleName} bkn merupakan primitif. Hasil akhir == NULL")
            null
        } //throw Exception("Tipe data \"${clazz.simpleName}\" bukan nilai primitif.")
    }
    return res as? T
}

/*
fun <T: Any> checkTypeSafety(any: T): Boolean{

}
 */