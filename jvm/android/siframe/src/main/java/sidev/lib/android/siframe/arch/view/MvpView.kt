package sidev.lib.android.siframe.arch.view

import androidx.annotation.CallSuper
import sidev.lib.android.siframe.arch.presenter.*
import sidev.lib.android.siframe.arch.type.Mvp

interface MvpView: ArchView, Mvp, InteractivePresenterDependentCommon<Presenter>, PresenterCallbackCommon