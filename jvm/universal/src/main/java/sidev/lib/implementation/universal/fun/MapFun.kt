package sidev.lib.implementation.universal.`fun`

/**
 * Menghapus first occurrence
 */
fun <K, V> MutableMap<K, V>.removeVal(value: V): Boolean {
    for(entry in this.entries)
        if(entry.value == value){
            this.remove(entry.key)
            return true
        }
    return false
}

/**
 * Menghapus semua occurrence
 */
fun <K, V> MutableMap<K, V>.removeAllVal(value: V): Boolean {
    var res= false
    for(entry in this.entries)
        if(entry.value == value){
            this.remove(entry.key)
            res= true
        }
    return res
}