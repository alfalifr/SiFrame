package com.sigudang.android.fragments.BoundProcessFrag

import android.view.View
import android.widget.Button
import com.sigudang.android.fragments.bottomsheet.BsPickDateFrag
import com.sigudang.android.fragments.bottomsheet.BsWarehouseSelectFr
import com.sigudang.android.models.Bound
import com.sigudang.android.utilities.receiver.createPopupDatePicker
import com.sigudang.android.utilities.receiver.createPopupWarehousePicker
import kotlinx.android.synthetic.main._simul_sigud_component_fill_cb.view.*
import kotlinx.android.synthetic.main._simul_sigud_content_item_product_bound_send.view.*
import kotlinx.android.synthetic.main._simul_sigud_fragment_bound_proses_overview_upper.view.*
//import kotlinx.android.synthetic.main.component_fill_cb.view.*
//import kotlinx.android.synthetic.main.content_item_product_bound_send.view.*
//import kotlinx.android.synthetic.main.fragment_bound_proses_overview_upper.view.*
import org.jetbrains.anko.support.v4.act
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.idName
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.sigudang.dummy.warehouseList_full
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.`fun`.toArrayList
import java.lang.Exception
import sidev.lib.universal.`fun`.plus

class BoundProcessOverviewFrag : Frag(){
    override val layoutId: Int
        get() = R.layout._simul_sigud_fragment_bound_proses_overview_upper

    lateinit var boundData: Bound

    enum class OverviewIndex{
        INVOICE, //= 0
        DATE, //= 1
        WAREHOUSE, //= 2
        LESSEE, //= 3
        CB, //= 4
        SEND_METHOD, //= 5
        SEND_ADDRESS, //= 6
        SEND_SAME_FILL, //= 7
        RECEIPT, //= 8
        SENDER, //= 9
        SENDER_NO, //= 10
        PRODUCT_SEARCH, //= 11
        PRODUCT, //= 12
    }
    var columnViewList= ArrayList<View>()
        protected set
    var shownColumnInd_int: Array<Int> = Array(columnViewList.size){it} //setShownColumnInd()
        set(v){
            v.sort()
            field= v
            loge("shownColumnInd_int.size= ${shownColumnInd_int.size}")
            for((i, view) in columnViewList.withIndex()){
                view.visibility= View.GONE
            }

            for((i, ind) in v.withIndex()){
                loge("shownColumnInd_int i= $i ind= $ind")
                try{ columnViewList[ind].visibility= View.VISIBLE }
                catch (e: Exception){ /*ignore*/ }
            }
        }

    enum class EditableIndex{ DATE, WAREHOUSE }
    var editableViewList= ArrayList<View>()
        protected set
    var editableColumnInd_int: Array<Int> = arrayOf() //setShownColumnInd()
        set(v){
            v.sort()
            field= v
            for((i, view) in editableViewList.withIndex()){
                view.visibility= View.GONE
                loge("editableColumnInd_int i= $i view.idName= ${view.idName} view.findViewByType<Button>()= ${view.findViewByType<Button>()?.idName}")
            }
            for(ind in v){
                try{ editableViewList[ind].visibility= View.VISIBLE }
                catch (e: Exception){ /*ignore*/ }
            }
        }

    protected val datePickerPopup: BsPickDateFrag by lazy {
        createPopupDatePicker(title = "Pilih tanggal", desc = "", btnComfirmTxt = "Simpan"){
            if(it != null){
                layoutView.tv_date.text= it
                boundData.date= it
            }
        }
    }
    protected val warehousePickerPopup: BsWarehouseSelectFr by lazy {
        createPopupWarehousePicker(title = "Pilih Gudang", desc = "", btnComfirmTxt = "Simpan"){
            if(it != null){
                layoutView.tv_warehouse.text= it.name
                boundData.warehouse?.name= it.name //TODO <27 juli 2020> => tanya ntinya info warehouse disimpan di mana pada bound
            }
        }.apply { dataList= warehouseList_full.toArrayList() } //TODO <27 juli 2020> => dummy
    }


    var isSendMethodSame= false
        set(v){
            field= v
            val vis= if(v) View.VISIBLE else View.GONE
            shownColumnInd_int.find { it == OverviewIndex.SEND_SAME_FILL.ordinal }
                .notNull { columnViewList[it].comp_bar_fill_send_method.visibility= vis }
            shownColumnInd_int.find { it == OverviewIndex.SEND_METHOD.ordinal }
                .notNull { columnViewList[it].visibility= vis }
            shownColumnInd_int.find { it == OverviewIndex.RECEIPT.ordinal }
                .notNull { columnViewList[it].visibility= vis }
        }
    var isSendAddressSame= false
        set(v){
            field= v
            val vis= if(v) View.VISIBLE else View.GONE
            shownColumnInd_int.find { it == OverviewIndex.SEND_SAME_FILL.ordinal }
                .notNull { columnViewList[it].comp_bar_fill_send_address.visibility= vis }
            shownColumnInd_int.find { it == OverviewIndex.SEND_ADDRESS.ordinal }
                .notNull { columnViewList[it].visibility= vis }
        }


    override fun _initView(layoutView: View) {
        loge("BoundProcessOverviewFrag _initView()")
        initColumnViewList()
        initTopViewEditableList(layoutView)

        layoutView.comp_bar_send_same_fill.comp_number_picker.visibility= View.GONE //Gak dipake

        layoutView.comp_cb_send_kind_same.cb.text= "Metode pengiriman sama"
        layoutView.comp_cb_send_kind_same.cb.setOnCheckedChangeListener { buttonView, isChecked -> isSendMethodSame= isChecked }
        layoutView.comp_cb_send_address_same.cb.text= "Alamat pengiriman sama"
        layoutView.comp_cb_send_address_same.cb.setOnCheckedChangeListener { buttonView, isChecked -> isSendAddressSame= isChecked }

        try{ readData() } catch (e: UninitializedPropertyAccessException){ /*ignore*/ }
    }

    fun initColumnViewList(){
        columnViewList.clear()
        columnViewList.add(layoutView.ll_invoice_container)
        columnViewList.add(layoutView.ll_date_container)
        columnViewList.add(layoutView.ll_warehouse_container)
        columnViewList.add(layoutView.ll_lessee_container)
        columnViewList.add(layoutView.ll_cb_container) //4
        columnViewList.add(layoutView.ll_send_kind_container)
        columnViewList.add(layoutView.ll_send_address_container)
        columnViewList.add(layoutView.comp_bar_send_same_fill)
        columnViewList.add(layoutView.ll_receipt_container)
        columnViewList.add(layoutView.ll_sender_container)
        columnViewList.add(layoutView.ll_sender_no_container) //10
        columnViewList.add(layoutView.comp_product_search)
        columnViewList.add(layoutView.fl_product_container)
        loge("columnViewList.size= ${columnViewList.size}")
    }

    fun initTopViewEditableList(layoutView: View){
        editableViewList.clear()
        editableViewList + layoutView.comp_date_edit + layoutView.comp_warehouse_edit
        (layoutView.comp_date_edit as Button).text= "Edit"
        layoutView.comp_date_edit.setOnClickListener {
            val date= boundData.date.split("/").map { it.toInt() }
            datePickerPopup.setDate(date[0], date[1], date[2])
            datePickerPopup.show(act.supportFragmentManager, "")
        }
        (layoutView.comp_warehouse_edit as Button).text= "Edit"
        layoutView.comp_warehouse_edit.setOnClickListener {
            warehousePickerPopup.selectedWarehouseName= boundData.warehouse?.name ?: ""
            warehousePickerPopup.show(act.supportFragmentManager, "")
        }
    }

    fun readData(){
        try{
            isSendMethodSame= boundData?.send?.method != null
            isSendAddressSame= boundData?.send?.address?.isNotBlank() == true

            layoutView.tv_invoice.text= boundData?.invoice
            loge("layoutView.tv_invoice.text= ${layoutView.tv_invoice.text}")
            layoutView.tv_date.text= boundData?.date
            layoutView.tv_warehouse.text= boundData?.warehouse?.name
            layoutView.tv_lessee.text= boundData?.lessee?.name

            val sendData= boundData?.send
            val senderData= boundData?.shipper
//        if(sendData != null){
            layoutView.tv_send_kind.text= sendData?.method?.name
            layoutView.tv_send_address.text= sendData?.address
            layoutView.tv_receipt.text= sendData?.receipt?.receipt
//        }

            loge("senderData.phone = ${senderData?.phone}")
//        if(senderData!= null){
            layoutView.tv_sender.text= senderData?.name
            layoutView.tv_sender_no.text= senderData?.phone
            layoutView.tv_sender.visibility= View.VISIBLE
            layoutView.tv_sender_no.visibility= View.VISIBLE
            if(senderData == null){
                layoutView.tv_sender.visibility= View.GONE
                layoutView.tv_sender_no.visibility= View.GONE
            }
            layoutView.comp_cb_send_kind_same.cb.isChecked= isSendMethodSame
            layoutView.comp_cb_send_address_same.cb.isChecked= isSendAddressSame
        } catch (e: UninitializedPropertyAccessException){ /*ignore*/ }
    }
}