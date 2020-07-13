package sidev.lib.android.siframe.lifecycle.fragment.mvi

import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag

//import sidev.lib.android.siframe.presenter.RepositoryCallback

/**
 * Kelas dasar dalam framework yang digunakan sbg Fragment sbg pengganti dari Fragment
 */
abstract class MviFrag<S: ViewState, I: ViewIntent> : Frag(), MviView<S, I>{
    override var currentViewState: S?= null
    override var intentConverter: IntentConverter<I>?= null
    abstract override fun initPresenter(): Presenter?
}