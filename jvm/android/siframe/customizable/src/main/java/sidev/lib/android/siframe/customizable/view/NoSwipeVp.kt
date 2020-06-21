package sidev.lib.android.siframe.customizable.view

import android.content.Context
import android.util.AttributeSet

class NoSwipeVp : ModVp {
    override var isTouchable: Boolean
        get() = false
        set(value) {}
    override var isTouchInterceptable: Boolean
        get() = false
        set(value) {}

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}