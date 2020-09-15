package com.sigudang.android.fragments.bottomsheet

import android.view.View
import com.sigudang.android.Model.Warehouse
import sidev.kuliah.agradia.template.adp.SimpleAbsRecyclerViewAdapter
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.collection.findIndexed
import sidev.lib.implementation._simulation.sigudang.adapters.WarehouseLinearAdp
import sidev.lib.number.isNegative

class BsWarehouseSelectFr : BsSimpleRv<WarehouseLinearAdp, Warehouse>(){
//    private var dataList: ArrayList<Warehouse>? = warehouseList_full.toCollection(ArrayList())
    var selectedWarehousePos: Int = -1
    var selectedWarehouseName: String= ""

    override fun initAdp(): WarehouseLinearAdp {
        return WarehouseLinearAdp(context!!, null)
    }

    override fun initView(v: View) {
        loge("BsWarehouseSelectFr.initView() selectedWarehouseName= $selectedWarehouseName")
        if(selectedWarehouseName.isNotBlank()){
            selectedWarehousePos = rvAdp.dataList?.findIndexed { it.value.name == selectedWarehouseName }?.index ?: -1
            selectedWarehouseName= ""
        }
        loge("BsWarehouseSelectFr.initView() selectedWarehousePos= $selectedWarehousePos")
        if(!selectedWarehousePos.isNegative()){
            rvAdp.selectItem(selectedWarehousePos)
            selectedWarehousePos= -1
        }
    }
    fun getWarehouse(): Warehouse? = rvAdp.getSelectedData()?.first()

    public override var onBsBtnlickListener: ((data: Warehouse?) -> Unit)?= null
}