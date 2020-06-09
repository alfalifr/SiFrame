package sidev.lib.android.siframe.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

//!!!!!!@@ 18 Jan 2020
abstract class RvAdpWithSelection <D, LM: RecyclerView.LayoutManager> (
    ctx: Context, dataList: ArrayList<D>?
    )
    : RvAdp<D, LM>(ctx, dataList){
    /**
     * Isi di dalamnya harus sesuai dengan yang ada di @see dataList
     */
    var dataListSelected= this.dataList
}
