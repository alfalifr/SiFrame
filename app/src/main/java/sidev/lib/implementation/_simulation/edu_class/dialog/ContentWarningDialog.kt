package sidev.lib.implementation._simulation.edu_class.dialog

import android.content.Context
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main._simul_edu_dialog_content_frag_quiz.view.*
import sidev.lib.android.siframe.tool.util._ViewUtil.Comp.setBtnHollow
import sidev.lib.android.siframe.tool.util._ViewUtil.Comp.setBtnSolid
import sidev.lib.android.siframe.view.tool.dialog.DialogAbsView
import sidev.lib.check.notNull
import sidev.lib.implementation.R

open class ContentWarningDialog(c: Context) : DialogAbsView<ContentWarningDialog>(c){
    override val layoutId: Int
        get() = R.layout._simul_edu_dialog_content_frag_quiz

    var onButtonClickListener: ((btn: Button, isCancelled: Boolean) -> Unit)?= null

    init{
        layoutView.btn_right.setOnClickListener { btn ->
            onButtonClickListener.notNull { l ->
                l(btn as Button, false)
            }
        }
        layoutView.btn_left.setOnClickListener { btn ->
            onButtonClickListener.notNull { l ->
                l(btn as Button, true)
            }
        }

        (layoutView.btn_left as Button).text= "Batal"
        (layoutView.btn_right as Button).text= "Kirim"

        setBtnHollow(layoutView.btn_left as Button)
        setBtnSolid(layoutView.btn_right as Button)

        showTitle(false)
    }

    fun setMainMsg(msg: String){
        layoutView.tv.text= msg
    }

    fun setRightBtnTxt(txt: String){
        (layoutView.btn_right as Button).text= txt
    }

    fun showPb(show: Boolean= true){
        layoutView.pb.visibility= if(show) View.VISIBLE
        else View.GONE
        layoutView.rl_btn_container.visibility= if(!show) View.VISIBLE
        else View.GONE
    }
}