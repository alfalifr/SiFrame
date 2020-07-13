package sidev.lib.android.siframe.view.comp

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import sidev.lib.android.siframe.R
import sidev.lib.android.siframe._customizable._ColorRes
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.arch.value.BoxedVal
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.findView
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.view.ModEt
import sidev.lib.android.siframe.view.comp.data.NumberPickerData
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.`fun`.notNullTo
import sidev.lib.universal.`fun`.roundClosest

open class NumberPickerComp<I>(ctx: Context): ViewComp<NumberPickerData, I>(ctx){
    override val viewLayoutId: Int
        get() = R.layout._sif_comp_number_picker
    override val isViewSaved: Boolean= true

    open val etNumberId: Int= _Config.ID_ET_NUMBER
    open val ivPlusId: Int= _Config.ID_IV_PLUS
    open val ivMinusId: Int= _Config.ID_IV_MINUS

    var defaultLowerBorder: Int= 0
    var defaultUpperBorder: Int= Int.MAX_VALUE

    var enabledColorId= _ColorRes.COLOR_PRIMARY_DARK
    var disabledColorId= _ColorRes.COLOR_DISABLED
    var inBorderColorId= _ColorRes.COLOR_IMMUTABLE


    override fun initData(position: Int, inputData: I?): NumberPickerData?
        = NumberPickerData(defaultLowerBorder, defaultLowerBorder, defaultUpperBorder)

    override fun bindComponent(position: Int, v: View,
                               valueBox: BoxedVal<NumberPickerData>, inputData: I?) {
        val ivPlus= v.findViewById<ImageView>(ivPlusId)!!
        val ivMinus= v.findViewById<ImageView>(ivMinusId)!!
        val etNumber= v.findView(etNumberId) ?: v.findViewByType<ModEt>()!!
        //v.findViewById<ModEt>(R.id.et)!!
                // Diganti [findViewByType] agar lebih fleksibel dg view yg beda id.
        val data= valueBox.value!!

        ivMinus.setOnClickListener { etNumber.setText((data.number -1).toString()) }
        ivPlus.setOnClickListener { etNumber.setText((data.number +1).toString()) }

        etNumber.inputType= InputType.TYPE_CLASS_NUMBER
        etNumber.hint= data.lowerBorder.toString()
        etNumber.addOnceTextChangedListener(object : TextWatcher{
            var isInternalEdit= false
            override fun afterTextChanged(s: Editable?) {
                if(s?.isNotBlank() == true){
                    var num= s.toString().toInt()

                    val ivMinusColorId=
                        if(num == data.lowerBorder) inBorderColorId
                        else enabledColorId
                    val ivPlusColorId=
                        if(num == data.upperBorder) inBorderColorId
                        else enabledColorId

                    _ViewUtil.setBgColor(ivMinus, ivMinusColorId)
                    _ViewUtil.setBgColor(ivPlus, ivPlusColorId)

                    if(!isInternalEdit){
                        val numRange= data.lowerBorder .. data.upperBorder
                        if(num !in numRange){
                            isInternalEdit= true
                            num= num.roundClosest(numRange)
                            data.number= num
                            s.clear()
                            s.append(num.toString())
                            isInternalEdit= false
                        }
                    }
                    data.number= num
                } else{
                    data.number= data.lowerBorder
                    _ViewUtil.setBgColor(ivMinus, inBorderColorId)
                    _ViewUtil.setBgColor(ivPlus, enabledColorId)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etNumber.setText(data.number.toString())
    }

    override fun setComponentEnabled(position: Int, v: View?, enable: Boolean) {
        (v ?: getViewAt(position)).notNullTo { view ->
            val colorId= if(enable) enabledColorId
                else disabledColorId

            val ivPlus= view.findViewById<ImageView>(ivPlusId)!!
            val ivMinus= view.findViewById<ImageView>(ivMinusId)!!
            val etNumber= view.findView(etNumberId) ?: view.findViewByType<EditText>()!!
            //.findViewById<ModEt>(R.id.iv_plus)!!

            _ViewUtil.setBgColor(ivMinus, colorId)
            _ViewUtil.setBgColor(ivPlus, colorId)

            ivMinus.isEnabled= enable
            ivPlus.isEnabled= enable
            etNumber.isEnabled= enable

            //Agar warna [ivMinus] dan [ivMinus] sesuai dg keadaan data.number.
            if(enable)
                getDataAt(position).notNull { etNumber.setText(it.number.toString()) }

            true
        }
    }


    fun getNumberAt(pos: Int): Int? = getDataAt(pos)?.number

    fun setNumberAt(pos: Int, number: Int): Boolean{
        return getDataAt(pos).notNullTo {
            it.number= number
            getViewAt(pos).notNull { v ->
                //Jika view disimpan, maka assign saja [it.number] ke view
                //  agar warna [ivMinus] dan [ivPlus] bisa menyesuaikan perubahan border.
                (v.findView(etNumberId) ?: v.findViewByType<EditText>()) //.findViewById<EditText>(R.id.et)
                    .notNull { et -> et.setText(it.number.toString()) }
            }
            true
        } ?: false
    }

    /**
     * @param lowerBorder jika lebih besar atau sama dengan dari [NumberPickerData.upperBorder],
     *   maka scr otomatis [lowerBorder] diganti dg [NumberPickerData.upperBorder] -1.
     */
    fun setLowerNumberAt(pos: Int, lowerBorder: Int): Boolean{
        return getDataAt(pos).notNullTo {
            it.lowerBorder= if(lowerBorder < it.upperBorder) lowerBorder
                else it.upperBorder -1
            getViewAt(pos).notNull { v ->
                //Jika view disimpan, maka assign saja [it.number] ke view
                //  agar warna [ivMinus] dan [ivPlus] bisa menyesuaikan perubahan border.
                (v.findView(etNumberId) ?: v.findViewByType<EditText>())
                    .notNull { et ->
                        et.hint= it.lowerBorder.toString()
                        et.setText(it.number.toString())
                        loge("setLowerNumberAt($pos) lowerBorder= $lowerBorder assigned= ${it.lowerBorder}")
                    }
            }
            true
        } ?: false
    }
    /**
     * @param upperBorder jika lebih kecil atau sama dengan dari [NumberPickerData.lowerBorder],
     *   maka scr otomatis [upperBorder] diganti dg [NumberPickerData.lowerBorder] +1.
     */
    fun setUpperNumberAt(pos: Int, upperBorder: Int): Boolean{
        return getDataAt(pos).notNullTo {
            it.upperBorder= if(upperBorder > it.lowerBorder) upperBorder
                else it.lowerBorder +1
            getViewAt(pos).notNull { v ->
                //Jika view disimpan, maka assign saja [it.number] ke view
                //  agar warna [ivMinus] dan [ivPlus] bisa menyesuaikan perubahan border.
                (v.findView(etNumberId) ?: v.findViewByType<EditText>())
                    .notNull { et -> et.setText(it.number.toString()) }
            }
            true
        } ?: false
    }
}