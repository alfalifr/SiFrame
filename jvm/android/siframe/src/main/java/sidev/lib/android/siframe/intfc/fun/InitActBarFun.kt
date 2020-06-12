package sidev.lib.android.siframe.intfc.`fun`

import android.view.View
import android.view.ViewGroup
import sidev.lib.android.siframe.customizable._init._Config

interface InitActBarFun {
//    val actBarViewContainer: ViewGroup
    val actBarId: Int
        get()= _Config.LAYOUT_COMP_ACT_BAR_DEFAULT //R.layout.component_action_bar_default
    fun _initActBar(actBarView: View)
}