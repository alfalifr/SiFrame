package sidev.lib.implementation._simulation.edu_class.adp

import android.content.Context
import android.util.SparseArray
import android.widget.CheckBox
import androidx.core.util.set
import androidx.recyclerview.widget.LinearLayoutManager
import sidev.lib.android.siframe.adapter.RvAdp
import sidev.lib.android.siframe.tool.util.`fun`.findViewByType
import sidev.lib.check.isNull
import sidev.lib.check.notNull
import sidev.lib.implementation.R

class QuestionCheckBoxAdp(c: Context, data: ArrayList<String>?)
    : RvAdp<String, LinearLayoutManager>(c, data){
    override val itemLayoutId: Int
        get() = R.layout._simul_edu_comp_item_fill_choice_checkbox

    val cbCheckedList= SparseArray<Boolean>()
    var onCheckedChangeListener: ((isChecked: Boolean, pos: Int) -> Unit)?= null

    override fun bindVH(vh: SimpleViewHolder, pos: Int, data: String) {
        val v= vh.itemView.findViewByType<CheckBox>()!!
        v.text= data
        cbCheckedList.get(pos).notNull { bool ->
            v.isChecked= bool
        }.isNull {
            cbCheckedList[pos]= false
        }

        v.setOnCheckedChangeListener { buttonView, isChecked ->
            onCheckedChangeListener?.invoke(isChecked, pos)
            cbCheckedList[pos]= isChecked
        }
    }

    override fun setupLayoutManager(context: Context): LinearLayoutManager = LinearLayoutManager(context)
}