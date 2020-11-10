package com.sigudang.android.adapters.BoundProductListAdp.nested
///*
import android.content.Context
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.sigudang.android.models.PackagingModel
//import kotlinx.android.synthetic.main.component_fill_cb__old.view.*
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.std.tool.util.`fun`.findViewByType
import sidev.lib.implementation.R
import java.lang.Exception

class BpPackagingAdp(c: Context) //, dataList: ArrayList<PackagingModel>?
    : RvAdp<PackagingModel, LinearLayoutManager>(c){
    override val itemLayoutId: Int
        get() = R.layout._simul_sigud_component_fill_cb

    init{
        onUpdateDataListener= object: OnUpdateDataListener(){
            override fun onUpdateData(
                dataArray: List<PackagingModel>?,
                pos: Int,
                kind: DataUpdateKind
            ) {
                if(dataArray != null){
                    val isCheckedList= ArrayList<Boolean>()
                    for(data in dataArray)
                        isCheckedList.add(data.isSelected)
                    this@BpPackagingAdp.isCheckedList= isCheckedList
                }
            }
        }
    }

//    private var internalEdit= false
    /**
     * Hanya melakukan pengecekan saat @if containerView.size < dataList!!.size
     * karena @see bindVH() @param pos disesuaikan dg dataList!!.size,
     * jadi jika @param pos trahir kurang dari containerView.size tidak masalah
     */
    var isCheckedList: ArrayList<Boolean> = ArrayList()
        set(v){
//            val newV= v//ArrayList<Boolean>()
            Log.e("PackagingAdp", "SBLUM v.size= ${v.size} dataList!!.size= ${dataList?.size}")
            if(dataList != null){
                if(v.size < dataList!!.size){
//                    newV= ArrayList(v)
                    Log.e("PackagingAdp", "MASUK v.size= ${v.size}")
                    val endLimit= dataList!!.size -v.size
                    for(i in 0 until endLimit)
                        v.add(false)
                }
            }
            Log.e("PackagingAdp", "SESUDAH v.size= ${v.size} dataList!!.size= ${dataList?.size}")
            field= v
//            notifyDataSetChanged_()
        }
    var checkedDataList= ArrayList<PackagingModel>()
        private set

    var isCheckEnabled= false
        set(v){
            field= v
            notifyDataSetChanged_()
        }

/*
    override fun updateData_int(dataList: ArrayList<PackagingModel>?, isInternalEdit: Boolean) {
        Log.e("TES_PACK_ADP", "isCheckedList != null = ${isCheckedList != null}")
        Log.e("TES_PACK_ADP", "isCheckedList != null = ${isCheckedList != null}")
        Log.e("TES_PACK_ADP", "isCheckedList != null = ${isCheckedList != null}")
        isCheckedList=
            if(isCheckedList != null) isCheckedList
            else ArrayList()
    }
 */

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: PackagingModel) {
        val cb= vh.itemView.findViewByType<CheckBox>()!! //(vh.itemView as CheckBox)
        cb.text= data.name
//        internalEdit= true
        Log.e("PackagingAdp", "isCheckedList.size= ${isCheckedList.size} itemCount= $itemCount pos= $pos")
        cb.isChecked= isCheckedList[pos] // catch (e: Exception){false}
//        internalEdit= false
/*
        cb.setOnClickListener { v ->
            val isChecked= cb.isChecked
            cb.isChecked= !isChecked
        }
// */
        cb.setOnCheckedChangeListener{ bv, isChecked ->
            if(vh.isAdpPositionSameWith(pos)){
                isCheckedList[pos]= isChecked
                data.isSelected= isChecked
//                data.isSelected= isChecked
                if(isChecked)
                    checkedDataList.add(data)
                else
                    checkedDataList.remove(data)
                onPackagingCheckedChangeListener?.invoke(bv, isChecked, data)
            }
        }
        cb.isEnabled= isCheckEnabled
    }

    override fun setupLayoutManager(context: Context): LinearLayoutManager {
        return LinearLayoutManager(context)
    }

    /**
     * @return empty jika gakda yang dipilih
     */
    fun getChecked(): ArrayList<PackagingModel>{
        val array= ArrayList<PackagingModel>()
        if(dataList != null)
            for((i, packaging) in dataList!!.withIndex())
                if(isCheckedList[i])
                    array.add(packaging)
        return array
    }

    private var isCheckProceed= false
    override val selectFilterFun: ((dataFromList: PackagingModel, dataFromInput: PackagingModel, posFromList: Int) -> Boolean)?
        = { dataFromList, dataFromInput, posFromList ->
            val bool= dataFromList.name == dataFromInput.name
        Log.e("Packaging_Adp", "dataFromList.name == dataFromInput.name = ${dataFromList.name == dataFromInput.name} dataFromList.name = ${dataFromList.name } posFromList= $posFromList")
            if(bool){
                isCheckedList[posFromList]= true
                isCheckProceed= true
            }
            bool
        }

    override fun selectItem(list: Collection<PackagingModel>?) {
        isCheckProceed= false
        for(i in isCheckedList.indices)
            isCheckedList[i]= false
        super.selectItem(list)
        if(isCheckProceed)
            notifyDataSetChanged_()
    }

    /**
     * Digunakan untuk memasang [isCheckedList] ke [dataList].
     */
    fun finishIsCheckedList(){
        if(dataList != null){
            for((i, data) in dataList!!.withIndex())
                data.isSelected= try{ isCheckedList[i] } catch (e: Exception){ false }
        }
    }
/*
    fun setCheckedItem(vararg packaging: PackagingModel){
        Log.e("PackagingAdp", "setCheckedItem packaging.size = ${packaging.size}")
        for(i in isCheckedList.indices)
            isCheckedList[i]= false
        checkedDataList= ArrayList()

        var isProceed= false
        for(perPackaging in packaging){
            dataList?.searchElement { element, pos ->
                val bool= element.name == perPackaging.name
                if(bool) {
                    isCheckedList[pos]= true
                    checkedDataList.add(perPackaging)
                    isProceed= true
                }
                bool
            }
        }
        if(isProceed)
            notifyDataSetChanged_()
    }
 */

    var onPackagingCheckedChangeListener: ((buttonView: CompoundButton, isChecked: Boolean, packaging: PackagingModel) -> Unit)?= null
}

// */