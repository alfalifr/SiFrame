package sidev.lib.android.siframe.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.customizable.view.intfc.CustomView
import sidev.lib.android.siframe.tool.util._ViewUtil

class CustomSizeRV : RecyclerView, CustomView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val newHeightSpec = MeasureSpec.makeMeasureSpec(_ViewUtil.dpToPx(300), MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, newHeightSpec)
    }
}