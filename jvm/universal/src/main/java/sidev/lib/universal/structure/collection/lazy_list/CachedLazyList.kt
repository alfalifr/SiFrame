package sidev.lib.universal.structure.collection.lazy_list

import sidev.lib.universal.`fun`.prine

/**
 * [LazyList] yg menyimpan data dari [Iterator.next].
 */
interface CachedLazyList<K, V> : LazyList<Pair<K, V>> {
    /** Digunakan untuk mengambil iterator yg berfungsi sbg pengisi [CachedLazyList] ini. */
    override val builderIterator: Iterator<Pair<K, V>>

    /** Mengambil element yg sudah ada pada [MutableCollection], bkn dari [builderIterator]. */
    fun getExisting(key: K): V?
    /**
     * Mengambil key yg sudah ada pada [MutableCollection].
     * Perlu diperhatikan bahwa pada koleksi yg key nya [K] berupa [Int], interface [CachedLazyList] ini
     * menganggap key ada jika hasil @return fungsi ini tidak null. Jadi jika fungsi ini @return -1,
     * interface ini menganggap key -1 ada di [MutableCollection].
     */
    fun getExistingKey(value: V): K?

    /** Mengecek apakah [value] sudah ada belum pada [MutableCollection]. */
    fun containsExistingValue(value: V): Boolean
    /** Mengecek apakah [key] sudah ada belum pada [MutableCollection]. */
    fun containsExistingKey(key: K): Boolean

    /**
     * Digunakan untuk menentukan apakah [addedNext] yg baru diambil dari [builderIterator.next]
     * sesuai dg definisi elemen yg dicari di dalam [MutableCollection].
     * Definisi elemen yg dicari biasanya memiliki pola sprti pada [List.get] yg menggunakan [key]
     * sebagai parameter.
     *
     * [key] dapat berupa null
     */
    fun isNextMatched(key: K, addedNext: V): Boolean

    /** Untuk menirukan [MutableCollection.add]. */
    fun addNext(key: K, value: V): Boolean

//    /** Digunakan untuk mengambil key dari [addedNext] yg diambil dari [builderIterator]. */
//    fun extractKeyFrom(addedNext: V): K

    /**
     * @return nilai [V] yg baru saja di tambahkan menggunakan [addNext] mengembalikan `true`,
     *   null jika operasi [addNext] mengembalikan `false`.
     */
    fun getNext(): Pair<K, V>?{
        val added= builderIterator.next()
//        prine("getNext() added.first= ${added.first} added.second= ${added.second}")
//        val key= extractKeyFrom(added)
        return if(addNext(added.first, added.second)) added
        else null
    }

    /** Mengambil dari [MutableCollection] jika sudah ada, jika belum maka mengambil dari [builderIterator]. */
    fun findNext(key: K): V?{
        var existing= getExisting(key)
//        prine("findNext() existing $existing existing == null && iteratorHasNext() = ${existing == null && iteratorHasNext()}")
        if(existing == null && iteratorHasNext()){
            do{
                val next= getNext()
                existing= next?.second
//                prine("findNext() while existing $existing")
            } while((existing == null || !isNextMatched(next!!.first, existing)) && iteratorHasNext())
        }
/*
        while((existing == null || !isNextMatched(key, existing)) && iteratorHasNext().also { hasNext= it }){
            val bool= if(existing != null) !isNextMatched(key, existing) else null
            prine("bool= $bool hasNext= $hasNext")
            val next= getNext()
            existing= next?.second
        }
 */
        return existing
    }
    /** Mengecek apakah [value] sudah ada belum pada [MutableCollection]. Jika belum, maka pengecekan dilakukan pada [builderIterator]. */
    fun containsNextValue(value: V): Boolean{
        var containsValue= containsExistingValue(value)
        if(!containsValue && iteratorHasNext()){
            do {
                val addedNext= getNext()
                containsValue= addedNext?.second == value
            } while((addedNext == null || !containsValue) //!containsExistingValue(value).also { containsValue= it }
                && iteratorHasNext())
        }
        return containsValue
    }


    /** Mengambil key dari [MutableCollection] jika sudah ada, jika belum maka mengambil dari fungsi [getExistingKey]. */
    fun findNextKey(value: V): K?{
        var existingKey= getExistingKey(value)
        if(existingKey == null && iteratorHasNext()){
            var addedNext: Pair<K, V>?
            do {
                addedNext= getNext() //Anggapanya jika [value] gakda, maka cek hingga [addedNext] sama dg [value].
            } while(addedNext?.second != value && iteratorHasNext())

            existingKey= addedNext?.first
/*
            if(addedNext != null)
                existingKey= getExistingKey(addedNext)
 */
        }
        return existingKey
    }
    /** Mengecek apakah [key] sudah ada belum pada [MutableCollection]. Jika belum, maka pengecekan dilakukan pada [builderIterator]. */
    fun containsNextKey(key: K): Boolean{
        var containsKey: Boolean= containsExistingKey(key)
        if(!containsKey && iteratorHasNext()){
            do {
                val addedNext= getNext()
                containsKey= addedNext?.first == key
            } while((addedNext == null || !containsKey) //!containsExistingKey(key).also { containsKey= it }
                && iteratorHasNext())
        }
        return containsKey
    }
}