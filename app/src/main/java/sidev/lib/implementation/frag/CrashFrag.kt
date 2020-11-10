package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.tool.util._ThreadUtil
import sidev.lib.implementation.R
import java.lang.Exception

class CrashFrag : Frag(){
    override val layoutId: Int
        get() = R.layout.frag_txt

    override fun _initView(layoutView: View) {
        layoutView.pb.visibility= View.VISIBLE
        layoutView.tv.text= 3.toString()
        countDown(3){
            layoutView.tv.text= it.toString()
            if(it == 0)
                throw Exception("Waktu habis")
        }
    }

    /**
     * Menghitung mundur, jeda 1 detik tiap iterasi.
     * @param func dilakukan sampai iterasi ke-0.
     */
    fun countDown(iterate: Int, func: (Int) -> Unit){
        _ThreadUtil.delayRun(1000){
            func(iterate)
            if(iterate >= 0)
                countDown(iterate -1, func)
        }
    }
}