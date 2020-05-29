package sidev.lib.implementation.universal.`fun`

fun <T> Array<T>.filterIn(array: Array<T>): Iterable<T> {
    val out= ArrayList<T>()
    for(e in this)
        if(e in array)
            out.add(e)
    return out
}
fun <T> Iterable<T>.filterIn(array: Array<T>): Iterable<T> {
    val out= ArrayList<T>()
    for(e in this)
        if(e in array)
            out.add(e)
    return out
}


fun CharSequence.filterIn(array: Array<String>): Iterable<String> {
    val out= ArrayList<String>()
    for(e in array)
        if(e in this)
            out.add(e)
    return out
}


fun <T: String> Iterable<T>.indexOf(e: T, ignoreCase: Boolean= false): Int {
    if(ignoreCase){
        val e2= e.toLowerCase()
        for((i, e1) in this.withIndex()){
            if(e1.toLowerCase() == e2)
                return i
        }
    } else
        return this.indexOf(e)
    return -1
}
fun <T: String> Array<T>.indexOf(e: T, ignoreCase: Boolean= false): Int {
    if(ignoreCase){
        val e2= e.toLowerCase()
        for((i, e1) in this.withIndex()){
            if(e1.toLowerCase() == e2)
                return i
        }
    } else
        return this.indexOf(e)
    return -1
}



inline fun <T> Iterable<T>.filter(func: (element: T, pos: Int) -> Boolean): List<T> {
    val filteredList= ArrayList<T>()
    for((i, e) in this.withIndex())
        if(func(e, i))
            filteredList.add(e)
    return filteredList
}


inline fun <T> Iterable<T>.search(filter: (element: T, pos: Int) -> Boolean): T?{
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