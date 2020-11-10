package com.sigudang.android.fragments.bottomsheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main._simul_sigud_bottom_sheet_header_yes.view.*
import org.jetbrains.anko.layoutInflater
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.implementation.R


abstract class BottomSheetAbsFr<D> : BottomSheetDialogFragment() {

    lateinit var containerView: View

    lateinit var contentView: View //? = null
    var ctx: Context? = null

    // pilihan apakah menggunakan header atau tidak
    enum class BottomSheetType {
        WITH_HEADER, NO_HEADER
    }

    private var title= ""
    private var description= ""
    private var btnConfirmText= ""

    abstract val bottomSheetType: BottomSheetType
    abstract val bsLayoutId: Int
    val bsRootLayoutId= R.layout._simul_sigud_bottom_sheet_header_yes

    abstract fun initView(v: View)
    open fun initView_int(v: View){}

    abstract fun createData(contentView: View): D?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        containerView = inflater.inflate(R.layout._simul_sigud_bottom_sheet_header_yes, container, false)
        containerView.ll_header_container.visibility= when(bottomSheetType){
            BottomSheetType.WITH_HEADER -> View.VISIBLE
            else -> View.GONE
        }
        containerView.comp_btn_confirm.setOnClickListener { v ->
            onBsBtnlickListener?.invoke(createData(contentView))
            dismiss()
        }
        setBtnConfirmText(btnConfirmText)
        loge("BottomSheetAbsFr.onCreateView()")
/*
         when(bottomSheetType) {
            BottomSheetType.WITH_HEADER -> inflater.inflate(R.layout.bottom_sheet_header_yes, container, false)
            else -> inflater.inflate(R.layout.bottom_sheet_header_no, container, false)
        }
 */
        return containerView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // cek apakah dia merupakan childFragment atau tidak
        ctx = if(parentFragment != null) parentFragment?.context else context

        loge("BottomSheetAbsFr.onViewCreated()")

        setContainerView(bsLayoutId)
        setTitle(title)
        setDescription(description)

        initView_int(view)
        initView(view)

        // ini untuk memastikan ketika bottom sheet ditampilkan akan expand semua
        isCancelable = true

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog

            val bottomSheet = dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            val mBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, @BottomSheetBehavior.State newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                        dismiss()
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            }

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0 // merupakan tinggi ketika STATE_COLLAPSED
            behavior.setBottomSheetCallback(mBottomSheetCallback)
        }
    }

    fun setTitle(title: String){
        this.title= title
        if(::containerView.isInitialized){
            val tv= containerView.findViewById<TextView>(R.id.tv_bs_title)
            tv.visibility=
                if(title.isEmpty()) View.GONE
                else View.VISIBLE
            tv.text = title
        }
    }

    fun setDescription(desc: String) {
        this.description= desc
        if(::containerView.isInitialized){
            val tv= containerView.findViewById<TextView>(R.id.tv_bs_desc)
            tv.visibility=
                if(desc.isEmpty()) View.GONE
                else View.VISIBLE
            tv.text = desc
        }
    }

    fun showBtnConfirm(isShown: Boolean= true){
        containerView.comp_btn_confirm.visibility=
            if(isShown) View.VISIBLE
            else View.GONE
    }

    fun setBtnConfirmText(txt: String){
        btnConfirmText= txt
        if(::containerView.isInitialized)
            (containerView.comp_btn_confirm as Button).text= txt
    }

    /**
     * Wajib untuk diimplementasikan di awal
     */
    fun setContainerView(layoutId: Int){
        val container = containerView.findViewById<RelativeLayout>(R.id.bs_container)
        contentView = ctx!!.layoutInflater.inflate(layoutId, null, false)

        container.addView(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)

        if(ctx == null){
            val messsage = "lagi null ni context di bottom sheet nya"
            Log.d("bs", messsage)
            Log.i("bs", messsage)
            Log.e("bs", messsage)
            println(messsage)
        } else {
            val message = "lagi context != null ni context di bottom sheet nya"
            Log.d("bs", message)
            Log.i("bs", message)
            Log.e("bs", message)
            println(message)
        }
    }

    /**
     * Saat btn confirm dipencet
     */
    protected open var onBsBtnlickListener: ((data: D?) -> Unit)?= null
/*
    interface OnBsButtonClickListener{
        fun onBsBtnlickListener()
    }
 */
}