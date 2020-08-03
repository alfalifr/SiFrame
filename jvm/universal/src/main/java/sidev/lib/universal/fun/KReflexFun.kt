package sidev.lib.universal.`fun`

//import sidev.lib.universal._cob.isInstance
//import sidev.lib.universal._cob.isSubtypeOf
import sidev.lib.universal.`val`.StringLiteral
import sidev.lib.universal.`val`.SuppressLiteral
import sidev.lib.universal.annotation.Interface
import sidev.lib.universal.annotation.renamedName
import sidev.lib.universal.exception.ClassCastExc
import sidev.lib.universal.exception.NonInstantiableTypeExc
import sidev.lib.universal.exception.TypeExc
import sidev.lib.universal.exception.UndefinedDeclarationExc
import sidev.lib.universal.structure.collection.iterator.*
import sidev.lib.universal.structure.collection.sequence.NestedSequence
import sidev.lib.universal.structure.data.InferredType
import sidev.lib.universal.structure.data.LinkedTypeParameter
import sidev.lib.universal.structure.data.TypedValue
import sidev.lib.universal.structure.data.withType
import sidev.lib.universal.structure.type.Null
import java.io.Serializable
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible


const val K_CLASS_BASE_NAME= "KClassImpl"
const val K_FUNCTION_CONSTRUCTOR_NAME_PREFIX= "fun <init>"
val K_PROPERTY_ARRAY_SIZE_STRING= Array<Any>::size.toString()
val K_CLASS_ENUM_STRING= Enum::class

/*
fun Any.findSealedSubclass(func: ()): Class{
    this::class.isInstance()
}
 */



/*
==========================
KType - KTypeParameter - KTypeProjection
==========================
 */
val <T: Any> KClass<T>.arrayTypeArgument: KTypeProjection?
    get(){
        if(!isArray) return null

        var variance= typeParameters.firstOrNull()?.variance
        val classifier= if(toString() == Array<Any>::class.toString()) typeParameters.first()
        else {
            variance= KVariance.INVARIANT
            when(this){
                IntArray::class -> Int::class
                LongArray::class -> Long::class
                FloatArray::class -> Float::class
                DoubleArray::class -> Double::class
                CharArray::class -> Char::class
                ShortArray::class -> Short::class
                BooleanArray::class -> Boolean::class
                ByteArray::class -> Byte::class
                else -> return null
            }
        }
        return KTypeProjection(variance, classifier.createType())
    }


/**
 * Mengambil [KTypeProjection] dari `this.extension` [KTypeParameter] dg [KType]
 * dari kelas sesungguhnya yg disimpulkan dari nilai properti yg dimiliki oleh [owner].
 */
fun KTypeParameter.getClassProjectionIn(owner: Any): KTypeProjection {
    if(owner::class.isArray && this in owner::class.typeParameters){
        return if(owner::class.isObjectArray) KTypeProjection(variance, inferElementType(owner as Array<*>))
        else owner::class.arrayTypeArgument
            ?: throw UndefinedDeclarationExc(undefinedDeclaration = owner::class, detailMsg = "Terdapat sebuah array primitif, namun tidak terdefinisi dalam bahasa Kotlin.")
    }
    for(prop in owner::class.nestedDeclaredMemberPropertiesTree){
        if((prop.returnType.classifier as? KClass<*>)?.isArray == true){ //Ada kasus spesial, yaitu array, di mana elemennya tidak ada pada properti.
            if(prop.returnType.arguments.find { it.type?.classifier == this } != null)
                prop.forcedGet(owner).notNull { array ->
                    (prop.returnType.classifier as KClass<*>).typeParameters.first()
                        .getClassProjectionIn(array).also { if(it != KTypeProjection.STAR) return it }
                }
        } else if(this == prop.returnType.classifier)
            prop.forcedGet(owner).notNull { valueProjection ->
//                    prine("owner= $owner prop= $prop valueProjection= $valueProjection class= ${valueProjection::class}")
                val typeParamProjectionList= ArrayList<KTypeProjection>()
//                for(ownerTypeParam in owner::class.typeParameters)
                for(typeParam in valueProjection::class.typeParameters)
                    typeParam.getClassProjectionIn(valueProjection)
                        .notNull { typeParamProjectionList += it }
//                            .isNull { typeParamProjectionList += KTypeProjection.STAR }
                val type= valueProjection::class.createType(typeParamProjectionList, prop.returnType.isMarkedNullable)
                return KTypeProjection(variance, type)
            }
    }
    prine("""KTypeParameter.getProjectionIn() -> Tidak bisa mendapatkan proyeksi dari KTypeParameter: "$this", proyeksi akhir "KTypeProjection.STAR".""")
    return KTypeProjection.STAR
}

/** Mengambil hubungan nested antar [KTypeParameter] dalam satu [KClass]. */
fun KTypeParameter.getTypeParameterLink(clazz: KClass<*>): LinkedTypeParameter? {
    val targetTypeParams= clazz.typeParameters
    if(this !in targetTypeParams) return null

    val linkedTypeParamList= ArrayList<KTypeParameter>()
    for(typeArg in nestedUpperBoundTypeArguments){
        val typeParamItr= typeArg.type?.classifier
        if(typeParamItr in targetTypeParams && typeParamItr !in linkedTypeParamList)
            linkedTypeParamList += typeParamItr as KTypeParameter
    }
    return LinkedTypeParameter(this, linkedTypeParamList)
}

/** Sequence semua nested [KTypeProjection] dari `upperBounds`. `this.extension` upperBounds juga disertakan. */
val KTypeParameter.nestedUpperBoundTypeArguments: NestedSequence<KTypeProjection>
    get()= object : NestedSequence<KTypeProjection>{
        private val initUpperBounds= this@nestedUpperBoundTypeArguments.upperBounds
        override fun iterator(): NestedIterator<KType, KTypeProjection>
                = object : NestedIteratorImpl<KType, KTypeProjection>(initUpperBounds.iterator()){
            private var initOutputEmitionLimit= initUpperBounds.size
            private var emissionNumber= 0
            override fun getOutputIterator(nowInput: KType): Iterator<KTypeProjection>?
                    = if(emissionNumber++ < initOutputEmitionLimit) {
                newIteratorSimple(KTypeProjection(this@nestedUpperBoundTypeArguments.variance, nowInput))
            } else nowInput.arguments.iterator()
            override fun getInputIterator(nowOutput: KTypeProjection): Iterator<KType>?
                    = if(nowOutput.type != null) newIteratorSimple(nowOutput.type!!) else null
        }
    }

/**
 * Sequence semua nested [KTypeProjection] yg ada di dalam `this.extension`.
 * Properti ini mirip dg [nestedUpperBoundTypeArguments], namun yg dimaksud [KTypeProjection] di sini
 * adalah proyeksi yg sesungguhnya, bkn merupakan yg dideklarasikan pada sebuah kelas (upperBounds).
 */
val KTypeProjection.nestedProjections: NestedSequence<KTypeProjection>
    get()= object : NestedSequence<KTypeProjection>{
        override fun iterator(): NestedIteratorSimple<KTypeProjection>
                = object : NestedIteratorSimpleImpl<KTypeProjection>(this@nestedProjections){
            override fun getOutputIterator(nowInput: KTypeProjection): Iterator<KTypeProjection>?
                    = nowInput.type?.arguments?.iterator()
        }
    }
/**
 * Sama dg [KTypeProjection.nestedProjections]. `this.extension` juga diikutkan namun sbg [simpleTypeProjection].
 */
val KType.nestedProjections: NestedSequence<KTypeProjection>
    get()= object : NestedSequence<KTypeProjection>{
        override fun iterator(): NestedIteratorSimple<KTypeProjection>
                = object : NestedIteratorSimpleImpl<KTypeProjection>(this@nestedProjections.simpleTypeProjection){
            override fun getOutputIterator(nowInput: KTypeProjection): Iterator<KTypeProjection>?
                    = nowInput.type?.arguments?.iterator()
        }
    }
/**
 * Sama dg [KTypeProjection.nestedProjections]. `this.extension` juga diikutkan namun sbg [simpleTypeProjection].
 */
val KType.nestedProjectionsTree: NestedSequence<KTypeProjection>
    get()= object : NestedSequence<KTypeProjection>{
        override fun iterator(): NestedIteratorSimple<KTypeProjection>
                = object : NestedIteratorSimpleImpl<KTypeProjection>(this@nestedProjectionsTree.typesTree.map { it.simpleTypeProjection }.iterator()){
            override fun getOutputIterator(nowInput: KTypeProjection): Iterator<KTypeProjection>?
                    = nowInput.type?.arguments?.iterator()
        }
    }
val KType.simpleTypeProjection: KTypeProjection
    get()= KTypeProjection(KVariance.INVARIANT, this)


val KClass<*>.nestedTypeParameters: NestedSequence<KTypeParameter>
    get()= object : NestedSequence<KTypeParameter>{
        override fun iterator(): NestedIteratorSimple<KTypeParameter>
                = object : NestedIteratorSimpleImpl<KTypeParameter>(this@nestedTypeParameters.typeParameters){
            override fun getOutputIterator(nowInput: KTypeParameter): Iterator<KTypeParameter>? {
                val upperBoundSeq= nowInput.upperBounds.asSequence().map { it.classifier }
                val typeParamSeq= upperBoundSeq.filter { it is KTypeParameter } as Sequence<KTypeParameter>
                val classSeq= upperBoundSeq.filter { it is KClass<*> } as Sequence<KClass<*>>
                val typeSeqFromClass= classSeq.map { it.typeParameters.asSequence() }.toLinear()
                return (typeParamSeq + typeSeqFromClass).iterator()
            }
        }
    }

/**
 * Mengambil [InferredType] yg berisi [KType] yg disimpulkan dari properti yg ada di `this.extension` [Any].
 * Properti ini juga dapat menyimpulkan tipe dari null instance, namun tipe yg disimpulkan menjadi [Null?].
 */
val Any?.inferredType: InferredType
    get(){
        if(this == null)
            return Null.type.asInferredType()

        val typeParams= this::class.typeParameters

        val typeParamProjectionList= typeParams.map{ it.getClassProjectionIn(this) } as ArrayList
        typeParamProjectionList.filterIndex { it.value == KTypeProjection.STAR }
            .notEmpty { indexedList -> //Jika terdapat star-projection, maka cari apakah projection ada terdapat pada type-param lainnya.
                val indexList= indexedList.map { it.index } as ArrayList //Untuk mempermudah pengecekan apakah indeks iterasi merupakan indeks untuk star-projection.

                val linkedTypeParamsSeq=
                    typeParams.asSequence() //.filterIndexed{ i, typeParam -> i !in indexList }
                        .map { it.getTypeParameterLink(this::class)!! }.asCached()
                //Digunakan untuk mencari hubungan type-param yg star-projected dg type-param lain.
                // Dalam konteks ini, nested pada type-param lain.

                var resolvedProjectionCount= 0
                //Pencairan pada type-param lain di dalam while dilatar-belakangi karena urutan nested tidak urut sesuai [KClass.typeParameters].
                while(resolvedProjectionCount in 0 until indexList.size){
                    resolvedProjectionCount= -1
                    val itrLimit= indexList.size
                    var itrProgress= 0

                    for(starProjectedIndex in typeParams.indices){
                        if(itrProgress >= itrLimit) break //Jika indeks iterasi udah melebihi jumlah star-projection, maka akhiri iterasi.
                        if(starProjectedIndex !in indexList) continue
                        itrProgress++

                        val starProjectedTypeParam= typeParams[starProjectedIndex] //Cari type-param yg star-projected.
                        //linkedTypeParamsSeq[starProjectedIndex].typeParam
                        linkedTypeParamsSeq.findIndexed {
                            it.value.upperBoundTypeParam.contains(starProjectedTypeParam) //Cari apakah type-param yg star-projected nested di type-param lain.
                                    && it.index !in indexList // Tentunya nested pada type-param yg bkn star-projected.
                        }.notNull {
                            val foundLinkedTypeParam= it.value
                            val typeParam= foundLinkedTypeParam.typeParam //Type-param lain tempat nested type-param yg star-projected.
                            val projectionsItr= typeParamProjectionList[it.index].nestedProjections.iterator()
                            //Iterator untuk projection pada type-param lain.

                            for((upperBoundIndexItr, upperBoundProjection) in typeParam.nestedUpperBoundTypeArguments.withIndex()){
                                if(upperBoundProjection.type?.classifier == starProjectedTypeParam){
                                    var itr= -1
                                    while(++itr < upperBoundIndexItr){ projectionsItr.next() } //while digunakan ketimbang CachedSequence agar tidak membebani heap.
                                    val realProjectionOfStarProjectedTypeParam= projectionsItr.next()
                                    typeParamProjectionList[starProjectedIndex]= realProjectionOfStarProjectedTypeParam
                                    resolvedProjectionCount++ //+= if(resolvedProjectionCount == -1) 2 else 1
                                    indexList -= starProjectedIndex
                                    prinr("""Berhasil menyimpulkan starProjectedTypeParam: "$starProjectedTypeParam" proyeksi: "$realProjectionOfStarProjectedTypeParam".""")
                                    break
                                }
                            }
                        }.isNull { prine("""Tidak bisa menyimpulkan starProjectedTypeParam: "$starProjectedTypeParam" karena tidak tersedia info proyeksi dari type param lain.""") }
                    }
                }
            }
        return this::class.createType(typeParamProjectionList).asInferredType()
    }

/** Mengambil [InferredType] yg berisi [KType] yg disimpulkan dari properti yg ada di `this.extension` [Any]. */
@Deprecated("Tidak dapat meng-infer nested type-projection", ReplaceWith("Any.inferredType"))
internal val Any.inferredType_old: InferredType
    get(){
        val typeParamProjectionList= this::class.typeParameters
            .map{ it.getClassProjectionIn(this) }
        return this::class.createType(typeParamProjectionList).asInferredType()
    }

/** Membungkus `this.extension` [KType] menjadi [InferredType]. */
fun KType.asInferredType(): InferredType = InferredType(this)

/**
 * Menentukan apakah `this.extension` [KTypeParameter] merupakan subtype dari [base]
 * berdasarkan [KTypeParameter.upperBounds]. Fungsi ini menggunakan upperBounds karena
 * tidak mungkin membandingkan menggunakan tipe sesungguhnya mengingat tidak terdapat type-projection.
 */
fun KTypeParameter.isUpperBoundSubTypeOf(base: KTypeParameter): Boolean{
    return when(val baseBound= base.upperBounds.first().classifier){
        is KClass<*> -> isUpperBoundSubTypeOf(baseBound)
        is KTypeParameter -> isUpperBoundSubTypeOf(baseBound)
        else -> false
    }
}
fun KTypeParameter.isUpperBoundSuperTypeOf(derived: KTypeParameter): Boolean{
    return when(val baseBound= derived.upperBounds.first().classifier){
        is KClass<*> -> isUpperBoundSuperTypeOf(baseBound)
        is KTypeParameter -> isUpperBoundSuperTypeOf(baseBound)
        else -> false
    }
}

fun KTypeParameter.isUpperBoundSubTypeOf(base: KClass<*>): Boolean{
    return when(val thisBound= upperBounds.first().classifier){
        is KClass<*> -> thisBound.isSubclassOf(base)
        is KTypeParameter -> thisBound.isUpperBoundSubTypeOf(base)
        else -> false
    }
}
fun KTypeParameter.isUpperBoundSuperTypeOf(derived: KClass<*>): Boolean{
    return when(val thisBound= upperBounds.first().classifier){
        is KClass<*> -> thisBound.isSuperclassOf(derived)
        is KTypeParameter -> thisBound.isUpperBoundSuperTypeOf(derived)
        else -> false
    }
}

fun KClass<*>.isUpperBoundSubTypeOf(base: KTypeParameter): Boolean{
    return when(val baseBound= base.upperBounds.first().classifier){
        is KClass<*> -> isSubclassOf(baseBound)
        is KTypeParameter -> isUpperBoundSubTypeOf(baseBound)
        else -> false
    }
}
fun KClass<*>.isUpperBoundSuperTypeOf(base: KTypeParameter): Boolean{
    return when(val baseBound= base.upperBounds.first().classifier){
        is KClass<*> -> isSuperclassOf(baseBound)
        is KTypeParameter -> isUpperBoundSuperTypeOf(baseBound)
        else -> false
    }
}

/**
 * Menentukan pakah [any] memiliki tipe yg sama dg `this.extension` [KType].
 * Fungsi ini hanya menyocokan [KClass] dan nullability.
 * Untuk TypeParameter dan TypeProjection, pengecekan tidak mungkin dilakukan dari KClass.
 */
fun KType.isInstance(any: Any?): Boolean
        = any == null && isMarkedNullable || any!!.clazz == classifier


fun KClassifier.isUpperBoundSubTypeOf(base: KClassifier): Boolean{
    return when(this){
        is KClass<*> -> when(base){
            is KClass<*> -> isSubclassOf(base)
            is KTypeParameter -> isUpperBoundSubTypeOf(base)
            else -> false
        }
        is KTypeParameter -> when(base){
            is KClass<*> -> isUpperBoundSubTypeOf(base)
            is KTypeParameter -> isUpperBoundSubTypeOf(base)
            else -> false
        }
        else -> false
    }
}
fun KClassifier.isUpperBoundSuperTypeOf(derived: KClassifier): Boolean{
    return when(this){
        is KClass<*> -> when(derived){
            is KClass<*> -> isSuperclassOf(derived)
            is KTypeParameter -> isUpperBoundSuperTypeOf(derived)
            else -> false
        }
        is KTypeParameter -> when(derived){
            is KClass<*> -> isUpperBoundSuperTypeOf(derived)
            is KTypeParameter -> isUpperBoundSuperTypeOf(derived)
            else -> false
        }
        else -> false
    }
}

fun KType.isSameTypeAs(other: KType, includeNullability: Boolean= true): Boolean
        = classifier == other.classifier
        && arguments == other.arguments
        && (!includeNullability || isMarkedNullable == other.isMarkedNullable)

/**
 * Menentukan apakah `this.extension` [KType] merupakan subtype dari [base].
 * Definisi subtype pada fungsi ini berbeda sedikit dg [KType.isSubtypeOf] pada [kotlin.reflect.full],
 * yaitu:
 *  -> subtype jika `this.extension`.classifier merupakan subtype atau sama dg [base].classifier, dan
 *  -> `this.extension`.arguments merupakan subtype atau sama dg [base].arguments.
 */
fun KType.isSubTypeOf(base: KType): Boolean{
    val isClassifierSubtype= base.classifier != null && classifier?.isUpperBoundSubTypeOf(base.classifier!!) ?: false
    var isTypeArgSubtype= true

    val otherArgs= base.arguments

    for((i, typeArg) in arguments.withIndex()){
        val otherTypeArg= try{ otherArgs[i].type }
        catch (e: Exception){ break }
        isTypeArgSubtype= isTypeArgSubtype && (otherTypeArg != null && typeArg.type?.isSubtypeOf(otherTypeArg) ?: true)
    }
    return isClassifierSubtype && isTypeArgSubtype
}
fun KType.isSuperTypeOf(derived: KType): Boolean = derived.isSubTypeOf(this)

//fun InferredType.isSubtypeOf(other: InferredType): Boolean = type.isSubtypeOf(other.type)
//fun InferredType.isSupertypeOf(other: InferredType): Boolean = type.isSupertypeOf(other.type)

@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun getCommonClass(vararg classes: KClass<*>): KClass<*>{
    if(classes.isEmpty())
        throw NoSuchElementException("""Tidak bisa mendapatkan common-class dari list "classes" kosong.""")
    val usedClasses= classes.asSequence().filter { it != Null::class }.toList() //Kelas Null tidak dihitung karena hanya sbg representasi pada operasi [getCommonType].
    val superClassList= usedClasses.first().classesTree.withLevel().toMutableList().distinct()
        .sortedBy { it.level }.map { it.value } as MutableCollection<KClass<*>>

    for(i in 1 until usedClasses.size)
        superClassList intersect usedClasses[i].classesTree.toMutableList()
    return superClassList.first()
}
fun getCommonClass(vararg any: Any): KClass<*> = getCommonClass(*any.toArrayOf { it::class })

fun getCommonType(vararg types: KType): KType{
//    prine("==types= ${types.string}")
    if(types.isEmpty())
        throw NoSuchElementException("""Tidak bisa mendapatkan common-type dari list "types" kosong.""")

    val isMarkedNullable= types.find { it.isMarkedNullable }?.isMarkedNullable ?: false

    val usedTypes= types.asSequence().filter { it.classifier != Null::class }.toList()
    val classesArray= types.mapNotNull { it.classifier as? KClass<*> }.toTypedArray()
    val commonClass= getCommonClass(*classesArray)

    val commonTypeArgs: MutableList<KTypeProjection> = mutableListOf()
    if(commonClass.typeParameters.isNotEmpty()){ //Agar tidak terjadi komputasi mahal yg melibatkan [KTypeProjection.nestedProjectionsTree].
        val foundTypeArgs: MutableList<List<KTypeProjection>> = mutableListOf()
        for(typeArg in usedTypes.map { it.nestedProjectionsTree }.asSequence().toLinear()){
//        prine("typeArg= $typeArg getCommonType() -> typeArg.type?.classifier= ${typeArg.type?.classifier} commonClass= $commonClass")
            if(typeArg.type?.classifier == commonClass){
                foundTypeArgs += typeArg.type!!.arguments
//            break
            }
        }

//    prine("==== foundTypeArgs= $foundTypeArgs")
        //Jika foundTypeArgs == types, maka typeArg disimpulkan menjadi supertype dari commonClass.
        // Contoh kasus ini adalah Int, String, dan Double di mana common-class adalah Comparable<T> di mana T merupakan cyclic type-param.
        // Jika scr program, foundTypeArgs == [Int, String, Double] di mana jika dipanggil fungsi [getCommonType] ini lagi,
        // maka akan terjadi infinite loop.
        if(foundTypeArgs.toLinear().mapNotNull{ it.type }.contentEquals(usedTypes))
            commonTypeArgs += commonClass.supertypes[0].simpleTypeProjection
        else{
            for(typeArgs in foundTypeArgs.leveledIterator){
                if(typeArgs.isEmpty()) continue
                commonTypeArgs += getCommonType(*typeArgs.toArrayOf { it.type }).simpleTypeProjection
            }
        }
    }
    return commonClass.createType(commonTypeArgs, isMarkedNullable)
}
fun getCommonType(vararg any: Any?): KType = getCommonType(*any.toArrayOf { it.inferredType.type })


/**
 * Menyimpulkan common-class dari element pada sebuah array.
 * Penyimpulan melibatkan semua anggota array dg batas maksimal 200.
 */
fun <T: Any> inferElementClass(array: Array<T>): KClass<*>
    = getCommonClass(
        *(
            if(array.size <= 200) array
            else array.sliceArray(0 until 200)
        )
    )

/**
 * Menyimpulkan common-class dari element pada sebuah array.
 * Penyimpulan melibatkan semua anggota array dg batas maksimal 200.
 */
fun <T> inferElementType(array: Array<T>): KType
    = getCommonType(
        *(
            if(array.size <= 200) array
            else array.sliceArray(0 until 200)
        )
    )

/**
 * Menentukan apakah `this.extension` [KType] dapat diassign dengan nilai yg memiliki tipe [other].
 * Fungsi ini juga memperhitungkan type-arg variance.
 */
fun KType.isAssignableFrom(other: KType): Boolean{
///*
    prine("isAssignableFrom classifier= $classifier")
    prine("isAssignableFrom other.classifier= ${other.classifier}")
    prine("isAssignableFrom arguments= $arguments")
    prine("isAssignableFrom other.arguments= ${other.arguments}")
// */
    return when{
        (classifier as? KClass<*>)?.isPrimitiveArray == true -> {
            when {
                (other.classifier as? KClass<*>)?.isPrimitiveArray == true -> classifier == other.classifier
                (other.classifier as? KClass<*>)?.isObjectArray == true ->
                    (classifier as KClass<*>).arrayTypeArgument?.type == other.arguments.first().type //Karena array INVARIANT.
                else -> false
            }
        }
        (classifier as? KClass<*>)?.isObjectArray == true -> {
            when {
                (other.classifier as? KClass<*>)?.isObjectArray == true -> classifier == other.classifier
                (other.classifier as? KClass<*>)?.isPrimitiveArray == true ->
                    arguments.first().type == (other.classifier as KClass<*>).arrayTypeArgument?.type //Karena array INVARIANT.
                else -> false
            }
        }
        else -> {
            val commonType= other.typesTree.find { it.classifier == this.classifier } ?: return false
                //Menentukan apakah `this.extension`.classifier ada pada supertype dari `other`.
                // Jika ada, maka ambil commonType-nya.

            when(classifier){
                is KClass<*> -> { //Jika classifier adalah KClass<*>, maka tentukan apakah variance pada type-param udah sesuai dg commonType.arguments.
                    var isTypeArgCompatible= true
                    for((i, typeParam) in (classifier as KClass<*>).typeParameters.withIndex()){
                        isTypeArgCompatible= isTypeArgCompatible && when(typeParam.variance){
                            KVariance.INVARIANT -> arguments[i].type == commonType.arguments[i].type
                            KVariance.OUT -> commonType.arguments[i].type != null && arguments[i].type?.isSupertypeOf(commonType.arguments[i].type!!) ?: false
                            KVariance.IN -> commonType.arguments[i].type != null && arguments[i].type?.isSubtypeOf(commonType.arguments[i].type!!) ?: false
                        }
                    }
                    isTypeArgCompatible
                }
                else -> true //Jika classifier KTypeParameter atau yg lainnya, maka return true karena pengecekan sudah terjadi pada proses pengambilan commonType.
            }
        }
    }
}
//fun InferredType.isAssignableFrom(other: InferredType): Boolean = type.isAssignableFrom(other.type)




/*
==========================
Constructor
==========================
 */

val KParameter.isInConstructor: Boolean
    get()= this.toString().contains(K_FUNCTION_CONSTRUCTOR_NAME_PREFIX)

fun KParameter.isPropertyLike(prop: KProperty<*>, isInConstructorKnown: Boolean= false): Boolean{
    return if(isInConstructorKnown || isInConstructor){
        name == prop.name && type.classifier == prop.returnType.classifier
    } else false
}


/** Mengambil konstruktor dg jumlah parameter paling sedikit. */
val <T: Any> KClass<T>.leastParamConstructor: KFunction<T>
    get(){
        var constr= try{ constructors.first() }
        catch (e: NoSuchElementException){ throw NoSuchElementException("Kelas \"$qualifiedName\" tidak punya konstruktor (interface, abstract, anonymous class, atau null)") }

        var minParamCount= constr.parameters.size
        for(constrItr in constructors){
            if(minParamCount > constrItr.parameters.size){
                constr= constrItr
                minParamCount= constrItr.parameters.size
            }
            if(minParamCount == 0) break //Karena gakda fungsi yg jumlah parameternya krg dari 0
        }
        return constr
    }

/** Mirip dg [leastParamConstructor], namun param opsional tidak disertakan. Nullable tetap disertakan. */
val <T: Any> KClass<T>.leastRequiredParamConstructor: KFunction<T>
    get(){
        var constr= try{ constructors.first() }
        catch (e: NoSuchElementException){ throw NoSuchElementException("Kelas \"$this\" tidak punya konstruktor (interface, abstract, anonymous class, atau null)") }
        var minParamCount= constr.parameters.size
        //<20 Juli 2020> => Konstruktor dg jml param tersedikit belum tentu merupakan konstruktor dg jml param wajib paling sedikit.
        for(constrItr in constructors){
//            prine("leastRequiredParamConstructor class= $simpleName constrItr.parameters.size= ${constrItr.parameters.size}")
            if(minParamCount > constrItr.parameters.size){
                constr= constrItr
                minParamCount= constrItr.parameters.size
                for(param in constrItr.parameters)
                    if(param.isOptional) minParamCount--
            }
            if(minParamCount == 0) break //Karena gakda fungsi yg jumlah parameternya krg dari 0
        }
        val paramList= ArrayList<KParameter>()
        for(param in constr.parameters)
            if(!param.isOptional) paramList.add(param)

        //Bungkus constructor yg ditemukan dengan fungsi yg parameter listnya hanya terlihat yg non-optional.
        return object: KFunction<T> by constr{
            override val parameters: List<KParameter> = paramList
            override fun toString(): String = constr.toString()
        }
    }

/** Mengambil konstruktor dg param yg memiliki tipe data sesuai [paramClass]. Jika tidak ketemu, maka throw [NoSuchMethodException]. */
fun <T: Any> KClass<T>.findConstructorWithParam(vararg paramClass: KClass<*>): KFunction<T>{
    if(!isInstantiable) throw NoSuchElementException("Kelas \"$this\" tidak punya konstruktor (interface, abstract, anonymous class, atau null)")

    for(constr in constructors){
        var paramClassMatch= true
        if(constr.parameters.size == paramClass.size){
            for(param in constr.parameters){
                paramClassMatch= paramClassMatch && param.type.classifier == paramClass[param.index]
            }
        } else continue

        if(paramClassMatch)
            return constr
    }
    throw NoSuchMethodException("Tidak ada konstruktor \"$qualifiedName\" dg parameter \"${paramClass.string}\"")
}

/** Mengambil semua konstruktor yg tersedia mulai dari `this.extension` [KClass] hingga superclass. */
val KClass<*>.contructorsTree: NestedSequence<KFunction<*>>
    get()= object : NestedSequence<KFunction<*>>{
        override fun iterator(): NestedIterator<KClass<*>, KFunction<*>>
                = object : NestedIteratorImpl<KClass<*>, KFunction<*>>(this@contructorsTree.classesTree.iterator()){
            override val tag: String
                get() = "contrustorsTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KFunction<*>>? = nowInput.constructors.iterator()
            override fun getInputIterator(nowOutput: KFunction<*>): Iterator<KClass<*>>? = null
        }
    }

/** Mengambil semua parameter yg tersedia dari [contructorsTree]. */
val KClass<*>.contructorParamsTree: NestedSequence<KParameter>
    get()= object : NestedSequence<KParameter>{
        override fun iterator(): NestedIterator<KFunction<*>, KParameter> //!!!<20 Juli 2020> => Blum bisa pake NestedIterator.
                = object : NestedIteratorImpl<KFunction<*>, KParameter>(this@contructorParamsTree.contructorsTree.iterator()){
            override val tag: String
                get() = "contructorParamsTree"

            override fun getOutputIterator(nowInput: KFunction<*>): Iterator<KParameter>? = nowInput.parameters.iterator()
            override fun getInputIterator(nowOutput: KParameter): Iterator<KFunction<*>>? = null
        }
    }

/** Mengambil semua parameter konstruktor yg diambil dari [contructorParamsTree] yg merupakan properti. */
val KClass<*>.contructorPropertiesTree: Sequence<KProperty1<*, *>>
    get()= object : Sequence<KProperty1<*, *>>{
        override fun iterator(): Iterator<KProperty1<*, *>>
                = object : SkippableIteratorImpl<KProperty1<*, *>>(this@contructorPropertiesTree.implementedMemberPropertiesTree.iterator()){
            val constrsParams= this@contructorPropertiesTree.contructorParamsTree.asCached()

            override fun skip(now: KProperty1<*, *>): Boolean
                    = constrsParams.find { it.isPropertyLike(now, true) } == null
        }
    }
// */




/*
==========================
KCallable - Call - Set - Get
==========================
 */

/**
 * Cara aman untuk memanggil [KCallable.callBy].
 *
 * Fungsi ini melakukan pengecekan terhadap tipe argumen, nullabilitas, dan opsionalitasnya
 * sehingga meminimalisir terjadi error saat pemanggilan [KCallable].
 */
fun <R> KCallable<R>.callBySafely(args: Map<KParameter, Any?>): R{
    val newArgs= HashMap<KParameter, Any?>()
    for(param in parameters){
        var value= args[param]
        if(value == null){
            if(param.isOptional) continue
            if(!param.type.isMarkedNullable)
                throw IllegalArgumentException("Nilai argumen param: \"$param\" tidak boleh null")
        } else if(param.type.classifier != value::class){
            if(param.isOptional) continue
            if(param.type.isMarkedNullable) value= null
            else throw IllegalArgumentException("Tipe param: \"$param\" adalah ${param.type.classifier}, namun argumen bertipe ${value::class}")
        }
        newArgs[param]= value
    }
    return callBy(newArgs)
}


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

/** @return -> `true` jika operasi set berhasil,
 *   -> `false` jika refleksi dilarang,
 *   -> @throws [TypeExc] jika [value] yg dipass tidak sesuai tipe `this.extension`. */
fun <I, O> KMutableProperty1<I, O>.forcedSet(receiver: I, value: O, alsoInferValueType: Boolean= true): Boolean{
    return try{
/*
value?.clazz?.isSubClassOf(returnType.classifier as KClass<*>) == true
            || (value == null && returnType.isMarkedNullable)
 */
        val isAssignable= if(alsoInferValueType) returnType.isAssignableFrom(value.inferredType)
            else value?.clazz?.isSubClassOf(returnType.classifier as KClass<*>) == true
                    || (value == null && returnType.isMarkedNullable)
        if(isAssignable){
            val oldIsAccesible= isAccessible
            isAccessible= true
            set(receiver, value)
            isAccessible= oldIsAccesible
            true
        } else {
            throw TypeExc(
                expectedType = returnType.classifier as KClass<*>, actualType = value?.clazz,
                msg = "Tidak dapat meng-assign value dg tipe tersebut ke properti: $this."
            )
        }
    } catch (e: IllegalCallableAccessException){ //Jika Kotlin melarang melakukan call melalui refleksi
        false
    } catch (e: InvocationTargetException){
        //Jika terjadi error secara internal pada refleksi Java.
        // Biasanya terjadi pada operasi call melibatkan `lateinit var`
        false
    }
}

/** @return -> `true` jika operasi set berhasil,
 *   -> `false` jika refleksi dilarang,
 *   -> @throws [TypeExc] jika [value] yg dipass tidak sesuai tipe `this.extension`. */
fun <I, O> KMutableProperty1<I, O>.forcedSetTyped(receiver: I, typedValue: TypedValue<O>): Boolean{
    return try{
        val value= typedValue.value
        val type= typedValue.type

        if(returnType.isAssignableFrom(typedValue.type)){
            val oldIsAccesible= isAccessible
            isAccessible= true
            set(receiver, typedValue.value)
            isAccessible= oldIsAccesible
            true
        } else {
            throw TypeExc(
                expectedType = returnType.classifier as KClass<*>, actualType = value?.clazz,
                msg = "Tidak dapat meng-assign value dg tipe tersebut ke properti: $this."
            )
        }
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
/** @return [R] jika operasi call berhasil, null jika refleksi dilarang. */
fun <R> KCallable<R>.forcedCallBy(args: Map<KParameter, Any?>): R?{
    return try{
        val oldIsAccesible= isAccessible
        isAccessible= true
        val value= callBy(args) //get(receiver)
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
==========================
Enum
==========================
 */
/** Mengambil semua enum anggota `this.extension` [Enum] [Enum.ordinal] dan [Enum.name]. */
inline fun <reified E: Enum<E>> ordinalNamePairs(): Array<Pair<Int, String>>{
    val vals= enumValues<E>()
    return Array(vals.size){ Pair(vals[it].ordinal, vals[it].name) }
}
/** Mengubah semua enum anggota `this.extension` [Enum] menjadi array. */
inline fun <reified E: Enum<E>, reified A> Enum<E>.toArray(init: (E) -> A): Array<A>{
    val vals= enumValues<E>()
    return Array(vals.size){init(vals[it])}
}
/** Mengambil data Enum yg berada pada konstruktor. Selain di konstruktor tidak diambil. */
val <E: Enum<E>> E.data: Sequence<Pair<KProperty1<*, *>, Any?>>
    get(){
        val constrProps=
            (this::class.contructorPropertiesTree - Enum::class.declaredMemberProperties).iterator()
        return object : Sequence<Pair<KProperty1<*, *>, Any?>>{
            override fun iterator(): Iterator<Pair<KProperty1<*, *>, Any?>>
                    = object : Iterator<Pair<KProperty1<*, *>, Any?>>{
                override fun hasNext(): Boolean = constrProps.hasNext()

                override fun next(): Pair<KProperty1<*, *>, Any?> {
                    val next= constrProps.next()
                    val value= (next as KProperty1<E, *>).forcedGet(this@data)
                    return Pair(next, value)
                }
            }
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
    get()= isAbstract && !isInstantiable

val <T: Any> KClass<T>.isInstantiable: Boolean
    get()= constructors.isNotEmpty()

val KType.isInterface: Boolean
    get()= (this.classifier as? KClass<*>)?.isInterface ?: false

val <T: Any> KClass<T>.isPrimitive: Boolean
    get()= this.javaPrimitiveType != null

/**
 * Mengindikasikan bahwa data dg tipe `this.extension` [KClass] ini aman untuk di-copy scr langsung
 * tanpa harus di-instantiate agar tidak menyebabkan masalah yg berkaitan dg referential.
 *
 * Contoh tipe data yg aman untuk di-copy scr langsung tanpa harus di-instantiate adalah
 * tipe data primitif dan String. Jika ada tipe data lain yg immutable, tipe data tersebut juga
 * copy-safe.
 */
val <T: Any> KClass<T>.isCopySafe: Boolean
    get()= isPrimitive || this == String::class || isSubclassOf(Enum::class)
/*
//        prine("isCopySafe @$this isPrimitive= $isPrimitive this == String::class= ${this == String::class} this.isSubclassOf(Enum::class)= ${this.isSubclassOf(Enum::class)} isKReflectionElement= $isKReflectionElement")
        val res
//                || isKReflectionElement
        return res
    }
 */

val <T> T.isKReflectionElement: Boolean
    get()= when(this){
        is KParameter -> true
        is KCallable<*> -> true
        is KClass<*> -> true
        is KType -> true
        is KTypeParameter -> true
        is KClassifier -> true
        else -> false
    }

/**
 * Menunjukan jika kelas `this.extension` ini merupakan anonymous karena di-extend
 * oleh variabel lokal dan kelas yg di-extend bkn merupakan kelas abstract.
 */
val <T: Any> KClass<T>.isShallowAnonymous: Boolean
    get()= qualifiedName == null && supertypes.size == 1
            && !(supertypes.first().classifier as KClass<*>).isAbstract
            && isAllMembersImplemented

/**
 * Menunjukan apakah `this.extension` [KClass] abstrak scr sederhana, yaitu
 * tidak memiliki fungsi atau properti yg abstrak dan supertype hanya satu.
 * Berguna untuk operasi [new] pada kelas abstrak sehingga dapat mengembalikan
 * instance dg superclass.
 */
val <T: Any> KClass<T>.isShallowAbstract: Boolean
    get()= isAbstract && supertypes.size == 1
            && !(supertypes.first().classifier as KClass<*>).isAbstract
            && isAllMembersImplemented

val <T: Any> KClass<T>.isAllMembersImplemented: Boolean
    get()= members.find { it.isAbstract } == null

val <T: Any> KClass<T>.isArray: Boolean
    get()= isObjectArray || isPrimitiveArray

val <T: Any> KClass<T>.isObjectArray: Boolean
    get()= toString() == Array<Any>::class.toString()

val <T: Any> KClass<T>.isPrimitiveArray: Boolean
    get()= when(this){
        IntArray::class -> true
        LongArray::class -> true
        FloatArray::class -> true
        DoubleArray::class -> true
        CharArray::class -> true
        ShortArray::class -> true
        BooleanArray::class -> true
        ByteArray::class -> true
        else -> false
    }

/** Fungsi yg dapat dipanggil melalui Java. */
fun <T: Any> getKClass(javaClass: Class<T>): KClass<T>
        = javaClass.kotlin

/** Berguna untuk mengambil kelas dari instance dg tipe generic yg tidak punya batas atas Any. */
val Any.clazz: KClass<*>
    get()= this::class


/** Memiliki fungsi sama dg [isSuperclassOf] namun berguna untuk variabel generic tanpa batas atas Any. */
fun <T1, T2> T1.isSuperClassOf(derived: T2): Boolean{
    return try{
        val thisClass= if(this !is KClass<*>) (this as Any)::class else this
        val derivedClass= if(derived !is KClass<*>) (derived as Any)::class else derived
        thisClass.isSuperclassOf(derivedClass)
    } catch (e: Exception){ false }
}
/** Sama seperti [isSuperClassOf], namun @return `false` jika `this.extension` bertipe sama dg [derived]. */
fun <T1, T2> T1.isExclusivelySuperClassOf(derived: T2): Boolean{
    return try{
        val thisClass= if(this !is KClass<*>) (this as Any)::class else this
        val derivedClass= if(derived !is KClass<*>) (derived as Any)::class else derived
        thisClass.isSuperclassOf(derivedClass) && thisClass != derivedClass
    } catch (e: Exception){ false }
}

/** Memiliki fungsi sama dg [isSubclassOf] namun berguna untuk variabel generic tanpa batas atas Any. */
fun <T1, T2> T1.isSubClassOf(base: T2): Boolean{
    return try{
        val thisClass= if(this !is KClass<*>) (this as Any)::class else this
        val baseClass= if(base !is KClass<*>) (base as Any)::class else base
        thisClass.isSubclassOf(baseClass)
    } catch (e: Exception){ false }
}
/** Sama seperti [isSubClassOf], namun @return `false` jika `this.extension` bertipe sama dg [base]. */
fun <T1, T2> T1.isExclusivelySubClassOf(base: T2): Boolean{
    return try{
        val thisClass= if(this !is KClass<*>) (this as Any)::class else this
        val baseClass= if(base !is KClass<*>) (base as Any)::class else base
        thisClass.isSubclassOf(baseClass) && thisClass != baseClass
    } catch (e: Exception){ false }
}


/*
==========================
Inheritance Tree
==========================
 */

/**
 * [includeInterfaces] true jika iterasi hanya dilakukan pada super class dg jenis Class, bkn Interface.
 * Fungsi ini mendefinisikan supertype sbg interface berdasarkan refleksi Java.
 *
 * Fungsi ini msh bergantung dari Java Reflection.
 */
fun KClass<*>.supertypes(includeInterfaces: Boolean= true): NestedSequence<KType> {
    return object :
        NestedSequence<KType> {
        override fun iterator(): NestedIteratorSimple<KType>
                = object: NestedIteratorSimpleImpl<KType>(supertypes){

            override fun getOutputIterator(nowInput: KType): Iterator<KType>? {
                return if((nowInput.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                else (nowInput.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }

            override fun skip(now: KType): Boolean
                    = ((now.classifier as? KClass<*>)?.isInterface ?: false) && !includeInterfaces
        }
    }
}

/**
 * [includeInterfaces] true jika iterasi hanya dilakukan pada super class dg jenis Class, bkn Interface.
 * Fungsi ini mendefinisikan supertype sbg interface berdasarkan anotasi framework SiFrame,
 * yaitu tipe data yg memiliki anotasi [Interface].
 */
fun KClass<*>.supertypesSif(includeInterfaces: Boolean= true): NestedSequence<KType> {
    return object :
        NestedSequence<KType> {
        override fun iterator(): NestedIteratorSimple<KType>
                = object: NestedIteratorSimpleImpl<KType>(supertypes){

            override fun getOutputIterator(nowInput: KType): Iterator<KType>? {
                return if((nowInput.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                else (nowInput.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }

            override fun skip(now: KType): Boolean
                    = ((now.classifier as? KClass<*>)?.findAnnotation<Interface>() != null)
                    && !includeInterfaces
        }
    }
}

val KClass<*>.supertypesTree: NestedSequence<KType>
    get()= object :
        NestedSequence<KType> {
        override fun iterator(): NestedIteratorSimple<KType>
                = object: NestedIteratorSimpleImpl<KType>(supertypes){
            override fun getOutputIterator(nowInput: KType): Iterator<KType>? {
                return if((nowInput.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                else (nowInput.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }
        }
    }
val KClass<*>.superclassesTree: NestedSequence<KClass<*>>
    get()= object :
        NestedSequence<KClass<*>> {
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
    get()= object : NestedSequence<KType> {
        override fun iterator(): NestedIteratorSimple<KType>
                = object: NestedIteratorSimpleImpl<KType>(
                    try{ this@typesTree.createType() } catch (e: Exception){ this@typesTree.inferredType.type }
                ){
                    override fun getOutputIterator(nowInput: KType): Iterator<KType>? {
                        return if((nowInput.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                        else (nowInput.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
                    }
                }
    }
/** Sama dg [KClass.typesTree]. */
val KType.typesTree: NestedSequence<KType>
    get()= object : NestedSequence<KType> {
        override fun iterator(): NestedIteratorSimple<KType>
                = object: NestedIteratorSimpleImpl<KType>(this@typesTree){
                    override fun getOutputIterator(nowInput: KType): Iterator<KType>? {
                        return if((nowInput.classifier as? KClass<*>)?.simpleName == K_CLASS_BASE_NAME) null
                        else (nowInput.classifier as? KClass<*>)?.supertypes?.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
                    }
                }
    }
/** Sama seperti [superclassesTree] namun termasuk `this.extension` [KClass]. */
val KClass<*>.classesTree: NestedSequence<KClass<*>>
    get()= object :
        NestedSequence<KClass<*>> {
        override fun iterator(): NestedIteratorSimple<KClass<*>>
                = object: NestedIteratorSimpleImpl<KClass<*>>(this@classesTree){
            override val tag: String
                get() = "classesTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KClass<*>>? {
                return if(nowInput.simpleName == K_CLASS_BASE_NAME) null
                else nowInput.superclasses.iterator() //Jika (now.classifier as? KClass<*>)?.simpleName menghasilkan null, maka cabang ini tetap aman.
            }
        }
    }

val KClass<*>.sealedSubclassesTree: NestedSequence<KClass<*>>
    get()= object :
        NestedSequence<KClass<*>> {
        override fun iterator(): NestedIteratorSimple<KClass<*>>
                = object: NestedIteratorSimpleImpl<KClass<*>>(this@sealedSubclassesTree){
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
            for(supertype in this.supertypes(false)){
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
 * Properti yg mengembalikan [NestedSequence] yg berisi semua function dan property yg
 * terdapat di this [KClass] dan supertypes.
 */
val KClass<*>.declaredMembersTree: NestedSequence<KCallable<*>>
    get()= object : NestedSequence<KCallable<*>> {
        override fun iterator(): NestedIterator<KClass<*>, KCallable<*>>
                = object: NestedIteratorImpl<KClass<*>, KCallable<*>>(classesTree.iterator()){
            override val tag: String
                get() = "declaredMembersTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KCallable<*>>?
                    = nowInput.declaredMembers.iterator()
            override fun getInputIterator(nowOutput: KCallable<*>): Iterator<KClass<*>>? = null
        }
    }
/**
 * Properti yg mengembalikan [NestedSequence] yg berisi semua function dan property yg
 * terdapat di this [KClass] dan supertypes. Sequence juga berisi [KCallable] dari [KCallable]
 * jika returnType berupa object.
 */
val KClass<*>.nestedDeclaredMembersTree: NestedSequence<KCallable<*>>
    get()= object : NestedSequence<KCallable<*>> {
        override fun iterator(): NestedIterator<KClass<*>, KCallable<*>>
                = object: NestedIteratorImpl<KClass<*>, KCallable<*>>(classesTree.iterator()){
            override val tag: String
                get() = "declaredMembersTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KCallable<*>>?
                    = nowInput.declaredMembers.iterator()
            override fun getInputIterator(nowOutput: KCallable<*>): Iterator<KClass<*>>?
                    = (nowOutput.returnType.classifier as? KClass<*>)?.classesTree?.iterator()
        }
    }
/**
 * Properti yg mengembalikan [NestedSequence] yg berisi semua function dan property yg
 * terdapat di this [KClass]. Sequence juga berisi [KCallable] dari [KCallable]
 * jika returnType berupa object.
 */
val KClass<*>.nestedMembers: NestedSequence<KCallable<*>>
    get()= object : NestedSequence<KCallable<*>> {
        override fun iterator(): NestedIteratorSimple<KCallable<*>>
                = object: NestedIteratorSimpleImpl<KCallable<*>>(members.iterator()){
            override val tag: String
                get() = "nestedMembers"

            override fun getOutputIterator(nowInput: KCallable<*>): Iterator<KCallable<*>>?
                    = (nowInput.returnType.classifier as? KClass<*>)?.members?.iterator()
        }
    }

/** Sama dg [declaredMembersTree], namun tidak termasuk yg abstrak. */
val KClass<*>.implementedMembersTree: NestedSequence<KCallable<*>>
    get()= object : NestedSequence<KCallable<*>> {
        override fun iterator(): NestedIterator<KClass<*>, KCallable<*>>
                = object: NestedIteratorImpl<KClass<*>, KCallable<*>>(classesTree.iterator()){
            override val tag: String
                get() = "implementedMembersTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KCallable<*>>?
                    = nowInput.declaredMembers.iterator()
            override fun getInputIterator(nowOutput: KCallable<*>): Iterator<KClass<*>>? = null
            override fun skip(now: KCallable<*>): Boolean = now.isAbstract
        }
    }
/** Sama dg [nestedDeclaredMembersTree], namun tidak termasuk yg abstrak. */
val KClass<*>.nestedImplementedMembersTree: NestedSequence<KCallable<*>>
    get()= object : NestedSequence<KCallable<*>> {
        override fun iterator(): NestedIterator<KClass<*>, KCallable<*>>
                = object: NestedIteratorImpl<KClass<*>, KCallable<*>>(classesTree.iterator()){
            override val tag: String
                get() = "nestedImplementedMembersTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KCallable<*>>?
                    = nowInput.declaredMembers.iterator()
            override fun getInputIterator(nowOutput: KCallable<*>): Iterator<KClass<*>>?
                    = (nowOutput.returnType.classifier as? KClass<*>)?.classesTree?.iterator()
            override fun skip(now: KCallable<*>): Boolean = now.isAbstract
        }
    }

/**
 * Properti yg mengembalikan [NestedSequence] yg berisi semua properti yg
 * terdapat di `this.extension` [KClass]. Sequence juga termasuk properti dari properti jika tipenya
 * berupa object.
 */
val KClass<*>.nestedMemberProperties: NestedSequence<KProperty1<*, *>>
    get()= object :
        NestedSequence<KProperty1<*, *>> {
        override fun iterator(): NestedIteratorSimple<KProperty1<*, *>>
            = object: NestedIteratorSimpleImpl<KProperty1<*, *>>(memberProperties){
            override fun getOutputIterator(nowInput: KProperty1<*, *>): Iterator<KProperty1<*, *>>?
                = (nowInput.returnType.classifier as? KClass<*>)?.memberProperties?.iterator()
        }
    }
/**
 * Properti yg mengembalikan [NestedSequence] yg berisi semua properti yg
 * terdapat di this [KClass] dan supertypes.
 */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
val KClass<*>.declaredMemberPropertiesTree: NestedSequence<KProperty1<Any, *>>
    get()= object :
        NestedSequence<KProperty1<Any, *>> {
        override fun iterator(): NestedIterator<KClass<*>, KProperty1<Any, *>>
            = object: NestedIteratorImpl<KClass<*>, KProperty1<Any, *>>(classesTree.iterator()){
            override val tag: String
                get() = "declaredPropertiesTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KProperty1<Any, *>>?
                = nowInput.declaredMemberProperties.iterator() as Iterator<KProperty1<Any, *>>
            override fun getInputIterator(nowOutput: KProperty1<Any, *>): Iterator<KClass<*>>? = null
        }
    }


/** Sama dengan [declaredMemberPropertiesTree], ditambah isi dari properti jika properti merupakan object. */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
val KClass<*>.nestedDeclaredMemberPropertiesTree: NestedSequence<KProperty1<Any, *>>
    get()= object :
        NestedSequence<KProperty1<Any, *>> {
        override fun iterator(): NestedIterator<KClass<*>, KProperty1<Any, *>>
            = object: NestedIteratorImpl<KClass<*>, KProperty1<Any, *>>(classesTree.iterator()){
            override val tag: String
                get() = "nestedDeclaredPropertiesTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KProperty1<Any, *>>?
                = nowInput.declaredMemberProperties.iterator() as Iterator<KProperty1<Any, *>>
            override fun getInputIterator(nowOutput: KProperty1<Any, *>): Iterator<KClass<*>>?
                = (nowOutput.returnType.classifier as? KClass<*>)?.classesTree?.iterator()
        }
    }

/** Sama dengan [declaredMemberPropertiesTree], namun tidak termasuk abstract property. */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
val KClass<*>.implementedMemberPropertiesTree: NestedSequence<KProperty1<Any, *>>
    get()= object :
        NestedSequence<KProperty1<Any, *>> {
        override fun iterator(): NestedIterator<KClass<*>, KProperty1<Any, *>>
                = object: NestedIteratorImpl<KClass<*>, KProperty1<Any, *>>(classesTree.iterator()){
            override val tag: String
                get() = "declaredPropertiesTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KProperty1<Any, *>>?
               = nowInput.declaredMemberProperties.iterator() as Iterator<KProperty1<Any, *>>
            override fun getInputIterator(nowOutput: KProperty1<Any, *>): Iterator<KClass<*>>? = null
            override fun skip(now: KProperty1<Any, *>): Boolean = now.isAbstract
        }
    }

/** Sama dengan [nestedDeclaredMemberPropertiesTree], namun tidak termasuk abstract property. */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
val KClass<*>.nestedImplementedMemberPropertiesTree: NestedSequence<KProperty1<Any, *>>
    get()= object :
        NestedSequence<KProperty1<Any, *>> {
        override fun iterator(): NestedIterator<KClass<*>, KProperty1<Any, *>>
            = object: NestedIteratorImpl<KClass<*>, KProperty1<Any, *>>(classesTree.iterator()){
            override val tag: String
                get() = "nestedDeclaredPropertiesTree"

            override fun getOutputIterator(nowInput: KClass<*>): Iterator<KProperty1<Any, *>>?
                = nowInput.declaredMemberProperties.iterator() as Iterator<KProperty1<Any, *>>

            override fun getInputIterator(nowOutput: KProperty1<Any, *>): Iterator<KClass<*>>?
                = (nowOutput.returnType.classifier as? KClass<*>)?.classesTree?.iterator()

            override fun skip(now: KProperty1<Any, *>): Boolean = now.isAbstract
        }
    }


/*
==========================
Properties Tree - Value
==========================
 */
/**
 * Melakukan copy properti dari [source] ke [destination] yg memiliki nama dan tipe data yg sama.
 *
 * Fungsi ini berbeda [clone] karena hanya melakukan copy referential scr langsung tanpa melakukan
 * instantiate. Operasi copy nilai hanya dilakukan pada permukaan.
 */
fun copySimilarProperty(source: Any, destination: Any){
    for(valMap in source.implementedAccesiblePropertiesValueMapTree){
        (destination.implementedAccesiblePropertiesValueMapTree.find {
            it.first.renamedName == valMap.first.renamedName
                    && it.first.returnType.classifier == valMap.first.returnType.classifier
                    && it.first is KMutableProperty1<*, *>
        }?.first as? KMutableProperty1<Any, Any?>)
            ?.set(destination, valMap.second)
    }
}


/** Mengambil semua properti berserta nilainya dari `this.extension` termasuk yg `private`. */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
val Any.implementedPropertiesValueMap: Sequence<Pair<KProperty1<Any, *>, Any?>>
    get()= object : Sequence<Pair<KProperty1<Any, *>, Any?>>{
        override fun iterator(): Iterator<Pair<KProperty1<Any, *>, Any?>>
            = object: Iterator<Pair<KProperty1<Any, *>, Any?>>{
            private val declaredPropsItr=
                this@implementedPropertiesValueMap::class.declaredMemberProperties.iterator() as Iterator<KProperty1<Any, *>>

            override fun hasNext(): Boolean = declaredPropsItr.hasNext()

            override fun next(): Pair<KProperty1<Any, *>, Any?> {
                val prop= declaredPropsItr.next()
                val value= if(prop.toString() != K_PROPERTY_ARRAY_SIZE_STRING)
                    prop.getter.forcedCall(this@implementedPropertiesValueMap)
                else (this@implementedPropertiesValueMap as Array<*>).size
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
                return if(prop.toString() != K_PROPERTY_ARRAY_SIZE_STRING)
                    prop.getter.forcedCall(this@implementedPropertiesValue)
                else (this@implementedPropertiesValue as Array<*>).size
            }
        }
    }

/** Sama dengan [implementedPropertiesValueMap], beserta properti dari properti. */
val Any.nestedImplementedPropertiesValueMap: NestedSequence<Pair<KProperty1<Any, *>, Any?>>
    get()= object :
        NestedSequence<Pair<KProperty1<Any, *>, Any?>> {
        override fun iterator(): NestedIteratorSimple<Pair<KProperty1<Any, *>, Any?>>
            = object : NestedIteratorSimpleImpl<Pair<KProperty1<Any, *>, Any?>>(
                this@nestedImplementedPropertiesValueMap.implementedPropertiesValueMap.iterator()
            ){
            override val tag: String
                get() = "nestedImplementedPropertiesValueMap"

            override fun getOutputIterator(nowInput: Pair<KProperty1<Any, *>, Any?>): Iterator<Pair<KProperty1<Any, *>, Any?>>?
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
            override fun getOutputIterator(nowInput: Any?): Iterator<Pair<KProperty1<*, *>, Any?>>? {}

            override fun getInputIterator(nowOutput: Pair<KProperty1<*, *>, Any?>): Iterator<Any?>? {}
        }
    }
*/
///*
/** Sama dg [implementedPropertiesValueMap], beserta semua properti `private` dari superclass. */
val Any.implementedPropertiesValueMapTree: Sequence<Pair<KProperty1<Any, *>, Any?>>
    get()= object : Sequence<Pair<KProperty1<Any, *>, Any?>>{
        override fun iterator(): Iterator<Pair<KProperty1<Any, *>, Any?>>
                = object: Iterator<Pair<KProperty1<Any, *>, Any?>>{
            private val declaredPropsItr= this@implementedPropertiesValueMapTree::class.implementedMemberPropertiesTree.iterator()

            override fun hasNext(): Boolean = declaredPropsItr.hasNext()

            override fun next(): Pair<KProperty1<Any, *>, Any?> {
                val prop= declaredPropsItr.next()
                val value= if(prop.toString() != K_PROPERTY_ARRAY_SIZE_STRING)
                    prop.getter.forcedCall(this@implementedPropertiesValueMapTree)
                else (this@implementedPropertiesValueMapTree as Array<*>).size
                return Pair(prop, value)
            }
        }
    }
// */

///*
/** Sama dg [implementedPropertiesValueMapTree], beserta semua properti dari properti, termasuk yg `private`. */
val Any.nestedImplementedPropertiesValueMapTree: NestedSequence<Pair<KProperty1<Any, *>, Any?>>
    get()= object:
        NestedSequence<Pair<KProperty1<Any, *>, Any?>> {
        override fun iterator(): NestedIteratorSimple<Pair<KProperty1<Any, *>, Any?>>
            = object: NestedIteratorSimpleImpl<Pair<KProperty1<Any, *>, Any?>>(
                this@nestedImplementedPropertiesValueMapTree.implementedPropertiesValueMapTree.iterator()
            ){
            override val tag: String
                get() = "nestedImplementedPropertiesValueMapTree"

            override fun getOutputIterator(nowInput: Pair<KProperty1<Any, *>, Any?>): Iterator<Pair<KProperty1<Any, *>, Any?>>?
                = nowInput.second?.implementedPropertiesValueMapTree?.iterator()
        }
    }
// */


/** Sama dg [implementedPropertiesValueMapTree], namun tidak mengambil property yg `private`. */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
val Any.implementedAccesiblePropertiesValueMapTree: Sequence<Pair<KProperty1<Any, *>, Any?>>
    get()= object : Sequence<Pair<KProperty1<Any, *>, Any?>>{
        override fun iterator(): Iterator<Pair<KProperty1<Any, *>, Any?>>
                = object: Iterator<Pair<KProperty1<Any, *>, Any?>>{
            private val memberPropsItr=
                this@implementedAccesiblePropertiesValueMapTree::class.memberProperties
                    .asSequence().filterNot { it.isAbstract }.iterator()
                        as Iterator<KProperty1<Any, *>>

            override fun hasNext(): Boolean = memberPropsItr.hasNext()

            override fun next(): Pair<KProperty1<Any, *>, Any?> {
                val prop= memberPropsItr.next()
                val value= if(prop.toString() != K_PROPERTY_ARRAY_SIZE_STRING)
                    prop.getter.forcedCall(this@implementedAccesiblePropertiesValueMapTree)
                else (this@implementedAccesiblePropertiesValueMapTree as Array<*>).size
                return Pair(prop, value)
            }
        }
    }





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
 * Fungsi yg memiliki fungsi sama dg [clone], namun lebih runtime-safety karena tipe data
 * yg di-clone dg tipe data tujuan berbeda. Hal tersebut berguna saat `this.extension`
 * berupa [isShallowAnonymous] == true.
 */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <T: Any> Any.anyClone(isDeepClone: Boolean= true, constructorParamValFunc: ((KClass<*>, KParameter) -> Any?)?= null): T{
    return clone(isDeepClone, constructorParamValFunc) as T
}
/**
 * Digunakan untuk meng-clone object `this.extension` [T] sehingga menciptakan instance baru dg
 * nilai properti yg sama.
 *
 * Fungsi ini dapat meng-clone instance yg merupakan [isShallowAnonymous], namun tidak menjamin
 * attribut overriding maupun attribut tambahan yg ada di dalamnya.
 *
 * @param [isDeepClone] `true` jika seluruh nilai properti yg berupa `object` di-instantiate
 *   menjadi `instance` yg baru. Operasi deep-clone juga berlaku terhadap properti yg dimiliki properti.
 *   [isDeepClone] `false` jika clone hanya dilakukan pada `this.extension` tidak termasuk properti.
 *
 * @return -> instance dg tipe [T] yg baru,
 *   -> supertype dari `this.extension` [T] jika [T] merupakan [isShallowAnonymous],
 *   -> throw [NonInstantiableTypeExc] jika tipe yg di-clone tidak memiliki kontruktor karena
 *   berupa interface, abstract, atau anonymous class.
 */
//@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <T: Any> T.clone(isDeepClone: Boolean= true, constructorParamValFunc: ((KClass<*>, KParameter) -> Any?)?= null): T{
    var clazz= this::class.also{ if(it.isCopySafe) return this }
    var continueCreateNewInstance= true
    val valueMapTree= implementedPropertiesValueMapTree
    //Berguna untuk mengecek apakah `KProperty` merupakan properti dg mengecek kesamaannya dg `KParameter` di contrustor.

    val constr= try{ clazz.leastRequiredParamConstructor }
    catch (e: Exception){
        if(clazz.isShallowAnonymous){
            continueCreateNewInstance= false
            clazz= this::class.supertypes.first().classifier as KClass<T>
            clazz.leastRequiredParamConstructor
        } else if(clazz.isCopySafe || this.isKReflectionElement) return this
        else throw NonInstantiableTypeExc(typeClass = this::class,
                msg = "Tipe data tidak punya konstruktor dan bkn merupakan shallow-anonymous.")
    }

    val newInsConstrParamValFunc= { paramOfNew: KParameter ->
        (constructorParamValFunc ?: { clazz, param ->
            if(constr.parameters.find { it == param } != null){
                valueMapTree.find { pairValueMap -> param.isPropertyLike(pairValueMap.first) }
                    .notNullTo { it.second }
            } else null
        }).invoke(clazz, paramOfNew)
    }

//    prine("clazz= $clazz !clazz.isArray= ${!clazz.isArray} continueCreateNewInstance= $continueCreateNewInstance")

    val newInstance= if(continueCreateNewInstance){
        if(!clazz.isArray)
            new(clazz, constructor = constr, defParamValFunc = newInsConstrParamValFunc)
                ?: throw NonInstantiableTypeExc(typeClass = this::class,
                    msg = "Tidak tersedia nilai default untuk di-pass ke konstruktor.")
        else
            arrayClone(isDeepClone, constructorParamValFunc) //as T
    } else{
        new(clazz.supertypes.first().classifier as KClass<out T>,  constructor = constr,
            defParamValFunc = newInsConstrParamValFunc)
            ?: throw NonInstantiableTypeExc(typeClass = this::class,
                msg = "Tidak tersedia nilai default untuk di-pass ke konstruktor.")
    }

    for(valueMap in valueMapTree){
        val prop= valueMap.first
        if(prop is KMutableProperty1<Any, *>){
            val value= valueMap.second
//            prine("clone() prop= $prop value= $value value.clazz.isPrimitive= ${value?.clazz?.isPrimitive} bool=${!isDeepClone || value == null || value.clazz.isPrimitive}")
            prop.asNotNull { mutableProp: KMutableProperty1<T, Any?> ->
//                prine(" masukkkk... clone() prop= $prop value= $value value.clazz.isPrimitive= ${value?.clazz?.isPrimitive} bool=${!isDeepClone || value == null || (value.clazz.isPrimitive && constr.parameters.find { it.isPropertyLike(mutableProp, true) } == null)}")
                if(!isDeepClone || value == null || value.clazz.isCopySafe || this.isKReflectionElement){
                    if(constr.parameters.find { it.isPropertyLike(mutableProp, true) } == null)
                    //Jika ternyata [mutableProp] terletak di konstruktor dan sudah di-instansiasi,
                    // itu artinya programmer sudah memberikan definisi nilainya sendiri saat intansiasi,
                    // maka jangan salin nilai lama [mutableProp] ke objek yg baru di-intansiasi.
                        mutableProp.forcedSetTyped(newInstance, value.withType(mutableProp.returnType))
                } else{
//                    prine("prop= $mutableProp mutableProp.returnType= ${mutableProp.returnType.classifier}")
                    mutableProp.forcedSetTyped<T, Any?>(newInstance, value.clone(true, constructorParamValFunc).withType(mutableProp.returnType))
                }
            }
        }
    }
//    prine("newInstance::class= ${newInstance::class} this::class= ${this::class}")
    if(newInstance.isExclusivelySuperClassOf(this))
        prine("Kelas yg di-clone: \"${this::class}\" merupakan shallow-anonymous, newInstance yg di-return adalah superclass: \"${newInstance::class}\".")
    return newInstance
}

/**
 * Meng-clone `this.extension` [Array] beserta elemen di dalamnya. Untuk clone elemen di dalamnya,
 * apakah clone element dilakukan scr deep atau tidak bergantung dari [isElementDeepClone].
 * Array hasil clone berisi element yg di-clone, atau element dg instance yg sama jika element
 * tidak dapat di-instantiate.
 */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <T> Array<T>.deepClone(isElementDeepClone: Boolean= true, elementConstructorParamValFunc: ((KClass<*>, KParameter) -> Any?)?= null): Array<T>{
    val newArray= this.clone()
    for((i, e) in this.withIndex()){
        if(e != null)
            newArray[i]= try{ (e as Any).clone(isElementDeepClone, elementConstructorParamValFunc) }
                catch (e: NonInstantiableTypeExc){ e } as T
    }
    return newArray
}

/**
 * Meng-clone array berupa apapun itu.
 * @return -> array baru hasil deepClone() jika `this.extension` berupa [Array],
 *   -> array hasil clone() biasa jika `this.extension` berupa array primitif,
 *   -> `this.extension` sendiri jika berupa array yg lain.
 */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
fun <T: Any> T.arrayClone(isElementDeepClone: Boolean= true, elementConstructorParamValFunc: ((KClass<*>, KParameter) -> Any?)?= null): T{
    if(!this::class.isArray) throw ClassCastExc(fromClass = this::class, toClass = Array<Any>::class, msg = "Instance yg di-arrayClone() bkn array")

    val res: Any = if(this::class.toString() == Array<Any>::class.toString())
        (this as Array<*>).deepClone(isElementDeepClone, elementConstructorParamValFunc)
    else when(this){
        is IntArray -> clone()
        is LongArray -> clone()
        is FloatArray -> clone()
        is DoubleArray -> clone()
        is CharArray -> clone()
        is ShortArray -> clone()
        is BooleanArray -> clone()
        is ByteArray -> clone()
        else -> this
    }
    return res as T
}

/** Sama dg [IntArray.clone], namun agar serasi dg [Array.deepClone]. */
fun IntArray.deepClone(): IntArray = clone()
fun LongArray.deepClone(): LongArray = clone()
fun FloatArray.deepClone(): FloatArray = clone()
fun DoubleArray.deepClone(): DoubleArray = clone()
fun CharArray.deepClone(): CharArray = clone()
fun ShortArray.deepClone(): ShortArray = clone()
fun BooleanArray.deepClone(): BooleanArray = clone()
fun ByteArray.deepClone(): ByteArray = clone()



/**
 * <14 Juli 2020> => Versi baru fungsi inline yg kecil.
 */
inline fun <reified T: Any> new(constructorParamClass: Array<KClass<*>>?= null,
                                constructor: KFunction<T>? = null,
                                noinline defParamValFunc: ((param: KParameter) -> Any?)?= null): T?
    = new(T::class, constructorParamClass, constructor, defParamValFunc)

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
fun <T: Any> new(clazz: KClass<out T>, constructorParamClass: Array<KClass<*>>?= null,
                 constructor: KFunction<T>? = null,
                 defParamValFunc: ((param: KParameter) -> Any?)?= null): T?{
    if(clazz.isCopySafe)
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
    val usedClazz=
        if(!clazz.isShallowAbstract) clazz
        else try{
            val superclazz= clazz.supertypes.first().classifier as KClass<out T>
            prine("""Kelas: "$clazz" merupakan shallow-abstract, newInstance yg di-intanstiate adalah superclass: "$superclazz".""")
            superclazz
        } catch (e: ClassCastException){
            prine("""Kelas: "$clazz" abstrak sehingga tidak dapat di-intanstiate.""")
            return null
        }

    val constr = constructor ?:
        try {
            if (constructorParamClass.isNullOrEmpty()) usedClazz.leastRequiredParamConstructor
            else usedClazz.findConstructorWithParam(*constructorParamClass)
        } catch (e: Exception){
            return if(!usedClazz.isShallowAnonymous) { //Kelas udah gak bisa di-instantiate.
                prine("""Kelas: "$clazz" tidak punya konstruktor sehingga tidak dapat di-intanstiate.""")
                null
            } else {
                val superclazz= clazz.supertypes.first().classifier as KClass<out T>
                prine("""Kelas: "$clazz" merupakan shallow-anonymous, newInstance yg di-intanstiate adalah superclass: "$superclazz".""")
                new(superclazz, constructorParamClass, constructor, defParamValFunc)
            }
        }

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
    return constr.forcedCallBy(defParamVal) //.forcedCall()//.callBy(defParamVal)
}

inline fun <reified T: Any> defaultPrimitiveValue(): T?= defaultPrimitiveValue(T::class)
/**
 * Digunakan untuk mendapatkan nilai default dari suatu tipe data yg ada pada [clazz].
 * Nilai default dapat diperoleh jika tipe data pada [clazz] merupakan tipe primitif
 * sesuai definisi yg ada.
 *
 * Fungsi ini tidak dapat menghasilkan nilai default dari Array<*>.
 */
@Suppress(SuppressLiteral.UNCHECKED_CAST)
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