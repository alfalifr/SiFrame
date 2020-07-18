package sidev.lib.universal.`fun`

import kotlin.reflect.KClass


interface A
interface Y
interface X: Y{
    val x: String
}
open class B

class C: Z, B(){
    override val a: Boolean
        get() = true
}

data class AAD(val a: Int= 100, val b: Boolean, val bClss: B= B(), val dClss: D?)



interface Z: A{
    val a: Boolean
}
open class F{
    val f= 0
}
open class E{
    val e= true
    val fDiE= F()
}
open class D{
    val d= 1
    val dStr= ""
    val eDiD= E()
    val fDiD= F()
}

open class AA____
open class AA___: AA____()
open class AA__: AA___(){
    val aa__= 1
}
open class AA_: AA__(){
    var aa_= 123
}
sealed class AA: AA_(){
    private val aa= ""
    val dDariAA= D()
}
sealed class AB: AA(){
    private val ab= "ok"
    private val ab_2= "ok"
}
class AC(val poinConstr: Poin): Z, Y, X, AB(){
    val ac= "ppop"
    private val acPriv= "aaa"
    override val a: Boolean
        get() = true
    override val x: String
        get() = "as"
    var poin= Poin(1, 2)
    lateinit var poinLate: Poin //= Poin(1, 2)
    fun someFun(x: Int){}
}

class Poin(var x: Int= 13, var y: Int, z: Int= 10){
    var aa_diPoin= AA_()
}

class Bool{
    val bool= true
}


class AD()


fun main(args: Array<String>){
//    Class.forName("").kotlin.java.isInterface
    val classX= X::class
    val classX2= X::class

    prind("classX= $classX classX == classX2 => ${classX == classX2}")

    for(supert in AC::class.supertypes){
        (supert.classifier as KClass<*>)
        println("super= $supert supert is KClass<*> = ${supert.classifier is KClass<*>} isInterface= ${supert.isInterface}")
    }
/*
    println("\n=============BATAS #1==============\n")

    val supertypeSeq= AC::class.supertypesJvm(true)

    for((i, supert) in supertypeSeq.withIndex()){
        println("i= $i super= $supert isInterface= ${supert.isInterface}")
    }

    println("\n=============BATAS #2==============\n")

    for((i, supert) in supertypeSeq.withIndex()){
        println("i= $i super= $supert isInterface= ${supert.isInterface}")
    }

    println("\n=============BATAS superInterfacesTree ==============\n")

    for((i, supert) in AC::class.java.superInterfacesTree.withIndex()){
        println("i= $i super= $supert isInterface= ${supert.isInterface}")
    }

    println("\n=============BATAS==============\n")

    for((i, supert) in AC::class.java.superclassesTree.withIndex()) {
        println("i= $i super= $supert isInterface= ${supert.isInterface}")
    }

    println("\n=============BATAS sealedSubClass for biasa ==============\n")

    for((i, subclass) in AA::class.sealedSubclasses.withIndex()){
        println("i= $i subclass= $subclass isSealed= ${subclass.isSealed}")
    }

    println("\n=============BATAS sealedSubClass==============\n")

    for((i, subclass) in AA::class.sealedSubClassesTree.withIndex()){
        println("i= $i subclass= $subclass isSealed= ${subclass.isSealed}")
    }


    println("\n============= AC::class.getSealedClassName()= ${AC::class.getSealedClassName()} ==============\n")

    println("\n=============BATAS==============\n")

    println("default int= ${defaultPrimitiveValue<Int>()}")
    println("default val == null => ${defaultPrimitiveValue<Array<Int>>() == null}")
    println("Array<String>::class.qualifiedName= ${Array<Int>::class.qualifiedName}")
    println("Array<Int>::class == Array<String>::class => ${Array<Int>::class == Array<String>::class}")
    val acObj= AC()
    println("AAD data= ${new<AAD>()}")
* /
    println("\n=============BATAS nestedPropertiesTree ==============\n")
    for((i, prop) in AC::class.nestedPropertiesTree.withIndex())
        println("i= $i prop name= $prop ") //val= ${try{ prop.forcedGet(acObj as AC)}}
/ *
    println("\n=============BATAS propertiesTree_simple ==============\n")
    for((i, prop) in AC::class.propertiesTree_.withIndex())
        println("i= $i prop name= ${prop.name} ") //val= ${try{ prop.forcedGet(acObj as AC)}}
// * /
    println("\n=============BATAS declaredPropertiesTree ==============\n")
    for((i, prop) in AC::class.declaredPropertiesTree.withIndex())
        println("i= $i prop name= ${prop.name} ") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS nestedDeclaredPropertiesTree ==============\n")
    for((i, prop) in AC::class.nestedDeclaredPropertiesTree.withIndex())
        println("i= $i prop name= ${prop.name} prop= $prop") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS implementedPropertiesTree ==============\n")
    for((i, prop) in AC::class.implementedPropertiesTree.withIndex())
        println("i= $i prop name= ${prop.name} prop= $prop") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS nestedImplementedPropertiesTree ==============\n")
    for((i, prop) in AC::class.nestedImplementedPropertiesTree.withIndex())
        println("i= $i prop name= ${prop.name} prop= $prop") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS declaredMemberProperties ==============\n")
    for((i, prop) in AC(Poin(1,2))::class.declaredMemberProperties.withIndex())
        println("i= $i prop name= ${prop.name} ") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS Z declaredMemberProperties ==============\n")
    for((i, prop) in Z::class.declaredMemberProperties.withIndex())
        println("i= $i prop name= ${prop.name} ") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS superclasses ==============\n")
    for((i, superclass) in AC::class.superclasses.withIndex())
        println("i= $i superclass name= ${superclass.qualifiedName} ") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS classesTree ==============\n")
    for((i, clazz) in AC::class.classesTree.withIndex())
        println("i= $i class name= ${clazz.qualifiedName} ") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS implementedPropertiesValueMap ==============\n")
    for((i, pairOfProp) in AC(Poin(1,2)).implementedPropertiesValueMap.withIndex())
        println("i= $i prop= ${pairOfProp.first} value= ${pairOfProp.second}") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS nestedImplementedPropertiesValueMap ==============\n")
    for((i, pairOfProp) in AC(Poin(1,2)).nestedImplementedPropertiesValueMap.withIndex())
        println("i= $i prop= ${pairOfProp.first} value= ${pairOfProp.second}") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS implementedPropertiesValueMapTree ==============\n")
    for((i, pairOfProp) in AC(Poin(1,2)).implementedPropertiesValueMapTree.withIndex())
        println("i= $i prop= ${pairOfProp.first} value= ${pairOfProp.second}") //val= ${try{ prop.forcedGet(acObj as AC)}}

    println("\n=============BATAS nestedImplementedPropertiesValueMapTree ==============\n")
    for((i, pairOfProp) in AC(Poin(1,2)).nestedImplementedPropertiesValueMapTree.withIndex())
        println("i= $i prop= ${pairOfProp.first} value= ${pairOfProp.second}") //val= ${try{ prop.forcedGet(acObj as AC)}}

    val oldAc= AC(Poin(2,3))
    var newAc= oldAc//.clone(true)
    oldAc.poin.x= 100

    newAc= oldAc.clone(true)
/ *
    { clazz, param ->
        prine("Poin param= $param nmae= ${param.name} kind= ${param.kind}")
        when(clazz){
            AC::class -> Poin(31, 41)
            Poin::class -> {
//                param.kind
//                prine("Poin param= $param nmae= ${param.name} kind= ${param.kind}")
                when(param.name){
//                    "x" -> 451
                    "y" -> 651
                    else -> null
                }
            }
            else -> null
        }
    }
// * /
    oldAc.poin.x= 70
    oldAc.poin.y= 7
    oldAc.poin.aa_diPoin.aa_= 17
    oldAc.aa_= 43
    oldAc.poinLate= Poin(33, 73)
    oldAc.poinConstr.x= 12

    println("\n=============BATAS memberFunctions ==============\n")
    for((i, func) in oldAc::class.memberFunctions.withIndex()){
        println("i= $i func= $func")
        for((u, param) in func.parameters.withIndex()){
            println("__ u= $u param= $param name= ${param.name} kind= ${param.kind} className= ${param::class.simpleName}")
        }
    }

/ *
    oldAc.aa_
    newAc.aa_
    oldAc.poin.aa_diPoin.aa_
    newAc.poin.aa_diPoin.aa_
    oldAc.poin.x
    oldAc.poin.y
    newAc.poin.x
    newAc.poin.y
 * /

    println("\n=============BATAS clone() ==============\n")
    println("oldAc.poin.x= ${oldAc.poin.x} oldAc.poin.y= ${oldAc.poin.y} newAc.poin.x= ${newAc.poin.x} newAc.poin.y= ${newAc.poin.y}")
    println("oldAc.poin.aa_diPoin.aa_= ${oldAc.poin.aa_diPoin.aa_} newAc.poin.aa_diPoin.aa_= ${newAc.poin.aa_diPoin.aa_}")
    println("oldAc.aa_= ${oldAc.aa_} newAc.aa_= ${newAc.aa_}")
    println("oldAc.poinConstr.x= ${oldAc.poinConstr.x} oldAc.poinConstr.y= ${oldAc.poinConstr.y} newAc.poinConstr.x= ${newAc.poinConstr.x} newAc.poinConstr.y= ${newAc.poinConstr.y}")
    println("oldAc.poinLate.x= ${oldAc.poinLate.x} oldAc.poinLate.y= ${oldAc.poinLate.y} newAc.poinLate?.x= ${newAc.getLateinit { poinLate.x }} newAc.poinLate?.y= ${newAc.getLateinit { poinLate.y }}")
 */

    println("\n============= BATAS sealedSubclasses ==============\n")
    for((i, sub) in AA::class.sealedSubclassesTree.withIndex()){
        println("i= $i sub sealed= $sub")
    }
}
