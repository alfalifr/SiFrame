package sidev.lib.android.siframe.arch.presenter

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.intfc.lifecycle.ExpirableBase

interface PresenterDependentCommon<P: Presenter>
    : PresenterDependent<P, String, PresenterCallback<String>>