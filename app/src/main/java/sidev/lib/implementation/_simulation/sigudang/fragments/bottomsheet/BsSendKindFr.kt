package com.sigudang.android.fragments.bottomsheet

import android.view.View
import com.sigudang.android.models.SendMethodModel
import sidev.lib.implementation._simulation.sigudang.adapters.SendMethodAdp

class BsSendKindFr : BsSimpleRv<SendMethodAdp, SendMethodModel>(){
//    private var dataList: ArrayList<SendKindModel>? = sendKindModel_list_full.toCollection(ArrayList())
    override fun initAdp(): SendMethodAdp {
        return SendMethodAdp(context!!, dataList)
    }

    override fun initView(v: View) {
        setTitle("Pilih Jenis Pengiriman")
        setBtnConfirmText("Simpan")
    }
}

/*
class BsSendKindFr : BottomSheetAbsFr() {
    override val bottomSheetType: BottomSheetType
        get() = BottomSheetType.WITH_HEADER
    override val bsLayoutId: Int
        get() = R.layout.content_bs_rv

    lateinit var sendKindAdp: SendKindAdp

    override fun initView(containerView: View) {
        setTitle("Pilih Jenis Pengiriman")

        sendKindAdp= SendKindAdp(context!!, sendKindModel_list_full.toCollection(ArrayList()))
        sendKindAdp.onItemSelectedFun= {containerView, pos, data ->
            containerView?.findViewById<ImageView>(R.id.iv_right)?.setImageResource(R.drawable.ic_plus)
        }
        sendKindAdp.onItemUnSelectedFun= {containerView, pos, data ->
            containerView?.findViewById<ImageView>(R.id.iv_right)?.setImageBitmap(null)
        }

        contentView?.findViewById<Button>(R.id.comp_btn_confirm)
            ?.setOnClickListener { containerView ->
                val selectedData= getSelectedSendKind()
                if(selectedData != null)
                    onSelectedItemConfirmedListener!!.onSelectedItemConfirmed(containerView, sendKindAdp.selectedItemPos_single, selectedData)
                dismiss()
            }

//        setDescription("Produkmu kesepian? sini ditemenin SiGudang!")
    }

    fun getSelectedSendKind(): SendKindModel?{
        return sendKindAdp.getSelectedData()
    }

    var onSelectedItemConfirmedListener: OnSelectedItemConfirmedListener?= null
    interface OnSelectedItemConfirmedListener{
        fun onSelectedItemConfirmed(containerView: View?, pos: Int, data: SendKindModel)
    }
}
 */