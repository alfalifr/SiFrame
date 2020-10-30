package sidev.lib.android.siframe.arch.presenter

import sidev.lib.android.siframe.arch.view.MvpView
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.check.asntNotNull
import sidev.lib.check.isNull
import sidev.lib.exception.DataIntegrityExc
import java.lang.Exception

/**
 * Interface untuk instance request tunggal yg diajukan ke [ArchPresenter].
 * Interface ini berguna pada multiple request pada [ArchPresenter] yang sama
 * di mana tiap request diproses scr asinkronus sehingga hasil yang datang
 * tidak dapat diprediksi urutannya. Hal tersebut menyebabkan [request] tidak dapat
 * diambil dg pasti jika hanya tersedia data [callback].
 *
 * Interface ini meringkas jumlah parameter yang dibutuhkan untuk meneruskan
 * data [callback] dan [request] pada fungsi dalam [MultipleCallbackArchPresenter]
 * dg cara menjadikan data [callback] dan [request] satu-kesatuan.
 */
interface PresenterRequest<Req, Res> {
//    val presenter: ArchPresenter<Req, Res, out PresenterCallback<Req, Res>>
    val request: Req
    val callback: PresenterCallback<Req, Res>
}