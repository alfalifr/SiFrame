package sidev.lib.android.std.tool.util.`fun`

import android.content.Context
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.support.v4.runOnUiThread
import sidev.lib.structure.data.Postable


inline fun <R> Context.asyncOnContext(
    crossinline asyncBlock: () -> R,
    crossinline onResult: (R) -> Unit
): Job = GlobalScope.launch {
    val res= withContext(Dispatchers.Default) { asyncBlock() }
    runOnUiThread { onResult(res) }
}

inline fun <R> Fragment.asyncOnContext(
    crossinline asyncBlock: () -> R,
    crossinline onResult: (R) -> Unit
): Job = GlobalScope.launch {
    val res= withContext(Dispatchers.Default) { asyncBlock() }
    runOnUiThread { onResult(res) }
}


inline fun <P, R> Context.asyncOnContext(
    crossinline asyncBlock: Postable<P>.() -> R,
    crossinline onProgress: (P) -> Unit,
    crossinline onResult: (R) -> Unit
): Job = GlobalScope.launch {
    val postable= object : Postable<P> {
        override fun post(obj: P) = runOnUiThread { onProgress(obj) }
    }
    val res= withContext(Dispatchers.Default) { asyncBlock(postable) }
    runOnUiThread { onResult(res) }
}

inline fun <P, R> Fragment.asyncOnContext(
    crossinline asyncBlock: Postable<P>.() -> R,
    crossinline onProgress: (P) -> Unit,
    crossinline onResult: (R) -> Unit
): Job = GlobalScope.launch {
    val postable= object : Postable<P> {
        override fun post(obj: P) = runOnUiThread { onProgress(obj) }
    }
    val res= withContext(Dispatchers.Default) { asyncBlock(postable) }
    runOnUiThread { onResult(res) }
}