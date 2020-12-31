package sidev.lib.android.std.tool.util.`fun`

import android.content.Context
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.support.v4.runOnUiThread
import sidev.lib.async.structure.*
import sidev.lib.structure.data.Postable
import sidev.lib.structure.data.iteration.Iteration
import sidev.lib.structure.data.iteration.MutableIteration
import sidev.lib.structure.data.iteration.iterationOf
import sidev.lib.structure.data.iteration.mutableIterationOf
import sidev.lib.structure.data.value.Val


inline fun <R> Context.asyncOnContext(
    crossinline asyncBlock: suspend () -> R,
    crossinline onResult: (R) -> Unit
): Job = GlobalScope.launch {
    val res= withContext(Dispatchers.Default) { asyncBlock() }
    runOnUiThread { onResult(res) }
}

inline fun <R> Fragment.asyncOnContext(
    crossinline asyncBlock: suspend () -> R,
    crossinline onResult: (R) -> Unit
): Job = GlobalScope.launch {
    val res= withContext(Dispatchers.Default) { asyncBlock() }
    runOnUiThread { onResult(res) }
}


inline fun <P, R> Context.asyncOnContext(
    crossinline asyncBlock: suspend PostableCoroutineScope<P>.() -> R,
    crossinline onProgress: (P) -> Unit,
    crossinline onResult: (R) -> Unit
): Job = GlobalScope.launch {
    val postable= asPostable<P> { runOnUiThread { onProgress(it) } }
/*
    object : Postable<P>, Val<CoroutineScope> {
        override fun post(obj: P) =
        override val value: CoroutineScope= this@launch
    }
 */
    val res= withContext(Dispatchers.Default) { asyncBlock(postable) }
    runOnUiThread { onResult(res) }
}

inline fun <P, R> Fragment.asyncOnContext(
    crossinline asyncBlock: suspend PostableCoroutineScope<P>.() -> R,
    crossinline onProgress: (P) -> Unit,
    crossinline onResult: (R) -> Unit
): Job = GlobalScope.launch {
    val postable= asPostable<P> { runOnUiThread { onProgress(it) } }
/*
        object : Postable<P> {
        override fun post(obj: P) = runOnUiThread { onProgress(obj) }
    }
 */
    val res= withContext(Dispatchers.Default) { asyncBlock(postable) }
    runOnUiThread { onResult(res) }
}


@ExperimentalStdlibApi
inline fun <P> Context.asyncBuffer(
    crossinline conditionCheck: (MutableIteration) -> Boolean,
    crossinline onProgress: Iteration.(P) -> Unit = {},
    crossinline onFinish: (Iteration) -> Unit = {},
    delay: Long = 5000,
    crossinline onWait: (Iteration) -> Boolean = { true },
    crossinline asyncBlock: suspend CoroutineScopeIteration.(before: P?) -> P
): Job = asyncBuffer(null, conditionCheck, onProgress, onFinish, delay, onWait, asyncBlock)

/**
 * [onWait] return `true`, maka fungsi buffer / while akan di-delay, dan juga sebaliknya.
 */
@ExperimentalStdlibApi
inline fun <P0, P: P0> Context.asyncBuffer(
    init: P0,
    crossinline conditionCheck: (MutableIteration) -> Boolean,
    crossinline onProgress: Iteration.(P) -> Unit = {},
    crossinline onFinish: (Iteration) -> Unit = {},
    delay: Long = 5000,
    crossinline onWait: (Iteration) -> Boolean = { true },
    crossinline asyncBlock: suspend CoroutineScopeIteration.(before: P0) -> P
): Job {
    val itr= mutableIterationOf(0, 0, 0)
    return asyncOnContext<P, Unit>({
        val scope= asPostableIteration(itr)
        var before: P?= null
        while (conditionCheck(itr)){
            post(scope.asyncBlock(before ?: init).also { before= it })
            itr.index += 1
            if(onWait(itr))
                delay(delay)
        }
    }, { itr.onProgress(it) }, { onFinish(itr) })
}

@ExperimentalStdlibApi
inline fun <P> Fragment.asyncBuffer(
    crossinline conditionCheck: (MutableIteration) -> Boolean,
    crossinline onProgress: Iteration.(P) -> Unit = {},
    crossinline onFinish: (Iteration) -> Unit = {},
    delay: Long = 5000,
    crossinline onWait: (Iteration) -> Boolean = { true },
    crossinline asyncBlock: suspend CoroutineScopeIteration.(before: P?) -> P
): Job = requireActivity().asyncBuffer(conditionCheck, onProgress, onFinish, delay, onWait, asyncBlock)

/**
 * [onWait] return `true`, maka fungsi buffer / while akan di-delay, dan juga sebaliknya.
 */
@ExperimentalStdlibApi
inline fun <P0, P: P0> Fragment.asyncBuffer(
    init: P0,
    crossinline conditionCheck: (MutableIteration) -> Boolean,
    crossinline onProgress: Iteration.(P) -> Unit = {},
    crossinline onFinish: (Iteration) -> Unit = {},
    delay: Long = 5000,
    crossinline onWait: (Iteration) -> Boolean = { true },
    crossinline asyncBlock: suspend CoroutineScopeIteration.(before: P0) -> P
): Job = requireActivity().asyncBuffer(init, conditionCheck, onProgress, onFinish, delay, onWait, asyncBlock)