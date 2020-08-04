package com.sigudang.android.fragments.bottomsheet
/*
import android.view.View
import com.sigudang.android.R
import com.sigudang.android.activities._lessee.CrossDockingActivity
import com.sigudang.android.activities._lessee.InboundActivity
import com.sigudang.android.activities._lessee.OutboundActivity
import kotlinx.android.synthetic.main.content_bs_start_service.*
import org.jetbrains.anko.support.v4.startActivity


class BsStartServiceFr : BottomSheetAbsFr<Nothing>(), View.OnClickListener {
    override val bottomSheetType: BottomSheetType
        get() = BottomSheetType.WITH_HEADER
    override val bsLayoutId: Int
        get() = R.layout.content_bs_start_service

    var serviceType = 0

    var textTitle = ""
    var textDesc = ""

    // tipe masing - masing
    companion object {
        val TYPE_INBOUND = 1
        val TYPE_OUTBOUND = 2
        val TYPE_CROSS_DOCKING = 3
    }

    override fun initView(v: View) {
        showBtnConfirm(false)
        setTitle(textTitle)
        setDescription(textDesc)
        btn_start_service_next.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            btn_start_service_next -> {
                when(serviceType) {
                    TYPE_INBOUND -> startActivity<InboundActivity>()
                    TYPE_OUTBOUND -> startActivity<OutboundActivity>()
                    TYPE_CROSS_DOCKING -> startActivity<CrossDockingActivity>()
                }
            }
        }
    }

    override fun createData(contentView: View): Nothing? {
        return null
    }
}
 */