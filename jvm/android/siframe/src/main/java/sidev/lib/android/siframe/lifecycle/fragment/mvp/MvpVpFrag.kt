package sidev.lib.android.siframe.lifecycle.fragment.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.lifecycle.fragment.VpFrag

abstract class MvpVpFrag<F: Frag> : VpFrag<F>(), MvpView {
    abstract override fun initPresenter(): Presenter?
}