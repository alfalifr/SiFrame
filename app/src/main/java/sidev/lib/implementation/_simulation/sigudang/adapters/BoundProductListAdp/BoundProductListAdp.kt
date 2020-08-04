package com.sigudang.android.adapters.BoundProductListAdp
///*
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.sigudang.android.Model.Product
import com.sigudang.android._template.util.T_Util
import com.sigudang.android.adapters.BoundProductListAdp.component.*
import com.sigudang.android.models.BoundProduct
import com.sigudang.android.models.BoundProductSendModel
import kotlinx.android.synthetic.main._simul_sigud_component_add_item.view.*
import kotlinx.android.synthetic.main._simul_sigud_content_item_product_bound.view.*
import kotlinx.android.synthetic.main._simul_sigud_item_product_bound_with_addition.view.*
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.adapter.layoutmanager.LinearLm
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.view.comp.ViewComp
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.new
import sidev.lib.universal.`fun`.notNull
import kotlin.reflect.KMutableProperty0

open class BoundProductListAdp(c: Context)
    : RvAdp<BoundProduct, LinearLm>(c){
    override val itemLayoutId: Int
        get() = R.layout._simul_sigud_item_product_bound_with_addition

    var productCountComp: BpProductCountComp?= null
        private set
    var sendComp_lessee: BpSendComp_Lessee?= null
        private set
    var sendComp_warehouse: BpSendComp_Warehouse?= null
        private set
    var packagingComp: BpPackagingComp?= null
        private set
    var trackingComp: BpTrackComp?= null
        private set
    var containerComp: BpContainerComp?= null
        private set


    /*
               /Old definition
                if(field != v){
                    sendComp_lessee= if(!v){
                        sendComp_lessee?.setupWithRvAdapter(null)
                        null
                    } else{
                        sendComp_lessee?.setupWithRvAdapter(this)
                        BpSendComp_Lessee(ctx)
                    }
                }
     */
    var isProductCountFixed= false
        set(v){
            field= v
//            productCountComp= productCountComp.setup(!v)
            ::productCountComp.setup(!v)
            productCountComp?.setupWithRvAdapter(this)
        }
    var isSendLesseeVisible= false
        set(v){
            field= v
            ::sendComp_lessee.setup(v)
        }
    var isSendWarehouseVisible= false
        set(v){
            field= v
            ::sendComp_warehouse.setup(v)
        }
    var isPackagingVisible= false
        set(v){
            field= v
            ::packagingComp.setup(v)
        }
    var isTrackingVisible= false
        set(v){
            field= v
            ::trackingComp.setup(v)
        }
    var isContainerVisible= false
        set(v){
            field= v
            ::containerComp.setup(v)
        }

    var onProductAmountChange: ((product: Product, oldAmount: Int, newAmount: Int) -> Unit)?= null
///*
    override fun __bindVH(vh: SimpleViewHolder, pos: Int, data: BoundProduct?, isHeaderFooter: Boolean) {
        loge("__bindVH pos= $pos isHeaderFooter= $isHeaderFooter data == null => ${data == null}")
        if(!isHeaderFooter){
            val dataInd= getDataShownIndex(pos)
            val v= vh.itemView
///*
            v.ll_packaging_container.visibility= View.GONE
            v.ll_send_warehouse_container.visibility= View.GONE
            v.findViewById<View>(R.id.ll_send_lessee_container).visibility= View.GONE
            v.rl_track_container.visibility= View.GONE
            v.ll_container_container.visibility= View.GONE
//        v.ll_send_warehouse_container.visibility= View.GONE
// */
            v.setOnClickListener {
                loge("dataInd= $dataInd getDataAt(dataInd, false) = ${getDataAt(dataInd!!, false)!!}")
            }
            loge("footerView == null => ${footerView == null}")
            loge("itemCount= $itemCount dataList?.size= ${dataList?.size} pos= $pos dataInd= $dataInd getDataAt(dataInd, true) = ${getDataAt(dataInd ?: 0, false, isIndexProcessed = true)}")
            loge("v.ll_send_warehouse_container.visibility == View.VISIBLE => ${v.ll_send_warehouse_container.visibility == View.VISIBLE}")
        }
        super.__bindVH(vh, pos, data, isHeaderFooter)
    }
// */

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: BoundProduct) {
//        val dataInd= getDataShownIndex(pos)
        val v= vh.itemView

        v.content_product_info.tv_name.text= data.product.name
        v.content_product_info.tv_kind.text= data.product.category?.name
        v.content_product_info.tv_amount_number.text= data.amount.toString()
        v.content_product_info.tv_amount_unit.text= data.product.unit?.name
        (v.comp_btn_add_container as Button).text= "Tambah Container"

        v.comp_btn_add_send.tv.text= "Tambah Pengiriman"
        v.comp_btn_add_send.setOnClickListener {
            sendComp_lessee?.bsSendKindFr?.selectItem(null)
            sendComp_lessee?.bsSendKindFr?.onBsRvBtnClickListener= { vPriv, dataPriv, posPriv ->
                loge("pos= $pos getDataProcessedIndex(pos, true) = ${getDataProcessedIndex(pos, true)}")
                sendComp_lessee?.getDataAt(getDataProcessedIndex(pos, true) ?: pos)
                    ?.addData(BoundProductSendModel(dataPriv, ""), isAddedVisible = true)
/*
                val selectedData= dataPriv //adp.getSelectedData()
                Log.e("SendAdp_Lessee", "selectedData?.name= ${selectedData?.name}")
                if(selectedData != null){
                    ViewUtil.setCompData_dropDown(v.comp_bar_fill_send_method, dataPriv)
                    data.method= selectedData
                }
 */
            }
            sendComp_lessee?.bsSendKindFr?.show(T_Util.getFM(ctx)!!, "")
        }
    }

    override fun setupLayoutManager(context: Context): LinearLm
        = LinearLm(context)

    fun setProductAmountAt(pos: Int, amount: Int/*, alsoSetToData: Boolean= true*/){
//        if(alsoSetToData){
        val data= getDataAt(pos).notNull {
            val old= it.amount
            it.amount= amount
            onProductAmountChange?.invoke(it.product, old, amount)
        } //?.amount= amount
        loge("bound adp setProductAmountAt() pos= $pos amount= $amount data == null => ${data == null} onProductAmountChange == null => ${onProductAmountChange == null}")
//        }
        sendComp_lessee?.getDataAt(getDataShownIndex(pos) ?: pos)?.maxSendSum= amount
        containerComp?.getDataAt(getDataShownIndex(pos) ?: pos)?.maxContainerSum= amount
        trackingComp?.getDataAt(getDataShownIndex(pos) ?: pos)?.upperInt= amount
    }


    private inline fun <reified T: ViewComp<*, *>> KMutableProperty0<T?>.setup(isVisible: Boolean): T?{
//        val cls= T::class.java.simpleName
        var comp= get()
//        var adp: SimpleRvAdp<*, *>? = null
        if(/*isVisible && */comp == null){
            comp= new{ctx}
            loge("comp awal null comp == null => ${comp == null}")
            set(comp)
        }
        comp?.setupWithRvAdapter(if(isVisible) this@BoundProductListAdp else null)
        comp?.isCompVisible= isVisible
        //set(if(isVisible) comp else null)
        return comp
    }

/*
    private inline fun <reified T: ViewComp<*, *>> T?.setup(isVisible: Boolean): T?{
//        val cls= T::class.java.simpleName
        var comp= this
        var adp: SimpleRvAdp<*, *>? = null
        if(comp == null){
            comp= new{ctx}
            loge("comp awal null comp == null => ${comp == null}")
            adp= this@BoundProductListAdp
        }
        comp?.setupWithRvAdapter(adp)
        comp?.isCompVisible= isVisible
/*
        return if(isVisible){
            var comp= this
            if(comp == null){
                comp= new{ctx}!!
                comp.setupWithRvAdapter(this@BpListAdp)
            }
            comp
        } else{
            this?.setupWithRvAdapter(null)
            null
        }
*/
        return comp
    }
 */
}
// */