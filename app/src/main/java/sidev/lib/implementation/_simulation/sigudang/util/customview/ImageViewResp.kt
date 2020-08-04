package sidev.lib.implementation._simulation.sigudang.util.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

@SuppressLint("AppCompatCustomView")
class ImageViewResp: ImageView {
    constructor(ctx: Context): super(ctx)
    constructor(ctx: Context, attrs: AttributeSet?): super(ctx,  attrs)

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        onDrawableChangeListener?.onDrawableChange(this, drawable)
        onDrawableChangeListener?.onDrawableOverallChange(this)
    }

    override fun setBackground(background: Drawable?) {
        super.setBackground(background)
        onDrawableChangeListener?.onBgDrawableChange(this, drawable)
        onDrawableChangeListener?.onDrawableOverallChange(this)
    }


    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        onDrawableChangeListener?.onDrawableOverallChange(this)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        onDrawableChangeListener?.onDrawableOverallChange(this)
    }

    override fun setBackgroundResource(resid: Int) {
        super.setBackgroundResource(resid)
        onDrawableChangeListener?.onDrawableOverallChange(this)
    }

    var onDrawableChangeListener: OnDrawableChangeListener?= null
    interface OnDrawableChangeListener{
        fun onDrawableChange(v: View, drawable: Drawable?)
        fun onBgDrawableChange(v: View, drawable: Drawable?)
        fun onDrawableOverallChange(v: View)
    }
}