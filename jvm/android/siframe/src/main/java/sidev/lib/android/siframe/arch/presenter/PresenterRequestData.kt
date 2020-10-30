package sidev.lib.android.siframe.arch.presenter


data class PresenterRequestData<Req, Res>(
//    override val presenter: ArchPresenter<Req, Res, out PresenterCallback<Req, Res>>,
    override val request: Req,
    override val callback: PresenterCallback<Req, Res>
): PresenterRequest<Req, Res>