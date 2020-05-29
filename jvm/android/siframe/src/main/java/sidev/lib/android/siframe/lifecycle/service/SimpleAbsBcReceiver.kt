package sidev.lib.android.siframe.lifecycle.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

abstract class SimpleAbsBcReceiver(val c: Context) : BroadcastReceiver() {
    var actionFilters: Array<out String>?= null
        private set

    fun register(){
        if(actionFilters != null)
            register(*actionFilters!!)
    }
    fun register(vararg filter: String){
        this.actionFilters= filter
        val intFilter= IntentFilter()
        for(f in filter!!)
            intFilter.addAction(f)
        c.registerReceiver(this, intFilter)
    }

    fun unregister(){
        c.unregisterReceiver(this)
    }
}