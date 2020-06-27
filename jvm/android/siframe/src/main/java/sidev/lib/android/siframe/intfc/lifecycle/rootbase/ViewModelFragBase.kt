package sidev.lib.android.siframe.intfc.lifecycle.rootbase

import sidev.lib.android.siframe.lifecycle.viewmodel.FiewModel

/**
 * Sama dg ViewModelBase, namun khusus untuk Fragment.
 */
interface ViewModelFragBase :
    ViewModelBase {
//    var activity: FragmentActivity?
    /**
     * Berguna untuk shared ViewModel
     */
    fun <T : FiewModel> getViewModelFromAct(cls: Class<T>): T?
//        return activity.asNotNullTo { act: ViewModelBase -> act.getViewModel(cls) }
}