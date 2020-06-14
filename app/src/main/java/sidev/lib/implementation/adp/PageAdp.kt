package sidev.lib.implementation.adp

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.comp_page_row.view.*
import org.jetbrains.anko.imageResource
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.tool.util._ViewUtil
import sidev.lib.android.siframe.tool.util.`fun`.loge
import sidev.lib.implementation.R
import sidev.lib.implementation.model.Page
import sidev.lib.implementation.util.PageSqlite
import sidev.lib.universal.`fun`.isNull
import sidev.lib.universal.`fun`.notNull

class PageAdp(c: Context, data: ArrayList<Page>?) : RvAdp<Page, LinearLayoutManager>(c, data){
    override val itemLayoutId: Int
        get() = R.layout.comp_page_row

    val sqlite= PageSqlite(c)

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: Page) {
        vh.itemView.tv_id.text= data.id
        vh.itemView.tv_content.text= data.content?.getFkId(0)
        vh.itemView.tv_no.text= data.no.toString()

        loge("data.id= ${data.id} data.content?.getFkId(0)= ${data.content?.getFkId(0)} data.no= ${data.no}")

        var rowExists= false
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
                _ViewUtil.setColor(vh.itemView.iv, color)
                _ViewUtil.setColor(vh.itemView.iv_delete, colDelete)
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
                            _ViewUtil.setColor(vh.itemView.iv, R.color.ijo)
                            _ViewUtil.setColor(vh.itemView.iv_delete, R.color.merah)
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
                            _ViewUtil.setColor(vh.itemView.iv_delete, R.color.abuSangatTua)
                            vh.itemView.iv.imageResource= R.drawable.ic_cross_circle
                            _ViewUtil.setColor(vh.itemView.iv, R.color.merah)
                            rowExists= false
                        }
                    }
                }
            }
        }
    }

    override fun setupLayoutManager(): LinearLayoutManager = LinearLayoutManager(ctx)
}