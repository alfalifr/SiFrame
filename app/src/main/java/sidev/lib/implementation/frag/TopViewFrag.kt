package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.comp_nav_arrow.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.implementation.R

class TopViewFrag : Frag(){
    override val layoutId: Int
        get() = R.layout.comp_nav_arrow

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Teks Palsu"
    }
}