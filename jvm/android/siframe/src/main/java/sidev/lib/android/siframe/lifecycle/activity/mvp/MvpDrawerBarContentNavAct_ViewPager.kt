package sidev.lib.android.siframe.lifecycle.activity.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.activity.DrawerBarContentNavAct_ViewPager
import sidev.lib.android.siframe.lifecycle.fragment.Frag

abstract class MvpDrawerBarContentNavAct_ViewPager<F: Frag>
    : DrawerBarContentNavAct_ViewPager<F>(), MvpView {
    abstract override fun initPresenter(): Presenter?
}