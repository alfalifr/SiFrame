package sidev.lib.android.siframe.lifecycle.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.universal.tool.util.ReflexUtil

class VmFactory(val lifecycleOwner: LifecycleOwner) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try{
            //Kelas ini menganggap T sebagai subclass dari SifViewModel
            ReflexUtil.newInstance<T>(
                modelClass.name,
                arrayOf(ViewModelBase::class.java),
                arrayOf(lifecycleOwner)
            )
        } catch (e: Exception){
            //Jika ternyata T bkn subclass dari SifViewModel, maka dilakukan instantiate ViewModel dg param konstruktor kosongan
            ReflexUtil.newInstance(modelClass.name)
        }
    }
}