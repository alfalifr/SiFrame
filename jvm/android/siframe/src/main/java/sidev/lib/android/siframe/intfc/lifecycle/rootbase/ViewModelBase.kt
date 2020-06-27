package sidev.lib.android.siframe.intfc.lifecycle.rootbase

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.base.LifecycleRootBase
import sidev.lib.android.siframe.lifecycle.viewmodel.FiewModel
import sidev.lib.android.siframe.lifecycle.viewmodel.VmFactory

//var dg awalan _ brarti udah diinit scr internal, atau lateinit
interface ViewModelBase : LifecycleRootBase, ExpirableBase, LifecycleOwner, ViewModelStoreOwner{
    var _vmProvider: ViewModelProvider
//    val vmStoreOwner: ViewModelStoreOwner
//    val lifecycleOwner: LifecycleOwner

    override fun ___initRootBase(vararg args: Any) {
        _vmProvider= ViewModelProvider(this, VmFactory(this))
    }

    fun <T: FiewModel> getViewModel(cls: Class<T>): T {
        return _vmProvider[cls]
    }

//    fun onViewModelDataFail(reqCode: String, resCode: Int, msg: String?, e: Exception?)
}