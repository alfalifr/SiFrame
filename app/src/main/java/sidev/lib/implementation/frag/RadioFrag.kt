package sidev.lib.implementation.frag

import android.view.View
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.page_rg.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.tool.util.`fun`.withIndex
import sidev.lib.implementation.R
import sidev.lib.universal.`fun`.notNull

class RadioFrag : Frag(){
    override val layoutId: Int
        get() = R.layout.page_rg

    override fun _initView(layoutView: View) {
        loge("radioFrag initView")

        val rgId= layoutView.findViewById<View>(R.id.rg)
        val rgIsRg= rgId is RadioGroup
        val rgIdBool= rgId.id == R.id.rg
        loge("rgId= $rgId rgIsRg= $rgIsRg rgIdBool= $rgIdBool")

        layoutView.findViewByType<RadioGroup>().notNull { rg ->
            loge("rg.id == R.id.rg => ${rg.id == R.id.rg}")
            for((i, child) in rg.withIndex()){
                val clsName= child::class.java.simpleName
                loge("rg clsName= $clsName i= $i")
                loge("child= ${child.findViewById<View>(R.id.tv)}")
            }

//            loge("child= ${rg.getChildAt(0).findViewById<View>(R.id.tv)}")
///*
            rg.getChildAt(0).tv.text= "ini pilhan 1"
            rg.getChildAt(1).tv.text= "ini pilhan 2"
            rg.getChildAt(2).tv.text= "ini pilhan 3"
            rg.getChildAt(3).tv.text= "ini pilhan 4"
// */
        }
    }
}