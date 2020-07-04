package sidev.lib.android.siframe.arch.view

import sidev.lib.android.siframe.arch.type.Mvvm
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase

/**
 * Hanya sbg penanda bahwa kelas turunan ini merupakan komponen View pada arsitektur MVVM.
 * Scr default semua lifecycle view (activity dan fragment) pada framework ini menggunakan
 * arsitektur MVVM, namun tidak meng-extend interface [MvvmView].
 */
interface MvvmView: ArchView, Mvvm, ViewModelBase