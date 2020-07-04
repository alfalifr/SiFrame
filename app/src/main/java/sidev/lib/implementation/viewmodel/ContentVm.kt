package sidev.lib.implementation.viewmodel

import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.arch.viewmodel.LifeData
import sidev.lib.android.siframe.arch.viewmodel.FiewModel
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.presenter.ContentPresenter
import sidev.lib.implementation.util.Const
import sidev.lib.universal.tool.util.ThreadUtil

class ContentVm(vmBase: ViewModelBase) : FiewModel(vmBase){
    companion object{
        val CONTENT= ContentPresenter.REQ_GET_CONTENT
        val LOGIN= ContentPresenter.REQ_LOGIN
    }
    override var presenter: Presenter?= ContentPresenter(this)

    override fun onRepoRes(
        reqCode: String,
        resCode: Int,
        data: Map<String, Any>?,
        liveData: LifeData<Any>
    ) {
        loge("onRepoRes()")
        when(reqCode){
            CONTENT -> {
                loge("onRepoRes() CONTENT")
                val va= data?.get(Const.DATA_CONTENT)

                loge("onRepoRes() CONTENT val == null => ${va == null}")

                liveData.value= va
/*
                ThreadUtil.delayRun(3000){
                    liveData.value= null
                }
 */
            }
            LOGIN -> {
                loge("onRepoRes() LOGIN")
                liveData.value= resCode == Const.RES_OK
            }
        }
    }
}