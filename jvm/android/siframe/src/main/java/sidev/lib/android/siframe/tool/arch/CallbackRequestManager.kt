package sidev.lib.android.siframe.tool.arch

import sidev.lib.`val`.QueueMode
import sidev.lib.android.siframe.arch.presenter.PresenterCallback
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/*
abstract class CallbackRequestManager<Req, Res> {
    open val queueMode: QueueMode = QueueMode.FIFO
    protected open val callbackRequestMap: MutableMap<in PresenterCallback<Req, Res>, MutableList<Req>> by lazy { HashMap() }

    fun registerRequest(callback: PresenterCallback<Req, Res>, request: Req){
        callbackRequestMap[callback]= Stack()
        (callbackRequestMap[callback]
            ?: createList().also { callbackRequestMap[callback]= it }).add(request)
    }
    fun getRequest(callback: PresenterCallback<Req, Res>): Req?{

    }

    private fun createList(): MutableList<Req> = when(queueMode){
        QueueMode.LIFO -> Stack()
        else -> ArrayList()
    }
}
 */