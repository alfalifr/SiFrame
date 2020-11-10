package com.sigudang.android.fragments.bottomsheet

import android.view.View
import android.widget.EditText
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.txt
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.sigudang.util.T_TimeUtil
import sidev.lib.number.isPositive
import java.lang.Exception

class BsPickDateFrag : BottomSheetAbsFr_OpenListener<String>(){
    override val bottomSheetType: BottomSheetType
        get() = BottomSheetType.WITH_HEADER
    override val bsLayoutId: Int
        get() = R.layout._simul_sigud_content_bs_date_pick

    private lateinit var etDay: EditText
    private lateinit var etMonth: EditText
    private lateinit var etYear: EditText

    private var day: Int= -1
    private var month: Int= -1
    private var year: Int= -1


    override fun initView(v: View) {
        initET()
    }

    fun initET(){
        etDay= contentView.findViewById(R.id.et_dd)
        etMonth= contentView.findViewById(R.id.et_mm)
        etYear= contentView.findViewById(R.id.et_yyyy)

        val timeNow= T_TimeUtil.timestamp("dd/MM/yyyy")
        val timeNowArr= timeNow.split("/")

        etDay.setText(timeNowArr[0])
        etMonth.setText(timeNowArr[1])
        etYear.setText(timeNowArr[2])

        if(day.isPositive()) etDay.txt= day.toString()
        if(month.isPositive()) etMonth.txt= month.toString()
        if(year.isPositive()) etYear.txt= year.toString()

        val etM= etMonth.text
        loge("initET() day= $day month= $month etM= $etM year= $year")
/*
        contentView.comp_btn_confirm.setOnClickListener { v ->
            listener?.onConfirmDatePick(v, getDateStr())
            dismiss()
        }
 */
    }
/*
    fun setBtnConfirmText(text: String){
        btnConfirmText= text
        try {
            (contentView.comp_btn_confirm as Button).text= text
        } catch (e: Exception){}
    }
 */

    fun getDateStr(): String{
        val day= etDay.text.toString().toInt()
        val month= etMonth.text.toString().toInt()
        val year= etYear.text.toString().toInt()

        return T_TimeUtil.getDateString(day, month, year)
    }

    fun setDate(day: Int, month: Int, year: Int){
        loge("day= $day month= $month year= $year")
        this.day= day
        this.month= month
        this.year= year
        try{
            etDay.txt= day.toString()
            etMonth.txt= month.toString()
            etYear.txt= year.toString()
            loge("TRY berhasil day= $day month= $month year= $year")
        } catch (e: Exception){ /*ignore*/ }
    }

    override fun createData(contentView: View): String? {
        return getDateStr()
    }
/*
    var listener: OnConfirmDatePickListener?= null
    interface OnConfirmDatePickListener{
        fun onConfirmDatePick(v: View, dateString: String)
    }
 */
}