package sidev.lib.android.siframe.arch._obj

import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.viewmodel.FiewModel
import sidev.lib.android.siframe.arch.viewmodel.LifeData
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase

/**
 * Kelas ini hanya digunakan untuk menyimpan data [StateProcessor] pada arsitektur MVI
 * dan tidak melakukan obervasi apapun terhadap data yg disimpan di kelas ini.
 */
class MviFiewModel : FiewModel()