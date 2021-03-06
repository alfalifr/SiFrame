package sidev.lib.android.siframe.view.tool.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import sidev.lib.android.siframe.`val`._SIF_Config
import sidev.lib.android.siframe.tool.util._SIF_ViewUtil

open class DialogConfirmationView(c: Context): DialogAbsView<DialogConfirmationView>(c){
    override val layoutId: Int
        get() = _SIF_Config.LAYOUT_DIALOG_CONFIRM

    enum class ButtonKind{
        RIGHT, LEFT
    }
    companion object{
        val INDIC_WARNING= 1
    }

    override fun initView(dialogView: View) {
        showTitle(false)
        setOnClickListener(ButtonKind.RIGHT){}
        setOnClickListener(ButtonKind.LEFT){}

        setBtnHollow(ButtonKind.LEFT)
    }

    override fun setMessage(msg: String): DialogConfirmationView {
        layoutView.findViewById<TextView>(_SIF_Config.ID_TV_TITLE).text= msg //.tv_title.text= msg
        return this
    }

    fun setBtnRightMsg(msg: String): DialogConfirmationView {
        layoutView.findViewById<Button>(_SIF_Config.ID_BTN_RIGHT).text= msg
        return this
    }
    fun setBtnLeftMsg(msg: String): DialogConfirmationView {
//        (layoutView.btn_left as Button).text= msg
        layoutView.findViewById<Button>(_SIF_Config.ID_BTN_LEFT).text= msg
        return this
    }

    fun setBtnHollow(kind: ButtonKind): DialogConfirmationView {
        val btn= getBtn(kind)
        _SIF_ViewUtil.Comp.setBtnHollow(btn)
        return this
    }

    fun setBtnSolid(kind: ButtonKind): DialogConfirmationView {
        val btn= getBtn(kind)
        _SIF_ViewUtil.Comp.setBtnSolid(btn)
        return this
    }

    protected fun getBtn(kind: ButtonKind): Button{
        return when(kind){
            ButtonKind.RIGHT -> findView(_SIF_Config.ID_BTN_RIGHT) //R.id.btn_right
            ButtonKind.LEFT -> findView(_SIF_Config.ID_BTN_LEFT)
        }
    }

    fun setIndication(indicationKind: Int): DialogConfirmationView {
        layoutView.findViewById<ImageView>(_SIF_Config.ID_IV_INDICATION).setImageResource( //iv_indication
            when(indicationKind){
                INDIC_WARNING -> _SIF_Config.DRAW_IC_WARNING //R.drawable.ic_warning
                else -> _SIF_Config.DRAW_IC_WARNING //R.drawable.ic_warning
            }
        )
        return showIndication()
    }
    fun showIndication(show: Boolean= true): DialogConfirmationView {
        val tv= layoutView.findViewById<TextView>(_SIF_Config.ID_TV_TITLE)
        layoutView.findViewById<ImageView>(_SIF_Config.ID_IV_INDICATION).visibility=
            if(show) {
                tv.gravity= Gravity.LEFT
                View.VISIBLE
            }
            else {
                tv.gravity= Gravity.CENTER
                View.GONE
            }
        return this
    }

    fun setOnClickListener(kind: ButtonKind, func: (v: View) -> Unit): DialogConfirmationView {
        val btn= getBtn(kind)
        btn.setOnClickListener{ v ->
            func(v)
            if(dialogCancelOnCLick)
                cancel()
        }
        return this
    }
}