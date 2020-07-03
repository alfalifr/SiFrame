package sidev.lib.android.siframe.lifecycle.activity.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.activity.Act

abstract class MvpAct: Act(), MvpView{
    abstract override fun initPresenter(): Presenter? //Knp kok di kelas ini di-abstract padahal sblumnya tidak?
                //agar Programmer dituntut untuk meng-init presenter.
}