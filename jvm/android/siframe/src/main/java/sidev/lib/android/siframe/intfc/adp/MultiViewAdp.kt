package sidev.lib.android.siframe.intfc.adp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.tool.util.`fun`.inflate
import sidev.lib.universal.`fun`.notNull

interface MultiViewAdp<D, V: RecyclerView.ViewHolder>: Adp {
    /**
     * Untuk mengambil layoutId pada posisi tertentu dg data tertentu pula.
     */
    fun getItemViewType(pos: Int, data: D): Int
    fun bindVhMulti(vh: V, pos: Int, viewType: Int, data: D)

    override fun getItemViewType(pos: Int): Int

//    override fun bindVH(vh: SimpleRvAdp.SimpleViewHolder, pos: Int, data: D){}
/*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleRvAdp.SimpleViewHolder {
        val v= LayoutInflater.from(ctx).inflate(itemContainerLayoutId, parent, false)
//        val contentV= LayoutInflater.from(ctx).inflate(viewType, parent, false)
        ctx.inflate(viewType, parent, false).notNull { contentView ->
            v.findViewById<LinearLayout>(_Config.ID_VG_CONTENT_CONTAINER) //R.id.ll_content_container
                .addView(contentView)
        }
        return SimpleViewHolder(v)
    }
 */
/*
    override fun onBindViewHolder(holder: SimpleRvAdp.SimpleViewHolder, position: Int) {
//        selectedItemView= holder.itemView
        holder.itemView.findViewById<ImageView>(_Config.ID_IV_CHECK) //R.id.iv_check
            ?.visibility= if(isCheckIndicatorShown && position == selectedItemPos_single) View.VISIBLE
        else View.GONE

        val viewType= getItemViewType(position)
        __bindVH(holder, position, dataList!![position])
        bindVhMulti(holder, position, viewType, dataList!![position])

        holder.itemView.setOnClickListener { v ->
            selectItem(position)
            onItemClickListener?.onClickItem(v, holder.adapterPosition, dataList!![position])
        }
    }
 */
}