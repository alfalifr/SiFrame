package sidev.lib.implementation._simulation.sigudang.util

inline fun <T> Iterable<T>.filter(func: (pos: Int, e: T) -> Boolean): List<T> {
    val filteredList= ArrayList<T>()
    for((i, e) in this.withIndex())
        if(func(i, e))
            filteredList.add(e)
    return filteredList
}


inline fun <T> Iterable<T>.searchElement(filter: (element: T, pos: Int) -> Boolean): T?{
    for((i, el) in this.withIndex()){
        val bool= filter(el, i)
        if(bool) return el
    }
    return null
}

inline fun <T> Iterable<T>.indexOf(filter: (element: T, pos: Int) -> Boolean): Int{
    for((i, el) in this.withIndex()){
        val bool= filter(el, i)
        if(bool) return i
    }
    return -1
}


inline fun <T, C: MutableCollection<in String>> Iterable<T>.toStringList(dest: C, filter: (element: T, pos: Int) -> String?): C{
    dest.clear()
    for((i, el) in this.withIndex()){
        val res= filter(el, i)
        if(res != null)
            dest.add(res)
    }
    return dest
}