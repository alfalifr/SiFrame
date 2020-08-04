package com.sigudang.android._template.adapter.layoutmanager

import android.content.Context
import android.util.Log
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.kuliah.agradia.template.adp.SimpleAbsRecyclerViewAdapter

class LinearLayoutManagerResp(c: Context) : LinearLayoutManager(c), LayoutManagerResp{
    override var onLayoutCompletedListener: SimpleAbsRecyclerViewAdapter.OnLayoutCompletedListener?
        = null

    @CallSuper
    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        Log.e("LinearLayoutManagerResp", "onLayoutCompletedListener == null = ${onLayoutCompletedListener == null} state= ${state.toString()}")
        onLayoutCompletedListener?.onLayoutCompletedResp(state)
    }
}