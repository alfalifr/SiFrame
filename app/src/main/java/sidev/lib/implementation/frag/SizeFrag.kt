package sidev.lib.implementation.frag

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import kotlinx.android.synthetic.main.page_size.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util.`fun`.addOnGlobalLayoutListener
import sidev.lib.implementation.R

class SizeFrag: Frag() {
    override val layoutId: Int = R.layout.page_size

    override fun _initView(layoutView: View) {
        layoutView.apply {
            val pixel= tv_src.textSize
            val dpUtil= _ViewUtil.spToPx(20f, context!!)
            val dpRes= 20 * context!!.resources.displayMetrics.scaledDensity

            tv_dest.setTextSize(TypedValue.COMPLEX_UNIT_PX, dpUtil)

            tv_sp_pixel.text= pixel.toString()
            tv_sp_util.text= dpUtil.toString()
            tv_sp_res.text= dpRes.toString()
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                tv_auto, 20, 100, 2, TypedValue.COMPLEX_UNIT_SP
            )
            tv_auto.addOnGlobalLayoutListener {
                val autoSize= (it as TextView).textSize
                val autoSizeSp= _ViewUtil.pxToSp(autoSize, context!!)
                tv_auto_size.text= "Auto size= $autoSizeSp sp; src= ${_ViewUtil.pxToSp(pixel, context!!)} sp;"
            }
        }
    }
}