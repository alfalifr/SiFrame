package sidev.lib.android.siframe.lifecycle.fragment.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.fragment.DrawerVpFrag
import sidev.lib.android.siframe.lifecycle.fragment.Frag

abstract class MvpDrawerVpFrag<F: Frag> : DrawerVpFrag<F>(), MvpView {
    abstract override fun initPresenter(): Presenter?
}