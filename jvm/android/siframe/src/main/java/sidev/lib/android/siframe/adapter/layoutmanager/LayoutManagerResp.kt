package sidev.lib.android.siframe.adapter.layoutmanager

import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.adapter.SimpleAbsRecyclerViewAdapter

interface LayoutManagerResp{
    var onLayoutCompletedListener: SimpleAbsRecyclerViewAdapter.OnLayoutCompletedListener?
    fun setOnLayoutCompletedListener(l: (state: RecyclerView.State?) -> Unit){
        onLayoutCompletedListener= object : SimpleAbsRecyclerViewAdapter.OnLayoutCompletedListener {
            override fun onLayoutCompletedResp(state: RecyclerView.State?) {
                l(state)
            }
        }
    }
}