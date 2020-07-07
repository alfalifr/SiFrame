package sidev.lib.android.siframe.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.intfc.customview.CustomView
import sidev.lib.android.siframe.tool.util._ViewUtil

open class CustomSizeRV : RecyclerView, CustomView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val newHeightSpec = MeasureSpec.makeMeasureSpec(_ViewUtil.dpToPx(300), MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, newHeightSpec)
    }
}