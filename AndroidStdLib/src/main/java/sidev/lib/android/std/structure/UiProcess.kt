package sidev.lib.android.std.structure

import android.content.Context
import kotlinx.coroutines.delay
import org.jetbrains.anko.runOnUiThread
import sidev.lib.async.Process
import sidev.lib.async.runBlocking
import sidev.lib.structure.data.iteration.Iteration
import sidev.lib.structure.data.iteration.mutableIterationOf
import sidev.lib.structure.prop.IterationProp

//TODO: 29 Des 2020 -> Lanjutkan konsep Async untuk UI.

/**
 * Interface yang membedakan proses async untuk UI dan di luarnya.
 * Hal tersebut dilatar belakangi karena UI perlu waktu untuk memproses tampilan.
 * Jika proses async lebih cepat dari UI, maka buffer tidak akan terlihat.
 */
@ExperimentalStdlibApi
interface UiProcess<I, P, R>: Process<I, P, R> {
    val context: Context
    val asyncDelay: Long
    val asyncOnUiDelay: Long

    override val status: Process.Status
    override val tag: String?

    fun IterationProp.process(input: I): R
    fun IterationProp.processOnUi(input: I): R
    fun isOnUi(itr: Iteration): Boolean
    fun hasNext(itr: Iteration): Boolean

    fun postProgressOnUi(progress: P)
    fun postResultOnUi(result: R)

    override fun postProgress(progress: P) = postProgressOnUi(progress)
    override fun postResult(result: R) = postResultOnUi(result)

    override fun cancel(code: Int)
}