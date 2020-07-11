package sidev.lib.universal.structure.tool

import sidev.lib.universal.structure.Node3

class Node3List<T>: DataManager{
    var start: Node3<T>?= null
    var now: Node3<T>?= null

}

/**
 * <11 Juli 2020> => Sementara hanya bisa melakukan [DEPTH_FIRST_PRE_ORDER].
 */
class Node3Iterator<T>(var now: Node3<T>?): Iterator<T?>{
    companion object{
        @JvmStatic
        val DEPTH_FIRST_PRE_ORDER= 1
        @JvmStatic
        val DEPTH_FIRST_IN_ORDER= 2
        @JvmStatic
        val DEPTH_FIRST_POST_ORDER= 3
    }
//    var start: Node3<T>?= null

    var method= DEPTH_FIRST_PRE_ORDER


    override fun hasNext(): Boolean
        = now != null

    override fun next(): T?{
        return when(method){
            //value-child-next
            DEPTH_FIRST_PRE_ORDER -> {
                val value= now!!.value
                var now= if(now!!.child != null) now!!.child
                    else now!!.next

                /** Jika [now] gak punya child atau next, maka cek ke parent.next. */
                if(now == null){
                    do{
                        now= this.now!!.parent
                        if(now == null) break
                    } while (now!!.next == null)

                    if(now != null) now= now.next
                }
                this.now= now
                value
            }
/*
            //child-value-next
            DEPTH_FIRST_POST_ORDER -> {
                val value: T?= null
                var now= if(now!!.child != null) now!!.child
                    else now!!.next

                /** Jika [now] gak punya child atau next, maka cek ke parent.next. */
                if(now == null){
                    var nowTemp= now
                    while (nowTemp != null){
                        nowTemp= if(nowTemp!!.child != null) nowTemp!!.child
                            else nowTemp!!.next
                    }

                    if(now != null) now= now.next
                }
                this.now= now
                value
            }
 */
            else -> null
        }
    }
}