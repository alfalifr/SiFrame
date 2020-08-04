package com.sigudang.android.adapters.BoundProductListAdp
/*
import android.content.Context
import android.view.View
import com.sigudang.android.models.BoundProduct
import com.sigudang.android.utilities.view.component.BoundProductItemComp_old_Lessee

class BoundProductListAdp_Lessee(ctx: Context, dataList: ArrayList<BoundProduct>?)
    : BoundProductListAdp_Abs<BoundProductItemComp_old_Lessee>(ctx, dataList){
    init{
        isTrackVisible= false
        isContainerVisible= false
    }
    override fun createViewComp(c: Context, view: View?): BoundProductItemComp_old_Lessee {
        return BoundProductItemComp_old_Lessee(c, view)
    }
}
/*
abstract class BoundProductListAdp_Lessee<C: BoundProductItemComp>(ctx: Context, dataList: ArrayList<BoundProduct>?)
    : SimpleAbsRecyclerViewAdapter<BoundProduct, LinearLayoutManager>(ctx, dataList){
    override val itemLayoutId: Int
        get() = R.layout.item_product_bound_with_addition
    override val searchFilterFun: (data: BoundProduct, keyword: String) -> Boolean
        get() = searchBoundProductFun

    private var isPackagingVisible: Boolean= false
    private var isSendVisible_Lessee: Boolean= false
    private var isSendKindVisible= true
    private var isSendAddressVisible= true

    var isAmountOverallFixed= false
        set(containerView){
            field= containerView
            notifyDataSetChanged()
        }
    var isEditEnabled= false
        set(containerView){
            field= containerView
            isAmountOverallFixed= containerView
//            notifyDataSetChanged()
        }
    private val mapCount= HashMap<Int, Int>()
    private var compList= HashMap<Int, C>()

    var isOnCountChangeListenerPausedWhenDataListSearched= true
    private var isOnCountChangeListenerPaused= false


    abstract fun createViewComp(c: Context, view: View?): C

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: BoundProduct) {
        val containerView= vh.itemView
        var comp= compList[pos]
        if(comp == null){
            comp= createViewComp(ctx, null) //BoundProductItemComp_Lessee(ctx, null)
            compList[pos]= comp
        } else{
            comp.countListener= null
            comp.view= null
        }
        comp.view= containerView
        comp.countListener= object: NumberPickerComp.CompListener{
            override fun onCountChange(containerView: View, count: Int, direction: NumberPickerComp.Enum) {
                if(!isOnCountChangeListenerPaused){
                    if(vh.isAdpPositionSameWith(pos)){
                        modifyDataAt(pos){ data ->
                            data.amount= count
//                            Log.e("BoundProductAdp BIND", "pos= $pos, vh.layoutPosition= ${vh.layoutPosition} vh.adapterPosition= ${vh.adapterPosition} data= $data")
                            onAmountChangeListener?.onAmountChange(data, pos, count, getVolumeAt(pos))
                            onAmountChangeListener?.onAmountTotalChange(getTotalCount(), getTotalVolume())
                            data
                        }
                    }
                }
            }
        }
//        Log.e("BoundProductAdp BIND", "isOnCountChangeListenerPaused= $isOnCountChangeListenerPaused pos= $pos, vh.layoutPosition= ${vh.layoutPosition} vh.adapterPosition= ${vh.adapterPosition} data= $data")
        comp.data= data
        comp.isPackagingVisible= isPackagingVisible
        comp.isSendVisible_Lessee= isSendVisible_Lessee
        comp.isAmountOverallFixed= isAmountOverallFixed
        comp.setSendKindAddresVisible(isSendKindVisible, isSendAddressVisible)
        comp.resetNumberPickBtnVisibility()
    }

    override fun setupLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(ctx)
    }

    fun setSendKindAddresVisible(isSendKindVisible: Boolean= this.isSendKindVisible,
                                    isSendAddressVisible: Boolean= this.isSendAddressVisible){
        this.isSendKindVisible= isSendKindVisible
        this.isSendAddressVisible= isSendAddressVisible
        notifyDataSetChanged()
    }

    fun setPackagingSendVisible_Lessee(isPackagingVisible: Boolean= true, isSendVisible_Lessee: Boolean= true){
        this.isPackagingVisible= isPackagingVisible
        this.isSendVisible_Lessee= isSendVisible_Lessee
        notifyDataSetChanged()
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
}

 */
*/