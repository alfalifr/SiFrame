package sidev.lib.android.siframe.lifecycle.activity.mvi

import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.ViewIntent
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.intent_state.ViewState
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.android.siframe.lifecycle.activity.DrawerBarContentNavAct_ViewPager
import sidev.lib.android.siframe.lifecycle.fragment.Frag

abstract class MviDrawerBarContentNavAct_ViewPager<F: Frag, S: ViewState, I: ViewIntent>
    : DrawerBarContentNavAct_ViewPager<F>(), MviView<S, I> {
    override var currentViewState: S?= null
    override var intentConverter: IntentConverter<I>?= null
    abstract override fun initPresenter(): Presenter?
}