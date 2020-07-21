package sidev.lib.implementation.frag

import android.content.Context
import androidx.fragment.app.Fragment
import sidev.lib.android.siframe.arch.intent_state.IntentConverter
import sidev.lib.android.siframe.arch.intent_state.StateProcessor
import sidev.lib.android.siframe.arch.presenter.MviPresenter
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MviView
import sidev.lib.android.siframe.intfc.lifecycle.InterruptableBase
import sidev.lib.android.siframe.intfc.lifecycle.rootbase.ViewModelBase
import sidev.lib.implementation.intent_state.ContentFragIntent
import sidev.lib.implementation.intent_state.ContentFragState


class FragBiasa : Fragment(), MviView<ContentFragState, ContentFragIntent>{
//    override var intentConverter: IntentConverter<ContentFragIntent>?= null
    override var isBusy: Boolean= false
    override var busyOfWhat: String= InterruptableBase.DEFAULT_BUSY_OF_WHAT
    override val _prop_ctx: Context?
        get() = context
    override var presenter: Presenter?= null
    override var isExpired: Boolean= false
    override var currentViewState: ContentFragState?= null


    override fun onNoCurrentState() {}

    override fun initPresenter(): MviPresenter<ContentFragState, ContentFragIntent>? {
        TODO("Not yet implemented")
    }

    override fun initStateProcessor(): StateProcessor<ContentFragState, ContentFragIntent>? {
        TODO("Not yet implemented")
    }

    override fun render(state: ContentFragState) {
        TODO("Not yet implemented")
    }
}