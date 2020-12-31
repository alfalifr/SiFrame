package sidev.lib.android.std.structure

import android.content.Context
import kotlinx.coroutines.delay
import org.jetbrains.anko.runOnUiThread
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.async.Process
import sidev.lib.async.runBlocking
import sidev.lib.structure.data.iteration.Iteration
import sidev.lib.structure.data.iteration.mutableIterationOf
import sidev.lib.structure.prop.IterationProp

/**
 * [UiProcess] yang memiliki banyak process, sehingga setiap progress merupakan output.
 */
@ExperimentalStdlibApi
interface UiBuffer<I, O>: UiProcess<I, O, O> {
    fun getInput(currentOutput: O): I
}

@ExperimentalStdlibApi
internal open class UiBufferImpl<I, O>(
    override val context: Context,
    override val tag: String? = "<AsyncUiBuffer>",
    override val asyncDelay: Long,
    override val asyncOnUiDelay: Long,
    val onProgress: (O) -> Unit,
    val onFinish: () -> Unit,
    val onCancel: (Int) -> Unit,
    val hasNext: (Iteration) -> Boolean,
    val isOnUi: (Iteration) -> Boolean,
    val getInput: (O) -> I,
    val processBlock: IterationProp.(I) -> O
): UiBuffer<I, O> {
    final override var status: Process.Status = Process.Status.NOT_STARTED
        private set

    override fun IterationProp.process(input: I): O {
        runBlocking { delay(asyncDelay) }
        return processBlock(input)
    }

    override fun IterationProp.processOnUi(input: I): O {
        runBlocking { delay(asyncOnUiDelay) }
        return processBlock(input)
    }

    override fun isOnUi(itr: Iteration): Boolean = isOnUi.invoke(itr)
    override fun hasNext(itr: Iteration): Boolean = hasNext.invoke(itr)

    override fun postProgressOnUi(progress: O) {
        context.runOnUiThread { onProgress(progress) }
    }

    override fun postResultOnUi(result: O) {
        context.runOnUiThread { onFinish() }
    }

    override fun getInput(currentOutput: O): I = getInput.invoke(currentOutput)

    override fun cancel(code: Int) = onCancel(code)
    override fun start(input: I) {
        val itr= mutableIterationOf(0, 0, 0)
        val prop= object : IterationProp{
            override val iteration: Iteration= itr
        }
        @Suppress(SuppressLiteral.NAME_SHADOWING)
        var input= input
        var output: O
        while(hasNext(itr)){
            output= prop.run {
                if(isOnUi(itr)) processOnUi(input)
                else process(input)
            }
            input= getInput(output)
            itr.index++
        }
    }
}