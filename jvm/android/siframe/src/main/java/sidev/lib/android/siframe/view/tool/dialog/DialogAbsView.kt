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
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.siframe.`val`._SIF_Config
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.property.mutableLazy


abstract class DialogAbsView<T: DialogAbsView<T>>(val c: Context){
    /**
     * Jangan langsung memodifikasi var ini
     */
    var dialog: AlertDialog by mutableLazy {
//        loge("DialogAbsView.dialog init")
        val dialog= AlertDialog.Builder(c)
            .setView(layoutContainerView)
            .setCancelable(true)
            .create()
        dialog.window
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setOnDismissListener { dialog_ -> onDismissCallback?.invoke(dialog_) }
        dialog
    }
        protected set

//    lateinit var adp: DialogListAdapter
//    lateinit var rv: RecyclerView
    protected val layoutContainerId= _SIF_Config.LAYOUT_DIALOG_CONTAINER //R.layout.dialog_container_cardview
    protected val layoutContainerView: View by lazy {
//        loge("DialogAbsView.layoutContainerView init")
        val layoutContainerView= LayoutInflater.from(c).inflate(layoutContainerId, null, false) //as ViewGroup
        layoutContainerView.findViewById<LinearLayout>(_SIF_Config.ID_VG_CONTENT_CONTAINER) //
            .addView(layoutView)
        layoutContainerView
    }
    protected abstract val layoutId: Int
    private var layoutView_: View?= null
    protected val layoutView: View
        get()= layoutView_ ?: LayoutInflater.from(c).inflate(layoutId, null, false).apply {
            layoutView_= this
            initView(this)
        }
/*
    by sidev.lib.property.lazy ({
        layoutView_!!
    }) {
//        loge("DialogAbsView.layoutView init")

        if(!isInitializing){ //Karena initView() bisa memanggil `layoutView` yg menyebabkan infinite loop.
            layoutView_= LayoutInflater.from(c).inflate(layoutId, null, false)
            isInitializing= true
            initView(layoutView)
            isInitializing= false
        }
        layoutView_!!
    }
 */

    var dialogCancelOnCLick= true
    var onDismissCallback: ((DialogInterface?) -> Unit)?= null

    val isShowing: Boolean
        get()= dialog.isShowing

//    private var formatter: DialogListAdapter.DialogListListener?= null

    private fun __initView(dialogView: View){

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
        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        return this as T
    }

    open fun showTitle(show: Boolean= true): T {
        val vis= if(show) View.VISIBLE
            else View.GONE
        layoutContainerView.findViewById<TextView>(_SIF_Config.ID_TV_TITLE).visibility= vis //.tv_title.visibility= vis
        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        return this as T
    }

    open fun setMessage(msg: String): T {
        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        return this as T
    }


    fun show(){
        dialog.show()
    }

    fun cancel(){
        dialog.cancel()
    }
}