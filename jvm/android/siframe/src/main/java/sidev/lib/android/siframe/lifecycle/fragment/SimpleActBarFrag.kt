package sidev.lib.android.siframe.lifecycle.fragment

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFragBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFromFragBase
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.universal.`fun`.asNotNull

abstract class SimpleActBarFrag : SimpleAbsFrag(), ActBarFragBase{
    override var actBarView: View?= null
    override var isActBarViewFromFragment: Boolean= false
        set(v){
            field= v
            loge("isActBarViewFromFragment actSimple is ActBarFromFragBase = ${actSimple is ActBarFromFragBase}")
            actSimple.asNotNull { act: ActBarFromFragBase ->
                act.isActBarViewFromFragment= v
            }
        }
    override val _sideBase_act: AppCompatActivity
        get() = actSimple!!
}