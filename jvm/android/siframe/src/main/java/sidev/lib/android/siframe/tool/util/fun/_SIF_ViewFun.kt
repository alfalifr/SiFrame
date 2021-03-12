package sidev.lib.android.siframe.tool.util.`fun`

import android.view.View
import org.jetbrains.anko.runOnUiThread
import sidev.lib.android.siframe.adapter.SimpleRvAdp
import sidev.lib.android.siframe.tool.util._SIF_ViewUtil
//import sidev.lib.android.std._external._AnkoInternals.runOnUiThread


val View.animator: _SIF_ViewUtil.SimpleAnimator
    get()= _SIF_ViewUtil.SimpleAnimator(this)


fun SimpleRvAdp<*, *>.notifyDatasetChanged_ui(){
    ctx.runOnUiThread {
        notifyDataSetChanged_()
    }
}
