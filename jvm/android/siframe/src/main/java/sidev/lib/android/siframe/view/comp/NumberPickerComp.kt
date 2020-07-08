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
import sidev.lib.android.siframe.arch.value.BoxedVal
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.view.ModEt
import sidev.lib.android.siframe.view.comp.data.NumberPickerData
import sidev.lib.universal.`fun`.notNull
import sidev.lib.universal.`fun`.notNullTo
import sidev.lib.universal.`fun`.roundClosest

open class NumberPickerComp(ctx: Context): ViewComp<NumberPickerData>(ctx){
    override val viewLayoutId: Int
        get() = R.layout._sif_comp_number_picker
    override val isViewSaved: Boolean= true

    var defaultLowerBorder: Int= 0
    var defaultUpperBorder: Int= Int.MAX_VALUE

    var enabledColorId= _ColorRes.COLOR_PRIMARY_DARK
    var disabledColorId= _ColorRes.COLOR_DISABLED
    var inBorderColorId= _ColorRes.COLOR_IMMUTABLE


    override fun initData(position: Int): NumberPickerData?
        = NumberPickerData(defaultLowerBorder, defaultLowerBorder, defaultUpperBorder)

    override fun bindComponent(position: Int, v: View, valueBox: BoxedVal<NumberPickerData>) {
        val ivMinus= v.findViewById<ImageView>(R.id.iv_minus)!!
        val ivPlus= v.findViewById<ImageView>(R.id.iv_plus)!!
        val etNumber= v.findViewById<ModEt>(R.id.et)!!

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

    override fun setComponentEnabled(position: Int, v: View?, enable: Boolean): Boolean {
        return (v ?: getViewAt(position)).notNullTo { view ->
            val colorId= if(enable) enabledColorId
                else disabledColorId

            val ivMinus= view.findViewById<ImageView>(R.id.iv_minus)!!
            val ivPlus= view.findViewById<ImageView>(R.id.iv_plus)!!
            val etNumber= view.findViewById<ModEt>(R.id.iv_plus)!!

            _ViewUtil.setBgColor(ivMinus, colorId)
            _ViewUtil.setBgColor(ivPlus, colorId)

            ivMinus.isEnabled= enable
            ivPlus.isEnabled= enable
            etNumber.isEnabled= enable

            //Agar warna [ivMinus] dan [ivMinus] sesuai dg keadaan data.number.
            if(enable)
                getDataAt(position).notNull { etNumber.setText(it.number.toString()) }

            true
        } ?: false
    }


    fun getNumberAt(pos: Int): Int?
        = getDataAt(pos)?.number

    fun setNumberAt(pos: Int, number: Int): Boolean{
        return getDataAt(pos).notNullTo {
            it.number= number
            getViewAt(pos).notNull { v ->
                //Jika view disimpan, maka assign saja [it.number] ke view
                //  agar warna [ivMinus] dan [ivPlus] bisa menyesuaikan perubahan border.
                v.findViewById<EditText>(R.id.et)
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
                v.findViewById<EditText>(R.id.et)
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
                v.findViewById<EditText>(R.id.et)
                    .notNull { et -> et.setText(it.number.toString()) }
            }
            true
        } ?: false
    }
}