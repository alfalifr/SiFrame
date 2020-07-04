package sidev.lib.universal.`fun`

fun <T> T.toString(func: (obj: T) -> String): String{
    return func(this)
}

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

fun CharSequence.nextNonWhitespaceChar(startInd: Int= 0): Char? {
    for(i in startInd until this.length){
        if(!this[i].isWhitespace()){
            return this[i]
        }
    }
    return null
}


fun String.shorten(maxLen: Int, strInMid: String= " ... "): String{
    if(this.length <= maxLen) return this

//    val strInMid= " ... "

    val strInMidIndex= this.length /2
    val strLenDiff= this.length -maxLen

    val lenOfEachSideIsCut= (strLenDiff + strInMid.length) /2
    val lenOfEachSide= strInMidIndex -lenOfEachSideIsCut

    val strPrefix= this.substring(0, lenOfEachSide)
    val strSuffix= this.substring(this.lastIndex -lenOfEachSide)

    return strPrefix +strInMid +strSuffix
}