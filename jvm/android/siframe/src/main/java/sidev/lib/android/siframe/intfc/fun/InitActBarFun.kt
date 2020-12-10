package sidev.lib.android.siframe.intfc.`fun`

import android.view.View
import sidev.lib.android.siframe.`val`._SIF_Config

interface InitActBarFun {
//    val actBarViewContainer: ViewGroup
    val actBarId: Int
        get()= _SIF_Config.LAYOUT_COMP_ACT_BAR_DEFAULT //R.layout.component_action_bar_default
    fun _initActBar(actBarView: View)
}