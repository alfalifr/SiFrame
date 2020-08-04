package com.sigudang.android.utilities.view.component
/*
import android.content.Context
import android.view.View
import java.lang.Exception

//import kotlinx.android.synthetic.main.content_item_product_bound_packaging.view.*

class BoundProductItemComp_old_Warehouse(ctx: Context, view: View?) : BoundProductItemCompOld(ctx, view){
    var isReceiptVisible= false
        set(v){
            field= v
            try{
                sendAdpWarehouse.isReceiptVIsible= v
            }catch (e: Exception){}
        }
    override fun initViewComp() {
        super.initViewComp()

        initPackagingAdp()
        initSendAdp_Warehouse()
        initTrackAdp()
        initContainerAdp()

        isReceiptVisible= isReceiptVisible
    }

    override fun setSendKindAddresVisible(
        isSendKindVisible: Boolean,
        isSendAddressVisible: Boolean
    ) {
        super.setSendKindAddresVisible(isSendKindVisible, isSendAddressVisible)
        sendAdpWarehouse.setSendKindAddresVisible(isSendKindVisible, isSendAddressVisible)
    }
}
/*
//!!! SAMPE SINI
class BoundProductItemComp_Lessee(ctx: Context, view: View?) : BoundProductItemComp(ctx, view){
    override val layoutId: Int
        get() = R.layout.item_product_bound_with_addition

//    private lateinit var sendKindBtnComp: View
//    private lateinit var sendAddressComp: View

    init{
    }

    var isPackagingVisible: Boolean= false
        set(containerView){
            field= containerView
            if(this::packagingContent.isInitialized)
                packagingContent.visibility= if(containerView) View.VISIBLE
                    else View.GONE
        }
    var isSendVisible_Lessee: Boolean= false
        set(containerView){
            field= containerView
            if(!isInternalEdit)
                isSendVisible_Lessee_int= containerView
            if(this::sendContent_lessee.isInitialized)
                sendContent_lessee.visibility= if(containerView) View.VISIBLE
                else View.GONE
            isOverallAmountVisible= !containerView
        }
    var isTrackVisible: Boolean= false
        set(containerView){
            field= containerView
            if(this::trackContent.isInitialized)
                trackContent.visibility= if(containerView) View.VISIBLE
                    else View.GONE
        }
    var isContainerVisible: Boolean= false
        set(containerView){
            field= containerView
            if(this::containerContent.isInitialized)
                containerContent.visibility= if(containerView) View.VISIBLE
                    else View.GONE
        }
    /**
     * Digunakan untuk menyimpan nilai @see isSendVisible_Lessee sebelumnya
     * jika terjadi perubahan pada @see isSendVisible_Lessee
     */
    private var isSendVisible_Lessee_int= isSendVisible_Lessee
    private var isSendKindVisible= true
    private var isSendAddressVisible= true

/*
    var sendKind: SendKind= SendKind.INSTANT
        set(containerView){
            field= containerView
            sendKindBtnComp.btn.text= containerView.name.toLowerCase().capitalize()
        }
        get(){
            val str= sendKindBtnComp.btn.text.toString().toUpperCase()
            return SendKind.valueOf(str)
        }

    var sendAddress: String= ""
        set(containerView){
            field= containerView
            sendAddressComp.et.setText(containerView)
        }
        get(){
            return sendAddressComp.et.text.toString()
        }
 */

    private var overallNumber= 0
        set(containerView){
            field= containerView
            if(::mainContent.isInitialized)
                mainContent.tv_amount_number.text= containerView.toString()
        }
    private var isOverallAmountVisible= true
        set(containerView){
            field= containerView
            val vis= if(containerView) View.VISIBLE
                else View.GONE
            mainContent.tv_amount_number.visibility= vis
            mainContent.tv_amount_title.visibility= vis
            mainContent.rl_number_picker_container.visibility= vis
        }
    private var isNumberPickerVisible= true
        set(containerView){
            field= containerView
            if(this::mainContent.isInitialized){
                mainContent.rl_number_picker_container.visibility=
                    if(containerView) View.VISIBLE
                    else View.GONE
            }
        }

    override fun initViewComp() {
        if(view != null){
            mainContent= view!!.findViewById(R.id.content_product_info)
            packagingContent= view!!.findViewById(R.id.ll_packaging_container)
            sendContent_lessee= view!!.findViewById(R.id.ll_send_container)
            trackContent= view!!.findViewById(R.id.rv_track)
            containerContent= view!!.findViewById(R.id.ll_container_container)

            ivPict= view!!.findViewById<View>(R.id.content_product_pict).findViewById(R.id.iv_pict)

            numberPickerComp= NumberPickerComp(ctx, mainContent.comp_number_picker)
            numberPickerComp.numberLowerBorder= numberLowerBorder
            numberPickerComp.onAmountChangeListener= countListener
            initNumberPickerBtn()
//            initCheckBox()

            packagingAdp= BoundProductPackagingAdp(ctx, data?.boundPackaging)
            packagingAdp.rv= view!!.rv_packaging!!
            sendAdpLessee= BoundProductSendAdp_Lessee(ctx, data?.boundSend)
            sendAdpLessee.rv= view!!.rv_send_lessee!!
            sendAdpLessee.setOnUpdateDataListener { dataArray, pos ->
                isOverallAmountVisible=
                    (dataArray?.isZero() ?: true
                            || !isSendVisible_Lessee ) //(!isSendKindVisible && !isSendAddressVisible))
                Log.e("ProductItemComp_Lessee", "isOverallAmountVisible = $isOverallAmountVisible isSendVisible_Lessee = $isSendVisible_Lessee dataArray?.isZero() ?: true = ${dataArray?.isZero() ?: true}")
            }
            setSendKindAddresVisible()

            sendAddBtn= view!!.findViewById(R.id.comp_btn_add_send)
            sendAddBtn.setOnClickListener { containerView ->
                sendAdpLessee.addEmptyData()
            }

            trackAdp= BoundTrackAmountAdp(ctx)
            trackAdp.rv= view!!.rv_track

            containerAdp= BoundContainerAdp(ctx, null)
            containerAdp.rv= view!!.rv_container

            containerAddBtn= view!!.findViewById(R.id.comp_btn_add_container)
            containerAddBtn.setOnClickListener { containerView ->

            }

//            initSendKindComp()
//            sendAddressComp= packagingContent.comp_bar_fill_send_address
        }
    }

    fun setSendKindAddresVisible(isSendKindVisible: Boolean= this.isSendKindVisible,
                                    isSendAddressVisible: Boolean= this.isSendAddressVisible){
        this.isSendKindVisible= isSendKindVisible
        this.isSendAddressVisible= isSendAddressVisible
        sendAdpLessee.setSendKindAddresVisible(isSendKindVisible, isSendAddressVisible)
        internalEdit {
            isSendVisible_Lessee= (isSendKindVisible || isSendAddressVisible)
                    && isSendVisible_Lessee_int
        }
    }

    private fun initNumberPickerBtn(){
        (mainContent.comp_btn_add_amount as Button).text= "tambah"
        mainContent.comp_btn_add_amount.setOnClickListener { containerView ->
            containerView.visibility= View.GONE
            mainContent.comp_number_picker.visibility= View.VISIBLE
//            T_ViewUtil.toast(ctx, "Tes Count Bro")
        }
    }
/*
    private fun initCheckBox(){
        packagingCbComp= HashMap()
        packagingCbComp[PackagingEnum.PLASTIK]= packagingContent.cb_packaging_plastic
        packagingCbComp[PackagingEnum.BUBBLE_WRAP]= packagingContent.cb_packaging_bubble
        packagingCbComp[PackagingEnum.KERDUS]= packagingContent.cb_packaging_cardboard
    }
 */

/*
    private fun initSendContent(){
        sendRv= sendContent_lessee.rv_send

    }

    private fun initSendKindComp(){
        sendKindBtnComp= packagingContent.comp_bar_send_kind
        sendKindBtnComp.btn.isAllCaps= false
        sendKindBtnComp //Kurang dialog yang muncul saat ditekan
    }
 */


    override fun setData_int(data: BoundProduct){
        super.setData_int(data)
        sendAdpLessee.dataList= data.boundSend
        packagingAdp.dataList= data.boundPackaging
    }

    fun selectPackaging(pkg: PackagingModel, isSelected: Boolean= true){
        if(packagingAdp.dataList != null){
            for((i, pkgModel) in packagingAdp.dataList!!.withIndex())
                if(pkgModel.name.toUpperCase() == pkg.name.toUpperCase()){
                    packagingAdp.isCheckedList[i]= isSelected
                    return
                }
        }
//        packagingCbComp[pkg]?.isSelected= isSelected
    }

    fun getPackaging(): Array<PackagingModel>{
        val pkgRes= ArrayList<PackagingModel>()
        if(packagingAdp.dataList != null){
            for((i, pkgModel) in packagingAdp.dataList!!.withIndex()){
                if(packagingAdp.isCheckedList[i])
                    pkgRes.add(pkgModel)
            }
        }
        return pkgRes.toTypedArray()
    }

    fun getContainer(): Array<BoundContainerModel>{
        val ctnRes= ArrayList<BoundContainerModel>()
        if(containerAdp.dataList != null){
            for(ctnModel in containerAdp.dataList!!)
                ctnRes.add(ctnModel)
        }
        return ctnRes.toTypedArray()
    }
}


 */
 */