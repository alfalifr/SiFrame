package sidev.lib.android.siframe.lifecycle.fragment.mvp

import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag

abstract class MvpRvFrag<Adp: SimpleRvAdp<*, *>> : RvFrag<Adp>(), MvpView {
    abstract override fun initPresenter(): Presenter?
}