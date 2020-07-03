package sidev.lib.android.siframe.lifecycle.activity.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView


import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct

abstract class MvpSingleFragAct: SingleFragAct(), MvpView {
    abstract override fun initPresenter(): Presenter?
}