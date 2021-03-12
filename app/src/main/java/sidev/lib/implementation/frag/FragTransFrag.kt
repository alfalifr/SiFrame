package sidev.lib.implementation.frag

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.page_frag_trans.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.toast
import sidev.lib.exception.IllegalStateExc
import sidev.lib.implementation.R
import sidev.lib.implementation.util.Const

class FragTransFrag: Frag() {
    override val layoutId: Int = R.layout.page_frag_trans
    private var fragTrans: FragmentTransaction?= null

    private val fragTransOnClickListener= View.OnClickListener {
        val count= childFragmentManager.backStackEntryCount
        //layoutView.tv.text= "Backstack entry count= $count"

        //.addToBackStack(null)
        val frag= TxtFrag(count.toString())
        frag.arguments = Bundle().apply { putInt(Const.DATA_COLOR, count) }
        val id= it!!.id

        when(id){
            R.id.btn_commit -> {
                fragTrans!!.commit()
                fragTrans= null
            }
            R.id.btn_pop -> {
                childFragmentManager.popBackStack()
            }
            else -> {
                val trans= fragTrans ?: childFragmentManager.beginTransaction().also {
                    fragTrans= it
                }
                when(id){
                    R.id.btn_add -> trans.add(R.id.ll, frag, count.toString())
                    R.id.btn_remove -> {
                        childFragmentManager.findFragmentByTag((count -1).toString())?.also {
                            trans.remove(it)
                        } ?: toast("Gakda fragment dg tag '${count -1}'")
                    }
                    R.id.btn_replace -> trans.replace(R.id.ll, frag, count.toString())
                    else -> throw IllegalStateExc(
                        currentState = "v.id !in [R.id.btn_add, R.id.btn_remove, R.id.btn_replace]",
                        expectedState = "v.id in [R.id.btn_add, R.id.btn_remove, R.id.btn_replace]"
                    )
                }
                trans.addToBackStack(null)
            }
        }
        val countAfter= childFragmentManager.backStackEntryCount
        layoutView.tv.text= "Backstack entry count= $countAfter"
    }

    override fun _initView(layoutView: View) {
        layoutView.apply {
            btn_add.setOnClickListener(fragTransOnClickListener)
            btn_replace.setOnClickListener(fragTransOnClickListener)
            btn_remove.setOnClickListener(fragTransOnClickListener)
            btn_commit.setOnClickListener(fragTransOnClickListener)
            btn_pop.setOnClickListener(fragTransOnClickListener)
        }
    }
}