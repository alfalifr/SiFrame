package com.sigudang.android.utilities.view.component
/*
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.annotation.CallSuper
import androidx.viewpager.widget.ViewPager
import com.sigudang.android.R
import com.sigudang.android._template.intfc.`fun`.InternalEditFun
import sidev.kuliah.agradia.template.act.SimpleAbsAct

abstract class ViewComp_old(val ctx: Context, view: View?)
    : InternalEditFun{
    override var isInternalEdit: Boolean= false
    abstract val layoutId: Int
    val simpleAbsAct: SimpleAbsAct?
        get(){
            return when(ctx){
                is SimpleAbsAct -> ctx
                else -> null
            }
        }
    open var view: View?= null
        set(v){
            field= v
            initViewComp()
        }
    init{
        this.view= view
        initViewCompOnce()
    }
    protected abstract fun initViewComp()
    @CallSuper
    protected open fun initViewCompOnce(){}

    /**
     * Untuk menginflate @see view dengan yang baru.
     * Perlu diingat jika method ini dapat menggantikan @see view yang sudah ada
     */
    fun inflateView(){
        view= LayoutInflater.from(ctx).inflate(layoutId, null, false)
    }
}

 */