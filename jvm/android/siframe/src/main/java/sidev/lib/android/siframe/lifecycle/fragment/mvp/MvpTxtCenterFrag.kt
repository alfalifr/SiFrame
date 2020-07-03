package sidev.lib.android.siframe.lifecycle.fragment.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.fragment.TxtCenterFrag

abstract class MvpTxtCenterFrag : TxtCenterFrag(), MvpView {
    abstract override fun initPresenter(): Presenter?
//    override fun render(state: S) {}
}