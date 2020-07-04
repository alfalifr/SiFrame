package sidev.lib.implementation.app

import sidev.lib.android.siframe.customizable._init._ColorRes
import sidev.lib.android.siframe.customizable._init._Config
import sidev.lib.android.siframe.customizable._init._Constant
import sidev.lib.android.siframe.lifecycle.app.App
import sidev.lib.android.siframe.tool.`var`._SIF_Config
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.implementation.R
import sidev.lib.implementation.act.SingleFragImplAct

class Appl : App(){
    init{
        _SIF_Config.CLASS_SINGLE_FRAG_ACT= SingleFragImplAct::class.java
//        _Config.DEBUG= false
        _Config.LOG_ON_FILE= false
        _ColorRes.COLOR_PRIMARY= R.color.colorPrimaryDark
        _Config.TEMPLATE_VIEW_ACT_BAR_TYPE= _Constant.TEMPLATE_VIEW_TYPE_ACT_BAR_SQUARE

        _ViewUtil.Comp.getTv= { v ->
            v.findViewById(R.id.tv)
                ?: v.findViewById(R.id.tv_title)
                ?: v.findViewById(R.id.tv_desc)
                ?: v.findViewById(R.id.tv_content)
        }
    }
}