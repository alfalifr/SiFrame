package sidev.lib.implementation.frag

import android.content.Intent
import android.view.View
import kotlinx.android.synthetic.main.page_btn.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util._StorageUtil
import sidev.lib.android.std.tool.util.`fun`.bgColorTintRes
import sidev.lib.android.std.tool.util.`fun`.txtColorRes
import sidev.lib.implementation.R
import sidev.lib.implementation.service.OverlayService

class OverlayFrag: Frag() {
    private val overlayKey = "_overlay_view_"

    override val layoutId: Int = R.layout.page_btn
    private var isOverlayOn = false
        set(v){
            field= v
            _StorageUtil.SharedPref[requireContext(), overlayKey] = if(v) "1" else "0"
            setOverlayOn_(v)
        }

    override fun _initView(layoutView: View) {
        initOverlayStatus()
        makeBtnOn(isOverlayOn)

        layoutView.apply {
            btn.setOnClickListener {
                makeBtnOn(!isOverlayOn)
            }
        }
    }

    private fun initOverlayStatus(){
        val c = requireContext()
        val str = _StorageUtil.SharedPref[c, overlayKey] ?: "0"
        isOverlayOn = str == "1"
    }

    private fun makeBtnOn(on: Boolean){
        layoutView.btn.apply {
            if(on){
                bgColorTintRes = R.color.colorPrimary
                txtColorRes = R.color.putih
                text = "Matikan"
            } else {
                bgColorTintRes = R.color.abuSangatTua
                txtColorRes = R.color.hitam
                text = "Hidupkan"
            }
        }
        isOverlayOn = on
    }

    private fun setOverlayOn_(on: Boolean){
        val c = requireContext()
        val i = Intent(c, OverlayService::class.java)
        if(!c.stopService(i) && on)
            c.startService(i)
    }
}