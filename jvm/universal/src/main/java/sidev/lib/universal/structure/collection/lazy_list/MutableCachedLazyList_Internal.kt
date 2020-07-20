package sidev.lib.universal.structure.collection.lazy_list

internal interface MutableCachedLazyList_Internal<K, V>
    : MutableCachedLazyList<K, V>, MutableLazyList_Internal<Pair<K, V>>