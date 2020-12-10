package sidev.lib.android.viewrap

import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import sidev.lib.android.std.`val`._Constant
import sidev.lib.android.std.tool.util.`fun`.detachFromParent
import sidev.lib.android.std.tool.util.`fun`.findView
import sidev.lib.android.std.tool.util.`fun`.forcedAddView

/**
 * Common interface yg digunakan untuk menandai bahwa suatu kelas merupakan
 * kelas yg membungkus sebuah [view] yg nantinya dimanajemen.
 */
interface ViewWrapper<V: View> {
    val view: V?
    val context: Context

    fun <V: View> findViewById(@IdRes id: Int): V? = view?.findViewById(id)
    fun <V: View> findViewByTag(any: Any?): V? = view?.findViewWithTag(any)
    fun <V: View> findView(
        clazz: Class<V>,
        @IdRes id: Int?= null,
        tag: Any?= null,
        direction: Int= _Constant.DIRECTION_DOWN,
        includeItself: Boolean= true
    ): V? = view?.findView(clazz, id, tag, direction, includeItself)
    fun draw(canvas: Canvas) = view?.draw(canvas)

    /** @return [ViewGroup] tempat [view] sebelumnya menmpel, null jika [view] sblumnya belum nempel pada apapun. */
    fun attachTo(vg: ViewGroup): ViewGroup? = if(view != null) vg.forcedAddView(view!!) else null
    fun detachViewFromParent(): ViewGroup? = view?.detachFromParent()
}