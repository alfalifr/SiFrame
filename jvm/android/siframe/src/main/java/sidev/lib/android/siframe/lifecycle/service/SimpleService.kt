package sidev.lib.android.siframe.lifecycle.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import sidev.lib.collection.removeAllValue

open class SimpleService : Service(){
    private val bcReceivers= HashMap<Array<out String>, SimpleAbsBcReceiver>()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {}
    override fun onDestroy() {}


    fun registerReceiver(vararg action: String, r: SimpleAbsBcReceiver){
        bcReceivers[action]= r
        r.register(*action)
    }
    fun registerReceiver(vararg action: String, r: (context: Context?, intent: Intent?) -> Unit): SimpleAbsBcReceiver{
        val rObj= object : SimpleAbsBcReceiver(this){
            override fun onReceive(context: Context?, intent: Intent?) {
                r(context, intent)
            }
        }
        bcReceivers[action]= rObj
        rObj.register(*action)
        return rObj
    }

    fun unregister(action: Array<String>){
        bcReceivers.remove(action)
    }
    fun unregister(r: SimpleAbsBcReceiver){
        bcReceivers.removeAllValue(r)
    }
}