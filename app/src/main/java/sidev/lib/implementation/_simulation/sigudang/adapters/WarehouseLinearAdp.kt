package sidev.lib.implementation._simulation.sigudang.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sigudang.android.Model.Warehouse
import kotlinx.android.synthetic.main._simul_sigud_item_discover_warehouse.view.*
import sidev.kuliah.agradia.template.adp.SimpleAbsRecyclerViewAdapter
import sidev.lib.implementation.R

class WarehouseLinearAdp(c: Context, dataList: ArrayList<Warehouse>?)
    : SimpleAbsRecyclerViewAdapter<Warehouse, LinearLayoutManager>(c, dataList) {
    override val itemLayoutId: Int
        get()= R.layout._simul_sigud_item_discover_warehouse

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: Warehouse) {
        val v= vh.itemView

        v.tv_warehouse_name.text = data.name
        v.tv_warehouse_address.text = data.address
        v.tv_warehouse_volume.text = data.totalVolume.toString()
        v.tv_warehouse_rating.text = data.totalRating.toString()
    }

    override fun setupLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(ctx)
    }
}

    //(val warehouses: ArrayList<Warehouse>, val ctx: Context) : RecyclerView.Adapter<WarehouseLinearAdp.ViewHolder>() {
/*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_discover_gudang, parent, false))
    }

    override fun getItemCount(): Int {
        return warehouses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initContent(position)

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun initContent(position: Int){
            itemView.tv_warehouse_name.text = warehouses[position].name
            itemView.tv_warehouse_address.text = warehouses[position].address
            itemView.tv_warehouse_volume.text = warehouses[position].totalVolume.toString()
            itemView.tv_warehouse_rating.text = warehouses[position].totalRating.toString()
        }
    }
 */
