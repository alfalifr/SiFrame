package sidev.lib.implementation.act

import android.view.View
import sidev.lib.android.siframe.lifecycle.activity.Act
import sidev.lib.implementation.R

class Test2Act : Act() {
    override val layoutId: Int
        get() = R.layout.act_test2

    override fun _initView(layoutView: View) {}
}
