package sidev.lib.android.siframe.tool.util.`fun`

import android.util.SparseArray
import androidx.core.util.forEach
import androidx.core.util.isNotEmpty
import androidx.core.util.set
import sidev.lib.android.siframe.model.DataWithId
import sidev.lib.android.siframe.model.FK_M
import sidev.lib.collection.findIndexed


fun <TO> FK_M<TO>?.toObjList(): ArrayList<TO>? {
    return if(this != null){
        if(this.toObj != null && this.toObj!!.isNotEmpty()){
            val list= ArrayList<TO>()
            this.toObj!!.forEach { list.add(it) }
            list
        } else null
    } else null
}
fun <TO> FK_M<TO>?.toIdList(): ArrayList<String>? {
    return if(this != null){
        if(this.toId.isNotEmpty()){
            val list= ArrayList<String>()
            this.toId.forEach { list.add(it) }
            list
        } else null
    } else null
}

val <TO> FK_M<TO>?.firstObj: TO?
    get()= if(this == null) null
        else toObj?.get(0)

val <TO> FK_M<TO>?.firstId: String?
    get()= if(this == null) null
        else toId[0]


val <TO> FK_M<TO>?.isObjEmpty: Boolean
    get()= this?.toObj?.isEmpty() ?: true

val <TO> FK_M<TO>?.isIdEmpty: Boolean
    get()= this?.toId?.isEmpty() ?: true


/**
 * Mengecek apakah tiap toObj memiliki satu pasangan dg 1 toId.
 * Fungsi ini juga sekalian merapikan dg menyamakan indeks toObj dg indeks toId.
 * @return true jika 1 toObj berpasangan tepat dg 1 toId dan false sebaliknya.
 *         false jika:
 *          -FK_M sama dg null.
 *          -FK_M tidak memiliki toObj dan toId, artinya tidak terjadi proses perapian.
 *          -toId lebih banyak dari toObj.
 *          -Ada toId yg tidak punya pasangan dg toObj.
 */
fun <TO: DataWithId<TO>> FK_M<TO>?.tidyUp(): Boolean{
    if(this == null) return false

    //1. Cek apakah jml toObj dan toId sama. Jika toObj lebih banyak, maka toId dapat diturunkan dari toObj.id.
    //   Jika toId lebih banyak, maka langsung return false.
    val toObjCount= this.toObj?.size ?: 0
    val toIdCount= this.toId.size

    if(toIdCount > toObjCount) return false
    if(toObjCount == 0) return false

    //2. Menyamakan indeks toId dan toObj.
    val objIdWithNoIdPairList= SparseArray<String>() //Jika ada toObj yg gak punya pasangan toId, maka dapat diturunkan dari toObj.id.
    for((i, obj) in this.toObj!!.withIndex())
        objIdWithNoIdPairList[i]= obj.id

    this.toId.forEachIndexed { idInd, id ->
        val objInd= this.toObj!!.findIndexed { (_, obj) -> obj.id == id }?.index ?: -1 //.indexOf { obj -> obj.id == id }

        //3. Jika ternyata ada toId yg tidak memiliki pasangan toObj, maka return false.
        if(objInd == -1) return false
        objIdWithNoIdPairList.removeAt(objInd) //Menghilangkan toObj.id yg sudah memiliki pasangan toId.

        if(idInd != objInd){
            val tempObj= this.toObj!![objInd]
            this.toObj!![objInd]= this.toObj!![idInd]
            this.toObj!![idInd]= tempObj

            objIdWithNoIdPairList[objInd]= this.toObj!![objInd].id //this.toObj!![objInd] adalah toObj yg gak punya pasangan toId
            objIdWithNoIdPairList.removeAt(idInd) //this.toObj!![objInd] adalah toObj dg pasangan toId, jadi harus dihilangkan dari daftar.
        }
    }

    //4. Jika ada toObj yg gak punya pasangan toId, maka toId diturunkan dari toObj.id.
    if(objIdWithNoIdPairList.isNotEmpty()){
        val idList= this.toIdList()!!
        //5. Ditambah dulu idList-nya agar nti pas diassign dari objIdWithNoIdPairList ke idList tidak IndexOutOfBoundError.
        //   Hal tersebut dikarenakan objIdWithNoIdPairList merupakan SparseArray dg indeks yg tidak urut.
        for(i in 0 until objIdWithNoIdPairList.size())
            idList.add("")

        objIdWithNoIdPairList.forEach { i, objId -> idList[i]= objId }
        this.toId= idList.toTypedArray()
    }
    //6. Hasil akhir dari perapian ini adalah indeks toObj yg sama dg indeks toId.
    return true
}


fun <TO> fkmId(vararg id: String): FK_M<TO>{
    return FK_M(arrayOf(*id))
}

inline fun <reified TO> fkmOf(vararg pair: Pair<String, TO>): FK_M<TO>{
    val objList= ArrayList<TO>()
    val idArr= Array(pair.size){
        objList.add(pair[it].second)
        pair[it].first
    }
    return FK_M(idArr, objList.toTypedArray())
}

inline fun <reified TO: DataWithId<*>> fkmFrom(vararg model: TO): FK_M<TO>{
    val idArray= Array(model.size){ model[it].id }
    val objArray= Array(model.size){ model[it] }
    return FK_M(idArray, objArray)
}