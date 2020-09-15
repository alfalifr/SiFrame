package sidev.lib.android.siframe.adapter.layoutmanager

import android.content.Context
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.adapter.decoration.RvSmoothScroller
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.check.isNull
import sidev.lib.check.notNull

open class LinearLm(c: Context) : LinearLayoutManager(c), LayoutManagerResp{
    companion object{
        /**
         * Digunakan jika tidak tahu scr pasti scroll akan menuju ke posisi ke brp.
         * Berguna saat pemanggilan fungsi [scrollHorizontallyBy].
         */
        val POSITION_DYNAMIC= -2
        /**
         * Digunakan jika tidak tahu scr pasti jarak scroll yg ditempuh.
         * Berguna saat pemanggilan fungsi [smoothScrollTo].
         */
        val SCROLL_DYNAMIC= -3
    }
    var smoothScroller: RvSmoothScroller?= null
    var isAtFirstPosition= true
        protected set(v){
            field= v
            loge("isAtFirstPosition= $v")
        }
    var totalScrollX= 0
        protected set(v){
            field= v
            if(layoutDirection == HORIZONTAL)
                isAtFirstPosition= v <= 0
        }
    var totalScrollY= 0
        protected set(v){
            field= v
            if(layoutDirection == VERTICAL)
                isAtFirstPosition= v <= 0
        }

    var onScrollListener: ((targetPosition: Int, dx: Int, dy: Int, isAtFirstPos: Boolean, isSmoothScroll: Boolean) -> Unit)?= null

    override var onLayoutCompletedListener: RvAdp.OnLayoutCompletedListener? = null

    @CallSuper
    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
//        Log.e("LinearLayoutManagerResp", "onLayoutCompletedListener == null = ${onLayoutCompletedListener == null} state= ${state.toString()}")
        onLayoutCompletedListener?.onLayoutCompletedResp(state)
    }

    override fun scrollToPosition(position: Int) {
        isAtFirstPosition= position == 0
        onScrollListener?.invoke(position, SCROLL_DYNAMIC, SCROLL_DYNAMIC, isAtFirstPosition, false)
        super.scrollToPosition(position)
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val traveled= super.scrollHorizontallyBy(dx, recycler, state)
        totalScrollX += traveled
        onScrollListener?.invoke(POSITION_DYNAMIC, traveled, 0, isAtFirstPosition, true)
        return traveled
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val traveled= super.scrollVerticallyBy(dy, recycler, state)
        totalScrollY += traveled
        onScrollListener?.invoke(POSITION_DYNAMIC, 0, traveled, isAtFirstPosition, true)
        return traveled
    }

    fun smoothScrollTo(position: Int){
        isAtFirstPosition= position == 0
        smoothScroller.notNull { sm ->
            sm.targetPosition= position
            startSmoothScroll(sm)
            onScrollListener?.invoke(position, SCROLL_DYNAMIC, SCROLL_DYNAMIC, isAtFirstPosition, true)
        }.isNull {
            scrollToPosition(position)
        }
    }
}