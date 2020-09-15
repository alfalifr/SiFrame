package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import org.jetbrains.anko.textColor
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.check.asNotNull
import sidev.lib.check.notNull
import sidev.lib.implementation.R

class Frag2 : Frag(){
    override val layoutId: Int
        get() = R.layout.frag_txt

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag2"
        _ViewUtil.setBgColorRes(layoutView, R.color.biruLangit)
    }

    override fun onActive(parentView: View?, callingLifecycle: LifecycleViewBase?, pos: Int) {
        loge("Frag2 onActive pos= $pos")
        callingLifecycle.asNotNull { act: DrawerBase ->
            act.startDrawerContainer.asNotNull { v: View ->
                _ViewUtil.setBgColorRes(v, R.color.ijo)
                _ViewUtil.Comp.getTv?.invoke(v).notNull { tv ->
                    tv.textColor= R.color.putih
                }
            }
        }
    }
}