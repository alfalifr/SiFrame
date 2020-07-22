package sidev.lib.android.siframe.lifecycle.activity.mvi

import sidev.lib.android.siframe.arch.intent_state.*
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.lifecycle.activity.SingleFragDrawerAct_BarContentNav_Simple

open class MviSingleFragDrawerAct_BarContentNav_Simple
    : SingleFragDrawerAct_BarContentNav_Simple(), MviView<ViewIntent, IntentResult, ViewState<*>>{
    override var currentViewState: ViewState<*>?= null
//    override var intentConverter: IntentConverter<ViewIntent>?= null
    override fun initPresenter(): MviPresenter<ViewIntent, IntentResult, ViewState<*>>?= null
    override fun initStateProcessor(): StateProcessor<ViewIntent, IntentResult, ViewState<*>>?= null
    override fun onNoCurrentState() {}
    override fun render(state: ViewState<*>) {}
}