package sidev.lib.android.siframe.adapter

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import sidev.lib.android.siframe._customizable._Config
import sidev.lib.android.siframe.model.StringId


open class DialogListAdp<T>(ctx: Context, dataList: ArrayList<T>?)
    : AnyToStringAdp<T, LinearLayoutManager>(ctx, dataList){ //RvAdp<StringId, LinearLayoutManager>(ctx, dataList){
    constructor(ctx: Context): this(ctx, null)

    //Jadi `var` agar dapat dg mudah diubah lewat `DialogListView`
//    override var searchFilterFun: (data: T, keyword: String) -> Boolean = super.searchFilterFun

    override fun getString(pos: Int, data: T): String =
        if(data !is StringId) data.toString() else data.str
    override fun getId(pos: Int, data: T): String =
        if(data !is StringId) pos.toString() else data.id

    override fun setupLayoutManager(context: Context): LinearLayoutManager {
        return LinearLayoutManager(context)
    }
}