package sidev.lib.android.siframe.arch.intent_state

import sidev.lib.android.siframe.arch.presenter.ArchPresenter
import sidev.lib.android.siframe.arch.type.Mvi


/**
 * <21 Juli 2020> => Untuk pengemebangan ke depannya. Digunakan untuk [ArchPresenter.postSucc] dan [ArchPresenter.postFail].
 */
open class IntentResult: Mvi{
    var code: Int= 0
}

/**
 * Cara mudah untuk pemanggilan [ArchPresenter.postFail] yg membutuhkan kelas [IntentResult] sbg paramnya.
 * Kelas ini digunakan pada arsitektur MVI.
 */
open class IntentResultError: IntentResult(), Mvi{
    var cause: Throwable?= null
}