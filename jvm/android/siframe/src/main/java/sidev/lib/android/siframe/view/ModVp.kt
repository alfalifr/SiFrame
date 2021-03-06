package sidev.lib.android.siframe.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import sidev.lib.android.siframe.intfc.customview.ModView

open class ModVp : ViewPager, ModView {
    override var isTouchable: Boolean= true
    override var isTouchInterceptable: Boolean= true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // Never allow swiping to switch between pages
        return if(isTouchInterceptable) super.onInterceptTouchEvent(event)
        else isTouchInterceptable
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Never allow swiping to switch between pages
        return if(isTouchable) super.onTouchEvent(event)
        else isTouchable
    }
}