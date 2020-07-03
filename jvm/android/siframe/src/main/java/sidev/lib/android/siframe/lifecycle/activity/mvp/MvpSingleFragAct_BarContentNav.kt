package sidev.lib.android.siframe.lifecycle.activity.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct_BarContentNav

//import sidev.kuliah.agradia.R

abstract class MvpSingleFragAct_BarContentNav: SingleFragAct_BarContentNav(), MvpView {
    abstract override fun initPresenter(): Presenter?
}