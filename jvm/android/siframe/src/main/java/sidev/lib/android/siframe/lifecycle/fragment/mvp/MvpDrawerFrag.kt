package sidev.lib.android.siframe.lifecycle.fragment.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.fragment.DrawerFrag

abstract class MvpDrawerFrag : DrawerFrag(), MvpView {
    abstract override fun initPresenter(): Presenter?
}