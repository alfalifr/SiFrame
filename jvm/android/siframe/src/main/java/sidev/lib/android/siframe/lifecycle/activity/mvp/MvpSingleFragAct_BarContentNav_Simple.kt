package sidev.lib.android.siframe.lifecycle.activity.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct_BarContentNav_Simple

//import sidev.kuliah.agradia.R

open class MvpSingleFragAct_BarContentNav_Simple: SingleFragAct_BarContentNav_Simple(),
    MvpView {
    override fun initPresenter(): Presenter?= null

    override fun onPresenterSucc(request: String, result: Int,
        data: Map<String, Any>?, resCode: Int) {}

    override fun onPresenterFail(request: String, result: Int?,
        msg: String?, e: Exception?, resCode: Int) {}
}