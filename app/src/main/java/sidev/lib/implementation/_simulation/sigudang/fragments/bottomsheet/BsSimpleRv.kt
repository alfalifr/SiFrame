package com.sigudang.android.fragments.bottomsheet

import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main._simul_sigud_content_bs_rv.view.*
import sidev.kuliah.agradia.template.adp.SimpleAbsRecyclerViewAdapter
import sidev.lib.implementation.R

abstract class BsSimpleRv<RV: SimpleAbsRecyclerViewAdapter<D,*>, D> : BottomSheetAbsFr<D>(){
    override val bottomSheetType: BottomSheetType
        get() = BottomSheetType.WITH_HEADER
    override val bsLayoutId: Int
        get() = R.layout._simul_sigud_content_bs_rv

    lateinit var rvAdp: RV
        protected set
    var dataList: ArrayList<D>?= null
        set(v){
            field= v
            if(::rvAdp.isInitialized)
                rvAdp.dataList= v
        }

    var selectedPos= -1
    var selectedData: D?= null

    override fun initView_int(v: View) {
        rvAdp= initAdp()
        rvAdp.isCheckIndicatorShown= true
        rvAdp.rv= v.rv
        rvAdp.selectItem(selectedPos)
        rvAdp.selectItem(selectedData)
        rvAdp.dataList= dataList


        containerView.findViewById<View>(R.id.comp_btn_confirm).setOnClickListener { v ->
            Log.e("BsSimpleRv", "MASUK BRO!!! onBsRvBtnClickListener == null ${onBsRvBtnClickListener == null}")
            val selectedData= createData(contentView)
            onBsRvBtnClickListener?.invoke(v, selectedData, rvAdp.selectedItemPos_single)
            onBsBtnlickListener?.invoke(selectedData)
            setSelectedPos_int(rvAdp.selectedItemPos_single)
            dismiss()
        }
        dialog?.setOnDismissListener{ d ->
            resetItemSelection()
        }
    }

    abstract fun initAdp(): RV

    override fun createData(containerView: View): D? {
        return rvAdp.getDataAt(rvAdp.selectedItemPos_single)
    }

    private fun setSelectedPos_int(pos: Int){
        if(pos >= 0)
            selectedPos= pos
    }

    fun selectItem(data: D?){
        selectedData= data
        if(::rvAdp.isInitialized)
            selectedPos= rvAdp.selectItem(data)
    }
    fun selectItem(pos: Int= selectedPos){
        selectedPos= pos
        if(::rvAdp.isInitialized)
            rvAdp.selectItem(pos)
    }

    fun resetItemSelection(){
        rvAdp.resetItemSelection()
        selectedPos= -1
    }

    /**
     * @param pos -1 jika belum ada item dipilih
     */
    var onBsRvBtnClickListener: ((v: View?, data: D?, pos: Int) -> Unit) ?= null
}