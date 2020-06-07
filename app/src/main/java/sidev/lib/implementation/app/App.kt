package sidev.lib.implementation.app

import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.customizable._init._ConfigBase
import sidev.lib.android.siframe.lifecycle.app.BaseApp
import sidev.lib.implementation.R

class App : BaseApp(){
    init{
        _ConfigBase.DEBUG= false
        _ColorRes.COLOR_PRIMARY= R.color.colorPrimaryDark
    }
}