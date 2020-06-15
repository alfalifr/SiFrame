package sidev.lib.implementation.adp

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import sidev.lib.android.siframe.adapter.RvMultiViewAdp
import sidev.lib.implementation.model.Content

class TestAdp(c: Context, data: ArrayList<Content>) : RvMultiViewAdp<Content, LinearLayoutManager>(c, data){
    override fun getItemViewType(pos: Int, data: Content): Int {
        TODO("Not yet implemented")
    }

    override fun bindVhMulti(vh: SimpleViewHolder, pos: Int, viewType: Int, data: Content) {
        TODO("Not yet implemented")
    }

    override fun setupLayoutManager(): LinearLayoutManager {
        TODO("Not yet implemented")
    }
}