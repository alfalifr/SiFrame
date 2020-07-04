package sidev.lib.android.siframe.arch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.universal.`fun`.asNotNull
/*
<5 Juli 2020> => Kelas ini udah gak dipakai.
class VmProvider(owner: ViewModelStoreOwner, factory: Factory)
    : ViewModelProvider(owner, factory){

    operator fun <T: ViewModel?> get(modelClass: Class<T>, vmBaseReceiver: ViewModelBase): T{
        val vm= this[modelClass]
        vm.asNotNull { innerVm: FiewModel -> innerVm.vmBase= vmBaseReceiver }
        return vm
    }
}
 */