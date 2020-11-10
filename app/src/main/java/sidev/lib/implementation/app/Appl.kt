package sidev.lib.implementation.app

import sidev.lib.android.std._val._ColorRes
import sidev.lib.android.siframe.lifecycle.app.App
import sidev.lib.android.siframe._val._SIF_Config
import sidev.lib.android.siframe._val._SIF_Constant
import sidev.lib.android.siframe.tool.util._SIF_ViewUtil
import sidev.lib.android.std._val._Config
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util.`fun`.findViewByType
import sidev.lib.implementation.R
import sidev.lib.implementation.act.SingleFragImplAct

class Appl : App(){
    init{
        _SIF_Config.CLASS_SINGLE_FRAG_ACT= SingleFragImplAct::class.java
//        _Config.DEBUG= false
        _Config.LOG_ON_FILE= false
        _ColorRes.COLOR_PRIMARY= R.color.colorPrimaryDark
        _SIF_Config.TEMPLATE_VIEW_ACT_BAR_TYPE= _SIF_Constant.TEMPLATE_VIEW_TYPE_ACT_BAR_SQUARE

        _SIF_ViewUtil.Comp.getTv= { v ->
            v.findViewById(R.id.tv)
                ?: v.findViewById(R.id.tv_title)
                ?: v.findViewById(R.id.tv_desc)
                ?: v.findViewById(R.id.tv_content)
                ?: v.findViewById(R.id.tv_header)
                ?: v.findViewByType()
        }
/*
        _ViewUtil.Comp.getRv= { v ->
            v.findViewByType()
        }
 */
    }
}