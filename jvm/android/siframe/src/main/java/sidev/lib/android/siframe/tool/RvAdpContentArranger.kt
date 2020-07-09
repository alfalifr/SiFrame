package sidev.lib.android.siframe.tool

//import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.intfc.`fun`.AssertPropNotNullFun

class RvAdpContentArranger<T> : ContentArranger<T>(), AssertPropNotNullFun{
    var rvAdp: SimpleRvAdp<T, *>?= null

    override fun getProperty(propertyName: String?): Any? = rvAdp

    override fun getContent(pos: Int): T
        = assertNotNull("rvAdp") { adp: SimpleRvAdp<T, *> -> adp.dataList!![pos] }

    override fun getContentCount(): Int
        = assertNotNull("rvAdp") { adp: SimpleRvAdp<T, *> -> adp.dataList?.size ?: 0 }

    override fun isContentEmpty(): Boolean
        = assertNotNull("rvAdp") { adp: SimpleRvAdp<T, *> -> adp.isEmpty() }
}