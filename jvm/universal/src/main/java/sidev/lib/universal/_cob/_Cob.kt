package sidev.lib.universal._cob

import sidev.lib.universal.`fun`.*
import java.io.Serializable
import java.lang.Exception
import kotlin.reflect.*
import kotlin.reflect.full.*


class AE<T: Collection<S>, S: CharSequence>(var ae: T, var ae2: T?= null): Serializable
class AEB<T: Collection<List<S>>, S: CharSequence>(var ae: T, var ae2: T?= null)
class AEA{
    var ae= arrayOf(1, 2, 3)
}
data class AED(var a: Int= 1, var b: Int= 2)

open class AA
class AA2: AA()

open class Food
open class FastFood: Food()
class Burger: FastFood()

interface Consumer<in T: Food>{
    fun eat(food: T) = prin("eat ${food::class} food is Food = > ${food is Food}")
}
class Everybody: Consumer<Food>
class ModernPeople: Consumer<FastFood>
class American: Consumer<Burger>{
    override fun eat(food: Burger) {
        if(food !is Burger) throw Exception("food !is Burger")
    }
}

interface Producer<out T: Food>{
    fun eat() : T
}
class FoodStore: Producer<Food>{
    override fun eat(): Food = Food()
}
class FastFoodStore: Producer<FastFood>{
    override fun eat(): FastFood = FastFood()
}
class BurgerStore: Producer<Burger>{
    override fun eat(): Burger = Burger()
}

private abstract class AbsCl private constructor()
internal object ObjCl
internal interface IntfcCl

@ExperimentalStdlibApi
fun main(args: Array<String>) {
//    val v = BooleanWrap("a")
//    AbsCl::class.createInstance()
    prin("ObjCl::class.isInterface= ${ObjCl::class.isInterface} AbsCl::class.isInterface= ${AbsCl::class.isInterface} IntfcCl::class.isInterface= ${IntfcCl::class.isInterface}")
    prin("ObjCl::class.constructors.size= ${ObjCl::class.constructors.size} ObjCl::class.isAbstract= ${ObjCl::class.isAbstract}")
    prin("AbsCl::class.constructors.size= ${AbsCl::class.constructors.size}")

    val p= ADE()
    p.a.aoe()
    val aed1= AED()
    val aed2= aed1
    aed1.a= 10
    prin("aed1.a= ${aed1.a} aed2.a= ${aed2.a}")

    val arr= ArrayList<Int>()
    val list1= listOf(AA(), AA())
    val list2= listOf(AA2(), AA2())
    val inferredTypeList1= list1.inferredType
    val inferredTypeList2= list2.inferredType

    prin("inferredTypeList1.canBeAssignedWith(inferredTypeList2)= ${inferredTypeList1.isAssignableFrom(inferredTypeList2)}")

    prin("inferredTypeList1= $inferredTypeList1 inferredTypeList2= $inferredTypeList2")
    prin("inferredTypeList2.isSubtypeOf(inferredTypeList1)= ${inferredTypeList2.type.isSubtypeOf(inferredTypeList1.type)}")
    prin("inferredTypeList2.isSubTypeOf(inferredTypeList1)= ${inferredTypeList2.isSubTypeOf(inferredTypeList1)}")
    prin("inferredTypeList1.isSupertypeOf(inferredTypeList2)= ${inferredTypeList1.type.isSupertypeOf(inferredTypeList2.type)}")
    prin("inferredTypeList1.isSuperTypeOf(inferredTypeList2)= ${inferredTypeList1.type.isSuperTypeOf(inferredTypeList2.type)}")

    val arr2= arrayOf(1, 2, 3)
    val arr4= arrayOf(11, 2, null)
    prin("inferElementClass(arr2)= ${inferElementClass(arr2)}")
    prin("inferElementType(arr4)= ${inferElementType(arr4)}")

    val arr3= intArrayOf(1, 2)
    val inferredArr2= arr2.inferredType
    val inferredArr3= arr3.inferredType
    prin("arr2::class.arrayTypeArgument= ${arr2::class.arrayTypeArgument} arr3::class.arrayTypeArgument= ${arr3::class.arrayTypeArgument}")
    prin("arr2::class.isSuperclassOf(arr3::class)= ${arr2::class.isSuperclassOf(arr3::class)} arr3::class= ${arr3::class}")
    prin("arr2::class.isSubclassOf(arr3::class)= ${arr2::class.isSubclassOf(arr3::class)}")

    prin("inferredArr2.isAssignableFrom(inferredArr3)= ${inferredArr2.isAssignableFrom(inferredArr3)}")
    prin("inferredArr2= $inferredArr2 inferredArr3= $inferredArr3")

    val cons: Consumer<Burger> = Everybody()
    val prods: Producer<Food> = BurgerStore()

    val consBurgerType= Consumer::class.createType(listOf(Burger::class.createType().simpleTypeProjection))
    val consEverybody= Everybody::class.createType()

    val prodsFood= Producer::class.createType(listOf(Food::class.createType().simpleTypeProjection))
    val prodsBurger= Producer::class.createType(listOf(Burger::class.createType().simpleTypeProjection))
    val prodsFoodStore= FoodStore::class.createType()
    val prodsBurgerStore= BurgerStore::class.createType()

    prin("consBurgerType= $consBurgerType consEverybody= $consEverybody consBurgerType.canBeAssignedWith(consEverybody)= ${consBurgerType.isAssignableFrom(consEverybody)}")
    prin("prodsFood= $prodsFood prodsBurgerStore= $prodsBurgerStore prodsFood.canBeAssignedWith(prodsBurgerStore)= ${prodsFood.isAssignableFrom(prodsBurgerStore)}")
    prin("prodsFood= $prodsFood prodsBurgerStore= $prodsBurgerStore prodsFood.isSupertypeOf(prodsBurgerStore)= ${prodsFood.isSupertypeOf(prodsBurgerStore)}")
    prin("prodsFood= $prodsFood prodsBurgerStore= $prodsBurgerStore prodsFood.isSuperTypeOf(prodsBurgerStore)= ${prodsFood.isSuperTypeOf(prodsBurgerStore)}")
    prin("prodsBurger= $prodsBurger prodsBurgerStore= $prodsBurgerStore prodsBurger.isSupertypeOf(prodsBurgerStore)= ${prodsBurger.isSupertypeOf(prodsBurgerStore)}")
    prin("prodsBurger= $prodsBurger prodsBurgerStore= $prodsBurgerStore prodsBurger.isSuperTypeOf(prodsBurgerStore)= ${prodsBurger.isSuperTypeOf(prodsBurgerStore)}")
    prin("prodsFoodStore= $prodsFoodStore prodsBurger= $prodsBurgerStore prodsFoodStore.canBeAssignedWith(prodsBurger)= ${prodsFoodStore.isAssignableFrom(prodsBurgerStore)}")
    prin("prodsFoodStore= $prodsFoodStore prodsBurger= $prodsBurgerStore prodsFoodStore.isSupertypeOf(prodsBurgerStore)= ${prodsFoodStore.isSupertypeOf(prodsBurgerStore)}")
    prin("prodsFoodStore= $prodsFoodStore prodsBurger= $prodsBurgerStore prodsFoodStore.isSuperTypeOf(prodsBurgerStore)= ${prodsFoodStore.isSuperTypeOf(prodsBurgerStore)}")
    prin("prodsFood= $prodsFood prodsBurger= $prodsBurger prodsFood.isSupertypeOf(prodsBurger)= ${prodsFood.isSupertypeOf(prodsBurger)}")
    prin("prodsFood= $prodsFood prodsBurger= $prodsBurger prodsFood.isSuperTypeOf(prodsBurger)= ${prodsFood.isSuperTypeOf(prodsBurger)}")

    val ae= AE(listOf("String txt"))

    val commonClass= getCommonClass(Int::class, String::class, Double::class)
    val commonClass2= getCommonClass(1, "halo", 2.1, ae)
    prin("commonClass= $commonClass commonClass2= $commonClass2")

    val commonType1= getCommonType(1, 2, 3, null, 2.1) //(1, "halo", 2.1, ae)
    val inferredNullType= null.inferredType
//    val listaa= listOf(1, "halo", 2.1)
    prin("commonType1= $commonType1")
    prin("inferredNullType= $inferredNullType")

    prin("====Sorted=====")
    for(cls in Int::class.classesTree.withLevel().sortedBy { it.level })
        prin("cls= $cls")
    prin("====Non-Sorted=====")
    for(cls in Int::class.classesTree.withLevel())
        prin("cls= $cls")
    prin("====Non-Sorted=====")
    for(cls in String::class.classesTree.withLevel())
        prin("cls= $cls")

    for((i, typeParam) in ae::class.nestedTypeParameters.withIndex()){
        prin("i= $i typeParam= $typeParam")
    }
    prin("AEB::class.typeParameters[0].nestedUpperBoundTypeArguments = ${AEB::class.typeParameters[0].nestedUpperBoundTypeArguments.toList()}")
    prin("ae::class.typeParameters[0].nestedUpperBoundTypeArguments= ${ae::class.typeParameters[0].nestedUpperBoundTypeArguments.toList()}")
    prin("ae::class.typeParameters[1].getTypeParameterLink(ae::class)= ${ae::class.typeParameters[1].getTypeParameterLink(ae::class)}")
    prin("ae::class.typeParameters[0].getTypeParameterLink(ae::class)= ${ae::class.typeParameters[0].getTypeParameterLink(ae::class)}")
//    ae::class.typeParameters.first().upperBounds

//    fun KTypeParameter.get
    prin("ae::class.typeParameters.first().upperBounds= ${ae::class.typeParameters.first().upperBounds}")

//    prin("ae.ae::class.typeParameters.first().getProjection(ae.ae) = ${ae.ae::class.typeParameters.first().getProjectionIn(ae.ae)}")
    prin("ae::class.typeParameters.first().getProjection(ae) = ${ae::class.typeParameters.first().getClassProjectionIn(ae)}")
    prin("ae.inferredType_old= ${ae.inferredType_old}")
    prin("ae.inferredType= ${ae.inferredType}")

    val aeee= ae::class.typeParameters.asSequence().map { it.getTypeParameterLink(ae::class)!! }.asCached()
    prin("aeee[1]= ${aeee[1]}")
    for((i, eea) in aeee.withIndex()){
        prine("i= $i eea= $eea")
    }



// */
    prin("AE<List<String>>::ae.returnType.classifier?.createType() = ${AE<List<String>, String>::ae.returnType.classifier?.createType()}")

    prin("AE<*>::ae.returnType = ${AE<List<String>, String>::ae.returnType}")

    for((i, typeArg) in AE<*, *>::ae.returnType.arguments.withIndex())
        prin("i= $i typeArg= $typeArg")
    for((i, typeArg) in AEA::ae.returnType.arguments.withIndex())
        prin("i= $i typeArg= $typeArg")
    prin("AEA::ae.returnType.arguments.size = ${AEA::ae.returnType.arguments.size}")
    prin("AEA::ae.returnType= ${AEA::ae.returnType}")
/*
//    prin("AE<*>::ae.returnType == AE<*>::ae2.returnType => ${AE<ArrayList<String>>::ae.returnType == AE<*>::ae2.returnType}")
//    prin("AE<ArrayList<String>>::ae.returnType.isSametypeAs(AE<*>::ae2.returnType) => ${AE<ArrayList<String>>::ae.returnType.isSametypeAs(AE<*>::ae2.returnType, false)}")
//    prin("AE<ArrayList<String>>::ae.returnType.isSupertypeOf(AE<*>::ae2.returnType) => ${AE<ArrayList<String>>::ae.returnType.isSupertypeOf(AE<*>::ae2.returnType)}")

//    prin("AE::class.typeParameters.first().isSupertypeOf(List::class) => ${AE::class.typeParameters.first().isSupertypeOf(List::class)}")

    val decimal= 1.2315f -1.2315f.toInt()
    val decimalRound= decimal *10000
    val decimal2= decimalRound -(decimalRound.toInt())
    for((i, bound) in AE::class.typeParameters.first().upperBounds.withIndex()){
        prin("i= $i bound= $bound")
        for((u, arg) in bound.arguments.withIndex())
            prin("-- u= $u arg= $arg")
    }
    for((i, bound) in (AE::class.typeParameters.first().upperBounds.first().classifier as KClass<*>).typeParameters.first().upperBounds.withIndex())
        prin("i= $i bound= $bound")

    val ae= AE(arrayListOf("String"))
    prin("ae::ae.returnType.arguments.size = ${ae::ae.returnType.arguments.size}")
    prin("\n============ AE Projection ===============\n")
    for((i, projection) in ae::ae.returnType.arguments.withIndex())
        prin("i= $i projection= $projection")

    prin("ae::ae.returnType.classifier= ${ae::ae.returnType.classifier} ae::ae.returnType.classifier is KClass<*>= ${ae::ae.returnType.classifier is KClass<*>} ae::ae.returnType.classifier?.clazz= ${ae::ae.returnType.classifier?.clazz}")

/*
    fun ok(){}
    fun ada(): KFunction<Unit>{
        fun ya(){}
        return ::ya
    }
    ada().call()
 */

    prin("decimal= $decimal decimalRound= $decimalRound decimal2= $decimal2 1.2315f.toInt()= ${1.2315f.toInt()}")
    val roundClosest= (-1).roundClosest(0 .. Int.MAX_VALUE)
    val intMinVal= Int.MIN_VALUE
    val longMinVal= Long.MIN_VALUE
    prin("longMinVal -1 = ${longMinVal -1} (longMinVal -1) == Long.MAX_VALUE => ${(longMinVal -1) == Long.MAX_VALUE}")
    prin("roundClosest= $roundClosest intMinVal= $intMinVal intMinVal.absoluteValue= ${intMinVal.absoluteValue} Int.MAX_VALUE= ${Int.MAX_VALUE}")
    prin("longMinVal= $longMinVal longMinVal.absoluteValue= ${longMinVal.absoluteValue} Long.MAX_VALUE= ${Long.MAX_VALUE}")
//    val newVal= 1254.6178.round(-2)
    val double1= 12345.678.round(-2)
    val float1= 7654.321f//.round(-2, RoundMethod.CEIL)
    val float2= 7654.321//.round(-2, RoundMethod.CEIL)
    val float3= 7654.321f//.round(-2, RoundMethod.CEIL)
    val res= (((double1 + float1).round(-2) - float2).round(-2) + float3).round(-2)

    prin("double1= $double1 float1= $float1 res= $res")

    prin("log10(123.0).toInt() +1= ${log10(123.0).toInt() +1}")
    prin("1234.5146.getNumberAtDigit(-4)= ${1234.5146.getNumberAtDigit(-4)}")
    prin("1234.getNumberAtDigit(-3)= ${1234.getNumberAtDigit(-3)}")
    prin("1254.6178f.round(-2, RoundMethod.FLOOR)= ${1254.6178f.round(-2, RoundMethod.FLOOR)}")
    prin("1234.round(-3)= ${1234.round(-3)}")
    prin("10.toString(2) = ${10.toString(2)} 10 shl 3 = ${10 shr 3} 10 shl 1 = ${10 shl 1}")
    prin("123.456.getDecimal()= ${123.456.getDecimal()}")
//    prin("(123 shr 3) + (123 shr 1) = ${(123 shr 3) + (123 shr 1)}")

    val a= plus(2L, 3f)
    prin("a= $a type= ${a::class}")
    var pow= 2 pow 0x2
    pow -= 1
    val kur= 1 / 2.5
    pow %= 2.5
    ++pow
    val round= pow roundClosest 1..3
    val absVal= pow.absoluteValue
    val absVal2= (-2.3).absoluteValue
    val minVal= -absVal2
    prin("pow= $pow type= ${pow::class} kur= $kur round= $round absVal= $absVal absVal2= $absVal2 minVal= $minVal")

//    val enumCompare= En.A < En.Z

    prin("En.A < En.Z= ${En.A < En.Z} En.Z > En.E= ${En.Z > En.E}")

    prin("0.0.isZero() => ${0.0.isZero()} 0.00000000.isZero() => ${0.00000000.isZero()} 0.00000001.isZero() => ${0.00000001.isZero()} 0.0000001.isPositive() => ${0.0000001.isPositive()} (-0.0000001).isNegative() => ${(-0.0000001).isNegative()}")
    prin("(-1).isNegative() => ${(-1).isNegative()} (-0.1).isNegative() => ${(-0.1).isNegative()} (-0.00000000001).isPositive() => ${(-0.00000000001).isPositive()}")

    1("aak")

    val b= 1
//    val c= 1 +if(b == 2) b else 3
    prin("1 +if(b == 2) b else 3 => ${1 +if(b == 2) b else 3}")

    for((i, projection) in inferType<Array<Int>>().arguments.withIndex())
        prin("i= $i projection= $projection")

    (1 until 10).random()
    Int::class.typeParameters.first()
 */
}



//fun KType.isExclusivelySupertypeOf(derived: KType): Boolean = isSubtypeOf(derived) && this !=
// */

//val <T> Array<T>.inferredType: InferredType{}

@ExperimentalStdlibApi
inline fun <reified T> inferType(vararg any: T): KType = typeOf<T>()

internal fun plus(a: Number, b: Number): Number = a + b
internal fun rem(a: Number, b: Number): Number = a % b

internal class BooleanWrap(private val value: String): Comparable<String> by value

internal class ADE{
    val a: ArrayList<Int> by lazy { ArrayList<Int>() }
}

internal enum class En{A, C, Z, E}

internal fun <T> ArrayList<T>.aoe(){}

internal operator fun Int.invoke(other: Any, other2: Any?= null) = prine("Int.invoke() = this= $this other= $other other2= $other2")