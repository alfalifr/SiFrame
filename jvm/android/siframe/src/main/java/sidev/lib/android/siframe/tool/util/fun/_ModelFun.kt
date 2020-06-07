package id.go.surabaya.ediscont.utilities.modelutil

import sidev.lib.android.siframe.model.FK_M


fun <TO> FK_M<TO>?.firstObj(): TO? {
    return if(this == null) null
        else toObj?.get(0)
}
fun <TO> FK_M<TO>?.firstId(): String? {
    return if(this == null) null
        else toId[0]
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