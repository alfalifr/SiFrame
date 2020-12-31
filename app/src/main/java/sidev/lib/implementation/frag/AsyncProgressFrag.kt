package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util.`fun`.asyncBuffer
import sidev.lib.implementation.R

class AsyncProgressFrag: Frag() {
    override val layoutId: Int= R.layout.frag_txt

    @ExperimentalStdlibApi
    override fun _initView(layoutView: View) {
        layoutView.pb.visibility= View.VISIBLE
        val arr= arrayOf(
            "Halo", "bro", "yo", "ho", "ok", "lah", "ho", "ke"
        )
//        var i= 0
//        var strVar= ""//.asBoxed()
        asyncBuffer("", {it.index < arr.size}, {
            layoutView.tv.text= it
        }, delay = 90, onFinish = {
            layoutView.pb.visibility= View.GONE
        }) {
            it + " " +arr[iteration.index]
        }
    }
}