package sidev.lib.universal.`fun`

import sidev.lib.universal.`val`.StringLiteral
import sidev.lib.universal.annotation.Interface
import sidev.lib.universal.structure.*
import java.io.Serializable
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible


const val K_CLASS_BASE_NAME= "KClassImpl"
const val K_FUNCTION_CONSTRUCTOR_NAME_PREFIX= "fun <init>"

/*
fun Any.findSealedSubclass(func: ()): Class{
    this::class.isInstance()
}
 */


/*
==========================
Constructor
==========================
 */

val KParameter.isInConstructor: Boolean
    get()= this.toString().contains(K_FUNCTION_CONSTRUCTOR_NAME_PREFIX)

fun KParameter.isPropertyLike(prop: KProperty<*>, isInConstructorKnown: Boolean= false): Boolean{
    return if(isInConstructorKnown || isInConstructor){
        val res= name == prop.name && type.classifier == prop.returnType.classifier
        prine("isPropertyLike param name= $name res= $res prop= $prop")
        res
    } else false
}


/** Mengambil konstruktor dg jumlah parameter paling sedikit. */
val <T: Any> KClass<T>.leastParamConstructor: KFunction<T>
    get(){
        var constr= constructors.first()
        var minParamCount= constr.parameters.size
        for(constrItr in constructors){
            if(minParamCount > constrItr.parameters.size){
                constr= constrItr
                minParamCount= constrItr.parameters.size
            }
        }
        return constr
    }

/** Mirip dg [leastParamConstructor], namun param opsional tidak disertakan. Nullable tetap disertakan. */
val <T: Any> KClass<T>.leastRequiredParamConstructor: KFunction<T>
    get(){
        var constr= constructors.first()
        var minParamCount= constr.parameters.size

        for(constrItr in constructors){
            prine("leastRequiredParamConstructor class= $simpleName constrItr.parameters.size= ${constrItr.parameters.size}")
            if(minParamCount > constrItr.parameters.size){
                constr= constrItr
                minParamCount= constrItr.parameters.size
                for(param in constrItr.parameters)
                    if(param.isOptional) minParamCount--
            }
        }
        val paramList= ArrayList<KParameter>()
        for(param in constr.parameters)
            if(!param.isOptional) paramList.add(param)

        //Bungkus constructor yg ditemukan dengan fungsi yg parameter listnya hanya terlihat yg non-optional.
        return object: KFunction<T> by constr{
            override val parameters: List<KParameter> = paramList
        }
    }

/** Mengambil konstruktor dg param yg memiliki tipe data sesuai [paramClass]. Jika tidak ketemu, maka throw [NoSuchMethodException]. */
fun <T: Any> KClass<T>.findConstructorWithParam(vararg paramClass: KClass<*>): KFunction<T>{
    for(constr in constructors){
        var paramClassMatch= true
        if(constr.parameters.size == paramClass.size){
            for((i, param) in constr.parameters.withIndex()){
                paramClassMatch= paramClassMatch && param.type.classifier == paramClass[i]
            }
        } else continue

        if(paramClassMatch)
            return constr
    }
    throw NoSuchMethodException("Tidak ada konstruktor \"$qualifiedName\" dg parameter \"${paramClass.string}\"")
}



/*
==========================
KCallable - Call - Set - Get
==========================
 */

/** @return [O] jika operasi get berhasil, null jika refleksi dilarang. */
fun <I, O> KProperty1<I, O>.forcedGet(receiver: I): O?{
    return try{
        val oldIsAccesible= isAccessible
        isAccessible= true
        val value= get(receiver)
        isAccessible= oldIsAccesible
        value
    } catch (e: IllegalCallableAccessException){ //Jika Kotlin melarang melakukan call melalui refleksi
        null
    } catch (e: InvocationTargetException){
        //Jika terjadi error secara internal pada refleksi Java.
        // Biasanya terjadi pada operasi call melibatkan `lateinit var`
        null
    }
}
/** @return true jika operasi set berhasil, false jika refleksi dilarang. */
fun <I, O> KMutableProperty1<I, O>.forcedSet(receiver: I, value: O): Boolean{
    return try{
        val oldIsAccesible= isAccessible
        isAccessible= true
        set(receiver, value)
        isAccessible= oldIsAccesible
        true
    } catch (e: IllegalCallableAccessException){ //Jika Kotlin melarang melakukan call melalui refleksi
        false
    } catch (e: InvocationTargetException){
        //Jika terjadi error secara internal pada refleksi Java.
        // Biasanya terjadi pada operasi call melibatkan `lateinit var`
        false
    }
}
/** @return [R] jika operasi call berhasil, null jika refleksi dilarang. */
fun <R> KCallable<R>.forcedCall(vararg args: Any?): R?{
    return try{
        val oldIsAccesible= isAccessible
        isAccessible= true
        val value= call(*args) //get(receiver)
        isAccessible= oldIsAccesible
        value
    } catch (e: IllegalCallableAccessException){ //Jika Kotlin melarang melakukan call melalui refleksi
        null
    } catch (e: InvocationTargetException){
        //Jika terjadi error secara internal pada refleksi Java.
        // Biasanya terjadi pada operasi call melibatkan `lateinit var`
        null
    }
}
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


/*
==========================
Inheritance
==========================
 */

val <T: Any> KClass<T>.isInterface: Boolean
    get()= this.java.isInterface

val KType.isInterface
    get()= (this.classifier as? KClass<*>)?.java?.isInterface

val <T: Any> KClass<T>.isPrimitive: Boolean
    get()= this.javaPrimitiveType != null


/**
 * Fungsi yg dapat dipanggil melalui Java.
 */
fun <T: Any> getKClass(javaClass: Class<T>): KClass<T>
        = javaClass.kotlin

/** Berguna untuk mengambil kelas dari instance dg tipe generic yg tidak punya batas atas Any. */
val Any.clazz: KClass<*>
    get()= this::class

/** Memiliki fungsi sama dg [isSuperclassOf] namun berguna untuk variabel generic tanpa batas atas Any. */
fun <T1, T2> T1.isSuperClassOf(derived: T2): Boolean{
    val thisClass= this?.clazz
    return if(thisClass != null){
        derived?.clazz?.isSuperclassOf(thisClass) == true
    } else false
}

/** Memiliki fungsi sama dg [isSubclassOf] namun berguna untuk variabel generic tanpa batas atas Any. */
fun <T1, T2> T1.isSubClassOf(base: T2): Boolean{
    val thisClass= this?.clazz
    return if(thisClass != null){
        base?.clazz?.isSubclassOf(thisClass) == true
    } else false
}


/*
==========================
Inheritance Tree
==========================
 */

/**
 * [onlyExtendedClass] true jika iterasi hanya dilakukan pada super class dg jenis Class, bkn Interface.
 * Fungsi ini mendefinisikan supertype sbg interface berdasarkan refleksi Java.
 *
 * Fungsi ini msh bergantung dari Java Reflection.
 */
fun KClass<*>.supertypesJvm(onlyExtendedClass: Boolean= false): NestedSequence<KType>{
    return object : NestedSequence<KType>{
        override fun iterator(): NestedIteratorSimple<KType>
                = object: NestedIteratorSimpleImpl<KType>(supertypes){

            override fun getOutputIterator(nowInput: KType): Iterator<KType>? {
                return if((nowInput.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                else (nowInput.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
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
        override fun iterator(): NestedIteratorSimple<KType>
                = object: NestedIteratorSimpleImpl<KType>(supertypes){

            override fun getOutputIterator(nowInput: KType): Iterator<KType>? {
                return if((nowInput.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                else (nowInput.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }

            override fun skip(now: KType): Boolean
                    = ((now.classifier as? KClass<*>)?.findAnnotation<Interface>() != null)
                    && onlyExtendedClass
        }
    }
}

val KClass<*>.supertypesTree: NestedSequence<KType>
    get()= object : NestedSequence<KType>{
        override fun iterator(): NestedIteratorSimple<KType>
                = object: NestedIteratorSimpleImpl<KType>(supertypes){
            override fun getOutputIterator(nowInput: KType): Iterator<KType>? {
                return if((nowInput.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                else (nowInput.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }
        }
    }
val KClass<*>.superclassesTree: NestedSequence<KClass<*>>
    get()= object : NestedSequence<KClass<*>>{
        override fun iterator(): NestedIteratorSimple<KClass<*>>
                = object: NestedIteratorSimpleImpl<KClass<*>>(superclasses){
            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KClass<*>>? {
                return if(nowInput.simpleName == K_CLASS_BASE_NAME) null
                else nowInput.superclasses.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }
        }
    }

/** Sama seperti [supertypesTree] namun termasuk `this.extension` [KClass]. */
val KClass<*>.typesTree: NestedSequence<KType>
    get()= object : NestedSequence<KType>{
        override fun iterator(): NestedIteratorSimple<KType>
                = object: NestedIteratorSimpleImpl<KType>(this@typesTree.createType()){
            override fun getOutputIterator(nowInput: KType): Iterator<KType>? {
                return if((nowInput.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                else (nowInput.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }
        }
    }
/** Sama seperti [superclassesTree] namun termasuk `this.extension` [KClass]. */
val KClass<*>.classesTree: NestedSequence<KClass<*>>
    get()= object : NestedSequence<KClass<*>>{
        override fun iterator(): NestedIteratorSimple<KClass<*>>
                = object: NestedIteratorSimpleImpl<KClass<*>>(newIterator(this@classesTree)){
            override val tag: String
                get() = "classesTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KClass<*>>? {
                return if(nowInput.simpleName == K_CLASS_BASE_NAME) null
                else nowInput.superclasses.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }
        }
    }

val KClass<*>.sealedSubClassesTree: NestedSequence<KClass<*>>
    get()= object : NestedSequence<KClass<*>>{
        override fun iterator(): NestedIteratorSimple<KClass<*>>
                = object: NestedIteratorSimpleImpl<KClass<*>>(this@sealedSubClassesTree){
            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KClass<*>>?
                    = nowInput.sealedSubclasses.iterator()
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


/**
 * Digunakan untuk mengambil nama qualified dari turunan sealed class.
 * Qualified name yg diambil dg pola berikut <Super>...<Class> dg super merupakan superclass.
 * Bagian paling depan dari qualified name adalah nama class dg kata kunci sealed yg paling dekat
 * dg this [KClass] tempat fungsi ini dipanggil.
 *
 * Fungsi ini msh bergantung pada Java Reflection.
 *
 * [isQualifiedName] -true jika nama yg diambil adalah nama lengkap dimulai dari sealed super class
 *                    hingga kelas ini yg dipidahkan oleh titik (.).
 *                   -false jika nama yg diambil hanyalah nama kelas ini.
 * @return -null jika kelas ini gak punya [KClass.simpleName]
 *         -[KClass.simpleName] jika ternyata kelas ini gak punya sealed super class.
 */
fun KClass<*>.getSealedClassName(isQualifiedName: Boolean= true): String?{
//    Log.e("getSealedClassName", ".getSealedClassName() MAULAI")
    return this.simpleName.notNullTo { thisName ->
        var thisNameRes= thisName
        if(isQualifiedName){
//            Log.e("getSealedClassName", ".getSealedClassName() QUALIFIED MAULAI")
            var superName= ""
            var isSealedSuperFound= false
            for(supertype in this.supertypesJvm(true)){
                val clazz= (supertype.classifier as? KClass<*>)
//                if(clazz == Any::class) continue
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
//        println("getSealedClassName(): thisNameRes= $thisNameRes")
//        Log.e("getSealedClassName", "thisNameRes= $thisNameRes")
        thisNameRes
    }
}



/*
==========================
Properties Tree
==========================
 */

/**
 * Properti yg mengembalikan [NestedSequence] yg berisi semua properti yg
 * terdapat di `this.extension` [KClass]. Sequence juga termasuk properti dari properti jika tipenya
 * berupa object.
 */
val KClass<*>.nestedPropertiesTree: NestedSequence<KProperty1<*, *>>
    get()= object : NestedSequence<KProperty1<*, *>>{
        override fun iterator(): NestedIteratorSimple<KProperty1<*, *>>
            = object: NestedIteratorSimpleImpl<KProperty1<*, *>>(memberProperties){
            override fun getOutputIterator(nowInput: KProperty1<*, *>): Iterator<KProperty1<*, *>>?
                = (nowInput.returnType.classifier as? KClass<*>)?.memberProperties?.iterator()
        }
    }
/**
 * Properti yg mengembalikan [NestedSequence] yg berisi semua properti yg
 * terdapat di this [KClass]. Sequence juga termasuk properti dari properti jika tipenya
 * berupa object.
 */
val KClass<*>.declaredPropertiesTree: NestedSequence<KProperty1<*, *>>
    get()= object : NestedSequence<KProperty1<*, *>>{
        override fun iterator(): NestedIterator<KClass<*>, KProperty1<*, *>>
            = object: NestedIteratorImpl<KClass<*>, KProperty1<*, *>>(classesTree.iterator()){
            override val tag: String
                get() = "declaredPropertiesTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KProperty1<*, *>>?
                = nowInput.declaredMemberProperties.iterator()
            override fun getInputIterator(nowOutput: KProperty1<*, *>): Iterator<KClass<*>>? = null
        }
    }


/** Sama dengan [declaredPropertiesTree], ditambah isi dari properti jika properti merupakan object. */
val KClass<*>.nestedDeclaredPropertiesTree: NestedSequence<KProperty1<*, *>>
    get()= object : NestedSequence<KProperty1<*, *>>{
        override fun iterator(): NestedIterator<KClass<*>, KProperty1<*, *>>
            = object: NestedIteratorImpl<KClass<*>, KProperty1<*, *>>(classesTree.iterator()){
            override val tag: String
                get() = "nestedDeclaredPropertiesTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KProperty1<*, *>>?
                = nowInput.declaredMemberProperties.iterator()
            override fun getInputIterator(nowOutput: KProperty1<*, *>): Iterator<KClass<*>>?
                = (nowOutput.returnType.classifier as? KClass<*>)?.classesTree?.iterator()
        }
    }

/** Sama dengan [declaredPropertiesTree], namun tidak termasuk abstract property. */
val KClass<*>.implementedPropertiesTree: NestedSequence<KProperty1<*, *>>
    get()= object : NestedSequence<KProperty1<*, *>>{
        override fun iterator(): NestedIterator<KClass<*>, KProperty1<*, *>>
                = object: NestedIteratorImpl<KClass<*>, KProperty1<*, *>>(classesTree.iterator()){
            override val tag: String
                get() = "declaredPropertiesTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KProperty1<*, *>>?
               = nowInput.declaredMemberProperties.iterator()
            override fun getInputIterator(nowOutput: KProperty1<*, *>): Iterator<KClass<*>>? = null
            override fun skip(now: KProperty1<*, *>): Boolean = now.isAbstract
        }
    }

/** Sama dengan [nestedDeclaredPropertiesTree], namun tidak termasuk abstract property. */
val KClass<*>.nestedImplementedPropertiesTree: NestedSequence<KProperty1<*, *>>
    get()= object : NestedSequence<KProperty1<*, *>>{
        override fun iterator(): NestedIterator<KClass<*>, KProperty1<*, *>>
            = object: NestedIteratorImpl<KClass<*>, KProperty1<*, *>>(classesTree.iterator()){
            override val tag: String
                get() = "nestedDeclaredPropertiesTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KProperty1<*, *>>?
                = nowInput.declaredMemberProperties.iterator()

            override fun getInputIterator(nowOutput: KProperty1<*, *>): Iterator<KClass<*>>?
                = (nowOutput.returnType.classifier as? KClass<*>)?.classesTree?.iterator()

            override fun skip(now: KProperty1<*, *>): Boolean = now.isAbstract
        }
    }


/*
==========================
Properties Tree - Value
==========================
 */

/** Mengambil semua properti berserta nilainya dari `this.extension` termasuk yg `private`. */
val Any.implementedPropertiesValueMap: Sequence<Pair<KProperty1<*, *>, Any?>>
    get()= object : Sequence<Pair<KProperty1<*, *>, Any?>>{
        override fun iterator(): Iterator<Pair<KProperty1<*, *>, Any?>>
            = object: Iterator<Pair<KProperty1<*, *>, Any?>>{
            private val declaredPropsItr= this@implementedPropertiesValueMap::class.declaredMemberProperties.iterator()

            override fun hasNext(): Boolean = declaredPropsItr.hasNext()

            override fun next(): Pair<KProperty1<*, *>, Any?> {
                val prop= declaredPropsItr.next()
                val value= prop.getter.forcedCall(this@implementedPropertiesValueMap)
                return Pair(prop, value)
            }
        }
    }
/** Sama dengan [implementedPropertiesValueMap], namun hanya nilai tanpa mapping dg [KProperty]. */
val Any.implementedPropertiesValue: Sequence<Any?>
    get()= object : Sequence<Any?>{
        override fun iterator(): Iterator<Any?>
            = object: Iterator<Any?>{
            private val declaredPropsItr= this@implementedPropertiesValue::class.declaredMemberProperties.iterator()

            override fun hasNext(): Boolean = declaredPropsItr.hasNext()

            override fun next(): Any? {
                val prop= declaredPropsItr.next()
                return prop.getter.forcedCall(this@implementedPropertiesValue)
            }
        }
    }

/** Sama dengan [implementedPropertiesValueMap], beserta properti dari properti. */
val Any.nestedImplementedPropertiesValueMap: NestedSequence<Pair<KProperty1<*, *>, Any?>>
    get()= object : NestedSequence<Pair<KProperty1<*, *>, Any?>>{
        override fun iterator(): NestedIteratorSimple<Pair<KProperty1<*, *>, Any?>>
            = object : NestedIteratorSimpleImpl<Pair<KProperty1<*, *>, Any?>>(
                this@nestedImplementedPropertiesValueMap.implementedPropertiesValueMap.iterator()
            ){
            override val tag: String
                get() = "nestedImplementedPropertiesValueMap"

            override fun getOutputIterator(nowInput: Pair<KProperty1<*, *>, Any?>): Iterator<Pair<KProperty1<*, *>, Any?>>?
                = nowInput.second?.implementedPropertiesValueMap?.iterator()
        }
    }

/*
/** Sama dengan [nestedDeclaredPropertiesTree], namun tidak termasuk abstract property. */
val Any.implementedPropertiesValueMapTree: NestedSequence<Pair<KProperty1<*, *>, Any?>>
    get()= object: NestedSequence<Pair<KProperty1<*, *>, Any?>>{
        override fun iterator(): NestedIterator<Any?, Pair<KProperty1<*, *>, Any?>>
            = object: NestedIteratorImpl<Any?, Pair<KProperty1<*, *>, Any?>>(
                this@implementedPropertiesValueMapTree::class.declaredPropertiesTree.iterator()
            ){
            override fun getOutputIterator(nowInput: Any?): Iterator<Pair<KProperty1<*, *>, Any?>>? {
                TODO("Not yet implemented")
            }

            override fun getInputIterator(nowOutput: Pair<KProperty1<*, *>, Any?>): Iterator<Any?>? {
                TODO("Not yet implemented")
            }
        }
    }
*/
///*
/** Sama dg [implementedPropertiesValueMap], beserta semua properti `private` dari superclass. */
val Any.implementedPropertiesValueMapTree: Sequence<Pair<KProperty1<*, *>, Any?>>
    get()= object : Sequence<Pair<KProperty1<*, *>, Any?>>{
        override fun iterator(): Iterator<Pair<KProperty1<*, *>, Any?>>
                = object: Iterator<Pair<KProperty1<*, *>, Any?>>{
            private val declaredPropsItr= this@implementedPropertiesValueMapTree::class.implementedPropertiesTree.iterator()

            override fun hasNext(): Boolean
                = declaredPropsItr.asNotNullTo { itr: NestedIteratorImpl<*, *> -> itr.hasNext(true) }
                    ?: declaredPropsItr.hasNext()

            override fun next(): Pair<KProperty1<*, *>, Any?> {
                val prop= declaredPropsItr.next()
//                prine("implementedPropertiesValueMapTree next() prop= $prop")
                val value= prop.getter.forcedCall(this@implementedPropertiesValueMapTree)
//                prine("implementedPropertiesValueMapTree next() value= $value")
                return Pair(prop, value)
            }
        }
    }
// */

///*
/** Sama dg [implementedPropertiesValueMapTree], beserta semua properti dari properti, termasuk yg `private`. */
val Any.nestedImplementedPropertiesValueMapTree: NestedSequence<Pair<KProperty1<*, *>, Any?>>
    get()= object: NestedSequence<Pair<KProperty1<*, *>, Any?>>{
        override fun iterator(): NestedIteratorSimple<Pair<KProperty1<*, *>, Any?>>
            = object: NestedIteratorSimpleImpl<Pair<KProperty1<*, *>, Any?>>(
                this@nestedImplementedPropertiesValueMapTree.implementedPropertiesValueMapTree.iterator()
            ){
            override val tag: String
                get() = "nestedImplementedPropertiesValueMapTree"

            override fun getOutputIterator(nowInput: Pair<KProperty1<*, *>, Any?>): Iterator<Pair<KProperty1<*, *>, Any?>>?
                = nowInput.second?.implementedPropertiesValueMapTree?.iterator()
        }
    }
// */





/*
/**
 * Properti yg mengembalikan [NestedSequence_] yg berisi semua properti yg
 * terdapat di this [KClass]. Sequence juga termasuk properti dari properti jika tipenya
 * berupa object.
 */
val KClass<*>.propertiesTree_: NestedSequence_<KProperty1<*, *>>
    get()= object : NestedSequence_<KProperty1<*, *>>{
        override fun iterator(): NestedIterator_<KProperty1<*, *>>
            = object: NestedIterator_Impl<KProperty1<*, *>>(memberProperties){
            override fun getIterator(now: KProperty1<*, *>): Iterator<KProperty1<*, *>>? {
                val res= (now.returnType.classifier as? KClass<*>)?.memberProperties?.iterator()
                val nulll= (now.returnType.classifier as? KClass<*>) == null
                prind("propertiesTree_simple res= $res res.hasNext()= ${res?.hasNext()} nulll= $nulll")
                return res
            }
        }
    }
 */

/*
/**
 * Properti yg mengembalikan [NestedSequence] yg berisi semua properti yg
 * terdapat di this [KClass]. Sequence juga termasuk properti dari properti jika tipenya
 * berupa object.
 */
val KClass<*>.nestedDeclaredPropertiesTree: NestedSequence<KProperty1<*, *>>
    get()= object : NestedSequence<KProperty1<*, *>>{
        override fun iterator(): NestedIteratorSimple<KProperty1<*, *>>
            = object: NestedIteratorSimpleImpl<KProperty1<*, *>>(declaredMemberProperties){
            override fun getOutputIterator(now: KProperty1<*, *>): Iterator<KProperty1<*, *>>?
                = (now.returnType.classifier as? KClass<*>)?.declaredMemberProperties?.iterator()
        }
    }
 */
/*
/**
 * Properti yg mengembalikan [NestedSequence] yg berisi semua properti yg
 * terdapat di this [KClass]. Sequence juga termasuk properti dari properti jika tipenya
 * berupa object.
 *
 * [NestedSequence] dg typeParam adalah Map<String, Any?> dg key merupakan nama full-qualified name
 * dari sebuah KProperty. Nama tersebut memiliki format <this.Class>.<KProperty.name>.<KProperty.name>.<KProperty.name>.
 */
val KClass<*>.propertiesTreeValue: LazyHashMap<KProperty1<*, *>, Any?>
    get()= LazyHashMap(
        object : Sequence<Pair<KProperty1<*, *>, Any?>>{
            override fun iterator(): Iterator<Pair<KProperty1<*, *>, Any?>>
                = object : Iterator<Pair<KProperty1<*, *>, Any?>>{
                private val propertyTreeItr= nestedPropertiesTree.iterator()
                override fun hasNext(): Boolean = propertyTreeItr.hasNext()

                override fun next(): Pair<KProperty1<*, *>, Any?> {
                    val prop= propertyTreeItr.next()
                    return Pair(prop, )
                    //<15 Juli 2020> => Blum nemu caranya ngambil string <this.Class>.<KProperty.name>.<KProperty.name>.<KProperty.name>..
                }
            }
        }
    )

fun <T: Any> T.clone(constructorParamValFunc: ((KParameter) -> Any?)?= null): T{
    val newInstance= new(this::class, constructorParamValFunc)

}

// */

// */



/*
==========================
New Instance
==========================
 */

/**
 * Digunakan untuk meng-clone object `this.extension` [T] sehingga menciptakan instance baru dg
 * nilai properti yg sama.
 *
 * @param [isDeepClone] `true` jika seluruh nilai properti yg berupa `object` di-instantiate
 *   menjadi `instance` yg baru. Operasi deep-clone juga berlaku terhadap properti yg dimiliki properti.
 *   [isDeepClone] `false` jika clone hanya dilakukan pada `this.extension` tidak termasuk properti.
 */
fun <T: Any> T.clone(isDeepClone: Boolean= true, constructorParamValFunc: ((KClass<*>, KParameter) -> Any?)?= null): T{
    val valueMapTree= implementedPropertiesValueMapTree
    //Berguna untuk mengecek apakah `KProperty` merupakan properti dg mengecek kesamaannya dg `KParameter` di contrustor.
    val constr= this::class.leastRequiredParamConstructor

    val newInstance= new(this::class){ paramOfNew ->
        (constructorParamValFunc ?: { clazz, param ->
//            prine("clazz.simpleName = ${clazz.simpleName} param= $param")
            if(constr.parameters.find { it == param } != null){
                valueMapTree.find { pairValueMap -> param.isPropertyLike(pairValueMap.first) }
                    .notNullTo { it.second }
            } else null
        }).invoke(this::class, paramOfNew)
    } ?: return this

//    prine("clone() sampai sini")

    for(valueMap in valueMapTree){
        val prop= valueMap.first
        if(prop is KMutableProperty1<*, *>){
            val value= valueMap.second
//            prine("clone() prop= $prop value= $value value.clazz.isPrimitive= ${value?.clazz?.isPrimitive} bool=${!isDeepClone || value == null || value.clazz.isPrimitive}")
            prop.asNotNull { mutableProp: KMutableProperty1<T, Any?> ->
//                prine(" masukkkk... clone() prop= $prop value= $value value.clazz.isPrimitive= ${value?.clazz?.isPrimitive} bool=${!isDeepClone || value == null || (value.clazz.isPrimitive && constr.parameters.find { it.isPropertyLike(mutableProp, true) } == null)}")
                if(!isDeepClone || value == null || value.clazz.isPrimitive){
                    if(constr.parameters.find { it.isPropertyLike(mutableProp, true) } == null)
                    //Jika ternyata [mutableProp] terletak di konstruktor dan sudah di-instansiasi,
                    // itu artinya programmer sudah memberikan definisi nilainya sendiri saat intansiasi,
                    // maka jangan salin nilai lama [mutableProp] ke objek yg baru di-intansiasi.
                        mutableProp.forcedSet(newInstance, valueMap.second)
                } else
                    mutableProp.forcedSet(newInstance, value.clone(true, constructorParamValFunc))
            }
        }
    }
    return newInstance
}


/**
 * <14 Juli 2020> => Versi baru fungsi inline yg kecil.
 */
inline fun <reified T: Any> new(constructorParamClass: Array<KClass<*>>?= null, noinline defParamValFunc: ((param: KParameter) -> Any?)?= null): T?
    = new(T::class, constructorParamClass, defParamValFunc)

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
 * Fungsi ini melakukan instansiasi objek baru dg menggunakan konstruktor yg memiliki parameter
 * dg tipe data sesuai dg [constructorParamClass]. Jika [constructorParamClass] == null, maka
 * scr default fungsi akan mencari konstruktor dg jumlah parameter paling sedikit yg tersedia.
 * Parameter opsional tidak dihitung.
 *
 * <14 Juli 2020> => Tidak jadi inline karena fungsi ini besar. Sbg gantinya, fungsi [new] di atas
 *   adalah inline namun dg kode yg kecil.
 */
fun <T: Any> new(clazz: KClass<T>, constructorParamClass: Array<KClass<*>>?= null, defParamValFunc: ((param: KParameter) -> Any?)?= null): T?{
    if(clazz.isPrimitive)
        return defaultPrimitiveValue(clazz)

    //1. Cari constructor dg parameter tersedikit.
/*
    val constrs= clazz.constructors //T::class.constructors
    var constr: KFunction<T>? = null //constrs.first()
    var minParams= Int.MAX_VALUE
    for(cons in constrs){
        if(minParams > cons.parameters.size){
            constr= cons
            minParams= cons.parameters.size
        }
    }
 */
    val constr = if(constructorParamClass.isNullOrEmpty()) clazz.leastRequiredParamConstructor
        else clazz.findConstructorWithParam(*constructorParamClass)

    val params=  constr.parameters
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
                    prine("newInstance(): paramName= ${param.name} nilai param tidak terdefinisi!")
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


/*
fun <T: Any> checkTypeSafety(any: T): Boolean{

}
 */