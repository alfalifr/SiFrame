package sidev.lib.android.siframe.lifecycle.activity.mvi

import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.lifecycle.activity.SingleFragDrawerAct_BarContentNav_Simple

open class MviSingleFragDrawerAct_BarContentNav_Simple
    : SingleFragDrawerAct_BarContentNav_Simple(), MviView<ViewState, ViewIntent>{
    override var intentConverter: IntentConverter<ViewIntent>?= null
    override fun initPresenter(): Presenter?= null
    override fun initStateProcessor(): StateProcessor<ViewState, ViewIntent>?= null
    override fun render(state: ViewState) {}
}