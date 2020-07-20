package sidev.lib.universal.structure.collection.lazy_list

interface MutableCachedLazyList<K, V>: CachedLazyList<K, V>, MutableLazyList<Pair<K, V>>{
//    fun addKeyIterator(itr: Iterator<K>): Boolean => Jika dipikir-pikir lagi, gak mungkin programmer cuma menambahkan key tanpa value.
}