package sidev.lib.android.siframe.intfc.lifecycle.rootbase

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.base.LifecycleRootBase
import sidev.lib.android.siframe.arch.viewmodel.FiewModel
import sidev.lib.android.siframe.arch.viewmodel.VmFactory
//import sidev.lib.android.siframe.arch.viewmodel.VmProvider
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase
import sidev.lib.android.siframe.intfc.prop.CtxProp

//var dg awalan _ brarti udah diinit scr internal, atau lateinit
interface ViewModelBase : LifecycleRootBase, ExpirableBase, InterruptableBase,
    LifecycleOwner, ViewModelStoreOwner, CtxProp{

//    override var isBusy: Boolean

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