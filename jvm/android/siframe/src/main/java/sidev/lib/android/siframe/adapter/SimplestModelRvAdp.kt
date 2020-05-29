package sidev.lib.android.siframe.adapter
/*
import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager

open class SimplestModelRvAdp<D: SimplestModel>(c: Context, dataList: ArrayList<D>?)
    : SimpleAbsRecyclerViewAdapter<D, LinearLayoutManager>(c, dataList){
    override val itemLayoutId: Int
        get() = R.layout.item_bound_track_amount

    @CallSuper
    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: D) {
        val v= vh.itemView
        val vis= View.GONE
        v.rl_right.visibility= vis
        v.iv_minus.visibility= vis
        v.et_number.visibility= vis

        v.tv_title.text= data.name
    }

    override fun setupLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(ctx)
    }
}
 */