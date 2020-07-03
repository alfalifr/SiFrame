package sidev.lib.implementation.frag

import android.view.View
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.comp_rb.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.notNull

class RadioFrag : Frag(){
    override val layoutId: Int
        get() = R.layout.page_rg

    override fun _initView(layoutView: View) {
        layoutView.findViewByType<RadioGroup>().notNull { rg ->
            rg.getChildAt(0).tv.text= "ini pilhan 1"
            rg.getChildAt(1).tv.text= "ini pilhan 2"
            rg.getChildAt(2).tv.text= "ini pilhan 3"
            rg.getChildAt(3).tv.text= "ini pilhan 4"
        }
    }
}