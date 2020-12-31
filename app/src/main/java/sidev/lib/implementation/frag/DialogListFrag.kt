package sidev.lib.implementation.frag

import android.view.View
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.siframe.view.tool.dialog.DialogListView
import sidev.lib.implementation.R

class DialogListFrag: Frag() {
    override val layoutId: Int= R.layout.frag_txt

    private lateinit var dialog: DialogListView<String>

    override fun _initView(layoutView: View) {
        dialog= DialogListView(context!!)
        dialog.dataList= arrayListOf("a", "b", "c", "d", "a", "b", "c", "d", "a", "b", "c", "d" )
        dialog.showtBtnAction()
        dialog.show()
    }
}