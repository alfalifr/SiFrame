package sidev.lib.implementation._simulation.edu_class.adp

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main._simul_edu_comp_nav_modul_item.view.*
//import kotlinx.android.synthetic.main.comp_nav_modul_item.view.*
//import sidev.kuliah.tekber.edu_class.R
import sidev.kuliah.tekber.edu_class.model.Page
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.implementation.R

class PageNavAdp(c: Context, data: ArrayList<Page>?): RvAdp<Page, LinearLayoutManager>(c, data){
    override val itemLayoutId: Int
        get() = R.layout._simul_edu_comp_nav_modul_item

    var selectedPageInd= 0
        set(v){
            field= v
            notifyDataSetChanged_()
        }

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: Page) {
        val v= vh.itemView
        var indicTopVis= View.VISIBLE
        var indicBottomVis= View.VISIBLE
        if(pos == 0)
            indicTopVis= View.GONE
        if(pos == itemCount-1)
            indicBottomVis= View.GONE

        v.iv_indication_top.visibility= indicTopVis
        v.iv_indication_bottom.visibility= indicBottomVis

        v.tv.text= data.name
        v.iv_indication.setImageResource(
            if(pos != selectedPageInd) R.drawable.shape_border_oval_sm
            else R.drawable.shape_solid_oval
        )
    }

    override fun setupLayoutManager(context: Context): LinearLayoutManager = LinearLayoutManager(context)
}