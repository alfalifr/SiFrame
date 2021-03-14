package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.page_res.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util._ResUtil
import sidev.lib.android.std.tool.util.`fun`.asResEntryBy
import sidev.lib.android.std.tool.util.`fun`.asResNameBy
import sidev.lib.android.std.tool.util.`fun`.asResPackageBy
import sidev.lib.android.std.tool.util.`fun`.asResTypeBy
import sidev.lib.implementation.R

class ResFrag: Frag() {
    override val layoutId: Int = R.layout.page_res

    override fun _initView(layoutView: View) {
        val id= R.id.tv_id
        val name= id asResNameBy context!!
        val type= id asResTypeBy context!!
        val pkg= id asResPackageBy context!!
        val entry= id asResEntryBy context!!
        val idFromStr= _ResUtil.getResId(context!!, "$type/$entry")

        layoutView.apply {
            tv_name.text= name
            tv_type.text= type
            tv_pkg.text= pkg
            tv_entry.text= entry
            tv_ctx_pkg.text= context!!.packageName
            tv_from_str.text= (idFromStr == id).toString()
            tv_id_int.text= id.toString()
            tv_id_int_str.text= idFromStr.toString()
            //context!!.resources.getIdentifier()
        }
    }
}