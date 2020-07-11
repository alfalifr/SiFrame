package sidev.lib.implementation.frag

import android.view.View
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.cob_page.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.asNotNull

class CobFrag : Frag(){
    override val layoutId: Int
        get() = R.layout.cob_page

    override fun _initView(layoutView: View) {
        layoutView.btn.addOnGlobalLayoutListener {
            loge("COB yStartInWindow= ${layoutView.btn.yStartInWindow} yEndInWindow= ${layoutView.btn.yEndInWindow}")
        }
        layoutView.btn_fade_in.asNotNull { btn: Button ->
            btn.text= "Fade In"
            btn.setOnClickListener { layoutView.btn.animator.fadeIn(500) }
        }
        layoutView.btn_fade_out.asNotNull { btn: Button ->
            btn.text= "Fade Out"
            btn.setOnClickListener { layoutView.btn.animator.fadeTo(0.3f) }
        }

//        (layoutView as EditText).txt= "aku"
    }
}