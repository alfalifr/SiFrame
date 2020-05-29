package sidev.lib.implementation.universal.`fun`

fun CharSequence.getPrefixIn(array: Array<String>): String? {
    for(e in array)
        if(this.startsWith(e))
            return e
    return null
}
fun CharSequence.getPrefixIn(coll: Collection<String>): String? {
    for(e in coll)
        if(this.startsWith(e))
            return e
    return null
}

fun CharSequence.startsWithin(array: Array<String>): Boolean {
    for(e in array)
        if(this.startsWith(e))
            return true
    return false
}
fun CharSequence.startsWithin(coll: Collection<String>): Boolean {
    for(e in coll)
        if(this.startsWith(e))
            return true
    return false
}

fun CharSequence.getQuoted(quoter: CharSequence, startIndex: Int= 0, withQuote: Boolean= false): String? {
    for(i in startIndex until this.length){
        for(u in i+1 until this.length){
            if(this.substring(i, u) == quoter){
                for(o in startIndex until this.length)
                    for(p in i+1 until this.length)
                        if(this.substring(o, p) == quoter){
                            val value= this.substring(u, o)
                            return if(withQuote) quoter +value +quoter
                                else value
                        }
            }
        }
    }
    return null
}
