package sidev.lib.android.siframe.tool

//import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.intfc.`fun`.AssertPropNotNullFun
import sidev.lib.universal.`fun`.asNotNullTo

class RvAdpContentArranger<T> : ContentArranger<T>(), AssertPropNotNullFun{
    var rvAdp: SimpleRvAdp<T, *>?= null

    override fun getProperty(propertyName: String?): Any? = rvAdp

    //<9 Juli 2020> => Perlu dipikirkan lagi untuk metode pengambilan data dari adp.
    //  Jika pada rumpun RvAdp, pengambilan data dilakukan menggunakan fungsi [SimpleRvAdp.getDataAt].
    override fun getContent(pos: Int): T
        = assertNotNull("rvAdp") { adp: SimpleRvAdp<T, *> -> adp.getDataAt(pos, isIndexProcessed = true)!! } //adp.dataList!![pos]

    //<9 Juli 2020> => Jika fungsi [getContent] di atas menggunakan fungsi [SimpleRvAdp.getDataAt]
    //  untuk mengambil data, maka metode pengambilan jml konten menggunakan adp.dataList?.size ?: 0
    //  tidak aman karena menyebabkan jml konten menjadi 0. Hal tersebut dikarenakan [RvAdp.getItemCount]
    //  mengambil panjang dari [resultInd].
    override fun getContentCount(): Int
        = assertNotNull("rvAdp") { simpleAdp: SimpleRvAdp<T, *> ->
        simpleAdp.asNotNullTo { rvAdp: RvAdp<T, *> -> rvAdp.dataListCount }
            ?: simpleAdp.dataList?.size ?: 0
    }

    override fun isContentEmpty(): Boolean
        = assertNotNull("rvAdp") { adp: SimpleRvAdp<T, *> -> adp.isEmpty() }
}