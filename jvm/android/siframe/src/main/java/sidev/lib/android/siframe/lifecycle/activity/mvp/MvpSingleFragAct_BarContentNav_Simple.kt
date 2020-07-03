package sidev.lib.android.siframe.lifecycle.activity.mvp

import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.activity.SingleFragAct_BarContentNav_Simple

//import sidev.kuliah.agradia.R

open class MvpSingleFragAct_BarContentNav_Simple: SingleFragAct_BarContentNav_Simple(),
    MvpView {
    override fun onPresenterSucc(reqCode: String, resCode: Int, data: Map<String, Any>?) {}
    override fun onPresenterFail(reqCode: String, resCode: Int, msg: String?, e: Exception?) {}
}