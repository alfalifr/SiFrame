package sidev.lib.implementation.adp

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
//import kotlinx.android.synthetic.main._sif_comp_item_txt_desc.view.*
//import kotlinx.android.synthetic.main._sif_comp_item_txt_desc.view.*
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.implementation.R

class StrAdp(c: Context, data: ArrayList<String>?) : RvAdp<String, LinearLayoutManager>(c, data){
    override val itemLayoutId: Int
        get() = _Config.LAYOUT_COMP_ITEM_TXT_DESC

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: String) {
        vh.itemView.findViewById<TextView>(R.id.tv).text= data
    }

    override fun setupLayoutManager(): LinearLayoutManager= LinearLayoutManager(ctx)
}