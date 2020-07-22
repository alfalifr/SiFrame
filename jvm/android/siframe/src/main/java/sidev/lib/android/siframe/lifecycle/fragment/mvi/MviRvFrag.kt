package sidev.lib.android.siframe.lifecycle.fragment.mvi

import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.intent_state.IntentResult
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag

abstract class MviRvFrag<R: SimpleRvAdp<*, *>, I: ViewIntent, IR: IntentResult, S: ViewState<*>> : RvFrag<R>(), MviView<I, IR, S>{
    override var currentViewState: S?= null
//    override var intentConverter: IntentConverter<I>?= null
abstract override fun initPresenter(): MviPresenter<I, IR, S>?
}