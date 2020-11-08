package sidev.lib.implementation.frag

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.cob_page.view.*
import org.jetbrains.anko.textColor
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.*
import sidev.lib.android.siframe.tool.util.`fun`.*
import sidev.lib.android.viewrap.wrapWithBuffer
import sidev.lib.check.asNotNull
import sidev.lib.implementation.R

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
            btn.text= "R.id.tv_id => isId? -> ${id isIdIn context!!}" //(R.dimen.drawer_horizontal_width_percent asDimenIn context!!).toString() //
            btn.textColor= R.color.ijoRumputMuda asColorIn context!!
//            btn.setBgColorRes(R.)
            btn.setOnClickListener { layoutView.findView<TextView>(R.id.et_number)!! }
        }

        layoutView.rl_top_container.setOnClickListener { toast("Halo bro") }
        layoutView.rl_top_container.isClickable= false
        layoutView.rl_top_container.isClickable= true
        layoutView.rl_top_container.bgColorTintRes= R.color.colorPrimaryDark


        val wrapper= layoutView.rl_top_container.wrapWithBuffer()
        wrapper.showBuffer()
        _ThreadUtil.delayRun(7000){
//            wrapper.showAnimation(false)
        }

        commitFrag(TopViewFrag(), fragContainerId = R.id.rl)

        var btnClick= -1
        layoutView.btn1.setOnClickListener {
            loge("layoutView.ll_btn_container.height= ${layoutView.ll_btn_container.height}")
            if(++btnClick >= 2){
                btnClick= -1
                layoutView.btn2.visibility= View.GONE
                loge("AFTER GONE layoutView.ll_btn_container.height= ${layoutView.ll_btn_container.height}")
            } else
                layoutView.btn2.visibility= View.VISIBLE
        }


//        (layoutView as EditText).txt= "aku"
    }
}
