package sidev.lib.android.siframe.lifecycle.fragment

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFragBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.ActBarFromFragBase
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.check.notNullTo
import sidev.lib.exception.IllegalAccessExc
import sidev.lib.exception.IllegalStateExc

abstract class ActBarFrag : Frag(), ActBarFragBase {
    final override var actBarView: View?= null
    final override var isActBarInit: Boolean = false
        private set
    final override var isMultipleActBar: Boolean = false

    override var isActBarViewFromFragment: Boolean= false
        set(v){
            field= v
//            loge("isActBarViewFromFragment actSimple is ActBarFromFragBase = ${actSimple is ActBarFromFragBase}")
            actSimple.asNotNull { act: ActBarFromFragBase ->
                act.isActBarViewFromFragment= v
            }
        }
    final override val _prop_ctx: AppCompatActivity
        get() = activity.notNullTo {
            loge("ActBarFrag _prop_ctx it= $it")
            if(it is AppCompatActivity) it else null
        } ?: throw IllegalAccessExc(msg = "Fragment ($this) blum ter-attach di AppCompatActivity")

    override fun ___initSideBase() {
        super<Frag>.___initSideBase()
        super<ActBarFragBase>.___initSideBase()
    }

    final override fun initActBar(): View? {
///*
        if(isActBarInit && !isMultipleActBar) throw IllegalStateExc(
            stateOwner = this::class,
            currentState = "isActBarInit == true",
            expectedState = "isActBarInit == false"
        )
// */
        val v= super.initActBar()
        //isActBarInit = true
        return v
    }
}