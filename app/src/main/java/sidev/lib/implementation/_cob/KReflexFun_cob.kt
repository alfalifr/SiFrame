package sidev.lib.implementation._cob

import android.util.Log
import sidev.lib.universal.`fun`.notNullTo
import sidev.lib.universal.`fun`.printe
import sidev.lib.universal.`fun`.superInterfacesTree
import sidev.lib.universal.`fun`.superclassesTree
import sidev.lib.universal.`val`.StringLiteral
import sidev.lib.universal.annotation.Interface
import sidev.lib.universal.structure.NestedIterator
import sidev.lib.universal.structure.NestedIteratorImpl
import sidev.lib.universal.structure.NestedSequence
import java.io.Serializable
import java.lang.Exception
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

const val K_CLASS_BASE_NAME= "KClassImpl"
/*
fun Any.findSealedSubclass(func: ()): Class{
    this::class.isInstance()
}
 */
interface A
interface Y
interface X: Y
interface Z: A{
    val a: Boolean
}
open class B
open class D

class C: Z, B(){
    override val a: Boolean
        get() = true
}

sealed class AA
sealed class AB: AA(){
    protected val b= "ok"
}
sealed class AC:  Z, Y, X, AB(){
    val ok= "ppop"
}

data class AAD(val a: Int= 100, val b: Boolean, val bClss: B= B(), val dClss: D?)

class AD()


fun main(args: Array<String>){
//    Class.forName("").kotlin.java.isInterface
    for(supert in AC::class.supertypes){
        (supert.classifier as KClass<*>)
        println("super= $supert supert is KClass<*> = ${supert.classifier is KClass<*>} isInterface= ${supert.isInterface}")
    }

    println("\n=============BATAS #1==============\n")

    val supertypeSeq= AC::class.supertypesJvm(true)

    for((i, supert) in supertypeSeq.withIndex()){
        println("i= $i super= $supert isInterface= ${supert.isInterface}")
    }

    println("\n=============BATAS #2==============\n")

    for( supert in supertypeSeq){
        println("super= $supert isInterface= ${supert.isInterface}")
    }

    println("\n=============BATAS==============\n")

    for((i, supert) in AC::class.java.superInterfacesTree.withIndex()){
        println("i= $i super= $supert isInterface= ${supert.isInterface}")
    }

    println("\n=============BATAS==============\n")

    for((i, supert) in AC::class.java.superclassesTree.withIndex()){
        println("i= $i super= $supert isInterface= ${supert.isInterface}")
    }

    println("\n=============BATAS==============\n")

    println("default int= ${defaultPrimitiveValue<Int>()}")
    println("default val == null => ${defaultPrimitiveValue<Array<Int>>() == null}")
    println("Array<String>::class.qualifiedName= ${Array<Int>::class.qualifiedName}")
    println("Array<Int>::class == Array<String>::class => ${Array<Int>::class == Array<String>::class}")

    println("AAD data= ${new<AAD>()}")
    for(prop in AC::class.memberProperties)
        println("prop name= ${prop.name}")
}


val <T: Any> KClass<T>.isInterface
    get()= this.java.isInterface

val KType.isInterface
    get()= (this.classifier as? KClass<*>)?.java?.isInterface

/*
/**
 * [onlyExtendedClass] true jika iterasi hanya dilakukan pada super class dg jenis Class, bkn Interface.
 *
 * Fungsi ini msh bergantung dari Java Reflection.
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
        println()
        val supertype= this::class.supertypes.find { (it.classifier as? KClass<*>)?.isInterface ?: false } //.first() //super yg berupa class pasti ada di awal deklarasi.
//        Log.e("iterateSuperClass_k", "iterateSuperClass_k() supertype.classifier= ${(supertype.classifier as KClass<*>).simpleName}")
//        throw Exception()
        if(supertype != null)
            iterator(supertype.classifier as KClass<*>)
        supertype?.classifier!!.iterateSuperClass_k(onlyExtendedClass, iterator)
    } else{
        for(supertype in this::class.supertypes){
//            Log.e("iterateSuperClass_k", "!!!onlyExtendedClass iterateSuperClass_k() supertype.classifier= ${(supertype.classifier as KClass<*>).simpleName}")
            iterator(supertype.classifier as KClass<*>)
            supertype.classifier!!.iterateSuperClass_k(onlyExtendedClass, iterator)
        }
    }
}
 */

/**
 * [onlyExtendedClass] true jika iterasi hanya dilakukan pada super class dg jenis Class, bkn Interface.
 * Fungsi ini mendefinisikan supertype sbg interface berdasarkan refleksi Java.
 *
 * Fungsi ini msh bergantung dari Java Reflection.
 */
fun KClass<*>.supertypesJvm(onlyExtendedClass: Boolean= false): NestedSequence<KType>{
    return object : NestedSequence<KType>{
        override fun iterator(): NestedIterator<KType>
            = object: NestedIteratorImpl<KType>(supertypes){
            override fun getIterator(now: KType): Iterator<KType>?{
                return if((now.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                else (now.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }

            override fun skip(now: KType): Boolean
                    = ((now.classifier as? KClass<*>)?.isInterface ?: false) && onlyExtendedClass
        }
    }
}

/**
 * [onlyExtendedClass] true jika iterasi hanya dilakukan pada super class dg jenis Class, bkn Interface.
 * Fungsi ini mendefinisikan supertype sbg interface berdasarkan anotasi framework SiFrame,
 * yaitu tipe data yg memiliki anotasi [Interface].
 */
fun KClass<*>.supertypesSif(onlyExtendedClass: Boolean= false): NestedSequence<KType>{
    return object : NestedSequence<KType>{
        override fun iterator(): NestedIterator<KType>
            = object: NestedIteratorImpl<KType>(supertypes){
            override fun getIterator(now: KType): Iterator<KType>?{
                return if((now.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                else (now.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }

            override fun skip(now: KType): Boolean
                    = ((now.classifier as? KClass<*>)?.findAnnotation<Interface>() != null)
                    && onlyExtendedClass
        }
    }
}

val KClass<*>.supertypesTree: NestedSequence<KType>
    get()= object : NestedSequence<KType>{
        override fun iterator(): NestedIterator<KType>
            = object: NestedIteratorImpl<KType>(supertypes){
            override fun getIterator(now: KType): Iterator<KType>?{
                return if((now.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                else (now.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }
        }
    }

val KClass<*>.sealedSubClassesTree: NestedSequence<KClass<*>>
    get()= object : NestedSequence<KClass<*>>{
        override fun iterator(): NestedIterator<KClass<*>>
            = object: NestedIteratorImpl<KClass<*>>(this@sealedSubClassesTree){
            override fun getIterator(now: KClass<*>): Iterator<KClass<*>>?
                    = now.sealedSubclasses.iterator()
        }
    }

fun KClass<*>.iterateSealedClass(iterator: (Class<*>) -> Unit){
    for(clazz in this::class.sealedSubclasses){
        iterator(clazz.java)
    }
}
fun KClass<*>.findSealedSubclass(iterator: (Class<*>) -> Boolean): Class<*>?{
    for(clazz in this::class.sealedSubclasses){
        if(iterator(clazz.java))
            return clazz.java
    }
    return null
}
/*
/**
 * [isQualifiedName] -true jika nama yg diambil adalah nama lengkap dimulai dari sealed super class
 *                    hingga kelas ini yg dipidahkan oleh titik (.).
 *                   -false jika nama yg diambil hanyalah nama kelas ini.
 * @return -null jika kelas ini gak punya [KClass.simpleName]
 *         -[KClass.simpleName] jika ternyata kelas ini gak punya sealed super class.
 */
@Deprecated("Gunakan [KClass<*>.getSealedClassName] agar lebih sesuai konteks", ReplaceWith("KClass<*>.getSealedClassName"))
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
 */

/**
 * [isQualifiedName] -true jika nama yg diambil adalah nama lengkap dimulai dari sealed super class
 *                    hingga kelas ini yg dipidahkan oleh titik (.).
 *                   -false jika nama yg diambil hanyalah nama kelas ini.
 * @return -null jika kelas ini gak punya [KClass.simpleName]
 *         -[KClass.simpleName] jika ternyata kelas ini gak punya sealed super class.
 */
fun KClass<*>.getSealedClassName(isQualifiedName: Boolean= true): String?{
    Log.e("getSealedClassName", ".getSealedClassName() MAULAI")
    return this.simpleName.notNullTo { thisName ->
        var thisNameRes= thisName
        if(isQualifiedName){
//            Log.e("getSealedClassName", ".getSealedClassName() QUALIFIED MAULAI")
            var superName= ""
            var isSealedSuperFound= false
            for(supertype in this.supertypesTree){
                val clazz= (supertype.classifier as? KClass<*>)
                if(clazz != null){
                    superName= clazz.simpleName!! +"." +superName
                    if(clazz.isSealed){
                        isSealedSuperFound= true
                        break
                    }
                }
            }
/*
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
 */
//            Log.e("getSealedClassName", ".getSealedClassName() QUALIFIED SELESAI")
            if(isSealedSuperFound)
                thisNameRes= superName +thisNameRes
        }
        Log.e("getSealedClassName", "thisNameRes= $thisNameRes")
        thisNameRes
    }
}

/**
 * <14 Juli 2020> => Versi baru fungsi inline yg kecil.
 */
inline fun <reified T: Any> new(noinline defParamValFunc: ((param: KParameter) -> Any?)?= null): T?
    = new(T::class, defParamValFunc)

/**
 * Digunakan untuk meng-instatiate instance baru menggunakan Kotlin Reflection.
 *
 * [defParamValFunc] dipanggil jika nilai default dari [defParamValFunc.param] dg tipe [KParameter]
 * tidak dapat diperoleh.
 *
 * Nilai default suatu [KParameter] tidak diperoleh dari fungsi di dalam
 * framework ini disebabkan karena [KParameter] bkn merupakan tipe primitif atau
 * tidak terdefinisi pada fungsi [defaultPrimitiveValue].
 *
 * <14 Juli 2020> => Tidak jadi inline karena fungsi ini besar. Sbg gantinya, fungsi [new] di atas
 *   adalah inline namun dg kode yg kecil.
 */
fun <T: Any> new(clazz: KClass<T>, defParamValFunc: ((param: KParameter) -> Any?)?= null): T?{
    //1. Cari constructor dg parameter tersedikit.
    val constrs= clazz.constructors //T::class.constructors
    var constr: KFunction<T>? = null //constrs.first()
    var minParams= Int.MAX_VALUE
    for(cons in constrs){
        if(minParams > cons.parameters.size){
            constr= cons
            minParams= cons.parameters.size
        }
    }

    val params=  constr!!.parameters
    val defParamVal= HashMap<KParameter, Any?>()

    for(param in params){
        val type= param.type
        val typeClass= type.classifier as KClass<*>
        val paramVal=
            try{ defParamValFunc!!(param) } //Yg diprioritaskan pertama adalah definisi value dari programmer.
            catch (e: Exception) { //Jika programmer tidak mendefinisikan, coba cari nilai default.
                if(param.isOptional) continue //Tapi sebelum ke nilai default, cek apakah param opsional.
                            //Jika opsional, maka artinya programmer udah memberikan definisi sendiri untuk param itu.
                            // Maka gak perlu ditambahkan ke konstruktor.
                try{ defaultPrimitiveValue(typeClass)!! } //Jika ternyata gak opsional, maka coba cari nilai default.
                catch (e: Exception){
                    printe("newInstance(): paramName= ${param.name} nilai param tidak terdefinisi!")
//                    Log.e("newInstance()", "paramName= ${param.name} nilai param tidak terdefinisi!")
                    null
                }
            }
        if(paramVal != null){
            defParamVal[param]= paramVal
        } else{
            if(type.isMarkedNullable)
                defParamVal[param]= null
            else if(!param.isOptional) //Harusnya bisa langsung else, tapi biar readable aja.
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
    return constr.callBy(defParamVal)
}

inline fun <reified T: Any> defaultPrimitiveValue(): T?= defaultPrimitiveValue(T::class)
/**
 * Digunakan untuk mendapatkan nilai default dari suatu tipe data yg ada pada [clazz].
 * Nilai default dapat diperoleh jika tipe data pada [clazz] merupakan tipe primitif
 * sesuai definisi yg ada.
 *
 * Fungsi ini tidak dapat menghasilkan nilai default dari Array<*>.
 */
fun <T: Any> defaultPrimitiveValue(clazz: KClass<T>): T?{

//    Log.e("defaultPrimitiveValue()", "clazz.simpleName= ${clazz.simpleName} String.classSimpleName_k()= ${String::class.classSimpleName()}")
    val res= when (clazz) {
//        null -> intent.putExtra(it.first, null as Serializable?)
//        CharSequence.classSimpleName() -> ""
        Int::class -> 0
        Long::class -> 0L
        String::class -> ""
        Float::class -> 0f
        Double::class -> 0.0
        Char::class -> '_'
        Short::class -> 0.toShort()
        Boolean::class -> true
        Byte::class -> 0.toByte()

        Serializable::class -> 0

        IntArray::class -> IntArray(0)
        LongArray::class -> LongArray(0)
        FloatArray::class -> FloatArray(0)
        DoubleArray::class -> DoubleArray(0)
        CharArray::class -> CharArray(0)
        ShortArray::class -> ShortArray(0)
        BooleanArray::class -> BooleanArray(0)
        ByteArray::class -> ByteArray(0)

//        is Serializable -> intent.putExtra(it.first, value)
//        is Bundle -> intent.putExtra(it.first, value)
//        is Parcelable -> intent.putExtra(it.first, value)
//        Array(0){it as T}.classSimpleName_k() -> Array(0){ it as T }

        else -> {
            println("${StringLiteral.ANSI_RED}defaultPrimitiveValue(): Kelas: ${clazz.qualifiedName} bkn merupakan primitif. Hasil akhir == NULL ${StringLiteral.ANSI_RESET}")
//            Log.e("defaultPrimitiveValue()", "Kelas: ${clazz.simpleName} bkn merupakan primitif. Hasil akhir == NULL")
            null
        } //throw Exception("Tipe data \"${clazz.simpleName}\" bukan nilai primitif.")
    }
    return res as? T
}

/**
 * Fungsi yg dapat dipanggil melalui Java.
 */
fun <T: Any> getKClass(javaClass: Class<T>): KClass<T>
    = javaClass.kotlin

/*
fun <T: Any> checkTypeSafety(any: T): Boolean{

}
 */