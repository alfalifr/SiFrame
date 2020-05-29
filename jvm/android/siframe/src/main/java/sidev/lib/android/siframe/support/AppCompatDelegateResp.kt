package sidev.lib.android.siframe.support

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver

class AppCompatDelegateResp : AppCompatActivity(){
    fun ja(){
        lifecycle.addObserver(object: LifecycleObserver{

        })
    }
}