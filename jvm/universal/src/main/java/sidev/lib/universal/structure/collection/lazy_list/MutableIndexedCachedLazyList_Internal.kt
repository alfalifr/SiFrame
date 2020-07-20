package sidev.lib.universal.structure.collection.lazy_list

/** [MutableCachedLazyList] yg key-nya berupa [Int]. */
internal interface MutableIndexedCachedLazyList_Internal<T>
    : MutableIndexedCachedLazyList<T>, MutableCachedLazyList_Internal<Int, T>