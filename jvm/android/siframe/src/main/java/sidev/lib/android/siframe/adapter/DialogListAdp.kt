package sidev.lib.android.siframe.adapter

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.model.StringId

class DialogListAdp(ctx: Context, dataList: ArrayList<StringId>?)
    : RvAdp<StringId, LinearLayoutManager>(ctx, dataList){
    override val itemLayoutId: Int
        get() = _Config.LAYOUT_COMP_ITEM_TXT_DESC

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: StringId) {
        vh.itemView.findViewById<TextView>(_Config.ID_TV).text= data.str
    }

    override fun setupLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(ctx)
    }
}