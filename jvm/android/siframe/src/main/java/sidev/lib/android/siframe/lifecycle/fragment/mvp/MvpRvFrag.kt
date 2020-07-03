package sidev.lib.android.siframe.lifecycle.fragment.mvp

import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag

abstract class MvpRvFrag<R: RvAdp<*, *>> : RvFrag<R>(), MvpView {
    abstract override fun initPresenter(): Presenter?
}