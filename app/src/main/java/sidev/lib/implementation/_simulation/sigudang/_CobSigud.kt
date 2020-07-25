package sidev.lib.implementation._simulation.sigudang

import sidev.lib.implementation._simulation.sigudang.dummy.inboundList_created
import sidev.lib.universal.`fun`.clone
import sidev.lib.universal.`fun`.nestedImplementedPropertiesValueMapTree
import sidev.lib.universal.`fun`.prin

fun main(){
    val oldBound= inboundList_created[0]
    val oldProd= oldBound.productList?.get(2)?.product
    val newProd= oldProd?.clone()
    val newBound= oldBound.clone()

    oldProd?.name= "bajigur"
    newProd?.name= "om pyong"
    val nBoundNProd= newBound.productList?.get(2)?.product?.name


    prin("\n\n=============== BATAS product =============\n\n")
    prin("oldProd == newProd => ${oldProd == newProd} oldProd?.name= ${oldProd?.name} newProd?.name= ${newProd?.name} nBoundNProd= $nBoundNProd")

    prin("\n\n=============== BATAS oldProd props =============\n\n")
    for((i, valMap) in oldProd!!.nestedImplementedPropertiesValueMapTree.withIndex()){
        prin("i= $i prop= $valMap")
    }

    prin("\n\n=============== BATAS productList props =============\n\n")
    for((i, valMap) in newBound.productList!!.nestedImplementedPropertiesValueMapTree.withIndex()){
        prin("i= $i prop= $valMap")
    }

    prin("\n\n=============== BATAS newProd props =============\n\n")
    for((i, valMap) in newProd!!.nestedImplementedPropertiesValueMapTree.withIndex()){
        prin("i= $i prop= $valMap")
    }


    prin("\n\n=============== BATAS bbrp item =============\n\n")
    prin("oldBound == newBound => ${oldBound == newBound} oldBound.productList?.get(2)?.product == newBound.productList?.get(2)?.product => ${oldBound.productList?.get(2)?.product == newBound.productList?.get(2)?.product}")

    prin("\n\n=============== BATAS oldBound =============\n\n")
    for((i, valMap) in oldBound.nestedImplementedPropertiesValueMapTree.withIndex()){
        prin("i= $i prop= $valMap")
    }
    prin("\n\n=============== BATAS newBound =============\n\n")
    for((i, valMap) in newBound.nestedImplementedPropertiesValueMapTree.withIndex()){
        prin("i= $i prop= $valMap")
    }
}