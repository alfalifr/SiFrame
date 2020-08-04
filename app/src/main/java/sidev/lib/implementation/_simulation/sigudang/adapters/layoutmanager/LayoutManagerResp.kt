package com.sigudang.android._template.adapter.layoutmanager

import androidx.recyclerview.widget.RecyclerView
import sidev.kuliah.agradia.template.adp.SimpleAbsRecyclerViewAdapter

interface LayoutManagerResp{
    var onLayoutCompletedListener: SimpleAbsRecyclerViewAdapter.OnLayoutCompletedListener?
    fun setOnLayoutCompletedListener(l: (state: RecyclerView.State?) -> Unit){
        onLayoutCompletedListener= object : SimpleAbsRecyclerViewAdapter.OnLayoutCompletedListener {
            override fun onLayoutCompletedResp(state: RecyclerView.State?) {
                l(state)
            }
        }
    }
}