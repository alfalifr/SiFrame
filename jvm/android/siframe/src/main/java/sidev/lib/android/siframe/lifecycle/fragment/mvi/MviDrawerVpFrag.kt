package sidev.lib.android.siframe.lifecycle.fragment.mvi

import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.IntentResult
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.lifecycle.fragment.DrawerVpFrag
import sidev.lib.android.siframe.lifecycle.fragment.Frag

abstract class MviDrawerVpFrag<F: Frag, I: ViewIntent, R: IntentResult, S: ViewState<*>>
    : DrawerVpFrag<F>(), MviView<I, R, S>{
    override var currentViewState: S?= null
//    override var intentConverter: IntentConverter<I>?= null
abstract override fun initPresenter(): MviPresenter<I, R, S>?
}