package sidev.lib.implementation.frag

import android.view.View
import sidev.lib.android.siframe.lifecycle.fragment.RvFrag
import sidev.lib.implementation._cob.pageList
import sidev.lib.implementation.adp.PageAdp
import sidev.lib.universal.`fun`.toArrayList

class RvDbFrag : RvFrag<PageAdp>(){
    override fun initRvAdp(): PageAdp {
        return PageAdp(context!!, null)
    }

    override fun _initView(layoutView: View) {
        rvAdp.dataList= pageList.toArrayList()
    }
}