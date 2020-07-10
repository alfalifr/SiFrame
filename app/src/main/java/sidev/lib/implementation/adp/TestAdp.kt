package sidev.lib.implementation.adp

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.adapter.RvMultiViewAdp
import sidev.lib.implementation.model.Content

class TestAdp(c: Context, data: ArrayList<Content>) : RvMultiViewAdp<Content, RecyclerView.LayoutManager>(c, data){
    override fun getItemViewType(pos: Int, data: Content): Int {
        TODO("Not yet implemented")
    }

    override fun bindVhMulti(vh: SimpleViewHolder, pos: Int, viewType: Int, data: Content) {
        TODO("Not yet implemented")
    }

    override fun setupLayoutManager(context: Context): RecyclerView.LayoutManager {
        return object : RecyclerView.LayoutManager(){
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                TODO("Not yet implemented")
            }
        }
    }
}