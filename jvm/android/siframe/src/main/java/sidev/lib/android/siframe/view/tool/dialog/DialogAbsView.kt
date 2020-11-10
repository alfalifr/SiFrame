package sidev.lib.android.siframe.view.tool.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import sidev.lib.android.siframe._val._SIF_Config


abstract class DialogAbsView<T: DialogAbsView<T>>(val c: Context){
    /**
     * Jangan langsung memodifikasi var ini
     */
    var dialog: AlertDialog
        protected set
//    lateinit var adp: DialogListAdapter
//    lateinit var rv: RecyclerView
    protected val layoutContainerId= _SIF_Config.LAYOUT_DIALOG_CONTAINER //R.layout.dialog_container_cardview
    protected val layoutContainerView: View
    protected abstract val layoutId: Int
    protected val layoutView: View

    var dialogCancelOnCLick= true
    var onDismissCallback: ((DialogInterface?) -> Unit)?= null

    val isShowing: Boolean
        get()= dialog.isShowing

//    private var formatter: DialogListAdapter.DialogListListener?= null

    init{
        layoutView= LayoutInflater.from(c).inflate(layoutId, null, false)
        layoutContainerView= LayoutInflater.from(c).inflate(layoutContainerId, null, false) //as ViewGroup
        layoutContainerView.findViewById<LinearLayout>(_SIF_Config.ID_VG_CONTENT_CONTAINER) //
            .addView(layoutView)
        dialog= AlertDialog.Builder(c)
            .setView(layoutContainerView)
            .setCancelable(true)
            .create()
        dialog.window
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setOnDismissListener { dialog -> onDismissCallback?.invoke(dialog) }
        initView(layoutView)
    }

    protected open fun initView(dialogView: View){}

    fun <V: View> findViewInContainer(id: Int): V{
        return layoutContainerView.findViewById(id)
    }
    fun <V: View> findView(id: Int): V{
        return layoutView.findViewById(id)
    }

    open fun setTitle(title: String): T {
        layoutContainerView.findViewById<TextView>(_SIF_Config.ID_TV_TITLE).text= title //tv_title.text= title
        return this as T
    }

    open fun showTitle(show: Boolean= true): T {
        val vis= if(show) View.VISIBLE
            else View.GONE
        layoutContainerView.findViewById<TextView>(_SIF_Config.ID_TV_TITLE).visibility= vis //.tv_title.visibility= vis
        return this as T
    }

    open fun setMessage(msg: String): T {
        return this as T
    }


    fun show(){
        dialog.show()
    }

    fun cancel(){
        dialog.cancel()
    }
}