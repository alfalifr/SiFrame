package sidev.lib.android.siframe.view.comp

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import sidev.lib.`val`.Assignment
import sidev.lib.android.siframe.R
import sidev.lib.android.std.`val`._ColorRes
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util.`fun`.findView
import sidev.lib.android.std.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.`val`._SIF_Config
import sidev.lib.android.siframe.view.ModEt
import sidev.lib.android.siframe.view.comp.data.NumberPickerData
import sidev.lib.android.std.tool.util.`fun`.asResNameOrNullBy
import sidev.lib.android.std.tool.util.`fun`.txt
import sidev.lib.check.notNull
import sidev.lib.check.notNullTo
import sidev.lib.exception.ClassCastExc
import sidev.lib.exception.IllegalStateExc
import sidev.lib.number.roundClosest
import sidev.lib.structure.data.value.NullableVar
import java.lang.ClassCastException
import java.lang.NullPointerException

open class NumberPickerComp<I>(ctx: Context): ViewComp<NumberPickerData, I>(ctx){
    override val viewLayoutId: Int
        get() = R.layout._sif_comp_number_picker
    override val isViewSaved: Boolean= true

    open val etNumberId: Int= _SIF_Config.ID_ET_NUMBER
    open val vPlusId: Int= _SIF_Config.ID_IV_PLUS
    open val vMinusId: Int= _SIF_Config.ID_IV_MINUS

    open var defaultLowerBorder: Int= 0
    open var defaultUpperBorder: Int= Int.MAX_VALUE

    open var enabledColorId= _ColorRes.COLOR_PRIMARY_DARK
    open var disabledColorId= _ColorRes.COLOR_DISABLED
    open var inBorderColorId= _ColorRes.COLOR_IMMUTABLE

    var onNumberChangeListener: ((adpPos: Int, oldNumber: Int, newNumber: Int, assignment: Assignment) -> Unit)?= null

    protected inline fun <reified T: EditText> View.etNumberView(): T =
        try {
            findView(etNumberId) ?: findViewByType()!!
        } catch (e: ClassCastException){
            throw ClassCastExc(
                fromClass = findView<View>(etNumberId)!!::class,
                toClass = ModEt::class,
                msg = "Harap menggunakan `ModEt` sbg View yg menampilkan text angka pada `NumberPickerComp`."
            )
        } catch (e: NullPointerException){
            throw IllegalStateExc(
                currentState = "v.findView<ModEt>(etNumberId) == null",
                expectedState = "v.findView<ModEt>(etNumberId) != null",
                detMsg = "Tidak ditemukan view dg id (${etNumberId.asResNameOrNullBy(ctx) ?: etNumberId}) yg bertipe `ModEt`,\natau tidak ada view yg bertipe `ModEt`."
            )
        }

    /** Dipanggil satu satu kali saat [initData] dipanggil untuk mengambil [NumberPickerData.number] yg awal. */
    open fun getDefaultInitNumber(dataPos: Int, inputData: I?): Int = defaultLowerBorder

    override fun initData(dataPos: Int, inputData: I?): NumberPickerData?
        = NumberPickerData(getDefaultInitNumber(dataPos, inputData), defaultLowerBorder, defaultUpperBorder)

    override fun bindComponent(
        adpPos: Int, v: View,
        valueBox: NullableVar<NumberPickerData>,
        additionalData: Any?,
        inputData: I?
    ) {
        val vPlus= v.findViewById<View>(vPlusId)!!
        val vMinus= v.findViewById<View>(vMinusId)!!
        val etNumber= v.etNumberView<ModEt>()
        //v.findViewById<ModEt>(R.id.et)!!
                // Diganti [findViewByType] agar lebih fleksibel dg view yg beda id.
        val data= valueBox.value!!

        vMinus.setOnClickListener { etNumber.setText((data.number -1).toString()) }
        vPlus.setOnClickListener { etNumber.setText((data.number +1).toString()) }

        etNumber.inputType= InputType.TYPE_CLASS_NUMBER
        etNumber.hint= data.lowerBorder.toString()
        etNumber.addOnceTextChangedListener(object : TextWatcher{
            private var assignment= Assignment.INIT
            private var isInternalEdit= false
            override fun afterTextChanged(s: Editable?) {
                if(s?.isNotBlank() == true){
                    var num= s.toString().toInt()

                    val vMinusColorId=
                        if(num == data.lowerBorder) inBorderColorId
                        else enabledColorId
                    val vPlusColorId=
                        if(num == data.upperBorder) inBorderColorId
                        else enabledColorId

                    _ViewUtil.setBgColorTintRes(vMinus, vMinusColorId)
                    _ViewUtil.setBgColorTintRes(vPlus, vPlusColorId)

                    if(!isInternalEdit){
                        val numRange= data.lowerBorder .. data.upperBorder
                        if(num !in numRange){
                            isInternalEdit= true
                            num= num.roundClosest(numRange)
                            data.number= num
                            if(Build.VERSION.SDK_INT >= 15){
                                s.clear()
                                s.append(num.toString())
                            } else {
                                etNumber.txt= num.toString()
                            }
                            isInternalEdit= false
                            //TODO: 29 Des 2020: Opsional, buat kompatibilitas untuk API di bawah 15.
                            // Masalah: Untuk API 14, s.clear() dan s.append() tidak berfungsi sehingga
                            //  angka yg diinput bisa menembus limit.
                        }
                    }
                    val oldNumber= data.number
                    data.number= num
                    onNumberChangeListener?.invoke(adpPos, oldNumber, num, assignment)
                    assignment= Assignment.ASSIGN
                } else {
                    val oldNumber= data.number
                    data.number= data.lowerBorder
                    _ViewUtil.setBgColorTintRes(vMinus, inBorderColorId)
                    _ViewUtil.setBgColorTintRes(vPlus, enabledColorId)
                    onNumberChangeListener?.invoke(adpPos, oldNumber, data.number, assignment)
                    assignment= Assignment.ASSIGN
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etNumber.setText(data.number.toString())
    }

    override fun setComponentEnabled(adpPos: Int, v: View?, enable: Boolean) {
        (v ?: getViewAt(adpPos)).notNullTo { view ->
            val colorId= if(enable) enabledColorId
                else disabledColorId

            val vPlus= view.findViewById<View>(vPlusId)!!
            val vMinus= view.findViewById<View>(vMinusId)!!
            val etNumber= view.etNumberView<EditText>()
            //.findViewById<ModEt>(R.id.iv_plus)!!

            _ViewUtil.setBgColorTintRes(vMinus, colorId)
            _ViewUtil.setBgColorTintRes(vPlus, colorId)

            vMinus.isEnabled= enable
            vPlus.isEnabled= enable
            etNumber.isEnabled= enable

            //Agar warna [ivMinus] dan [ivMinus] sesuai dg keadaan data.number.
            if(enable)
                getDataAt(adpPos).notNull { etNumber.setText(it.number.toString()) }

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
            it.lowerBorder= if(lowerBorder <= it.upperBorder) lowerBorder
                else it.upperBorder
            getViewAt(pos).notNull { v ->
                //Jika view disimpan, maka assign saja [it.number] ke view
                //  agar warna [ivMinus] dan [ivPlus] bisa menyesuaikan perubahan border.
                (v.findView(etNumberId) ?: v.findViewByType<EditText>())
                    .notNull { et ->
                        et.hint= it.lowerBorder.toString()
                        et.setText(it.number.toString())
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
            it.upperBorder= if(upperBorder >= it.lowerBorder) upperBorder
                else it.lowerBorder
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