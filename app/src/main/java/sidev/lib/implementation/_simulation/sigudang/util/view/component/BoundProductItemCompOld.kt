package com.sigudang.android.utilities.view.component
/*
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import com.sigudang.android.R
import com.sigudang.android._Dummy.*
import com.sigudang.android._template.util.ReceiverFun.*
import com.sigudang.android._template.util.StaticManager
import com.sigudang.android._template.util.loge
import com.sigudang.android.activities.ScanAct
import com.sigudang.android.adapters.*
import com.sigudang.android.models.BoundContainerModel
import com.sigudang.android.models.BoundProduct
import com.sigudang.android.models.PackagingModel
import com.sigudang.android.models.db.WarehouseContainer
import com.sigudang.android.utilities.RequestUtil
import com.sigudang.android.utilities.constant.Constants
import id.go.surabaya.disperdagin.utilities.T_ViewUtil
import kotlinx.android.synthetic.main.content_item_product_bound.view.*
import kotlinx.android.synthetic.main.item_product_bound_with_addition.view.*
import org.jetbrains.anko.toast
import java.lang.Exception

//import kotlinx.android.synthetic.main.content_item_product_bound_packaging.view.*

//!!! SAMPE SINI
//!!! isSummaryMode masih blum bisa menghilangkan numberPickerComp View.GONE
open class BoundProductItemCompOld(ctx: Context, view: View?) : ViewComp_old(ctx, view){
    override val layoutId: Int
        get() = R.layout.item_product_bound_with_addition

    protected lateinit var mainContent: View
    protected lateinit var packagingContent: View
    protected lateinit var sendContent_lessee: View
    protected lateinit var sendContent_warehouse: View
    protected lateinit var containerContent: View
    protected lateinit var crossdockingAmountContent: View

    protected lateinit var ivPict: ImageView

    protected lateinit var numberPickerComp: NumberPickerCompOld
//    protected lateinit var packagingCbComp: HashMap<PackagingEnum, CheckBox>

/*
==========
Adapter
==========
 */
    protected lateinit var packagingAdp: BoundProductPackagingAdp
    var packagingDataListFull= packagingModel_list.toCollection(ArrayList())
        set(v){
            field= v
            if(::packagingAdp.isInitialized)
                packagingAdp.dataList= v
        }

    protected lateinit var sendAdpLessee: BoundProductSendAdp_Lessee
    protected lateinit var sendAdpWarehouse: BoundProductSendAdp_Warehouse
    protected lateinit var sendAddBtn: View

    protected open val processData get() = StaticManager.processDataList

    private val reqUtil = RequestUtil(ctx)

    var isReceiptOverallOk= false
        protected set
/*
    protected val bsSendKindFrag= BsSendKindFr()
    var sendKindDataListFull= sendKindModel_list_full.toCollection(ArrayList())
        set(v){
            field= v
            bsSendKindFrag.dataList= v
        }
 */
/*
    var sendDataListFull= boundProdSendList_full.toCollection(ArrayList())
        set(v){
            field= v
            if(::sendAdpLessee.isInitialized)
                sendAdpLessee.dataList= v
            if(::sendAdpWarehouse.isInitialized)
                sendAdpWarehouse.dataList= v
        }
 */

    protected lateinit var trackAdp: BoundTrackAmountAdp
    protected lateinit var trackContent: View
    protected lateinit var trackAddBtn: Button
    protected lateinit var trackStartBtn: Button
    /**
     * trackContent + trackAddBtn
     */
    protected lateinit var trackContent_all: View

    protected lateinit var containerAdpOld: BoundContainerAdp_old
    protected lateinit var containerAddBtn: Button


//    protected lateinit var sendKindBtnComp: View
//    protected lateinit var sendAddressComp: View

    var isCrossdockingMode= false
        set(v){
            field= v
            if(::crossdockingAmountContent.isInitialized){
                crossdockingAmountContent.visibility= if(v) View.VISIBLE else View.GONE
                view?.content_product_info?.rl_amount_container_outer
                    ?.visibility= if(!v) View.VISIBLE else View.GONE
            }
        }
    var isPackagingVisible: Boolean= false
        set(v){
            field= v
            if(this::packagingContent.isInitialized)
                packagingContent.visibility= if(v) View.VISIBLE
                    else View.GONE
        }
    var isPackagingFixed= false
        set(v){
            field= v
            if(::packagingAdp.isInitialized)
                packagingAdp.isCheckEnabled= !v
        }
    var isSendVisible_Lessee: Boolean= false
        set(v){
            field= v
            if(!isInternalEdit)
                isSendVisible_Lessee_int= v
            if(this::sendContent_lessee.isInitialized)
                sendContent_lessee.visibility= if(v) View.VISIBLE
                else View.GONE
            isOverallAmountVisible= !v
        }
    var isSendVisible_Warehouse: Boolean= false
        set(v){
            field= v
            Log.e("BoundProductItemComp", "isSendVisible_Warehouse= $v")
            if(::sendContent_warehouse.isInitialized){
                Log.e("BoundProductItemComp", "isSendVisible_Warehouse= DALAM $v")
                sendContent_warehouse.visibility= if(v) View.VISIBLE
                else View.GONE
            }
        }

    var isTrackVisible: Boolean= false
        set(v){
            field= v
            Log.e("BoundProductItemComp", "isTrackVisible priv= $isTrackVisible")
            if(this::trackContent_all.isInitialized){
                Log.e("BoundProductItemComp", "isTrackVisible priv= DALAM $isTrackVisible")
                trackContent_all.visibility= if(v) View.VISIBLE
                    else View.GONE
            }
        }
    var isTrackForceSetNumber= false
    /**
     * Track tidak dapat ditambah/kurang
     * Track harus dibuka dulu yaitu dg scan dulu
     */
    var isTrackSealed= false
        set(v){
            val disabled= v && data?.boundTrack?.progres ?: 0 <= 0
            field= v
            loge("v= $v data?.boundTrack?.progres ?: 0 = ${data?.boundTrack?.progres ?: 0} disabled= $disabled isTrackVisible= $isTrackVisible")
//            Log.e("BoundProductItemComp", "v= $v data?.boundTrack?.progres ?: 0 = ${data?.boundTrack?.progres ?: 0} disabled= $disabled")
            Log.e("BoundProductItemComp", "disabled= $disabled ::trackAddBtn.isInitialized= ${::trackAddBtn.isInitialized} data?.product?.name= ${data?.product?.name} data?.boundTrack?.progres= ${data?.boundTrack?.progres}")
            if(::trackAddBtn.isInitialized){
                Log.e("BoundProductItemComp", "disabled= $disabled MASUK BRO!!!")
                if(isTrackVisible){
                    trackAddBtn.visibility=
                        if(disabled) View.VISIBLE
                        else View.GONE
                    trackStartBtn.visibility=
                        if(disabled) View.VISIBLE
                        else View.GONE
                    trackContent.visibility=
                        if(disabled) View.GONE
                        else View.VISIBLE
                }
            }
        }
    var isContainerVisible: Boolean= false
        set(v){
            field= v
            if(this::containerContent.isInitialized)
                containerContent.visibility= if(v) View.VISIBLE
                    else View.GONE
        }
    /**
     * Digunakan untuk menyimpan nilai @see isSendVisible_Lessee sebelumnya
     * jika terjadi perubahan pada @see isSendVisible_Lessee
     */
    protected var isSendVisible_Lessee_int= isSendVisible_Lessee
    protected var isSendKindVisible= true
    protected var isSendAddressVisible= true

    var data: BoundProduct?= null
        set(v){
            field= v
            if(v != null)
                setData_int(v)
        }

    var productAmount: Int= 0
        set(v){
            field= v
            if(this::numberPickerComp.isInitialized)
                numberPickerComp.setCount(v)
        }
        get(){
            return numberPickerComp.getCount()
        }
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

    var countListener: NumberPickerCompOld.CompListener?= null
        set(v){
            field= v
            numberPickerComp.listener= object: NumberPickerCompOld.CompListener{
                override fun onCountChange(
                    view: View,
                    before: Int,
                    after: Int,
                    direction: NumberPickerCompOld.Enum
                ) {
                    if(v != null){
//                        Log.e("NumberPickerComp", "Terjadi perubahan >> number= $count direction= $direction")
                        v.onCountChange(view, before, after, direction)
                        setOverallProdAmount(after)
                    }
                }
            }
        }

    protected var overallNumber= 0
        set(v){
            field= v
            if(::mainContent.isInitialized)
                mainContent.tv_amount_number.text= v.toString()
        }
    protected var isOverallAmountVisible= true
        set(v){
            field= v
            val vis= if(v) View.VISIBLE
                else View.GONE
            mainContent.tv_amount_number.visibility= vis
            mainContent.tv_amount_title.visibility= vis
            mainContent.rl_number_picker_container.visibility= vis
        }
    protected var isNumberPickerVisible= true
        set(v){
            field= v
            if(this::mainContent.isInitialized){
                mainContent.rl_number_picker_container.visibility=
                    if(v) View.VISIBLE
                    else View.GONE
            }
        }
    var isAmountOverallFixed= false
        set(v){
            field= v
            if(::numberPickerComp.isInitialized)
                numberPickerComp.enableSelection(!v)
        }
    var isAmountTrackFixed= false
        set(v){
            field= v
            if(::trackAdp.isInitialized)
                trackAdp.isAmountTrackFixed= v
        }
    var isAmountContainerFixed= false
        set(v){
            field= v
            if(::containerAdpOld.isInitialized)
                containerAdpOld.isAmountContainerFixed= v
        }

    var isSummaryMode= false
        set(v){
            field= v
            if(::numberPickerComp.isInitialized){ //if(view != null){
                val vis= if(v) View.GONE
                    else View.VISIBLE
                val v1= view!!.content_product_info.tv_amount_title!!
                val v2= view!!.content_product_info.rl_number_picker_container!!
                Log.e("BoundProdItemComp", "isSummaryMode= $v vis= $vis v1 == null= ${v1 == null} v2 == null= ${v2 == null} numberPickerComp.getCount() = ${numberPickerComp.getCount()}")
                v1.visibility= vis //view!!.content_product_info.tv_amount_title!!.visibility= View.GONE
                v2.visibility= vis //view!!.content_product_info.rl_number_picker_container!!.visibility= vis //numberPickerComp.view?.visibility= vis//.isSummaryMode= containerView
                numberPickerComp.isSummaryMode= v
            }
        }

    var isReceiptOk_comp= false
        set(v){
            if(field != v){
                field= v
                onReceiptChangeListener_Comp?.invoke(v)
            }
        }

    var numberLowerBorder= 0
        set(v){
            field= v
            if(this::numberPickerComp.isInitialized)
                numberPickerComp.numberLowerBorder= v
        }

    @CallSuper
    override fun initViewComp() {
        if(view != null){
            mainContent= view!!.findViewById(R.id.content_product_info)
            packagingContent= view!!.findViewById(R.id.ll_packaging_container)
            sendContent_lessee= view!!.findViewById(R.id.ll_send_lessee_container)
            sendContent_warehouse= view!!.findViewById(R.id.ll_send_warehouse_container)
            trackContent= view!!.findViewById(R.id.rv_track)
            trackContent_all= view!!.findViewById(R.id.rl_track_container)
            trackAddBtn= view!!.findViewById(R.id.comp_btn_add_track)
            trackStartBtn= view!!.findViewById(R.id.comp_btn_start_track)

            containerContent= view!!.findViewById(R.id.ll_container_container)
            containerAddBtn= view!!.findViewById(R.id.comp_btn_add_container)
            crossdockingAmountContent= view!!.content_product_info.rl_amount_crossdocking_container

//            trackAddBtn.visibility= View.VISIBLE

            ivPict= view!!.findViewById<View>(R.id.content_product_pict).findViewById(R.id.iv_pict)

            trackAddBtn.background.setTint(T_ViewUtil.getColor(ctx, R.color.colorPrimaryDark))

            numberPickerComp= NumberPickerCompOld(ctx, mainContent.comp_number_picker)
            numberPickerComp.numberLowerBorder= numberLowerBorder
            numberPickerComp.listener= countListener
            initNumberPickerBtn()

            isTrackSealed= isTrackSealed
/*
            isPackagingVisible= isPackagingVisible
            isTrackVisible= isTrackVisible
            setSendKindAddresVisible(isSendKindVisible, isSendAddressVisible)
            isSendVisible_Lessee= isSendKindVisible
            isContainerVisible= isContainerVisible
 */

//            isSummaryMode= isSummaryMode
//            initCheckBox()

//            initSendKindComp()
//            sendAddressComp= packagingContent.comp_bar_fill_send_address
        }
    }

    protected fun initPackagingAdp(){
        if(view != null){
            packagingAdp= BoundProductPackagingAdp(ctx, null)
            packagingAdp.isMultiSelectionEnabled= true
            /**
             * Agar method turunan (override) di dalam BoundProductPackagingAdp bisa jalan semua
             */
            packagingAdp.dataList= packagingDataListFull
            packagingAdp.rv= view!!.rv_packaging!!

            packagingAdp.onPackagingCheckedChangeListener= { bv, isChecked, packaging ->
                Log.e("BoundProductItemComp", "isChecked =$isChecked packaging= $packaging data != null= ${data != null}")
                if(data != null){
                    if(isChecked){
                        if(data!!.boundPackaging == null)
                            data!!.boundPackaging= ArrayList()
                        data!!.boundPackaging!!.add(packaging)
                    } else
                        data!!.boundPackaging?.remove(packaging)
                }
            }
            isPackagingFixed= isPackagingFixed
        }
    }
    fun setCheckedItem(packagingList: Collection<PackagingModel>){
        packagingAdp.selectItem(packagingList)
    }

    protected fun initSendAdp_Lessee(){
        if(view != null){
            sendAdpLessee= BoundProductSendAdp_Lessee(ctx) //data?.boundSend
            sendAdpLessee.boundProduct= data
/*
            if(data != null){
                if(data!!.boundSend == null){
                    val emptyData= sendAdpLessee.addEmptyData()!!
//                    sendAdpLessee.setSendAmountAt(0, data!!.amount)
                    emptyData.amount= data!!.amount
                    data!!.boundSend= sendAdpLessee.dataList!! //ArrayList()
                    Log.e("ItemComp", "addEmptyData ATAS init emptyData= $emptyData data!!.boundSend!!.size= ${data!!.boundSend!!.size}")
                } else
                    sendAdpLessee.dataList= data?.boundSend
            }
 */
            sendAdpLessee.rv= view!!.rv_send_lessee!!
            sendAdpLessee.rv!!.addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL))
            sendAdpLessee.setOnUpdateDataListener { dataArray, pos, kind ->
                isOverallAmountVisible=
                    (dataArray?.isEmpty() ?: true
                            || !isSendVisible_Lessee ) //(!isSendKindVisible && !isSendAddressVisible))
                Log.e("BoundProductItemComp_L", "isOverallAmountVisible = $isOverallAmountVisible isSendVisible_Lessee = $isSendVisible_Lessee dataArray?.isZero() ?: true = ${dataArray?.isEmpty() ?: true}")
            }
            sendAdpLessee.onSendAmountChangeListener= { diff, pos ->
                data!!.amount += diff
                Log.e("ItemComp", "onSendAmountChangeListener data!!.boundSend!!.size= ${data!!.boundSend!!.size} data!!.amount= ${data!!.amount} data!!.boundSend!![pos].amount= ${data!!.boundSend!![pos].amount}")
            }
            sendAdpLessee.isAmountFixed= false

            setSendKindAddresVisible()
            sendAddBtn= view!!.findViewById(R.id.comp_btn_add_send)
            sendAddBtn.setOnClickListener { v ->
                sendAdpLessee.addEmptyData()
                Log.e("ItemComp", "addEmptyData BAWAH emptyData= || data!!.boundSend!!.size= ${data!!.boundSend!!.size}")
            }
        }
    }

    protected fun initSendAdp_Warehouse(){
        if(view != null){
            sendAdpWarehouse= BoundProductSendAdp_Warehouse(ctx, data?.boundSend) //boundProdSendList_full.toCollection(ArrayList())
            Log.e("ItemComp", "sendAdpWarehouse.dataList?.size= ATAS ${sendAdpWarehouse.dataList?.size} data?.boundSend= ${data?.boundSend} sendAdpWarehouse.itemCount= ${sendAdpWarehouse.itemCount}")
            sendAdpWarehouse.rv= view!!.rv_send_warehouse!!
            sendAdpWarehouse.rv!!.addItemDecoration(DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL))
            sendAdpWarehouse.onReceiptChangeListener= { receipt, pos, isBlank, isNotBlank_overall ->
//                if(isBlank_overall != isReceiptOk_comp)
                isReceiptOk_comp= isNotBlank_overall
                loge("receipt= $receipt pos= $pos isNotBlank_overall= $isNotBlank_overall")
            }
//            view!!.rv_send_warehouse!!.visibility= View.GONE
//            view!!.rv_send_lessee!!.visibility= View.GONE
/*
            sendAdpWarehouse.setOnUpdateDataListener { dataArray, pos, kind ->
                isOverallAmountVisible=
                    (dataArray?.isZero() ?: true
                            || !isSendVisible_Warehouse ) //(!isSendKindVisible && !isSendAddressVisible))
                Log.e("BoundProductItemComp_W", "isOverallAmountVisible = $isOverallAmountVisible isSendVisible_Lessee = $isSendVisible_Lessee dataArray?.isZero() ?: true = ${dataArray?.isZero() ?: true}")
            }
 */
//            isSendVisible_Warehouse= isSendVisible_Warehouse
            setSendKindAddresVisible()
        }
    }

    protected fun initTrackAdp(){
        if(view != null){
            trackAdp= BoundTrackAmountAdp(ctx)
            trackAdp.rv= view!!.rv_track
            trackAdp.setIntBorder(data?.amount ?: 0, 0, 0)
            trackAdp.setOnAmountTrackChangeListener_Comp { changeAmount, totalAmount, pos, direction ->
                if(pos == 1){
                    Log.e("BoundProductItemComp", "data?.product?.name = ${data?.product?.name} direction= $direction changeAmount= $changeAmount, totalAmount= $totalAmount, pos= $pos,")
                    if(direction != NumberPickerCompOld.Enum.INIT || isTrackForceSetNumber){
                        data?.boundTrack?.progres= totalAmount //+1 // --> Msh blum tau knp kok bisa berkurang 1
                        onTrackSingleChangeListener?.onTrackSingleChange(changeAmount, totalAmount)
                        loge(isTrackSealed, "isTrackSealed")
                        Log.e("BoundProductItemComp", "totalAmount= $totalAmount data?.boundTrack? (progres= ${data?.boundTrack?.progres} total= ${data?.boundTrack?.total} diff= ${data?.boundTrack?.diff})")
                    }
                    if(isTrackSealed && totalAmount > 0){
                        isTrackSealed= false
                        loge(totalAmount, "totalAmount")
                    }
                }
            }
            isAmountTrackFixed= isAmountTrackFixed
//            isTrackVisible= isTrackVisible

            trackAddBtn.setOnClickListener { v ->
                ctx.toast("trackAddBtn clicked")
                when(ctx){
                    is Activity -> ctx.finish()
                    is Fragment -> ctx.activity!!.finish()
                }
            }
            trackAddBtn.text= "Scan Produk"
            trackStartBtn.visibility = View.VISIBLE
            trackStartBtn.text = "Input Langsung"
            isTrackSealed= isTrackSealed

            trackStartBtn.setOnClickListener {
                data?.boundTrack?.progres = 1
                isTrackSealed = false
            }
        }
    }

    protected fun initContainerAdp(){
        if(view != null){
            containerAdpOld= BoundContainerAdp_old(ctx, null)
            containerAdpOld.rv= view!!.rv_container
            containerAdpOld.overallUpperBorder= data?.amount ?: 0

            containerAddBtn.setOnClickListener { v ->
                Log.e("startActForResult", "simpleAbsAct != null = ${simpleAbsAct != null}")
                if(simpleAbsAct != null){
                    val pairTitle= Pair(Constants.EXTRA_INTENT_TITLE, "Scan Container")
                    val containerDataList= downloadContainerData()
                    val strList= containerDataList?.toStringList(ArrayList()) { el, pos -> el.id }
                    val pairList= Pair(Constants.EXTRA_INTENT_LIST, strList)
                    startActForResult<ScanAct>(simpleAbsAct!!, Constants.REQUEST_SCAN, pairTitle, pairList){ reqCode, resCode, data ->
                        Log.e("startActForResult", "reqCode= $reqCode, resCode= $resCode")
                        if(reqCode == Constants.REQUEST_SCAN && resCode == Activity.RESULT_OK){
                            //!!! mulai request server data container
                            Log.e("startActForResult", "OK BRO")
                            Log.e("startActForResult", "OK BRO")
                            Log.e("startActForResult", "OK BRO")
                            val scannedContainerId= data?.getIntentData<String>(Constants.EXTRA_RESULT)
                            containerDataList?.searchElement { element, pos ->
                                element.id == scannedContainerId
                            }.notNull { scannedContainerData ->
                                val existingData= containerAdpOld.dataList?.searchElement { element, pos ->
                                    element.id == scannedContainerId
                                }
                                if(existingData == null)
                                    containerAdpOld.addData(scannedContainerData.copy())
                                else
                                    ctx.toast("Container ${scannedContainerData.name} sudah discan")
                            }
//                            containerAdp.addEmptyData()
                        }
                    }
                }
            }
/*
            containerAddBtn.background.setTint(T_ViewUtil.getColor(ctx, R.color.colorPrimaryDark))
            containerAdpOld.setOnUpdateDataListener { dataArray, pos, kind ->
                data?.boundContainer= dataArray as ArrayList<BoundContainerModel>
            }
            containerAdpOld.setOnAmountTrackChangeListener_Comp { changeAmount, totalAmount, pos, direction ->
                data.notNull { dataInt ->
                    val before= dataInt.boundContainer!![pos].amount
                    dataInt.boundContainer!![pos].amount= before +changeAmount
                }
            }
            containerAddBtn.text= "Scan Container"
            isAmountContainerFixed= isAmountContainerFixed

            containerAdpOld.setOnAmountTrackChangeListener_Comp { changeAmount, totalAmount, pos, direction ->
//                isAmountContainerFixed= totalAmount >= data?.amount ?: 0
                if(::trackAdp.isInitialized)
                    trackAdp.setAmountProgres(totalAmount)
            }

 */
        }
    }

    fun downloadContainerData(): ArrayList<BoundContainerModel>?{
       // return containerModelList.toCollection(ArrayList()) //!!!! Belum diimplemen
        if(processData != null) {
            val data = processData!![ctx as LifecycleOwner]  as Map<String, Any>?
            val containers = data!![Constants.Presenter.DATA_WH_CONTAINERS] as ArrayList<WarehouseContainer>?
            if(containers != null) {
                val returnData: ArrayList<BoundContainerModel> = ArrayList()
                containers.forEach {
                    returnData.add(BoundContainerModel(
                        it.id!!,
                        it.code!!
                    ))
                }

                return returnData
            }
        }
        return ArrayList()
    }

    @CallSuper
    open fun setSendKindAddresVisible(isSendKindVisible: Boolean= this.isSendKindVisible,
                                    isSendAddressVisible: Boolean= this.isSendAddressVisible){
        this.isSendKindVisible= isSendKindVisible
        this.isSendAddressVisible= isSendAddressVisible
        internalEdit {
            isSendVisible_Lessee= (isSendKindVisible || isSendAddressVisible)
                    && isSendVisible_Lessee_int
        }
    }

    fun resetNumberPickBtnVisibility(){
        if(numberPickerComp.getCount() <= numberLowerBorder){
            mainContent.comp_btn_add_amount.visibility= View.VISIBLE
            mainContent.comp_number_picker.visibility= View.GONE
        }
    }
    protected fun initNumberPickerBtn(){
        (mainContent.comp_btn_add_amount as Button).text= "tambah"
        mainContent.comp_btn_add_amount.setOnClickListener { v ->
            v.visibility= View.GONE
            mainContent.comp_number_picker.visibility= View.VISIBLE
//            T_ViewUtil.toast(ctx, "Tes Count Bro")
        }
    }
/*
    protected fun initCheckBox(){
        packagingCbComp= HashMap()
        packagingCbComp[PackagingEnum.PLASTIK]= packagingContent.cb_packaging_plastic
        packagingCbComp[PackagingEnum.BUBBLE_WRAP]= packagingContent.cb_packaging_bubble
        packagingCbComp[PackagingEnum.KERDUS]= packagingContent.cb_packaging_cardboard
    }
 */

/*
    protected fun initSendContent(){
        sendRv= sendContent_lessee.rv_send

    }

    protected fun initSendKindComp(){
        sendKindBtnComp= packagingContent.comp_bar_send_kind
        sendKindBtnComp.btn.isAllCaps= false
        sendKindBtnComp //Kurang dialog yang muncul saat ditekan
    }
 */

    @CallSuper
    protected fun setData_int(data: BoundProduct){
        if(data.product.img.dir.isNullOrEmpty())
            ivPict.setImageResource(data.product.img.resId!!)
        else
            reqUtil.loadImageToImageView(ivPict, RequestUtil.IMAGE_SIZE_TUMBNAIL_SMALL, data.product.img.dir!!)

        mainContent.tv_name.text= data.product.name
        mainContent.tv_kind.text= data.product.category?.name
        mainContent.tv_amount_unit.text= data.product.unit?.name

        val amount= data.amount
        setOverallProdAmount(amount)

//        if(isCrossdockingMode){
            crossdockingAmountContent.tv_amount_in_number.text= data.amount.toString()
            crossdockingAmountContent.tv_amount_out_number.text= data.amountOut.toString()

            crossdockingAmountContent.tv_amount_in_unit.text= data.product.unit?.name
            crossdockingAmountContent.tv_amount_out_unit.text= data.product.unit?.name
//        }

        Log.e("BoundProductItemComp", "setData_int() data.product.name= ${data.product.name}")
        if(::trackAdp.isInitialized && isTrackVisible){
            trackAdp.setIntBorder(amount, data.boundTrack!!.progres, data.boundTrack!!.diff)
            Log.e("BoundProductItemComp", "trackAdp data.boundTrack(amount= ${data.boundTrack!!.progres} progres= ${data.boundTrack!!.progres} diff= ${data.boundTrack!!.diff})")
            isTrackSealed= isTrackSealed
        }

        if(::packagingAdp.isInitialized){
            if(data.boundPackaging != null)
                setCheckedItem(data.boundPackaging!!)
        }

        if(::containerAdpOld.isInitialized && isContainerVisible){
            containerAdpOld.overallUpperBorder= data.amount
            containerAdpOld.dataList= data.boundContainer
        }

        if(::sendAdpLessee.isInitialized){
            if(data.amount.isNotZero()){
                if(data.boundSend == null){
                    val emptyData= sendAdpLessee.createEmptyData()!!
                    emptyData.amount= data.amount
                    data.boundSend= ArrayList() //sendAdpLessee.dataList!! //ArrayList()
                    data.boundSend!!.add(emptyData)
                    Log.e("ItemComp", "addEmptyData ATAS emptyData.amount= ${emptyData.amount} data.product.name= ${data.product.name} data!!.boundSend!!.size= ${data!!.boundSend!!.size}")
                }
                if(data.boundSend!!.size == 1
                    && data.boundSend!![0].amount != data.amount)
                    data.boundSend!![0].amount= data.amount
            }

//            sendAdpLessee.dataList= data.boundSend
            sendAdpLessee.boundProduct= data
        }

        if(::sendAdpWarehouse.isInitialized){
            sendAdpWarehouse.dataList= data.boundSend

            Log.e("ItemComp", "sendAdpWarehouse.dataList?.size= ${sendAdpWarehouse.dataList?.size} sendAdpWarehouse.itemCount= ${sendAdpWarehouse.itemCount}")
            try{
                for(send in data.boundSend!!)
                    Log.e("ItemComp", "data.product.name= ${data.product.name} send.kind?.name= ${send.method?.name} send.address= ${send.address} send.amount= ${send.amount}")
            }catch (e: Exception){}
        }


        if(amount > 0){
            mainContent.comp_btn_add_amount.visibility= View.GONE
            mainContent.comp_number_picker.visibility= View.VISIBLE
            numberPickerComp.setCount(amount)
//            numberPickerComp.enableSelection(false)
        }
/*
        val dataPackSend= data.boundPackSend
        if(dataPackSend != null){
            for(packaging in dataPackSend.packaging){
                packagingCbComp[packaging]?.isChecked= true
            }
            sendKind= dataPackSend.sendKind //.name.toLowerCase().capitalize()
            sendAddressComp.et.setText(dataPackSend.sendAddress)
        }
 */
//        sendAdpLessee.dataList= data.boundSend
//        packagingAdp.dataList= data.boundPackaging
//        sendAdpWarehouse.dataList= data.boundSend
//        containerAdp.dataList= data.boundContainer
    }

    protected fun setOverallProdAmount(amount: Int){
//        mainContent.tv_amount_number.text= amount.toString()
        overallNumber= amount
        mainContent.tv_amount_unit.text= data!!.product.unit?.name
    }

    fun setTrackText(txtUpper: String= trackAdp?.txtUpper ?: "outbound", txtLower: String= trackAdp?.txtLower ?: "keluar"){
        if(::trackAdp.isInitialized)
            trackAdp.setText(txtUpper, txtLower)
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
        if(containerAdpOld.dataList != null){
            for(ctnModel in containerAdpOld.dataList!!)
                ctnRes.add(ctnModel)
        }
        return ctnRes.toTypedArray()
    }

    fun getTrackDiffAmount(): Int{
        return if(::trackAdp.isInitialized) trackAdp.amountDiff
            else 0
    }
/*
    fun setOnAmountContainerAmountChangeListener(l: (changeAmount: Int, totalAmount: Int, pos: Int) -> Unit_){
        if(::containerAdp.isInitialized)
            containerAdp.setOnAmountTrackChangeListener_Comp(l)
    }
 */

    var onTrackSingleChangeListener: OnTrackSingleChangeListener?= null
    interface OnTrackSingleChangeListener{
        fun onTrackSingleChange(changeAmount: Int, progres: Int)
    }
    fun setOnTrackSingleChangeListener(l: (changeAmount: Int, progres: Int) -> Unit){
        onTrackSingleChangeListener= object: OnTrackSingleChangeListener{
            override fun onTrackSingleChange(changeAmount: Int, progres: Int) {
                l(changeAmount, progres)
            }
        }
    }
/*
    var onReceiptChangeListener: ((receipt: String, pos: Int, isBlank: Boolean, isBlank_overall: Boolean) -> Unit)?= null
        set(v){
            field= v
            if(::sendAdpWarehouse.isInitialized)
                sendAdpWarehouse.onReceiptChangeListener= v
        }
 */

    var onReceiptChangeListener_Comp: ((isNotBlank_overall: Boolean) -> Unit)?= null
}

 */