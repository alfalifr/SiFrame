package sidev.lib.android.siframe.lifecycle.fragment

import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import sidev.lib.android.siframe._val._SIF_Config

open class TxtCenterFrag : Frag(){
    final override val layoutId: Int
        get() = _SIF_Config.LAYOUT_TXT_CENTER //R.layout._t_frag_txt_center

    open var txt: String= "Tidak ada data"

    @CallSuper
    override fun _initView(layoutView: View) {
        layoutView.findViewById<TextView>(_SIF_Config.ID_TV).text= txt
    }
}