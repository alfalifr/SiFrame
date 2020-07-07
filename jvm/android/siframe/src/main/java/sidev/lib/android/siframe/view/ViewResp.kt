package sidev.lib.android.siframe.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import sidev.lib.android.siframe.intfc.customview.ViewRespIntfc

//import sidev.lib.android.siframe.customizable.view.intfc.ViewRespIntfc

open class ViewResp: View, ViewRespIntfc {
    constructor(c: Context, attrs: AttributeSet): super(c, attrs)
    constructor(c: Context) : super(c)

    override val onClickListenerList: List<OnClickListener>
        = ArrayList()

    init{
        setOnClickListener { v ->

        }
    }
}