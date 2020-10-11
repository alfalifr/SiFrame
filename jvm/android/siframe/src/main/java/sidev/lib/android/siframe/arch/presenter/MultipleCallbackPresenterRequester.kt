package sidev.lib.android.siframe.arch.presenter


interface MultipleCallbackPresenterRequester<Req, Res> {
    fun downloadData(callback: PresenterCallback<Req,  Res>, reqCode: Req, vararg data: Pair<String, Any>)
    fun uploadData(callback: PresenterCallback<Req,  Res>, reqCode: Req, vararg data: Pair<String, Any>)
    fun sendRequest(callback: PresenterCallback<Req,  Res>, reqCode: Req, vararg data: Pair<String, Any>)
}