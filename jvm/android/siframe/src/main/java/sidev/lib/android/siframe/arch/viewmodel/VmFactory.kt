package sidev.lib.android.siframe.arch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.universal.tool.util.ReflexUtil

class VmFactory(val vmBase: ViewModelBase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try{
            //Kelas ini menganggap T sebagai subclass dari ObsFiewModel
            ReflexUtil.newInstance<T>(
                modelClass.name,
                arrayOf(ViewModelBase::class.java),
                arrayOf(vmBase)
            )
        } catch (e: Exception){
            //Jika ternyata T bkn subclass dari ObsFiewModel, maka dilakukan instantiate ViewModel dg param konstruktor kosongan
            ReflexUtil.newInstance(modelClass.name)
        }
    }
}