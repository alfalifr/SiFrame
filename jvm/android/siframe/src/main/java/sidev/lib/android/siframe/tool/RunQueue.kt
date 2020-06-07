package sidev.lib.android.siframe.tool

class RunQueue<T, R> {
    private val queue= ArrayList< Pair<(T) -> R, Boolean> >()

    fun addRunQueue(runOnce: Boolean= true, f: (T) -> R){
        queue.add( Pair(f, runOnce) )
    }
    fun iterateRunQueue(input: T){
        if(queue.isNotEmpty()){
            val removedQueue= ArrayList< Pair<(T) -> R, Boolean> >()
            for(pair in queue){
                pair.first(input)
                if(pair.second)
                    removedQueue.add(pair)
            }
            queue.removeAll(removedQueue)
        }
    }
}