package sidev.lib.android.siframe.lifecycle.activity.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.activity.SingleFragDrawerAct_BarContentNav_Simple

open class MvpSingleFragDrawerAct_BarContentNav_Simple: SingleFragDrawerAct_BarContentNav_Simple(),
    MvpView {
    override fun initPresenter(): Presenter?= null
    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?) {}
    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?) {}
}