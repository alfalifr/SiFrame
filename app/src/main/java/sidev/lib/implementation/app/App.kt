package sidev.lib.implementation.app

import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.customizable._init._Constant
import sidev.lib.android.siframe.lifecycle.app.BaseApp
import sidev.lib.android.siframe.tool.`var`._SIF_Config
import sidev.lib.implementation.R
import sidev.lib.implementation.act.SingleFragImplAct

class App : BaseApp(){
    init{
        _SIF_Config.CLASS_SINGLE_FRAG_ACT= SingleFragImplAct::class.java
        _Config.DEBUG= false
        _ColorRes.COLOR_PRIMARY= R.color.colorPrimaryDark
        _Config.TEMPLATE_VIEW_ACT_BAR_TYPE= _Constant.TEMPLATE_VIEW_TYPE_ACT_BAR_SQUARE
    }
}