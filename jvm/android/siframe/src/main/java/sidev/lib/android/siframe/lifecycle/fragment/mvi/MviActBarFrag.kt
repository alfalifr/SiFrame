package sidev.lib.android.siframe.lifecycle.fragment.mvi

import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.lifecycle.fragment.ActBarFrag

abstract class MviActBarFrag<S: ViewState, I: ViewIntent> : ActBarFrag(), MviView<S, I>{
    override var currentViewState: S?= null
    override var intentConverter: IntentConverter<I>?= null
    abstract override fun initPresenter(): Presenter?
}