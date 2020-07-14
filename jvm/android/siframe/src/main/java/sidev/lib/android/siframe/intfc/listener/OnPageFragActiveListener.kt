package sidev.lib.android.siframe.intfc.listener

import android.view.View

interface OnPageFragActiveListener: Listener {
    fun onPageFragActive(vParent: View, pos: Int)
}