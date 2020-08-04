package sidev.lib.implementation._simulation.sigudang.adapters

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sigudang.android.models.SendMethodModel
import kotlinx.android.synthetic.main._simul_sigud_component_bar_iv_tv.view.*
import sidev.kuliah.agradia.template.adp.SimpleAbsRecyclerViewAdapter
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.sigudang.util.RequestUtil

class SendMethodAdp(c: Context, dataList: ArrayList<SendMethodModel>?)
    : SimpleAbsRecyclerViewAdapter<SendMethodModel, LinearLayoutManager>(c, dataList){
    override val itemLayoutId: Int
        get() = R.layout._simul_sigud_component_bar_iv_tv

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: SendMethodModel) {
        val v= vh.itemView
        v.tv.text= data.name
        if(data.img != null){
            v.iv.visibility= View.VISIBLE
            when {
                data.img!!.bm != null -> v.iv.setImageBitmap(data.img!!.bm)
                data.img!!.resId != null -> v.iv.setImageResource(data.img!!.resId!!)
                data.img!!.dir != null -> reqUtil?.loadImageToImageView(v.iv, RequestUtil.IMAGE_SIZE_TUMBNAIL_SMALL, data.img!!.dir!!)
            }
        }
        else
            v.iv.visibility= View.GONE
    }

    override fun setupLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(ctx)
    }
}