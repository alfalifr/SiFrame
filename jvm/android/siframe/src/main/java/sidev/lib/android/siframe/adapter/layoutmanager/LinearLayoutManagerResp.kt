package sidev.lib.android.siframe.adapter.layoutmanager

import android.content.Context
import android.util.Log
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.adapter.RvAdp

class LinearLayoutManagerResp(c: Context) : LinearLayoutManager(c), LayoutManagerResp{
    override var onLayoutCompletedListener: RvAdp.OnLayoutCompletedListener?
        = null

    @CallSuper
    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
//        Log.e("LinearLayoutManagerResp", "onLayoutCompletedListener == null = ${onLayoutCompletedListener == null} state= ${state.toString()}")
        onLayoutCompletedListener?.onLayoutCompletedResp(state)
    }
}