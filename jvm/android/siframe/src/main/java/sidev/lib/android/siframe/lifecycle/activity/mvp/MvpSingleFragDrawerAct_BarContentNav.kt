package sidev.lib.android.siframe.lifecycle.activity.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.activity.SingleFragDrawerAct_BarContentNav

abstract class MvpSingleFragDrawerAct_BarContentNav: SingleFragDrawerAct_BarContentNav(),
    MvpView {
    abstract override fun initPresenter(): Presenter?
}