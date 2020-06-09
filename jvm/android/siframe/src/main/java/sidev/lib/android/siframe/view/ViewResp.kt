package com.sigudang.android._template.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import sidev.lib.android.siframe.customizable.view.intfc.ViewRespIntfc

class ViewResp: View, ViewRespIntfc {
    constructor(c: Context, attrs: AttributeSet): super(c, attrs)
    constructor(c: Context) : super(c)

    override val onClickListenerList: List<OnClickListener>
        = ArrayList()

    init{
        setOnClickListener { v ->

        }
    }
}