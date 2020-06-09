package sidev.lib.implementation.app

import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.customizable._init._Constant
import sidev.lib.android.siframe.lifecycle.app.BaseApp
import sidev.lib.implementation.R

class App : BaseApp(){
    init{
        _ConfigBase.DEBUG= false
        _ColorRes.COLOR_PRIMARY= R.color.colorPrimaryDark
        _ConfigBase.TEMPLATE_VIEW_ACT_BAR_TYPE= _Constant.TEMPLATE_VIEW_TYPE_ACT_BAR_SQUARE
    }
}