package sidev.lib.android.siframe.lifecycle.fragment

import android.view.View
import android.widget.TextView
import sidev.lib.android.siframe.customizable._init._ConfigBase

open class TxtCenterFrag : SimpleAbsFrag(){
    override val layoutId: Int
        get() = _ConfigBase.LAYOUT_TXT_CENTER //R.layout._t_frag_txt_center

    open var txt: String= "Tidak ada data"

    override fun initView(layoutView: View) {
        layoutView.findViewById<TextView>(_ConfigBase.ID_TV).text= txt
    }
}