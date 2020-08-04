package com.sigudang.android.fragments.bottomsheet

import android.view.View
import com.sigudang.android.models.abs.SimplestModel
import sidev.lib.implementation._simulation.sigudang.adapters.SimplestModelRvAdp

class BsSimplestModelSelect<D: SimplestModel> : BsSimpleRv<SimplestModelRvAdp<D>, D>(){

//    var dataList= ArrayList<D>()

    override fun initAdp(): SimplestModelRvAdp<D> {
        return SimplestModelRvAdp(context!!, null)
    }

    override fun initView(v: View) {}
}