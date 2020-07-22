package sidev.lib.android.siframe.lifecycle.activity.mvi

import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.IntentResult
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.lifecycle.activity.SingleFragDrawerAct_BarContentNav

abstract class MviSingleFragDrawerAct_BarContentNav<I: ViewIntent, R: IntentResult, S: ViewState<*>>
    : SingleFragDrawerAct_BarContentNav(), MviView<I, R, S> {
    override var currentViewState: S?= null
//    override var intentConverter: IntentConverter<I>?= null
    abstract override fun initPresenter(): MviPresenter<I, R, S>?
}