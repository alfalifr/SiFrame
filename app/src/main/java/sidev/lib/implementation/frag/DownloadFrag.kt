package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.page_progres.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.asyncBuffer
import sidev.lib.implementation.R

class DownloadFrag: Frag() {
    override val layoutId: Int = R.layout.page_progres

    @ExperimentalStdlibApi
    override fun _initView(layoutView: View) {
        asyncBuffer(0, { it.index < 10 }, {
            layoutView.apply {
                pb.progress = it
                tv_progres.text = "$it %"
            }
        }, {
            layoutView.tv_progres.text = "Selesai"
        }, 100){
            it + 10
        }

    }
}