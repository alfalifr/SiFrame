package sidev.lib.implementation._simulation.sigudang.adapters

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import com.sigudang.android.models.abs.SimplestModel
import kotlinx.android.synthetic.main._simul_sigud_item_bound_track_amount.view.*
import sidev.kuliah.agradia.template.adp.SimpleAbsRecyclerViewAdapter
import sidev.lib.implementation.R

open class SimplestModelRvAdp<D: SimplestModel>(c: Context, dataList: ArrayList<D>?)
    : SimpleAbsRecyclerViewAdapter<D, LinearLayoutManager>(c, dataList){
    override val itemLayoutId: Int
        get() = R.layout._simul_sigud_item_bound_track_amount

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