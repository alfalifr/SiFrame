package sidev.lib.implementation.presenter

import sidev.lib.android.siframe.arch.presenter.*

class TestPres(c: PresenterCallbackCommon) : Presenter(c){
    override fun processRequest(request: String, data: Map<String, Any>?) {
        postSucc(1, null)
    }

    override fun checkDataIntegrity(
        request: String,
        direction: ArchPresenter.Direction,
        data: Map<String, Any>?
    ): Boolean = true
}