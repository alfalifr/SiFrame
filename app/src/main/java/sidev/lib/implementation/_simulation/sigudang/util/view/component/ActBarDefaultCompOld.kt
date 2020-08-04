package com.sigudang.android.utilities.view.component
/*
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sigudang.android.R

class ActBarDefaultCompOld(c: Context, view: View?): ViewComp_old(c, view) {
    override val layoutId= R.layout.component_action_bar_default

    override var view: View?= null
        set(v){
            field= v
            initViewComp()
        }
    var title= ""
        set(v){
            field= v
            tvTitle?.text= v
        }
    private var tvTitle: TextView?= null
    private var ivBack: ImageView?= null

    override fun initViewComp() {
        tvTitle= view?.findViewById(R.id.tv_title)
        ivBack= view?.findViewById(R.id.iv_back)
    }
}

 */