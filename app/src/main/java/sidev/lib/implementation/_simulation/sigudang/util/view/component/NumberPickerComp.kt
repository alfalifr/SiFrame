package com.sigudang.android.utilities.view.component
///*
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import sidev.lib.android.siframe.tool.util.`fun`.findView
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.asResEntryBy
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.notNull
import sidev.lib.android.siframe.view.comp.NumberPickerComp as NB

open class NumberPickerComp<I>(c: Context): NB<I>(c){
    override val isViewSaved: Boolean
        get() = true

    /**
     * @param enable false maka ivMinus dan ivPlus akan hilang.
     */
    override fun setComponentEnabled(position: Int, v: View?, enable: Boolean){
        v?.findView<ImageView>(R.id.iv_minus).notNull {
            it.visibility= if(enable) View.VISIBLE
            else View.INVISIBLE
        }
        v?.findView<ImageView>(R.id.iv_plus).notNull {
            it.visibility= if(enable) View.VISIBLE
            else View.INVISIBLE
        }
        v?.findView<EditText>().notNull { it.isEnabled= enable }
        v?.findView<TextView>(R.id.tv_unit).notNull { it.visibility= if(enable) View.INVISIBLE else View.VISIBLE }
    }
}
// */