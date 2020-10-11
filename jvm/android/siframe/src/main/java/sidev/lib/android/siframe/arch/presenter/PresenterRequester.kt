package sidev.lib.android.siframe.arch.presenter

/**
 * Interface yang berinteraksi dg [ArchPresenter], namun tidak memiliki properti [ArchPresenter].
 */
interface PresenterRequester<R> {
    fun downloadData(reqCode: R, vararg data: Pair<String, Any>)
    fun uploadData(reqCode: R, vararg data: Pair<String, Any>)
    fun sendRequest(reqCode: R, vararg data: Pair<String, Any>)
}