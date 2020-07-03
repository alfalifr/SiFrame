package sidev.lib.android.siframe.lifecycle.fragment.mvp

import sidev.lib.android.siframe.arch.presenter.Presenter
import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.lifecycle.fragment.Frag

//import sidev.lib.android.siframe.presenter.RepositoryCallback

/**
 * Kelas dasar dalam framework yang digunakan sbg Fragment sbg pengganti dari Fragment
 */
abstract class MvpFrag : Frag(), MvpView {
    abstract override fun initPresenter(): Presenter?
}