package sidev.lib.implementation.frag

import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import kotlinx.android.synthetic.main.frag_view_color.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.implementation.R

class ViewColorFrag: Frag() {
    override val layoutId: Int= R.layout.frag_view_color

    override fun _initView(layoutView: View) {
        val sdkInt= Build.VERSION.SDK_INT

        layoutView.iv.imgRes= R.drawable.ic_add_img
        layoutView.iv.colorTint= Color.parseColor("#00AA00")

        loge("layoutView.iv.imgDrawable!!::class= ${layoutView.iv.img!!::class}")
        val drw= layoutView.iv.drawable
        val colFilter= drw.colorFilter
        val colorPre= colFilter?.let { _ViewUtil.getColorTintInt(it) }

        loge("drw= $drw colFilter= $colFilter colorPre= $colorPre")
        val defCol= ImageViewCompat.getImageTintList(layoutView.iv)?.defaultColor

        val color= layoutView.iv.colorTint
        val r= Color.red(color)
        val g= Color.green(color)
        val b= Color.blue(color)
        val a= Color.alpha(color)

        loge("layoutView.iv.colorTint= $color colorPre= $colorPre defCol= $defCol r= $r g= $g b= $b a= $a")

        layoutView.tv.txtColor= Color.parseColor("#AA0000")

        val txtColor= layoutView.tv.txtColor
        val r2= Color.red(txtColor)
        val g2= Color.green(txtColor)
        val b2= Color.blue(txtColor)
        val a2= Color.alpha(txtColor)

        val str2= "sdkInt= $sdkInt layoutView.tv.txtColor= $txtColor r= $r2 g= $g2 b= $b2 a= $a2"
        layoutView.tv.txt= str2
//        layoutView.iv.setColorFilter(Color.parseColor("#00AA00"))
//        _ViewUtil.setColorTintRes(layoutView.iv, R.color.biruLangit)
//        layoutView.iv.setColorFilter(Color.parseColor("#00AA00"))
//        layoutView.iv.setBackgroundColor(Color.parseColor("#AA0000"))//.setColorFilter(Color.parseColor("#00AA00"))
//        layoutView.iv.setBackgroundColor(Color.parseColor("#AA0000"))//.setColorFilter(Color.parseColor("#00AA00"))
    }
}