package sidev.lib.implementation.frag

import android.view.View
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.implementation.adp.StrAdp

class RvFrag : RvFrag<StrAdp>(){
    override fun initRvAdp(): StrAdp = StrAdp(context!!, null)

    override fun _initView(layoutView: View) {
        val data= arrayOf(
            "Data 1",
            "Data 2",
            "Data 3",
            "Data 4",
            "Halo",
            "Bro",
            "Hoho",
            "Hihe"
        )
        rvAdp.dataList= ArrayList(data.toList())
    }
}