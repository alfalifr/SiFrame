package sidev.lib.implementation.frag

import android.view.View
import kotlinx.android.synthetic.main.frag_txt.view.*
import org.jetbrains.anko.support.v4.act
import sidev.lib.android.siframe.lifecycle.app.App
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.std.`val`._Config
import sidev.lib.android.std.tool.util.`fun`.asyncBuffer
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.implementation.R
import java.lang.IllegalStateException

class LogOnFileFrag: Frag() {
    override val layoutId: Int = R.layout.frag_txt
    @ExperimentalStdlibApi
    override fun _initView(layoutView: View) {
        loge("_Config.DEBUG = ${_Config.DEBUG}")
        val limit= 4
        layoutView.tv.text = "Bersiap.... $limit"
        asyncBuffer(limit, { it.index < limit }, {
            layoutView.tv.text = "Bersiap.... $it"
        }, {
            loge("LogOnFileFrag _Config.LOG_ON_FILE= ${_Config.LOG_ON_FILE}")
            throw IllegalStateException("Nyoba3 bro")
        }, 1000){
            it-1
        }
    }

    override fun onPause() {
        super.onPause()
        (act.application as App).logOnFileActive = false
    }

    override fun onResume() {
        super.onResume()
        (act.application as App).logOnFileActive = true
    }
}