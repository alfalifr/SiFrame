package sidev.lib.android.siframe.arch.viewmodel

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