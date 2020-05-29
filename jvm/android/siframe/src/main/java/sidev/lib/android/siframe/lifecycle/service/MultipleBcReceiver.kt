package sidev.lib.android.siframe.lifecycle.service

import android.content.Context
import android.content.Intent

class MultipleBcReceiver(c: Context) : SimpleAbsBcReceiver(c) {
    interface OnReceiveListener {
        fun onReceive(c: Context?, i: Intent?)
    }

    private val receivers= ArrayList<OnReceiveListener>()

    override fun onReceive(context: Context?, intent: Intent?) {
        iterateReceiver(context, intent)
    }

    private fun iterateReceiver(context: Context?, intent: Intent?){
        for(r in receivers)
            r.onReceive(context, intent)
    }

    fun addOnReceiveListener(l: OnReceiveListener){
        receivers.add(l)
    }
    fun addOnReceiveListener(l: (context: Context?, intent: Intent?) -> Unit){
        receivers.add(object : OnReceiveListener{
            override fun onReceive(c: Context?, i: Intent?) {
                l(c, i)
            }
        })
    }

    fun removeOnReceiveListener(l: OnReceiveListener){
        receivers.remove(l)
    }
    fun clearOnReceiveListener(){
        receivers.clear()
    }
}