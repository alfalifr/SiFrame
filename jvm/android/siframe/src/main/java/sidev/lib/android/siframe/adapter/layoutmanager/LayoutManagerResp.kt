package sidev.lib.android.siframe.adapter.layoutmanager

import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.adapter.RvAdp

interface LayoutManagerResp{
    var onLayoutCompletedListener: RvAdp.OnLayoutCompletedListener?
    fun setOnLayoutCompletedListener(l: (state: RecyclerView.State?) -> Unit){
        onLayoutCompletedListener= object : RvAdp.OnLayoutCompletedListener {
            override fun onLayoutCompletedResp(state: RecyclerView.State?) {
                l(state)
            }
        }
    }
}