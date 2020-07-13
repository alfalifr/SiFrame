package sidev.lib.implementation.frag

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.cob_page.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.android.siframe.tool.util.isIdIn
import sidev.lib.android.siframe.view.ModEt
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

        this.asNotNull { c: Context ->

        }

        layoutView.btn.asNotNull { btn: Button ->
            val id= R.id.tv_id
            btn.text= "R.id.tv_id => isId? -> ${id isIdIn context!!}"
            btn.setOnClickListener { layoutView.findView<TextView>(R.id.et_number)!! }
        }

//        (layoutView as EditText).txt= "aku"
    }
}