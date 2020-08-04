package com.sigudang.android.utilities.view.component
/*
import android.content.Context
import android.graphics.PorterDuff
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.sigudang.android.R
import com.sigudang.android._template.obj.lessThanEqual
import com.sigudang.android._template.obj.moreThanEqual
import id.go.surabaya.disperdagin.utilities.T_ViewUtil


open class NumberPickerCompOld(c: Context, view: View?): ViewComp_old(c, view){
    override val layoutId: Int
        get() = R.layout.component_number_picker

    private lateinit var ivMinus: ImageView
    private lateinit var ivPlus: ImageView
    private lateinit var etNumber: EditText

    private var directionJustNow= Enum.INIT

    val colorEnabled= R.color.colorPrimaryDark
    val colorDisabled= R.color.colorTransDark
    val colorBorder= R.color.colorText

    /**
     * Angka yang ada di dalam <code>etNumber</code> dapat ditulis
     * dari <code>numberLowerBorder</code> sampai <code>numberUpperBorder</code>
     */
    var numberLowerBorder= 1
        set(v){
            field= v
            if(::ivMinus.isInitialized)
                processPickerColorFromEt(ivMinus, etNumber.text, v, lessThanEqual)
        }
    var numberUpperBorder= Int.MAX_VALUE
        set(v){
            field= v
            if(::ivPlus.isInitialized)
                processPickerColorFromEt(ivPlus, etNumber.text, v, moreThanEqual)
        }

    private var enable= true

    var isSummaryMode= false
        set(v){
            field= v
            if(view != null){
                val vis= if(v) View.GONE
                    else View.VISIBLE
                ivMinus.visibility= vis
                ivPlus.visibility= vis
                enableSelection(!v)
            }
        }

    override fun initViewComp() {
        if(view != null){
            ivMinus= view!!.findViewById(R.id.iv_minus)
            ivPlus= view!!.findViewById(R.id.iv_plus)
            etNumber= view!!.findViewById(R.id.et_number)


//        val containerView= vDialog.comp_prod_detail.comp_selection
//            var direction= Enum.SET
            ivMinus.setOnClickListener { vInt ->
                var count= getCount()
                if(count > numberLowerBorder){
                    directionJustNow= Enum.MINUS
                    etNumber.setText((--count).toString())
//                    onAmountChangeListener?.onCountChange(view!!, count, Enum.MINUS)
                }
            }
            ivPlus.setOnClickListener { vInt ->
                var count= getCount()
                if(count < numberUpperBorder){
                    directionJustNow= Enum.PLUS
                    etNumber.setText((++count).toString())
                }
//                onAmountChangeListener?.onCountChange(view!!, count, Enum.PLUS)
            }
            etNumber.addTextChangedListener(
                object: TextWatcher {
                    var countBefore: Int= 0
                    var isCorrectionRunning= false

                    override fun afterTextChanged(s: Editable?) {
//                        var countStr= numberLowerBorder.toString()
                        if(!isCorrectionRunning && s != null && s.isNotEmpty()){
                            var numb= s.toString().toInt()
                            if(numb !in numberLowerBorder .. numberUpperBorder){
                                if(numb > numberUpperBorder)
                                    numb= numberUpperBorder
                                if(numb < numberLowerBorder)
                                    numb= numberLowerBorder
                                isCorrectionRunning= true
                                s.replace(0, s.length, numb.toString())
                                isCorrectionRunning= false
                            } else if(s.length > 1 && s.startsWith("0")){
                                isCorrectionRunning= true
                                s.delete(0, 1)
                                isCorrectionRunning= false
                            }
                        }
                        if(!isCorrectionRunning){
                            processPickerColorFromEt(ivMinus, s, numberLowerBorder, lessThanEqual)
                            processPickerColorFromEt(ivPlus, s, numberUpperBorder, moreThanEqual)

                            val countAfter= getCount()
                            Log.e("NumberPickerComp", "countAfter = $countAfter")
                            if(countAfter in numberLowerBorder .. numberUpperBorder){ ///*>= numberLowerBorder*/){
                                Log.e("NumberPickerComp", "MASUK BRO!!! countAfter = $countAfter directionJustNow= $directionJustNow")
//                            Log.e("NumberPickerComp", "Terjadi perubahan >> number= $number directionJustNow= $directionJustNow")
                                listener?.onCountChange(view!!, countBefore, countAfter, directionJustNow)
                                directionJustNow= Enum.SET_TEXT
//                                T_ViewUtil.toast(ctx, "Tes number= $number")
                            }
                        }
/*
                        if(enable){
                            if(s != null && s.isNotEmpty()){
                                if(s.toString().toInt() <= numberLowerBorder)
                                    ivMinus.background.setColorFilter(
                                        try {
                                            ContextCompat.getColor(ctx, colorBorder)
                                        } catch (e: Exception){
                                            ContextCompat.getColor(ctx, R.color.colorText)
                                        }, PorterDuff.Mode.SRC_ATOP
                                    )
                                else
                                    ivMinus.background.setColorFilter(
                                        ContextCompat.getColor(ctx, colorEnabled), PorterDuff.Mode.SRC_ATOP
                                    )
                                countStr= s.toString()
                            } else{
                                ivMinus.background.setColorFilter(
                                    ContextCompat.getColor(ctx, colorBorder), PorterDuff.Mode.SRC_ATOP
                                )
                            }
                        }
 */
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        if(!isCorrectionRunning)
                            countBefore= getCount()
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }
                }
            )
            etNumber.setText(numberLowerBorder.toString())
            etNumber.inputType= InputType.TYPE_CLASS_NUMBER
            etNumber.hint= numberLowerBorder.toString()
        }
    }

    enum class Enum{
        PLUS, MINUS, SET, SET_TEXT, INIT
    }

    var listener: CompListener?= null
    interface CompListener{
        fun onCountChange(v: View, before: Int, after: Int, direction: Enum) //(containerView: View, count: Int, direction: Enum)
    }
    fun setOnCountChange(func: ((v: View, before: Int, after: Int, direction: Enum) -> Unit)?){
        listener = if(func != null)
            object : CompListener{
                override fun onCountChange(v: View, before: Int, after: Int, direction: Enum) {
                    func(v, before, after, direction)
                }
            }
        else
            null
    }
/*
    private fun passNumberToListener(number: Int, direction: Enum){
        if(direction != Enum.SET_TEXT
            || ){

        }
    }
 */

    fun setCount(count: Int, isInit: Boolean= false){
        directionJustNow= if(!isInit) Enum.SET
            else Enum.INIT
        Log.e("NumberPickerComp", "setCount() count= $count isInit= $isInit directionJustNow= $directionJustNow")
        etNumber.setText(count.toString())
//        if(view != null)
//            onAmountChangeListener?.onCountChange(view!!, count, Enum.SET)
    }
    fun getCount(): Int{
        val str= etNumber.text.toString()
        return if(str.isNotEmpty()) str.toInt()
            else numberLowerBorder
    }

    fun enableUp(enable: Boolean, colorDisabled: Int= this.colorDisabled){
        val color= if(enable) colorEnabled
            else colorDisabled
        ivPlus.isEnabled= enable
        val mode= //if(enable) PorterDuff.Mode.SRC_ATOP
            PorterDuff.Mode.SRC_ATOP
        Log.e("MuberPickerComp", "enableUp enable= $enable, ivPlus.isEnabled= ${ivPlus.isEnabled}")
        processPickerColorFromEnable(ivPlus, numberUpperBorder, color, mode, moreThanEqual)
    }
    fun enableDown(enable: Boolean, colorDisabled: Int= this.colorDisabled){
        val color= if(enable) colorEnabled
            else colorDisabled
        ivMinus.isEnabled= enable
        val mode= //if(enable) PorterDuff.Mode.SRC_ATOP
            PorterDuff.Mode.SRC_ATOP
        processPickerColorFromEnable(ivMinus, numberLowerBorder, color, mode, lessThanEqual)
    }
    fun enableSelection(enable: Boolean){
        this.enable= enable
        if(view != null){
            val color= if(enable) colorEnabled
            else colorDisabled
            val mode= //if(enable) PorterDuff.Mode.SRC_ATOP
                PorterDuff.Mode.SRC_ATOP
            ivMinus.isEnabled= enable
            ivPlus.isEnabled= enable
            etNumber.isEnabled= enable

            processPickerColorFromEnable(ivMinus, numberLowerBorder, color, mode, lessThanEqual)
            processPickerColorFromEnable(ivPlus, numberUpperBorder, color, mode, moreThanEqual)
/*
            ivMinus.background.setColorFilter(
                ContextCompat.getColor(ctx,
                    if(etNumber.text.toString().toInt() <= numberLowerBorder)
                        colorBorder
                    else color
                ), mode
            )
            ivPlus.background.setColorFilter(
                ContextCompat.getColor(ctx, color), mode
            )
 */
        }
    }

    /**
     * @param conditionInBorder digunakan untuk mengisi apakah
     * etNumber.text.toString().toInt() melebihi atau kurang dari
     * @param numberBorder
     */
    private inline fun processPickerColorFromEnable(v: View, numberBorder: Int, color: Int, mode: PorterDuff.Mode,
                                         conditionInBorder: (i: Int, numberBorder: Int) -> Boolean){
        v.background.setColorFilter(
            ContextCompat.getColor(ctx,
                if(conditionInBorder(etNumber.text.toString().toInt(), numberBorder)) //if(etNumber.text.toString().toInt() <= numberLowerBorder)
                    colorBorder
                else color
            ), mode
        )
    }

    /**
     * @param conditionInBorder digunakan untuk mengisi apakah
     * @param s melebihi atau kurang dari
     * @param numberBorder
     */
    private inline fun processPickerColorFromEt(v: View, s: CharSequence?, numberBorder: Int,
                                         conditionInBorder: (i: Int, numberBorder: Int) -> Boolean){
        if(enable){
            if(s != null && s.isNotEmpty()){
                if(conditionInBorder(s.toString().toInt(), numberBorder))//if(s.toString().toInt() <= numberBorder)
                    v.background.setColorFilter(
                        try {
                            ContextCompat.getColor(ctx, colorBorder)
                        } catch (e: Exception){
                            ContextCompat.getColor(ctx, R.color.colorText)
                        }, PorterDuff.Mode.SRC_ATOP
                    )
                else
                    v.background.setColorFilter(
                        ContextCompat.getColor(ctx, colorEnabled), PorterDuff.Mode.SRC_ATOP
                    )
//                countStr= s.toString()
            } else{
                v.background.setColorFilter(
                    ContextCompat.getColor(ctx, colorBorder), PorterDuff.Mode.SRC_ATOP
                )
            }
        }
    }

    fun setEtTextColor(color: Int){
        if(::etNumber.isInitialized)
            T_ViewUtil.changeTextColor(color, ctx, etNumber)
    }
}

 */