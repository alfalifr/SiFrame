package sidev.lib.implementation.frag

import android.view.View
import org.apache.commons.lang3.ThreadUtils
import sidev.lib.android.siframe.lifecycle.fragment.Frag
import sidev.lib.android.viewrap.wrapChildWithBuffer
import sidev.lib.implementation.R
import sidev.lib.universal.tool.util.ThreadUtil

class BufferFrag : Frag(){
    override val layoutId: Int
        get() = R.layout.page_buffer_layout

    override fun _initView(layoutView: View) {
        val wrapperList= layoutView.wrapChildWithBuffer()
        wrapperList.forEach { it.showBuffer(keepView = true) }
        ThreadUtil.delayRun(7000){
            wrapperList.forEach { it.showBuffer(false) }
        }
    }
}