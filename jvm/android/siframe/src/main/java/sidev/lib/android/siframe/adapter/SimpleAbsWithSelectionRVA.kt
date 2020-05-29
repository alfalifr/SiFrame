package sidev.lib.android.siframe.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

//!!!!!!@@ 18 Jan 2020
abstract class SimpleAbsWithSelectionRVA <D, LM: RecyclerView.LayoutManager> (
    ctx: Context, dataList: ArrayList<D>?
    )
    : SimpleAbsRecyclerViewAdapter<D, LM>(ctx, dataList){
    /**
     * Isi di dalamnya harus sesuai dengan yang ada di @see dataList
     */
    var dataListSelected= this.dataList
}
