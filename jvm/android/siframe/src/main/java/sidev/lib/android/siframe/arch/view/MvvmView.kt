package sidev.lib.android.siframe.arch.view

import android.view.View
import sidev.lib.android.siframe.arch._obj.InternalFiewModel
import sidev.lib.android.siframe.arch.type.Mvvm
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.tool.ViewContentExtractor
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull

/**
 * Hanya sbg penanda bahwa kelas turunan ini merupakan komponen View pada arsitektur MVVM.
 * Scr default semua lifecycle view (activity dan fragment) pada framework ini menggunakan
 * arsitektur MVVM, namun tidak meng-extend interface [MvvmView].
 */
interface MvvmView: ArchView, Mvvm, ViewModelBase, AutoRestoreViewClient{
    companion object{
        val KEY_VM_MVVM_AUTO_RESTORE= "_internal_vm_mvvm_auto_restore"
    }

    override fun registerAutoRestoreView(id: String, v: View) {
        val vm= getViewModel(InternalFiewModel::class.java)
        vm.get<ViewContentExtractor>(KEY_VM_MVVM_AUTO_RESTORE)
            .notNull { it.value!!.registerView(id, v) }
            .isNull {
                val extractor= ViewContentExtractor()
                extractor.registerView(id, v)
                vm.addValue(KEY_VM_MVVM_AUTO_RESTORE, extractor)
            }
    }

    /**
     * Fungsi dipanggil sesaat sebelum view dihancurkan atau saat onDestroy
     */
    override fun extractAllViewContent() {
        getViewModel(InternalFiewModel::class.java).get<ViewContentExtractor>(KEY_VM_MVVM_AUTO_RESTORE)
            .notNull {
                val extractor= it.value!!
                extractor.extractAllViewContent()
                extractor.clearAllSavedViews()
            }
    }
}