package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.comp_nav_arrow_tall.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.implementation.R

class BottomViewFrag : Frag(){
    override val layoutId: Int
        get() = R.layout.comp_nav_arrow_tall

    override fun _initView(layoutView: View) {
        layoutView.tv.text= "Teks Palsu"
    }
}