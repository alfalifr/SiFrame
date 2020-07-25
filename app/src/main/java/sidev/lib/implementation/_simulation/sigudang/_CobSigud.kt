package sidev.lib.implementation._simulation.sigudang

import com.sigudang.android.models.Bound
import kotlinx.coroutines.*
import sidev.lib.implementation._simulation.sigudang.dummy.inboundList_created
import sidev.lib.universal.`fun`.clone
import sidev.lib.universal.`fun`.nestedImplementedPropertiesValueMapTree
import sidev.lib.universal.`fun`.prin

suspend fun Bound.cloneIt(): Bound = clone()

fun main(){
    val oldBound= inboundList_created[0]
    val oldProd= oldBound.productList?.get(2)?.product
    val newProd= oldProd?.clone()

    val startClone= System.nanoTime()
    val newBound: Bound= oldBound.clone()
    val endClone= System.nanoTime()
    val elapsedTime= (endClone - startClone) / 1e9

    prin("startClone= $startClone endClone= $endClone elapsedTime= $elapsedTime")

//    val newBoundList= ArrayList<Bound>()
//    val newBoundListDeferred= ArrayList<Deferred<Bound>>()
/*
    val startClone= System.nanoTime()
    GlobalScope.launch {

        val inStartClone= System.nanoTime()
        for(i in 0 until 10)
            newBoundListDeferred += async { oldBound.cloneIt() }
        for(i in 0 until 10){
            newBoundList += newBoundListDeferred[i].await()
            progress(i)
        }
        val inEndClone= System.nanoTime()
        val inElapsedTime= (inEndClone - inStartClone) / 1e9
        val inOuterElapsedTime= (inEndClone - startClone) / 1e9
        val clonedProd1= newBoundList[9].productList!!.first()
        val clonedProd2= newBoundList[3].productList!!.first()

//        newBoundListDeferred.size= ${newBoundListDeferred.size}
        prin("clonedProd1 == clonedProd2 => ${clonedProd1 == clonedProd2}")
        prin("newBoundList.size= ${newBoundList.size} newBoundListDeferred.size= ${newBoundListDeferred.size} inElapsedTime= $inElapsedTime inOuterElapsedTime= $inOuterElapsedTime")
    }
    val endClone= System.nanoTime()
    val elapsedTime= (endClone - startClone) / 1e9


    prin("startClone= $startClone endClone= $endClone elapsedTime= $elapsedTime")
    Thread.sleep(2500)
 */
///*
    oldProd?.name= "bajigur"
    newProd?.name= "om pyong"
    val nBoundNProd= newBound!!.productList?.get(2)?.product?.name


    prin("\n\n=============== BATAS product =============\n\n")
    prin("oldProd == newProd => ${oldProd == newProd} oldProd?.name= ${oldProd?.name} newProd?.name= ${newProd?.name} nBoundNProd= $nBoundNProd")

    prin("\n\n=============== BATAS oldProd props =============\n\n")
    for((i, valMap) in oldProd!!.nestedImplementedPropertiesValueMapTree.withIndex()){
        prin("i= $i prop= $valMap")
    }

    prin("\n\n=============== BATAS productList props =============\n\n")
    for((i, valMap) in newBound!!.productList!!.nestedImplementedPropertiesValueMapTree.withIndex()){
        prin("i= $i prop= $valMap")
    }

    prin("\n\n=============== BATAS newProd props =============\n\n")
    for((i, valMap) in newProd!!.nestedImplementedPropertiesValueMapTree.withIndex()){
        prin("i= $i prop= $valMap")
    }


    prin("\n\n=============== BATAS bbrp item =============\n\n")
    prin("oldBound == newBound => ${oldBound == newBound} oldBound.productList?.get(2)?.product == newBound.productList?.get(2)?.product => ${oldBound.productList?.get(2)?.product == newBound!!.productList?.get(2)?.product}")

    prin("\n\n=============== BATAS oldBound =============\n\n")
    for((i, valMap) in oldBound.nestedImplementedPropertiesValueMapTree.withIndex()){
        prin("i= $i prop= $valMap")
    }
    prin("\n\n=============== BATAS newBound =============\n\n")
    for((i, valMap) in newBound!!.nestedImplementedPropertiesValueMapTree.withIndex()){
        prin("i= $i prop= $valMap")
    }

// */
}

fun progress(int: Int) = prin("Progress: $int")