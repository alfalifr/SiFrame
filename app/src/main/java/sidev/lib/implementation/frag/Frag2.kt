package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import org.jetbrains.anko.textColor
import sidev.lib.android.siframe.intfc.lifecycle.LifecycleViewBase
import sidev.lib.android.siframe.intfc.lifecycle.sidebase.DrawerBase
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull
import sidev.lib.universal.`fun`.asNotNullTo
import sidev.lib.universal.`fun`.notNull

class Frag2 : Frag(){
    override val layoutId: Int
        get() = R.layout.frag_txt

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Frag2"
        _ViewUtil.setBgColor(layoutView, R.color.biruLangit)
    }

    override fun onActive(parentView: View, callingLifecycle: LifecycleViewBase?, pos: Int) {
        callingLifecycle.asNotNull { act: DrawerBase ->
            act.startDrawerContainer.asNotNull { v: View ->
                _ViewUtil.setBgColor(v, R.color.ijo)
                _ViewUtil.Comp.getTv?.invoke(v).notNull { tv ->
                    tv.textColor= R.color.putih
                }
            }
        }
    }
}