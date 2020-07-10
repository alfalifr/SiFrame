package sidev.lib.implementation.adp

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.comp_content.view.*
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.implementation.R
import sidev.lib.implementation.model.Content

class ContentAdp(c: Context) : RvAdp<Content, LinearLayoutManager>(c){
    override val itemLayoutId: Int
        get() = R.layout.comp_content

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: Content) {
        val v= vh.itemView
        v.tv_id.text= data.id
        v.tv_title.text= data.title
        v.tv_desc.text= data.desc
    }

    override fun setupLayoutManager(context: Context): LinearLayoutManager = LinearLayoutManager(context)
}