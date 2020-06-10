package sidev.lib.implementation.frag

import android.view.View
import sidev.lib.android.siframe.lifecycle.fragment.SimpleAbsFrag
import sidev.lib.android.siframe.lifecycle.fragment.VpFrag

class VpImplFrag : VpFrag<SimpleAbsFrag>(){
    override var vpFragList: Array<SimpleAbsFrag> =
        arrayOf(
            Frag2(),
            Frag4(),
            Frag1()
        )
    override var vpFragListStartMark: Array<Int>
        = arrayOf(0)

    override fun _initView(layoutView: View) {}
}