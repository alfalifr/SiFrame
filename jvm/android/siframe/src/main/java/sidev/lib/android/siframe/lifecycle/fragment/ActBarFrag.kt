package sidev.lib.android.siframe.lifecycle.fragment

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFragBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFromFragBase
import sidev.lib.check.asNotNull
import sidev.lib.exception.IllegalStateExc

abstract class ActBarFrag : Frag(), ActBarFragBase {
    final override var actBarView: View?= null
    final override var isActBarInit: Boolean = false
        private set
    override var isActBarViewFromFragment: Boolean= false
        set(v){
            field= v
//            loge("isActBarViewFromFragment actSimple is ActBarFromFragBase = ${actSimple is ActBarFromFragBase}")
            actSimple.asNotNull { act: ActBarFromFragBase ->
                act.isActBarViewFromFragment= v
            }
        }
    final override val _prop_act: AppCompatActivity?
        get() = actSimple

    override fun ___initSideBase() {
        super<Frag>.___initSideBase()
        super<ActBarFragBase>.___initSideBase()
    }

    final override fun initActBar(): View? {
        if(isActBarInit)
            throw IllegalStateExc(
                stateOwner = this::class,
                currentState = "isActBarInit == true",
                expectedState = "isActBarInit == false"
            )
        val v= super.initActBar()
        isActBarInit = true
        return v
    }
}