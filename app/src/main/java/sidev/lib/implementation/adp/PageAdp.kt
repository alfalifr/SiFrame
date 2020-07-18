package sidev.lib.implementation.adp

import android.content.Context
import kotlinx.android.synthetic.main.comp_page_row.view.*
import org.jetbrains.anko.imageResource
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.adapter.layoutmanager.LinearLm
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.android.siframe.view.comp.NumberPickerComp
import sidev.lib.android.siframe.view.comp.data.NumberPickerData
import sidev.lib.implementation.R
import sidev.lib.implementation.model.Page
import sidev.lib.implementation.util.PageSqlite
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull

class PageAdp(c: Context, data: ArrayList<Page>?) : RvAdp<Page, LinearLm>(c, data){
    override val itemLayoutId: Int
        get() = R.layout.comp_page_row

    val sqlite= PageSqlite(c)
///*
    val numberPicker= object: NumberPickerComp<Page>(c){

    override fun initData(position: Int, inputData: Page?): NumberPickerData?{
            val no= inputData!!.no %6
            val lowerBorder= if(position == 2) 0 else (inputData.no +1) %3
            val upperBorder= if(position == 2) 0 else inputData.no +2
            return NumberPickerData(no, lowerBorder, upperBorder)
        }
    }

    init{
        numberPicker.setupWithRvAdapter(this)
        numberPicker.onNumberChangeListener= { pos, old, new ->
            loge("pos= $pos oldNumber= $old new= $new")
        }
    }
// */
    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: Page) {
        vh.itemView.tv_id.text= data.id
        vh.itemView.tv_content.text= data.content?.getFkId(0)
        vh.itemView.tv_no.text= data.no.toString()
/*
        numberPicker.setUpperNumberAt(pos, data.no +2)
        numberPicker.setLowerNumberAt(pos, (data.no +1) %3)
        numberPicker.setNumberAt(pos, data.no %6)
// */
        loge("pos= $pos supposed upperBorder= ${data.no +2} lowerBorder= ${(data.no +1) %3} number= ${data.no %6}")

        loge("data.id= ${data.id} data.content?.getFkId(0)= ${data.content?.getFkId(0)} data.no= ${data.no}")

        var rowExists= false
//        sqlite.ifExists()
        sqlite.ifExists(data).observe { map ->
            map.notNull { map ->
                val exists= map.values.first()
                loge("sqlite.ifExists() NOT NULL exists= $exists")
                var res= R.drawable.ic_check_circle
                var color= R.color.ijo
                var colDelete= R.color.merah
                if(!exists){
                    res= R.drawable.ic_cross_circle
                    color= R.color.merah
                    colDelete= R.color.abuSangatTua
                }
                vh.itemView.iv.imageResource= res
                _ViewUtil.setColorRes(vh.itemView.iv, color)
                _ViewUtil.setColorRes(vh.itemView.iv_delete, colDelete)
                rowExists= exists
            }.isNull { loge("sqlite.ifExists() NULL!!!") }
        }
        vh.itemView.iv.setOnClickListener {
            loge("insert()")
            if(!rowExists){
                loge("insert() NOT EXISTS")
                sqlite.insert(data).observe { map ->
                    map.notNull { map ->
                        for((k, v) in map){
                            loge("insert k= $k v= $v")
                        }
                        if(map.values.first()){
                            vh.itemView.iv.imageResource= R.drawable.ic_check_circle
                            _ViewUtil.setColorRes(vh.itemView.iv, R.color.ijo)
                            _ViewUtil.setColorRes(vh.itemView.iv_delete, R.color.merah)
                            rowExists= true
                        }
                    }
                }
            }
            else
                loge("insert() EXISTS")
        }

        vh.itemView.iv_delete.setOnClickListener {
            loge("delete()")
            if(rowExists){
                loge("delete() EXISTS")
                sqlite.delete(data).observe { map ->
                    map.notNull { map ->
                        for((k, v) in map){
                            loge("delete k= $k v= $v")
                        }
                        if(map.values.first()){
                            _ViewUtil.setColorRes(vh.itemView.iv_delete, R.color.abuSangatTua)
                            vh.itemView.iv.imageResource= R.drawable.ic_cross_circle
                            _ViewUtil.setColorRes(vh.itemView.iv, R.color.merah)
                            rowExists= false
                        }
                    }
                }
            }
        }
    }

    override fun setupLayoutManager(context: Context): LinearLm = LinearLm(context)
}