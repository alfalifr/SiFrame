package com.sigudang.android.adapters
/*
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.sigudang.android.Model.Product
import com.sigudang.android.R
import com.sigudang.android.utilities.RequestUtil
import kotlinx.android.synthetic.main.content_item_product_bound.view.*
import kotlinx.android.synthetic.main.content_iv_square_small.view.*
import kotlinx.android.synthetic.main.item_product_bound_with_addition.view.*
import sidev.kuliah.agradia.template.adp.SimpleAbsRecyclerViewAdapter
import java.text.DecimalFormat

class ProductListAdp(c: Context, dataList: ArrayList<Product>?)
    : SimpleAbsRecyclerViewAdapter<Product, LinearLayoutManager>(c, dataList){
    override val itemLayoutId: Int
        get() = R.layout.item_product_bound_with_addition

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: Product) {
        val v= vh.itemView

        val vis= View.GONE
        v.ll_packaging_container.visibility= vis
        v.ll_send_lessee_container.visibility= vis
        v.rl_track_container.visibility= vis
        v.ll_container_container.visibility= vis
        v.ll_send_warehouse_container.visibility= vis
        v.rl_number_picker_container.visibility= vis

//        (v.comp_btn_add_track as Button).text= "TES BRO"
        Log.d("ProductList_adp->bindView", data.img.dir.toString())
        Log.e("ProductList_adp->bindView", data.img.dir.toString())
        Log.i("ProductList_adp->bindView", data.img.dir.toString())
        if(data.img.dir.isNullOrEmpty())
            v.content_product_pict.iv_pict.setImageResource(data.img.resId!!)
        else
            reqUtil?.loadImageToImageView(v.content_product_pict.iv_pict, RequestUtil.IMAGE_SIZE_TUMBNAIL_SMALL, data.img.dir!!)

        v.content_product_info.tv_amount_title.text= "Volume :"
        v.content_product_info.tv_amount_number.text= DecimalFormat("##.##").format(data.volPerUnit)
        v.content_product_info.tv_amount_unit.text= "m3"

        v.tv_name.text= data.name
        v.tv_kind.text= data.category?.name
    }

    override fun setupLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(ctx)
    }
}

/*
abstract class ProductListAdp<C: BoundProductItemComp>(ctx: Context, dataList: ArrayList<BoundProduct>?)
    : SimpleAbsRecyclerViewAdapter<BoundProduct, LinearLayoutManager>(ctx, dataList){
    override val itemLayoutId: Int
        get() = R.layout.item_product_bound_with_addition
    override val searchFilterFun: (c: Context, data: BoundProduct, keyword: String) -> Boolean
        get() = searchBoundProductFun

    protected var isPackagingVisible: Boolean= false
    protected var isSendVisible_Lessee: Boolean= false
    protected var isSendKindVisible= true
    protected var isSendAddressVisible= true
    protected var isTrackVisible= false
    protected var isContainerVisible= false

    var trackTxtUpper= "outbound"
        protected set
    var trackTxtLower= "keluar"
        protected set

    var totalVolume= 0.0
        private set
    var totalAmount= 0
        private set
    var totalProgres= 0
        private set

    var isAmountOverallFixed= false
        set(v){
            field= v
            notifyDataSetChanged_()
        }
    var isAmountTrackFixed= false
        set(v){
            field= v
            notifyDataSetChanged_()
        }
    var isAmountContainerFixed= false
        set(v){
            field= v
            notifyDataSetChanged_()
        }
    var isSummaryMode= false
        set(v){
            field= v
            notifyDataSetChanged_()
        }
    var isEditEnabled= false
        set(v){
            field= v
            isAmountOverallFixed= v
//            notifyDataSetChanged_()
        }
    var isTrackSealed= false
        set(v){
            field= v
            isAmountOverallFixed= v
//            notifyDataSetChanged_()
        }
    var isTrackForceSetNumber= false
        set(v){
            field= v
            notifyDataSetChanged_()
        }
    private val mapCount= HashMap<Int, Int>()
    private var compList= HashMap<Int, C>()

    var isOnCountChangeListenerPausedWhenDataListSearched= true
    private var isOnCountChangeListenerPaused= false


    override fun initAdp() {
        super.initAdp()
        Log.e("BoundProductListAdp_Abs", "initAdp() is called!!!")
        setOnUpdateDataListener { dataArray, pos, kind ->
            Log.e("BoundProductListAdp_Abs", "initAdp() DALAM!!! kind= $kind pos= $pos")
            if(kind == DataUpdateKind.SET)
                recountTotalProgres()
        }
    }

    abstract fun createViewComp(c: Context, view: View?): C
    fun getViewComp(pos: Int): C?{
        return compList[pos]
    }

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: BoundProduct) {
        val v= vh.itemView
        var comp= getViewComp(pos) //compList[pos]
        if(comp == null){
            comp= createViewComp(ctx, null) //BoundProductItemComp_Lessee(ctx, null)
            compList[pos]= comp
        } else{
            comp.countListener= null
            comp.view= null
        }
        comp.view= v
        comp.countListener= object: NumberPickerComp.CompListener{
            override fun onCountChange(
                v: View,
                before: Int,
                after: Int,
                direction: NumberPickerComp.Enum
            ) {
                if(!isOnCountChangeListenerPaused){
                    if(vh.isAdpPositionSameWith(pos)){
                        modifyDataAt(pos){ dataInt ->
                            val diff= after -before
                            totalAmount += diff
                            totalVolume += dataInt.product.volPerUnit * diff
                            dataInt.amount= after
//                            Log.e("BoundProductAdp BIND", "pos= $pos, vh.layoutPosition= ${vh.layoutPosition} vh.adapterPosition= ${vh.adapterPosition} data= $data")

                            onAmountChangeListener?.onAmountChange(dataInt, pos, after, getVolumeAt(pos))
                            onAmountChangeListener?.onAmountTotalChange(totalAmount, totalVolume)
                            Log.e("BoundProductListAdp_Abs", "onTrackOverallChangeListener inside NumberPickerComp listener dataInt.boundTrack.total= ${dataInt.boundTrack.total}, dataInt.boundTrack.progres= ${dataInt.boundTrack.progres} dataInt.boundTrack.diff= ${dataInt.boundTrack.diff}")
                            onTrackOverallChangeListener?.onTrackOverallChange(totalAmount -totalProgres, totalProgres, totalAmount)
                            dataInt
                        }
                    }
                }
            }
        }
        comp.setOnTrackSingleChangeListener { changeAmount, progres ->
            totalProgres += changeAmount
            Log.e("BoundProductListAdp_Abs", "setOnTrackSingleChangeListener totalAmount= $totalAmount, totalProgres= $totalProgres diff= ${totalAmount -totalProgres}")
            onTrackOverallChangeListener?.onTrackOverallChange(totalAmount -totalProgres, totalProgres, totalAmount)
        }

//        Log.e("BoundProductAdp BIND", "isOnCountChangeListenerPaused= $isOnCountChangeListenerPaused pos= $pos, vh.layoutPosition= ${vh.layoutPosition} vh.adapterPosition= ${vh.adapterPosition} data= $data")
        comp.isPackagingVisible= isPackagingVisible
        comp.isSendVisible_Lessee= isSendVisible_Lessee
        comp.isAmountOverallFixed= isAmountOverallFixed
        comp.isAmountTrackFixed= isAmountTrackFixed
        comp.isAmountContainerFixed= isAmountContainerFixed
        comp.isSummaryMode= isSummaryMode
        comp.isTrackVisible= isTrackVisible
        comp.isTrackForceSetNumber= isTrackForceSetNumber
        comp.isContainerVisible= isContainerVisible
        comp.setSendKindAddresVisible(isSendKindVisible, isSendAddressVisible)
        comp.resetNumberPickBtnVisibility()
        comp.data= data
    }

    override fun setupLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(ctx)
    }

    fun setSendKindAddresVisible(isSendKindVisible: Boolean= this.isSendKindVisible,
                                    isSendAddressVisible: Boolean= this.isSendAddressVisible){
        this.isSendKindVisible= isSendKindVisible
        this.isSendAddressVisible= isSendAddressVisible
        notifyDataSetChanged_()
    }

    fun setPackagingSendVisible_Lessee(isPackagingVisible: Boolean= true, isSendVisible_Lessee: Boolean= true){
        this.isPackagingVisible= isPackagingVisible
        this.isSendVisible_Lessee= isSendVisible_Lessee
        notifyDataSetChanged_()
    }

    fun setTrackContainerVisible(isTrackVisible: Boolean= this.isTrackVisible,
                                 isContainerVisible: Boolean= this.isContainerVisible){
        this.isTrackVisible= isTrackVisible
        this.isContainerVisible= isContainerVisible
        notifyDataSetChanged_()
    }

    fun setTrackText(txtUpper: String= this.trackTxtUpper, txtLower: String= this.trackTxtLower){
        this.trackTxtUpper= txtUpper
        this.trackTxtLower= txtLower
        notifyDataSetChanged_()
    }

    fun recountTotalProgres(): Int{
        Log.e("BoundProductListAdp_Abs", "recountTotalProgres() MULAI!!!")
        if(dataList != null){
            Log.e("BoundProductListAdp_Abs", "recountTotalProgres() dataList.size= ${dataList!!.size} totalProgres before= $totalProgres")
            totalProgres= 0
            for(product in dataList!!){
                totalProgres += product.boundTrack.progres
            }
//            onTrackOverallChangeListener?.onTrackOverallChange(totalAmount -totalProgres, totalProgres, totalAmount)
            Log.e("BoundProductListAdp_Abs", "recountTotalProgres() dataList.size= ${dataList!!.size} totalProgres after= $totalProgres")
        }
        return totalProgres
    }


    /**
     * Untuk memproses dulu total volume per produk
     * sesuai satuan standar
     *
     * BELUM ada proses standarisasi satuan!!!
     */
    fun getVolumeAt(ind: Int): Double{
        return try {
            val data= getDataAt(ind)!!
            data.product.volPerUnit * data.amount
        } catch (e: Exception){
            0.0
        }
    }
/*
    fun getTotalCount(): Int{
        var total= 0
//        T_ViewUtil.toast(ctx, "Tes mapCount.size= ${mapCount.size}")
        dataList.notNull { list ->
            for(data in list)
                total += data.amount
        }
/*
        for((i, count) in mapCount)
            try {
                total += dataList!![i].amount
            } catch (e: Exception){}

 */
        return total
    }
 */
/*
    fun getTotalVolume(): Double{
        var total= 0.0
        dataList.notNull { list ->
            for(data in list)
                total += data.product.volPerUnit *data.amount
        }
/*
        for((i, count) in mapCount)
            try {
                total += dataList!![i].volPerUnit * count
            } catch (e: Exception){}
 */
        return total
    }
 */

    override fun searchItem(keyword: String) {
        if(isOnCountChangeListenerPausedWhenDataListSearched)
            isOnCountChangeListenerPaused= true
        super.searchItem(keyword)
        isOnCountChangeListenerPaused= false
    }

    var onAmountChangeListener: OnAmountChangeListener?= null
    interface OnAmountChangeListener{
        fun onAmountChange(boundProduct: BoundProduct, pos: Int, amount: Int, volume: Double)
        fun onAmountTotalChange(totalAmount: Int, totalVolume: Double)
    }
/*
    var onAmountTrackChangeListener: OnAmountTrackChangeListener?= null
    interface OnAmountTrackChangeListener{
        fun onAmountTrackChange(boundProduct: BoundProduct, pos: Int, req: Int, progres: Int, diff: Int)
        fun onAmountTrackTotalChange(totalReq: Int, totalProgres: Int, totalDiff: Int)
        fun onAmountTrackComplete(complete: Boolean)
    }
 */
    fun setOnTrackOverallChangeListener(l: (diff: Int, progres: Int, total: Int) -> Unit){
        onTrackOverallChangeListener= object : OnTrackOverallChangeListener{
            override fun onTrackOverallChange(diff: Int, progres: Int, total: Int) {
                l(diff, progres, total)
            }
        }
    }
    var onTrackOverallChangeListener: OnTrackOverallChangeListener?= null
/*
        set(containerView){
            field= containerView
            containerView?.onTrackOverallChange(totalAmount -totalProgres, totalProgres, totalAmount)
        }
 */
    interface OnTrackOverallChangeListener{
        fun onTrackOverallChange(diff: Int, progres: Int, total: Int)
    }
}
 */
*/