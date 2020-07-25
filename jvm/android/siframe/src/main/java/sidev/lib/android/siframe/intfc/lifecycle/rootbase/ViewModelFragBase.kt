package sidev.lib.android.siframe.intfc.lifecycle.rootbase

import sidev.lib.android.siframe.arch.viewmodel.FiewModel

/**
 * Sama dg ViewModelBase, namun khusus untuk Fragment.
 *
 * <4 Juli 2020> => Interface ini gak dipakai karena perubahan definisi [ViewModelBase.getViewModel]
 *  yg sblumnya digunakan untuk mengambil dari masing-masing LifecycleOwner, namun skrg definisinya adalah
 *  untuk Fragment, definisi [ViewModelBase.getViewModel] adalah ViewModel milik activity-nya.
 */
interface ViewModelFragBase : ViewModelBase {
//    var activity: FragmentActivity?

    /**
     * Berguna untuk shared ViewModel
     */
    fun <T : FiewModel> getViewModelFromAct(cls: Class<T>): T?
//        return activity.asNotNullTo { act: ViewModelBase -> act.getViewModel(cls) }
}