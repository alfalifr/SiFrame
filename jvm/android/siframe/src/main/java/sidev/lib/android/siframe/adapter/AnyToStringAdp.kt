package sidev.lib.android.siframe.adapter

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sidev.lib.android.siframe.`val`._SIF_Config
import sidev.lib.collection.findIndexed
import sidev.lib.text.charCodeSum

/**
 * Adapter yg menampilkan isi dari [dataList] menjadi string
 * dan mengambil id berupa string dari tiap item [dataList].
 */
abstract class AnyToStringAdp<T, LM: RecyclerView.LayoutManager>(ctx: Context, dataList: ArrayList<T>?)
    : RvAdp<T, LM>(ctx, dataList){
    constructor(ctx: Context): this(ctx, null)

    override val itemLayoutId: Int
        get() = _SIF_Config.LAYOUT_COMP_ITEM_TXT_DESC

    //TODO: buat mekanisme cache untuk string dan id.
    /** Mengambil string yg akan ditampilkan oleh adapter ini dari [data]. */
    abstract fun getString(pos: Int, data: T): String
    /** Mengambil id yg akan ditampilkan oleh adapter ini dari [data]. */
    abstract fun getId(pos: Int, data: T): String

    final override fun getItemId(pos: Int): Long{
        if(dataList == null) throw IndexOutOfBoundsException("dataList null")
        return getId(pos, dataList!![pos]).charCodeSum
    }

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: T) {
        vh.itemView.findViewById<TextView>(_SIF_Config.ID_TV).text= getString(pos, data)
    }

    /** Mencari data dari [dataList] yg menghasilkan id dari [getId] sama dg param [id]. */
    fun searchById(id: String): T? {
        if(dataList == null) return null
        return dataList!!.findIndexed { getId(it.index, it.value) == id }?.value
    }
    /** Mencari data dari [dataList] yg menghasilkan string dari [getString] sama dg param [str]. */
    fun searchByString(str: String): T? {
        if(dataList == null) return null
        return dataList!!.findIndexed { getString(it.index, it.value) == str }?.value
    }
}