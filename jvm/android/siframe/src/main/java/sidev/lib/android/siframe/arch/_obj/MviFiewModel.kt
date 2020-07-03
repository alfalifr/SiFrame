package sidev.lib.android.siframe.arch._obj

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.viewmodel.FiewModel
import sidev.lib.android.siframe.arch.viewmodel.LifeData
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase

class MviFiewModel(vmBase: ViewModelBase) : FiewModel(vmBase){
    override fun onRepoRes(
        reqCode: String,
        resCode: Int,
        data: Map<String, Any>?,
        liveData: LifeData<Any>
    ) {
        /* Abaikan karena FiewModel ini hanya digunakan untuk menyimpan currentState */
    }

    /* Abaikan karena FiewModel ini hanya digunakan untuk menyimpan currentState */
    override val presenter: Presenter?= null
}