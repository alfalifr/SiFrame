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
import sidev.lib.implementation.intent_state.CFIntent
import sidev.lib.implementation.intent_state.CFRes
import sidev.lib.implementation.intent_state.CFState


class FragBiasa : Fragment(), MviView<CFIntent, CFRes, CFState<*>>{
//    override var intentConverter: IntentConverter<ContentFragIntent>?= null
    override var isBusy: Boolean= false
    override var busyOfWhat: String= InterruptableBase.DEFAULT_BUSY_OF_WHAT
    override val _prop_ctx: Context?
        get() = context
    override var presenter: Presenter?= null
    override var isExpired: Boolean= false
    override var currentViewState: CFState<*>?= null

    override fun onNoCurrentState() {}

    override fun initPresenter(): MviPresenter<CFIntent, CFRes, CFState<*>>? {
        TODO("Not yet implemented")
    }

    override fun initStateProcessor(): StateProcessor<CFIntent, CFRes, CFState<*>>? {
        TODO("Not yet implemented")
    }

    override fun render(state: CFState<*>) {
        TODO("Not yet implemented")
    }
}